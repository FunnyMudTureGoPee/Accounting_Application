package com.example.Accounting_Application;

import java.io.Serializable;

public class Item implements Serializable {
    private int item_key;
    private String item_name;
    private String item_type;
    private double item_value;
    private String item_image;


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public int getItem_key() {
        return item_key;
    }

    public void setItem_key(int item_key) {
        this.item_key = item_key;
    }

    public double getItem_value() {
        return item_value;
    }

    public void setItem_value(double item_value) {
        this.item_value = item_value;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public Item(int item_key, String item_name, String item_type, double item_value) {
        this.item_key = item_key;
        this.item_name = item_name;
        this.item_type = item_type;
        this.item_value = item_value;
        //占位图片
        item_image="@drawable/ic_menu_slideshow.xml";
    }
}
