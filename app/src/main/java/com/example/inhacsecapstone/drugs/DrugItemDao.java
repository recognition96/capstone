package com.example.inhacsecapstone.drugs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DrugItemDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert
    void insert(DrugItem word);

    @Query("DELETE FROM medicine_list")
    void deleteAll();

    @Query("SELECT * from medicine_list ORDER BY name ASC")
    LiveData<List<DrugItem>> getAlphabetizedWords();
}
