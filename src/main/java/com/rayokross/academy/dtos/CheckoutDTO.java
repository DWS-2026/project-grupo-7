package com.rayokross.academy.dtos;

import java.util.List;

public record CheckoutDTO(
        List<Long> courseIds) {
}
