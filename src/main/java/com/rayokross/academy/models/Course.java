package com.rayokross.academy.models;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

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

    public Course() {}

    public Course(String title, String description, String level, double price) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.price = price;
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
}