package com.example.rannaghor;

public class ItemModel {
    //========================= DECLARING DATA MEMBERS OF ITEMS ======================
    private String ITEM_ID;
    private String ITEM_NAME;
    private String ITEM_IMG;
    private String ITEM_PRICE;
    private String ITEM_CATEGORY;
    private String ITEM_BRAND;
    private String ITEM_RESTAURANT;
    private String ITEM_TAGS;

    //========================= CONSTRUCTOR ===============================

    public ItemModel(String ITEM_ID, String ITEM_NAME, String ITEM_IMG, String ITEM_PRICE, String ITEM_CATEGORY, String ITEM_BRAND, String ITEM_RESTAURANT, String ITEM_TAGS) {
        this.ITEM_ID = ITEM_ID;
        this.ITEM_NAME = ITEM_NAME;
        this.ITEM_IMG = ITEM_IMG;
        this.ITEM_PRICE = ITEM_PRICE;
        this.ITEM_CATEGORY = ITEM_CATEGORY;
        this.ITEM_BRAND = ITEM_BRAND;
        this.ITEM_RESTAURANT = ITEM_RESTAURANT;
        this.ITEM_TAGS = ITEM_TAGS;
    }
    //======================== GETTERS ========================

    public String getITEM_ID() {
        return ITEM_ID;
    }

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public String getITEM_IMG() {
        return ITEM_IMG;
    }

    public String getITEM_PRICE() {
        return ITEM_PRICE;
    }

    public String getITEM_CATEGORY() {
        return ITEM_CATEGORY;
    }

    public String getITEM_BRAND() {
        return ITEM_BRAND;
    }

    public String getITEM_RESTAURANT() {
        return ITEM_RESTAURANT;
    }

    public String getITEM_TAGS() {
        return ITEM_TAGS;
    }

    //============================== SETTERS =========================

    public void setITEM_ID(String ITEM_ID) {
        this.ITEM_ID = ITEM_ID;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public void setITEM_IMG(String ITEM_IMG) {
        this.ITEM_IMG = ITEM_IMG;
    }

    public void setITEM_PRICE(String ITEM_PRICE) {
        this.ITEM_PRICE = ITEM_PRICE;
    }

    public void setITEM_CATEGORY(String ITEM_CATEGORY) {
        this.ITEM_CATEGORY = ITEM_CATEGORY;
    }

    public void setITEM_BRAND(String ITEM_BRAND) {
        this.ITEM_BRAND = ITEM_BRAND;
    }

    public void setITEM_RESTAURANT(String ITEM_RESTAURANT) {
        this.ITEM_RESTAURANT = ITEM_RESTAURANT;
    }

    public void setITEM_TAGS(String ITEM_TAGS) {
        this.ITEM_TAGS = ITEM_TAGS;
    }
}
