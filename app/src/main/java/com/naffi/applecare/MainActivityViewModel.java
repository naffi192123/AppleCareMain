package com.naffi.applecare;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivityViewModel extends AndroidViewModel {

    private Repository repository;

    private MutableLiveData<Weather> myweather = new MutableLiveData<>();


    private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();

    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }

    public MainActivityViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.repository = new Repository(application.getApplicationContext());
        repository.getString(new VolleyResponseListener() {
            @Override
            public void onError(String message) {
//                Log.d("ahanger","inerror");
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                int id = response.getInt("id");
                int temperature = response.getInt("temperature");
                String location = response.getString("location");
                String datetime=response.getString("dateTime");
                String sunRise=response.getString("sunRiseTime");
                String sunSet=response.getString("sunSetTime");
                String weatherInfo=response.getString("weatherInfo");
                Weather weather = new Weather(id,temperature,location,datetime,sunRise,sunSet,weatherInfo);
                myweather.postValue(weather);
            }
        });
    }

    public MutableLiveData<Weather> getMyweather() {
        return myweather;
    }
}
