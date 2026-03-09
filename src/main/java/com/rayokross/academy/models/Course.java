package com.rayokross.academy.models;

import java.sql.Blob;
import java.util.*;
import jakarta.persistence.*;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String level;

    private double price;

    @Lob
    private Blob image;

    private double rating = 0.0;

    private int reviewCount = 0;

    private String creatorName = "RayoKross Team";

    private int hoursVideo = 0;

    private int articlesCount = 0;

    private int resourcesCount = 0;

    private String formattedUpdateDate = "Mar 2026";

    @ElementCollection
    private List<String> learningObjectives = new ArrayList<>();

    public Course() {
    }

    public Course(String title, String description, String level, double price, String creatorName, int hoursVideo) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.price = price;
        this.creatorName = creatorName;
        this.hoursVideo = hoursVideo;
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
        return getLevelBadgeClass(); // Reutilizamos la lógica que ya teníamos
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getHoursVideo() {
        return hoursVideo;
    }

    public void setHoursVideo(int hoursVideo) {
        this.hoursVideo = hoursVideo;
    }

    public int getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(int articlesCount) {
        this.articlesCount = articlesCount;
    }

    public int getResourcesCount() {
        return resourcesCount;
    }

    public void setResourcesCount(int resourcesCount) {
        this.resourcesCount = resourcesCount;
    }

    public String getFormattedUpdateDate() {
        return formattedUpdateDate;
    }

    public void setFormattedUpdateDate(String formattedUpdateDate) {
        this.formattedUpdateDate = formattedUpdateDate;
    }

    public List<String> getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(List<String> learningObjectives) {
        this.learningObjectives = learningObjectives;
    }
}