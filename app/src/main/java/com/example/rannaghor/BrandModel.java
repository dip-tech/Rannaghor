package com.example.rannaghor;

public class BrandModel {
    private String BRAND_ID;
    private String BRAND_IMAGE;
    private String BRAND_NAME;
    private String TAGS;
    private int IS_NEAR;
    private int IS_TOP;

    //=============== CONSTRUCTOR ===================

    public BrandModel(String BRAND_ID, String BRAND_IMAGE, String BRAND_NAME, String TAGS, int IS_NEAR, int IS_TOP) {
        this.BRAND_ID = BRAND_ID;
        this.BRAND_IMAGE = BRAND_IMAGE;
        this.BRAND_NAME = BRAND_NAME;
        this.TAGS = TAGS;
        this.IS_NEAR = IS_NEAR;
        this.IS_TOP = IS_TOP;
    }


    //======================= GETTER METHODS ============================

    public String getBRAND_ID() {
        return BRAND_ID;
    }

    public String getBRAND_IMAGE() {
        return BRAND_IMAGE;
    }

    public String getBRAND_NAME() {
        return BRAND_NAME;
    }

    public String getTAGS() {
        return TAGS;
    }

    public int getIS_NEAR() {
        return IS_NEAR;
    }

    public int getIS_TOP() {
        return IS_TOP;
    }
}
