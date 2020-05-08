package com.example.inhacsecapstone.drugs;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<DrugItem>> mAllWords;

    public ViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<DrugItem>> getAllWords() { return mAllWords; }

    public void insert(DrugItem item) { mRepository.insert(item); }
}