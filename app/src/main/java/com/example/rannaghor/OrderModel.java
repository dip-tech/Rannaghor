package com.example.rannaghor;

import java.util.ArrayList;

public class OrderModel {
    // ================ DECLARING OBJECTS AND VARIABLES =====================
    private final String ORDER_ID;
    private final String RESTAURENT_ID;
    private final String RESTAURENT_NAME;
    private final String TOTAL_AMOUNT;
    private final String DELIVERY_ADDRESS;
    private final String GRAND_TOTAL;
    private final String TAX_AND_CHARGES;
    private final String USER_ID;
    private final String ORDER_STATUS="Order Placed";
    private final String ORDER_DATE;
    private final String ORDER_TIME;
    private final ArrayList<OrderItemModel> ORDER_ITEMS;

    // ===================== CONSTRUCTOR ==================
    public OrderModel(String ORDER_ID, String RESTAURENT_ID, String RESTAURENT_NAME, String TOTAL_AMOUNT, String DELIVERY_ADDRESS, String GRAND_TOTAL, String TAX_AND_CHARGES, String USER_ID,String ORDER_DATE,String ORDER_TIME, ArrayList<OrderItemModel> ORDER_ITEMS) {
        this.ORDER_ID = ORDER_ID;
        this.RESTAURENT_ID = RESTAURENT_ID;
        this.RESTAURENT_NAME = RESTAURENT_NAME;
        this.TOTAL_AMOUNT = TOTAL_AMOUNT;
        this.DELIVERY_ADDRESS = DELIVERY_ADDRESS;
        this.GRAND_TOTAL = GRAND_TOTAL;
        this.TAX_AND_CHARGES = TAX_AND_CHARGES;
        this.USER_ID = USER_ID;
        this.ORDER_DATE=ORDER_DATE;
        this.ORDER_TIME=ORDER_TIME;
        this.ORDER_ITEMS = ORDER_ITEMS;
    }
    // =================== GETTER METHODS =======================

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public String getRESTAURENT_ID() {
        return RESTAURENT_ID;
    }

    public String getRESTAURENT_NAME() {
        return RESTAURENT_NAME;
    }

    public String getTOTAL_AMOUNT() {
        return TOTAL_AMOUNT;
    }

    public String getDELIVERY_ADDRESS() {
        return DELIVERY_ADDRESS;
    }

    public String getGRAND_TOTAL() {
        return GRAND_TOTAL;
    }

    public String getTAX_AND_CHARGES() {
        return TAX_AND_CHARGES;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public String getORDER_STATUS() {
        return ORDER_STATUS;
    }

    public String getORDER_DATE() {
        return ORDER_DATE;
    }

    public String getORDER_TIME() {
        return ORDER_TIME;
    }

    public ArrayList<OrderItemModel> getORDER_ITEMS() {
        return ORDER_ITEMS;
    }
}
