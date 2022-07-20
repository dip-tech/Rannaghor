package com.example.rannaghor;

public class RestaurentModel {
    //DECLARING RESTAURANTS MODEL MEMBERS
    private String RESTAURENT_ID;
    private String RESTAURENT_NAME;
    private String RESTAURENT_IMG;
    private String TAGS;
    private int IS_TOP;
    private int IS_NEAR;
    //=======================================

    //CONSTRUCTOR
    public RestaurentModel(String RESTAURENT_ID, String RESTAURENT_NAME, String RESTAURENT_IMG, String TAGS, int IS_TOP, int IS_NEAR) {
        this.RESTAURENT_ID = RESTAURENT_ID;
        this.RESTAURENT_NAME = RESTAURENT_NAME;
        this.RESTAURENT_IMG = RESTAURENT_IMG;
        this.TAGS = TAGS;
        this.IS_TOP = IS_TOP;
        this.IS_NEAR = IS_NEAR;
    }
    //=======================================

    //GETTERS

    public String getRESTAURENT_ID() {
        return RESTAURENT_ID;
    }

    public String getRESTAURENT_NAME() {
        return RESTAURENT_NAME;
    }

    public String getRESTAURENT_IMG() {
        return RESTAURENT_IMG;
    }

    public String getTAGS() {
        return TAGS;
    }

    public int getIS_TOP() {
        return IS_TOP;
    }

    public int getIS_NEAR() {
        return IS_NEAR;
    }
    //=======================================
}