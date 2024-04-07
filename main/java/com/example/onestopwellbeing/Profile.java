package com.example.onestopwellbeing;

import java.util.List;


public class Profile {
    private String name;
    private String type;
    private String location;
    private String contact;
    private int imageResId;
    private List<String> categories;

    private boolean showExtraInfo;

    private String additionalInfo;

    private String extraInfo;
    public Profile() {}

    public Profile(String name, String type, String location, String contact, int imageResId, List<String> categories) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.contact = contact;
        this.imageResId = imageResId;
        this.categories = categories;
    }

    public boolean isShowExtraInfo() {
        return showExtraInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setShowExtraInfo(boolean showExtraInfo) {
        this.showExtraInfo = showExtraInfo;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
