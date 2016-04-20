package com.natallia.vkategory.database.DAO;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.natallia.vkategory.models.Photo;

import java.sql.SQLException;
import java.util.List;

public class PhotoDAO extends BaseDaoImpl<Photo, Integer>{

    public PhotoDAO(ConnectionSource connectionSource,
                    Class<Photo> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Photo> getAllPhotos() throws SQLException{
        return this.queryForAll();
    }
}