package com.rayokross.academy.dtos;

import java.util.List;

public record CartDTO(
        List<CourseBasicDTO> courses,
        double totalPrice,
        boolean isEmpty) {
}