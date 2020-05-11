package com.example.inhacsecapstone.Entity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inhacsecapstone.Entity.MedicineEntity;
import com.example.inhacsecapstone.Entity.TakesEntity;

import java.util.List;

@Dao
public interface ListDao {
    @Insert
    void insert(MedicineEntity medi);

    @Insert
    void insert(TakesEntity take);

    @Query("DELETE FROM medicine_list")
    void deleteMedicineAll();

    @Query("DELETE FROM takes")
    void deleteTakesAll();

    @Query("SELECT * from medicine_list  ORDER BY name ASC")
    LiveData<List<MedicineEntity>> getAllMedicine();

    @Query("SELECT * FROM takes")
    LiveData<List<TakesEntity>> getAllTakes();


    @Query("SELECT * FROM medicine_list LEFT JOIN takes ON medicine_list.code = takes.code WHERE day = :day")
    LiveData<List<MedicineEntity>> getMedicineAtDay(String day);

    @Query("SELECT * FROM takes WHERE day = :day")
    LiveData<List<TakesEntity>> getTakesAtDay(String day);

    @Update
    void update(TakesEntity take);
}
