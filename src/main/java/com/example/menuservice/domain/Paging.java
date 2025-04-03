package com.example.menuservice.domain;

import lombok.Builder;
import lombok.Getter;


@Builder
public record Paging(
        int offset,
        int size
) {
}
