package com.example.ejarcico_2_3;

public class Photograph {
    private byte[] image;
    private String description;

    public Photograph(byte[] image, String description) {
        this.image = image;
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
