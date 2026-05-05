package com.rayokross.academy.models;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String syllabusFileName;

    private String level;

    private double price;

    @Lob
    private Blob image;

    private String creatorName;

    @CreationTimestamp
    private LocalDate updateDate;

    @ElementCollection
    private List<String> learningObjectives = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Course() {
    }

    public Course(String title, String description, String level, double price, String creatorName, int hoursVideo) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.price = price;
        this.creatorName = creatorName;
    }

    public boolean isFree() {
        return this.price <= 0;
    }

    public String getLevelBadgeClass() {
        if ("Foundations".equalsIgnoreCase(level) || "Beginner".equalsIgnoreCase(level)) {
            return "text-bg-success";
        }
        if ("Defensive".equalsIgnoreCase(level) || "Intermediate".equalsIgnoreCase(level)) {
            return "text-bg-warning";
        }
        return "text-bg-danger";
    }

    public String getBadgeClass() {
        return getLevelBadgeClass();
    }

    public int getStudentCount() {
        if (this.enrollments == null) {
            return 0;
        }
        return this.enrollments.size();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSyllabusFileName() {
        return syllabusFileName;
    }

    public void setSyllabusFileName(String syllabusFileName) {
        this.syllabusFileName = syllabusFileName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getCreatorName() {
        if (this.creatorName == null || this.creatorName.trim().isEmpty()) {
            return "RayoKross Academy";
        }
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getHoursVideo() {
        if (this.lessons == null || this.lessons.isEmpty()) {
            return 0;
        }

        int totalMinutes = 0;
        for (Lesson lesson : this.lessons) {
            totalMinutes = totalMinutes + lesson.getDurationInMinutes();
        }

        return totalMinutes / 60;
    }

    public int getArticlesCount() {
        if (this.lessons == null || this.lessons.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Lesson lesson : this.lessons) {
            if (lesson.getContent() != null && !lesson.getContent().trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public int getResourcesCount() {
        if (this.lessons == null || this.lessons.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Lesson lesson : this.lessons) {
            if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public String getFormattedUpdateDate() {
        if (this.updateDate == null) {
            return "Unknown";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
        return this.updateDate.format(formatter);
    }

    public List<String> getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(List<String> learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public boolean getHasImage() {
        return this.image != null;
    }
}