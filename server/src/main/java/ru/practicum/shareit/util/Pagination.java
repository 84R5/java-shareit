package ru.practicum.shareit.util;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class Pagination {
    public Pageable getPage(Integer from, Integer size) {
        if (from != null && size != null) {
            int pageNumber = (int) Math.ceil((double) from / size);
            return PageRequest.of(pageNumber, size, Sort.by("id").ascending());
        }
        return Pageable.unpaged();
    }
}

