package com.skillip.skillip_backend.models;

import com.skillip.skillip_backend.common.enums.ImageCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class ImageWithDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String imageURL;

    @Column(nullable = false, length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ImageCategory category;

    // Reference ID of the object (e.g. User, Product etc.) to which this image is attached/related
    @Column(nullable = false)
    private Long referenceID;

    public ImageWithDescription(Long id, String imageURL, String description, ImageCategory category, Long referenceID) {
        this.id = id;
        this.imageURL = imageURL;
        this.description = description;
        this.category = category;
        this.referenceID = referenceID;
    }

    public ImageWithDescription(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageCategory getCategory() {
        return category;
    }

    public void setCategory(ImageCategory category) {
        this.category = category;
    }

    public Long getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(Long referenceID) {
        this.referenceID = referenceID;
    }
}
