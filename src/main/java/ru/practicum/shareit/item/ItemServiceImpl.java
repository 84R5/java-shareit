package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentFullDto;
import ru.practicum.shareit.comment.dto.CommentInputDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.*;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;


    @Override
    public List<ItemFullDto> search(String text) {
        if (text.length() == 0) {
            log.debug("Search by empty text.");
            return Collections.emptyList();
        }
        List<ItemFullDto> result = itemRepository
                .search(text).stream()
                .map(ItemMapper::mapToFullDto)
                .collect(Collectors.toList());
        log.info("Found {} item(s).", result.size());
        return result;
    }

    @Override
    public List<ItemFullDto> getByUserId(Long userId) {
        List<ItemFullDto> result = itemRepository.findAllByOwnerId(userId).stream()
                .map(item -> addData(userId, item))
                .collect(Collectors.toList());
        log.info("Found {} item(s).", result.size());
        return result;
    }

    @Override
    public ItemFullDto getById(Long userId, Long itemId) {
        ItemFullDto result = itemRepository
                .findById(itemId)
                .map(item -> addData(userId, item))
                .orElseThrow(() -> new NullPointerException(String.format("Item %d is not found.", itemId)));
        log.info("User {} is found.", result.getId());
        return result;
    }

    @Override
    public ItemFullDto create(Long userId, ItemInputDto itemInputDto) {
        User user = userService.getUserById(userId);
        Item item = new Item();
        item.setOwner(user);
        ItemFullDto result = Optional.of(itemRepository.save(ItemMapper.mapToItem(itemInputDto, item)))
                .map(ItemMapper::mapToFullDto)
                .orElseThrow();
        log.info("Item {} {} created.", result.getId(), result.getName());
        return result;
    }

    @Override
    public ItemFullDto update(Long userId, Long itemId, ItemInputDto itemInputDto) {
        User user = userService.getUserById(userId);
        Item oldItem = getItemById(itemId);
        if (!user.getId().equals(oldItem.getOwner().getId())) {
            log.warn("User {} is not the owner of the item {}.", userId, oldItem.getId());
            throw new IllegalArgumentException("Only the owner can edit an item");
        }
        ItemFullDto result = Optional.of(itemRepository.save(ItemMapper.mapToItem(itemInputDto, oldItem)))
                .map(ItemMapper::mapToFullDto)
                .orElseThrow();
        log.info("Item {} {} updated.", result.getId(), result.getName());
        return result;
    }

    public Item getItemById(Long itemId) {
        return itemRepository
                .findById(itemId)
                .orElseThrow(() -> new NullPointerException(String.format("Item %d is not found.", itemId)));
    }

    @Override
    public CommentFullDto addComment(Long userId, Long itemId, CommentInputDto commentInputDto) {
        User author = userService.getUserById(userId);
        Item item = getItemById(itemId);

        if (!bookingRepository
                .existsByBookerIdAndItemIdAndEndBefore(author.getId(), item.getId(), LocalDateTime.now())) {
            throw new NoSuchElementException("The user has not booked this item.");
        }

        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(author);

        CommentFullDto commentFullDto =
                Optional.of(commentRepository.save(CommentMapper.mapToComment(commentInputDto, comment)))
                        .map(CommentMapper::mapToFullDto)
                        .orElseThrow();
        log.info("Comment {} added to item {}.", commentFullDto.getId(), item.getId());
        return commentFullDto;
    }

    public ItemFullDto addData(Long userId, Item item) {
        ItemFullDto result = ItemMapper.mapToFullDto(item);

        if (result.getOwner().getId().equals(userId)) {
            result.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(result.getId(),
                            LocalDateTime.now(),
                            Status.APPROVED)
                    .map(BookingMapper::mapToShortDto)
                    .orElse(null));

            result.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(result.getId(),
                            LocalDateTime.now(),
                            Status.APPROVED)
                    .map(BookingMapper::mapToShortDto)
                    .orElse(null));
        }

        result.setComments(commentRepository.findAllByItemId(result.getId())
                .stream()
                .map(CommentMapper::mapToFullDto)
                .collect(Collectors.toList()));

        return result;
    }
}