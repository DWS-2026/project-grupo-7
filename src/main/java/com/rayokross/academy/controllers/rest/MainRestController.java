package com.rayokross.academy.controllers.rest;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

@RestController
@RequestMapping("/api/v1")
public class MainRestController {

    private static final int POPULAR_COURSES_LIMIT = 3;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/")
    public Collection<CourseBasicDTO> getPopularCourses() {
        Pageable topThree = PageRequest.of(0, POPULAR_COURSES_LIMIT);

        List<Course> popularCourses = courseService.findAll(topThree).getContent();

        return courseMapper.toBasicDTOs(popularCourses);
    }
}