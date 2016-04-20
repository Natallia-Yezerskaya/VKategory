package com.natallia.vkategory.models;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.natallia.vkategory.database.DAO.NotesDAO;
import com.natallia.vkategory.database.HelperFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@DatabaseTable(tableName = "notes")
public class Note {
    public static final String FIELD_CATEGORY_ID = "id_category";
    public static final String FIELD_ID = "id";


    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String text;

    @DatabaseField (unique=true)
    private int vkID;

    @DatabaseField(columnName = "id_category", foreign = true)
    private Category category;


    @ForeignCollectionField(eager = true)
    private ForeignCollection<Photo> photos;


    @DatabaseField
    private String sourceName;

    @DatabaseField
    private String sourcePhoto_50;

    @DatabaseField
    private String sourcePhoto_100;

    @DatabaseField
    private long date;

    @DatabaseField()
    private int from_id;

    @DatabaseField()
    private int owner_id;


    public Note() {

        try {
            HelperFactory.getHelper().getNoteDao().assignEmptyForeignCollection(this, "photos");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addPhoto(Photo value){
        value.setNote(this);
        photos.add(value);
    }

    public void removePhoto(Photo value){
        photos.remove(value);
        try {
            HelperFactory.getHelper().getPhotoDao().delete(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ForeignCollection<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ForeignCollection<Photo> photos) {
        this.photos = photos;
    }

    public static String getFieldId() {
        return FIELD_ID;
    }

    public static String getFieldCategoryId() {
        return FIELD_CATEGORY_ID;
    }

    public int getVkID() {
        return vkID;
    }

    public void setVkID(int vkID) {
        this.vkID = vkID;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourcePhoto_50() {
        return sourcePhoto_50;
    }

    public void setSourcePhoto_50(String sourcePhoto_50) {
        this.sourcePhoto_50 = sourcePhoto_50;
    }

    public String getSourcePhoto_100() {
        return sourcePhoto_100;
    }

    public void setSourcePhoto_100(String sourcePhoto_100) {
        this.sourcePhoto_100 = sourcePhoto_100;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }
}
