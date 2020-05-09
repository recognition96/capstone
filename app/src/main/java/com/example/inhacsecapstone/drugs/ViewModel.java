package com.example.inhacsecapstone.drugs;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<MedicineEntity>> mAllMedicine;
    private LiveData<List<TakesEntity>> mAllTakes;

    public ViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllMedicine = mRepository.getAllDrugs();
        mAllTakes = mRepository.getAllTakes();
    }
    public LiveData<List<MedicineEntity>> getAllDrugs() {
        return mAllMedicine;
    }
    public LiveData<List<TakesEntity>> getAllTakes() { return mAllTakes; }

    public void insert(MedicineEntity item) { mRepository.insert(item);
    }
    public void insert(TakesEntity item) { mRepository.insert(item);
    }
    public void update(TakesEntity item) {mRepository.update(item);}

    public LiveData<List<MedicineEntity>> getMediAtDay(String day) {return mRepository.getMediAtDay(day);}
    public LiveData<List<TakesEntity>> getTakesAtDay(String day) { return mRepository.getTakesAtDay(day); }
}