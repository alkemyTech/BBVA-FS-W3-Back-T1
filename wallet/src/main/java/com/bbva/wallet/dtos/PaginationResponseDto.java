package com.bbva.wallet.dtos;

import org.springframework.hateoas.CollectionModel;

public record PaginationResponseDto(int totalPages,Long totalElements, CollectionModel<?> collectionModel) {
}
