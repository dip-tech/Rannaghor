package com.example.rannaghor;

public class OrderItemModel {

    //================= DECLARING OBJECTS AND VARIABLES ===============
    private final String ORDERED_ITEM_ID;
    private final String ORDERED_ITEM_IMAGE;
    private final String ORDERED_ITEM_NAME;
    private final String ORDERED_ITEM_PRICE;
    private final String ORDERED_ITEM_QTY;

    //================== CONSTRUCTOR ========================
    public OrderItemModel(String ORDERED_ITEM_ID, String ORDERED_ITEM_IMAGE, String ORDERED_ITEM_NAME, String ORDERED_ITEM_PRICE, String ORDERED_ITEM_QTY) {
        this.ORDERED_ITEM_ID = ORDERED_ITEM_ID;
        this.ORDERED_ITEM_IMAGE = ORDERED_ITEM_IMAGE;
        this.ORDERED_ITEM_NAME = ORDERED_ITEM_NAME;
        this.ORDERED_ITEM_PRICE = ORDERED_ITEM_PRICE;
        this.ORDERED_ITEM_QTY = ORDERED_ITEM_QTY;
    }

    //==================== GETTER METHOODES ==================

    public String getORDERED_ITEM_ID() {
        return ORDERED_ITEM_ID;
    }

    public String getORDERED_ITEM_IMAGE() {
        return ORDERED_ITEM_IMAGE;
    }

    public String getORDERED_ITEM_NAME() {
        return ORDERED_ITEM_NAME;
    }

    public String getORDERED_ITEM_PRICE() {
        return ORDERED_ITEM_PRICE;
    }

    public String getORDERED_ITEM_QTY() {
        return ORDERED_ITEM_QTY;
    }
}
