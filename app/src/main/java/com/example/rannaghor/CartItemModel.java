package com.example.rannaghor;

public class CartItemModel {
    //==================== DECLARING DATA MEMBERS =============
    private final String CART_ID;
    private final String USER_ID;
    private final String ITEM_ID;
    private final String RESTAURANT_ID;
    private final String RESTAURANT_NAME;
    private final String ITEM_IMAGE;
    private final String ITEM_NAME;
    private final String ITEM_PRICE;
    private final String ITEM_QTY;

    //=================== CREATING CONSTRUCTOR ===================

    public CartItemModel(String CART_ID,String USER_ID, String ITEM_ID, String RESTAURANT_ID, String RESTAURANT_NAME, String ITEM_IMAGE, String ITEM_NAME, String ITEM_PRICE, String ITEM_QTY) {
        this.CART_ID=CART_ID;
        this.USER_ID = USER_ID;
        this.ITEM_ID = ITEM_ID;
        this.RESTAURANT_ID = RESTAURANT_ID;
        this.RESTAURANT_NAME = RESTAURANT_NAME;
        this.ITEM_IMAGE = ITEM_IMAGE;
        this.ITEM_NAME = ITEM_NAME;
        this.ITEM_PRICE = ITEM_PRICE;
        this.ITEM_QTY = ITEM_QTY;
    }

    //=================== GETTERS ====================


    public String getCART_ID() {
        return CART_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public String getITEM_ID() {
        return ITEM_ID;
    }

    public String getRESTAURANT_ID() {
        return RESTAURANT_ID;
    }

    public String getRESTAURANT_NAME() {
        return RESTAURANT_NAME;
    }

    public String getITEM_IMAGE() {
        return ITEM_IMAGE;
    }

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public String getITEM_PRICE() {
        return ITEM_PRICE;
    }

    public String getITEM_QTY() {
        return ITEM_QTY;
    }

}
