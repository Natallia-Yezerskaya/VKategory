package com.natallia.vkategory.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "photo")
public class Photo {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String vkURL_130;
    @DatabaseField
    private String vkURL_640;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Note note;

    public Photo() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVkURL_640() {
        return vkURL_640;
    }

    public void setVkURL_640(String vkURL_640) {
        this.vkURL_640 = vkURL_640;
    }

    public String getVkURL_130() {
        return vkURL_130;
    }

    public void setVkURL_130(String vkURL_130) {
        this.vkURL_130 = vkURL_130;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
