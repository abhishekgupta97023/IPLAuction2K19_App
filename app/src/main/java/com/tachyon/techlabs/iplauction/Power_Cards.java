package com.tachyon.techlabs.iplauction;

public class Power_Cards {
    int image;
    String name, description;
    int price;

    public int getImage() {
        return image;
    }
    public int getPrice() {return price;}
    public void setImage(int image) {
        this.image = image;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}