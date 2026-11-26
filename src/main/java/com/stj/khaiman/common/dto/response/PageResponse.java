package com.stj.khaiman.common.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@JsonPropertyOrder({ "page", "size", "totalElements", "totalPages", "content"})
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PageResponse<T> from(Page<T> p) {
        return new PageResponse<>(
                p.getContent(),
                p.getNumber() + 1,
                p.getSize(),
                p.getTotalElements(),
                p.getTotalPages()
        );
    }
}
