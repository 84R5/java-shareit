package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Pagination;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    UserService userService;

    BookingRepository bookingRepository;

    ItemRepository itemRepository;

    CommentRepository commentRepository;

    RequestService requestService;

    RequestRepository requestRepository;

    Pagination pagination;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User owner = UserMapper.toUser(userService.getUserById(userId));
        itemDto.setOwner(UserMapper.toUserDto(owner));
        Request request = null;
        if (itemDto.getRequestId() != null) {
            RequestDto requestDto = requestService.getItemRequestById(userId, itemDto.getRequestId());
            itemDto.setRequestId(requestDto.getId());
            request = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Request %s not found.", itemDto.getRequestId())));
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setRequest(request);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDtoFull> getItemsByOwner(Long userId, Integer from, Integer size) {
        User owner = UserMapper.toUser(userService.getUserById(userId));

        if (from != null && size != null) {
            if (from < 0 || size <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong request.");
            }
            int pageNumber = (int) Math.ceil((double) from / size);
            Pageable pageable = PageRequest.of(pageNumber, size);

            return itemRepository.findByOwner(owner, pageable).stream()
                    .map(ItemMapper::toItemDtoWithDate)
                    .peek(itemDto -> {
                        List<Booking> bookings = bookingRepository.findBookingByItemIdOrderByStartAsc(itemDto.getId());
                        LocalDateTime now = LocalDateTime.now();
                        BookingRequestDto lastBooking = null;
                        BookingRequestDto nextBooking = null;

                        for (Booking booking : bookings) {
                            if (booking.getEnd().isBefore(now)) {
                                lastBooking = BookingMapper.toBookingRequestDto(booking);
                            } else if (booking.getStart().isAfter(now)) {
                                nextBooking = BookingMapper.toBookingRequestDto(booking);
                                break;
                            }
                        }

                        itemDto.setLastBooking(lastBooking);
                        itemDto.setNextBooking(nextBooking);
                    })
                    .collect(Collectors.toList());
        }

        return itemRepository.findByOwner(owner).stream()
                .map(ItemMapper::toItemDtoWithDate)
                .peek(itemDto -> {
                    List<Booking> bookings = bookingRepository.findBookingByItemIdOrderByStartAsc(itemDto.getId());
                    LocalDateTime now = LocalDateTime.now();
                    BookingRequestDto lastBooking = null;
                    BookingRequestDto nextBooking = null;

                    for (Booking booking : bookings) {
                        if (booking.getEnd().isBefore(now)) {
                            lastBooking = BookingMapper.toBookingRequestDto(booking);
                        } else if (booking.getStart().isAfter(now)) {
                            nextBooking = BookingMapper.toBookingRequestDto(booking);
                            break;
                        }
                    }

                    itemDto.setLastBooking(lastBooking);
                    itemDto.setNextBooking(nextBooking);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        User owner = UserMapper.toUser(userService.getUserById(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException(String.format("Item %s not found.", itemId)));
        if (!item.getOwner().equals(owner)) {
            throw new ObjectNotFoundException(String.format("Item %s not found.", itemId));
        }
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        if (available != null) {
            item.setAvailable(available);
        }
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDtoFull getItemByIdFromUser(Long userId, Long itemId) {
        userService.getUserById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException(String.format("Item %s not found.", itemId)));

        LocalDateTime now = LocalDateTime.now();
        BookingRequestDto lastBooking = bookingRepository.findTopByItemOwnerIdAndStatusAndStartBeforeOrderByEndDesc(userId, Status.APPROVED, now)
                .map(BookingMapper::toBookingRequestDto)
                .orElse(null);

        BookingRequestDto nextBooking = bookingRepository.findTopByItemOwnerIdAndStatusAndStartAfterOrderByStartAsc(userId, Status.APPROVED, now)
                .map(BookingMapper::toBookingRequestDto)
                .orElse(null);

        List<CommentDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        ItemDtoFull itemDtoFull = ItemMapper.toItemDtoWithDate(item);
        itemDtoFull.setLastBooking(lastBooking);
        itemDtoFull.setNextBooking(nextBooking);
        itemDtoFull.setComments(comments);

        return itemDtoFull;
    }

    @Override
    public List<ItemDto> getItemsBySearch(Long userId, String text, Integer from, Integer size) {
        userService.getUserById(userId);
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }


        if (from != null && size != null) {
            return itemRepository.searchItems(text, pagination.getPage(from, size))
                    .stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }

        return itemRepository.searchItems(text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Item %s not found.", itemId)));
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        UserDto author = userService.getUserById(userId);
        ItemDto item = ItemMapper.toItemDto(getItemById(itemId));
        Comment existingComment = commentRepository.findByAuthorIdAndItemId(userId, itemId);
        if (existingComment != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already commented this item.");
        }
        List<Booking> bookings = bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, Status.APPROVED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't comment.");
        }
        commentDto.setItemDto(item);
        commentDto.setAuthorName(author.getName());
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, UserMapper.toUser(author)));
        return CommentMapper.toCommentDto(comment);
    }
}

/*
package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Pagination;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final RequestService requestService;
    @Autowired
    private final Pagination pagination;


    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        UserDto user = userService.getUserById(userId);
        itemDto.setOwner(user);
        Request request = null;
        if (itemDto.getRequestId() != null) {
            RequestDto requestDto = requestService.getRequestById(userId, itemDto.getRequestId());
            itemDto.setRequestId(requestDto.getId());
            request = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Request %s not found.", itemDto.getRequestId())));
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setRequest(request);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException(String.format("Item %s not found.", itemId)));
    }

    @Override
    public ItemDtoWithDate getItemByIdFromUser(Long itemId, Long userId) {
        userService.getUserById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException(String.format("Item %s not found.", itemId)));

        LocalDateTime now = LocalDateTime.now();
        BookingRequestDto lastBooking = bookingRepository.findTopByItemOwnerIdAndStatusAndStartBeforeOrderByEndDesc(userId, Status.APPROVED, now)
                .map(BookingMapper::toBookingRequestDto)
                .orElse(null);

        BookingRequestDto nextBooking = bookingRepository.findTopByItemOwnerIdAndStatusAndStartAfterOrderByStartAsc(userId, Status.APPROVED, now)
                .map(BookingMapper::toBookingRequestDto)
                .orElse(null);

        List<CommentDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        ItemDtoWithDate itemDtoWithDate = ItemMapper.toItemDtoWithDate(item);
        itemDtoWithDate.setLastBooking(lastBooking);
        itemDtoWithDate.setNextBooking(nextBooking);
        itemDtoWithDate.setComments(comments);

        return itemDtoWithDate;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long userId, Long itemId) {
        User owner = UserMapper.toUser(userService.getUserById(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException(String.format("Item %s not found.", itemId)));
        if (!item.getOwner().equals(owner)) {
            throw new ObjectNotFoundException(String.format("Item %s not found.", itemId));
        }
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        if (available != null) {
            item.setAvailable(available);
        }
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void delete(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещь с ID=" + itemId + " не найдена!"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ObjectNotFoundException("У пользователя нет такой вещи!");
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDtoWithDate> getItemsByOwner(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);


        return itemRepository.findAllByOwnerId(userId, pagination.getPage(from,size))
                .stream()
                .map(item -> addData(userId, item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoWithDate> getItemsByOwner(Long userId) {

        return itemRepository.findAllByOwnerId(userId).stream()
                .map(item -> addData(userId, item))
                .collect(Collectors.toList());
    }
        @Override
        public List<ItemDto> getItemsBySearch(Long userId, String query, Integer from, Integer size){
            */
/*userService.getUserById(userId);


            if (query == null || query.isBlank()) {
                return Collections.emptyList();
            }
            if (from != null && size != null) {
                Pageable pageable = pagination.getPage(from,size);
                return itemRepository.getItemsBySearchQuery(query, pageable)
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList());
            }
            Pageable pageable = pagination.getPage(1,1000);
            return itemRepository.getItemsBySearchQuery(query,pageable)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());*//*

            userService.getUserById(userId);
            if (query == null || query.isBlank()) {
                return new ArrayList<>();
            }
            if (from != null && size != null) {
                if (from < 0 || size <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong request.");
                }
                int pageNumber = (int) Math.ceil((double) from / size);
                Pageable pageable = PageRequest.of(pageNumber, size);

                return itemRepository.searchItems(query, pageable)
                        .stream()
                        .filter(Item::getAvailable)
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList());
            }

            return itemRepository.searchItems(query)
                    .stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }

        @Override
        public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId){
            LocalDateTime clock = LocalDateTime.now(Clock.systemDefaultZone());
            UserDto author = userService.getUserById(userId);
            ItemDto item = ItemMapper.toItemDto(findItem(itemId));
            Comment existingComment = commentRepository.findByAuthorIdAndItemId(userId, itemId);
            if (existingComment != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already commented this item.");
            }
            List<Booking> bookings = bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, Status.APPROVED, clock);

            if (bookings.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't comment.");
            }
            commentDto.setItem(item);
            commentDto.setAuthorName(author.getName());
            commentDto.setCreated(clock);
            Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, UserMapper.toUser(author)));
            return CommentMapper.toCommentDto(comment);
        }

    private ItemDtoWithDate addData(Long userId, Item item) {
        ItemDtoWithDate itemDto = ItemMapper.toItemDtoWithDate(item);
        LocalDateTime clock = LocalDateTime.now(Clock.systemDefaultZone());
        if (Objects.equals(itemDto.getOwner().getId(), userId)) {
            itemDto.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(itemDto.getId(),
                            clock,
                            Status.APPROVED)
                    .map(BookingMapper::toBookingRequestDto)
                    .orElse(null));

            itemDto.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(itemDto.getId(),
                            clock,
                            Status.APPROVED)
                    .map(BookingMapper::toBookingRequestDto)
                    .orElse(null));
        }

        itemDto.setComments(commentRepository.findAllByItemId(itemDto.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

        return itemDto;
    }
}


*/
/*
    @Override
    public ItemDto getItemById(Long id, Long userId) {
        ItemDto itemDto;
        Item item = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + id + " не найдена!"));
        if (userId.equals(item.getOwner().getId())) {
            itemDto = mapper.toItemExtDto(item);
        } else {
            itemDto = mapper.toItemDto(item);
        }
        return itemDto;
    }

    @Override
    public Item findItemById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + id + " не найдена!"));
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long ownerId) {

        return mapper.toItemDto(repository.save(mapper.toItem(itemDto, ownerId)));
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId, Integer from, Integer size) {

        List<ItemDto> listItemExtDto = new ArrayList<>();
        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page<Item> page;
        Pagination pager = new Pagination(from, size);


        if (size == null) {
            pageable =
                    PageRequest.of(pager.getIndex(), pager.getPageSize(), sort);
            do {
                page = repository.findByOwnerId(ownerId, pageable);
                listItemExtDto.addAll(page.stream().map(mapper::toItemExtDto).collect(toList()));
                pageable = pageable.next();
            } while (page.hasNext());

        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sort);
                page = repository.findByOwnerId(ownerId, pageable);
                listItemExtDto.addAll(page.stream().map(mapper::toItemExtDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            listItemExtDto = listItemExtDto.stream().limit(size).collect(toList());
        }
        return listItemExtDto;
    }

    @Override
    public void delete(Long itemId, Long ownerId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + itemId + " не найдена!"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotFoundException("У пользователя нет такой вещи!");
        }
        repository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text, Integer from, Integer size) {
        List<ItemDto> listItemDto = new ArrayList<>();
        if ((text != null) && (!text.isEmpty()) && (!text.isBlank())) {
            text = text.toLowerCase();
            Pageable pageable;
            Sort sort = Sort.by(Sort.Direction.ASC, "name");
            Page<Item> page;
            Pagination pager = new Pagination(from, size);

            if (size == null) {
                pageable =
                        PageRequest.of(pager.getIndex(), pager.getPageSize(), sort);
                do {
                    page = repository.getItemsBySearchQuery(text, pageable);
                    listItemDto.addAll(page.stream().map(mapper::toItemDto).collect(toList()));
                    pageable = pageable.next();
                } while (page.hasNext());

            } else {
                for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                    pageable =
                            PageRequest.of(i, pager.getPageSize(), sort);
                    page = repository.getItemsBySearchQuery(text, pageable);
                    listItemDto.addAll(page.stream().map(mapper::toItemDto).collect(toList()));
                    if (!page.hasNext()) {
                        break;
                    }
                }
                listItemDto = listItemDto.stream().limit(size).collect(toList());
            }
        }
        return listItemDto;
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        checker.isExistUser(ownerId);
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + itemId + " не найдена!"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotFoundException("У пользователя нет такой вещи!");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return mapper.toItemDto(repository.save(item));
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        checker.isExistUser(userId);
        Comment comment = new Comment();
        Booking booking = checker.getBookingWithUserBookedItem(itemId, userId);
        if (booking != null) {
            comment.setCreated(LocalDateTime.now());
            comment.setItem(booking.getItem());
            comment.setAuthor(booking.getBooker());
            comment.setText(commentDto.getText());
        } else {
            throw new IllegalStateException("Данный пользователь вещь не бронировал!");
        }
        return mapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItemId(itemId,
                        Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(mapper::toCommentDto)
                .collect(toList());
    }

    @Override
    public List<ItemDto> getItemsByRequestId(Long requestId) {
        return repository.findAllByRequestId(requestId,
                        Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(mapper::toItemDto)
                .collect(toList());
    }*//*



 */
