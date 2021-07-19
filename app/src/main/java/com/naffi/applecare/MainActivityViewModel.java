package com.naffi.applecare;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public class MainActivityViewModel extends AndroidViewModel {
    private Repository repository = new Repository();
    private MutableLiveData<Weather> myweather = new MutableLiveData<>();

    private int user_id=1;



    public MainActivityViewModel(@NonNull @NotNull Application application) {
        super(application);
        myweather.postValue(repository.getWeather());
    }
    public LiveData<Weather> getMyweather() {
        return myweather;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
