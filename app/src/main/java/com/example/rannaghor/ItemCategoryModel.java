package com.example.rannaghor;

public class ItemCategoryModel {
    private String ITEM_CATEGORY_ID;
    private String ITEM_CATEGORY_IMAGE;
    private String ITEM_CATEGORY_NAME;

    public ItemCategoryModel(String ITEM_CATEGORY_ID, String ITEM_CATEGORY_IMAGE, String ITEM_CATEGORY_NAME) {
        this.ITEM_CATEGORY_ID = ITEM_CATEGORY_ID;
        this.ITEM_CATEGORY_IMAGE = ITEM_CATEGORY_IMAGE;
        this.ITEM_CATEGORY_NAME = ITEM_CATEGORY_NAME;
    }

    public String getITEM_CATEGORY_ID() {
        return ITEM_CATEGORY_ID;
    }

    public String getITEM_CATEGORY_IMAGE() {
        return ITEM_CATEGORY_IMAGE;
    }

    public String getITEM_CATEGORY_NAME() {
        return ITEM_CATEGORY_NAME;
    }
}
