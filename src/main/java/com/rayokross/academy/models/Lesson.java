package com.rayokross.academy.models;

import jakarta.persistence.*;

@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String videoUrl;

    private int durationInMinutes;

    private Course course;

    public Lesson() {
    }

    public Lesson(String title, String content, String videoUrl, int durationInMinutes, Course course) {
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.durationInMinutes = durationInMinutes;
        this.course = course;
    }

    public Long getId() {
        return id; 
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }
    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}