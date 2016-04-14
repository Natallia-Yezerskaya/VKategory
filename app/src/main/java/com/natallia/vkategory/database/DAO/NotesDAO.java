package com.natallia.vkategory.database.DAO;

import android.content.Intent;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.natallia.vkategory.models.Category;
import com.natallia.vkategory.models.Note;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Natallia on 23.03.2016.
 */
public class NotesDAO extends BaseDaoImpl<Note, Integer>{

    public NotesDAO(ConnectionSource connectionSource,
                    Class<Note> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Note> getAllNotes() throws SQLException{
        return this.queryForAll();
    }

    public Note getNoteById(int id) throws SQLException{
        return this.queryForId(id);
    }

}