package com.rayokross.academy.dtos;

import java.time.LocalDate;

public record EnrollmentBasicDTO(

        String userFullName,
        String courseTitle,
        LocalDate enrollmentDate,
        boolean completed

) {

}
