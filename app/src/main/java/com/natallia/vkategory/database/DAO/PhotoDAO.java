package com.natallia.vkategory.database.DAO;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.natallia.vkategory.models.Photo;
import com.natallia.vkategory.models.Post;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Natallia on 23.03.2016.
 */
public class PhotoDAO extends BaseDaoImpl<Photo, Integer>{

    public PhotoDAO(ConnectionSource connectionSource,
                    Class<Photo> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Photo> getAllPosts() throws SQLException{
        return this.queryForAll();
    }
}