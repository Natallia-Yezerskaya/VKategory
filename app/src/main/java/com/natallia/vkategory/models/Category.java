package com.natallia.vkategory.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "categories")


public class Category {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    //@DatabaseField(columnName = "id_p", foreign = true)
    //private int id_parent; // todo поменять на тип category

    public static final String FIELD_ID = "id";
// ORMLite требует пустой конструктор
    public Category() {

    }
    public Category(String name) {
        //this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public int getId_parent() {
        return id_parent;
    }

    public void setId_parent(int id_parent) {
        this.id_parent = id_parent;
    }
    */
}
