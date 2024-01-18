package com.example.Accounting_Application;

import android.util.Log;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;

public class Item extends LitePalSupport implements Serializable {
    private String item_notes;
    private String item_name;
    private String item_type;
    private double item_value;
    private String item_image;
   private String item_date;

    private static final String TAG = "Item";


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

    public String getItem_notes() {
        return item_notes;
    }

    public void setItem_notes(String item_notes) {
        this.item_notes = item_notes;
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

    public String getItem_date() {
        return item_date;
    }

    public void setItem_date(String item_date) {
        this.item_date = item_date;
    }

    public Item( String item_name, String item_type, double item_value, String date) {
        setItem_notes("随便说点什么吧");
        setItem_name(item_name);
        setItem_type(item_type);
        setItem_value(item_value);
        setItem_date(date);
        //占位图片
        item_image="@drawable/ic_menu_slideshow.xml";
    }

    public void ReNewItem(Item item){
        setItem_notes(item.getItem_notes());
        setItem_name(item.getItem_name());
        setItem_type(item.getItem_type());
        setItem_value(item.getItem_value());
        setItem_date(item.getItem_date());
        save();
    }

    public static Item saveItem(Item item){
        item.save();
        Log.d(TAG, "saveItem: "+new Date());
        return item;
    }

}
