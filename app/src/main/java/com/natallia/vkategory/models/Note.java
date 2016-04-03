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

    //@DatabaseField (columnName = "id_photo", foreign = true, foreignAutoRefresh = true)
    //private Photo photo;


    public Note() {

        try {
            HelperFactory.getHelper().getNoteDao().assignEmptyForeignCollection(this, "photos");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*photos = new ForeignCollection<Photo>() {
            @Override
            public CloseableIterator<Photo> iterator(int i) {
                return null;
            }

            @Override
            public CloseableIterator<Photo> closeableIterator(int i) {
                return null;
            }

            @Override
            public CloseableIterator<Photo> iteratorThrow() throws SQLException {
                return null;
            }

            @Override
            public CloseableIterator<Photo> iteratorThrow(int i) throws SQLException {
                return null;
            }

            @Override
            public CloseableWrappedIterable<Photo> getWrappedIterable() {
                return null;
            }

            @Override
            public CloseableWrappedIterable<Photo> getWrappedIterable(int i) {
                return null;
            }

            @Override
            public void closeLastIterator() throws SQLException {

            }

            @Override
            public boolean isEager() {
                return false;
            }

            @Override
            public int update(Photo photo) throws SQLException {
                return 0;
            }

            @Override
            public int updateAll() throws SQLException {
                return 0;
            }

            @Override
            public int refresh(Photo photo) throws SQLException {
                return 0;
            }

            @Override
            public int refreshAll() throws SQLException {
                return 0;
            }

            @Override
            public int refreshCollection() throws SQLException {
                return 0;
            }

            @Override
            public boolean add(Photo photo) {
                return false;
            }

            @Override
            public CloseableIterator<Photo> closeableIterator() {
                return null;
            }

            @Override
            public boolean addAll(Collection<? extends Photo> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object object) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Photo> iterator() {
                return null;
            }

            @Override
            public boolean remove(Object object) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] array) {
                return null;
            }
        };*/
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

//    public Photo getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(Photo photo) {
//        this.photo = photo;
//    }

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
}
