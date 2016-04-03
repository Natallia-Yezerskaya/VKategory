package com.natallia.vkategory.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "post")
public class Post {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String text;
    @DatabaseField
    private int vkId;
    @DatabaseField(columnName = "id_photo", foreign = true)
    private Photo photo;

    

    public Post() {

    }
    public Post(String name) {
        //this.id = id;
        this.text = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVkId() {
        return vkId;
    }

    public void setVkId(int vkId) {
        this.vkId = vkId;
    }
}
