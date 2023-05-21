package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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
    private final CommentMapper commentMapper;


    @Override
    public List<ItemResponseDto> search(String text) {
        if (text.length() == 0) {
            log.debug("Search by empty text.");
            return Collections.emptyList();
        }
        List<ItemResponseDto> result = itemRepository.search(text).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
        log.info("Found {} item(s).", result.size());
        return result;
    }

    @Override
    public List<ItemResponseDto> getByUserId(Long userId) {
        List<ItemResponseDto> result = itemRepository.findAllByOwnerId(userId).stream().map(item -> addData(userId, item)).collect(Collectors.toList());
        log.info("Found {} item(s).", result.size());
        return result;
    }

    @Override
    public ItemResponseDto getById(Long userId, Long itemId) {
        ItemResponseDto result = itemRepository.findById(itemId).map(item -> addData(userId, item)).orElseThrow(() -> new NullPointerException(String.format("Item %d is not found.", itemId)));
        log.info("User {} is found.", result.getId());
        return result;
    }

    @Override
    public ItemResponseDto create(Long userId, ItemInputDto itemInputDto) {
        User user = userService.getUserById(userId);
        Item item = new Item();
        item.setOwner(user);
        ItemResponseDto result = Optional.of(itemRepository.save(ItemMapper.mapToItem(itemInputDto, item))).map(ItemMapper::mapToItemDto).orElseThrow();
        log.info("Item {} {} created.", result.getId(), result.getName());
        return result;
    }

    @Override
    public ItemResponseDto update(Long userId, Long itemId, ItemInputDto itemInputDto) {
        User user = userService.getUserById(userId);
        Item oldItem = getItemById(itemId);
        if (!user.getId().equals(oldItem.getOwner().getId())) {
            log.warn("User {} is not the owner of the item {}.", userId, oldItem.getId());
            throw new IllegalArgumentException("Only the owner can edit an item");
        }
        ItemResponseDto result = Optional.of(itemRepository.save(ItemMapper.mapToItem(itemInputDto, oldItem))).map(ItemMapper::mapToItemDto).orElseThrow();
        log.info("Item {} {} updated.", result.getId(), result.getName());
        return result;
    }

    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NullPointerException(String.format("Item %d is not found.", itemId)));
    }

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User author = userService.getUserById(userId);
        Item item = getItemById(itemId);

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(author.getId(), item.getId(), LocalDateTime.now())) {
            throw new NoSuchElementException("The user has not booked this item.");
        }

        Comment comment = commentRepository.save(
                commentMapper.mapToComment(
                        commentRequestDto,
                        userService.getUserById(userId),
                        getItemById(itemId),
                        LocalDateTime.now().plusSeconds(1L)));
        log.info("Comment {} added to item {}.", comment.getId(), item.getId());
        return CommentMapper.mapToCommentResponseDto(comment);
    }

    public ItemResponseDto addData(Long userId, Item item) {

        ItemResponseDto.ItemResponseDtoBuilder itemResponseDto = ItemResponseDto.builder();
        itemResponseDto.id(item.getId()).owner(item.getOwner());
        if (item.getOwner().getId().equals(userId)) {
            itemResponseDto.lastBooking(bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), Status.APPROVED)
                    .map(BookingMapper::mapToRequestDto).orElse(null));
            itemResponseDto.nextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(item.getId(), LocalDateTime.now(), Status.APPROVED)
                    .map(BookingMapper::mapToRequestDto).orElse(null));
        }
        itemResponseDto.comments(commentRepository.findAllByItemId(item.getId()).stream().map(CommentMapper::mapToCommentResponseDto).collect(Collectors.toList()));

        return itemResponseDto.build();
    }
}