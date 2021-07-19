package com.naffi.applecare.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.naffi.applecare.Login;
import com.naffi.applecare.MainActivityViewModel;
import com.naffi.applecare.R;
import com.naffi.applecare.SessionManagement;
import com.naffi.applecare.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private MainActivityViewModel viewModel;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    Button logout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this method will remove session and move to main activity
                SessionManagement sessionManagement = new SessionManagement(getActivity());
                sessionManagement.removeSession();
                moveToLogInScreen();
            }
        });

        // UI elements of the fragment
        TextView date,year,location,temperature,sunRizeTime,sunSetTime,weatherInfo;
        date = view.findViewById(R.id.tv_date);
        year = view.findViewById(R.id.tv_year);
        location = view.findViewById(R.id.tv_location);
        temperature = view.findViewById(R.id.tv_temp);
        sunRizeTime = view.findViewById(R.id.tv_sunRise);
        sunSetTime = view.findViewById(R.id.tv_sunSet);
        weatherInfo = view.findViewById(R.id.tv_weather_info);

        // Populate UI elements from Live data
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        viewModel.getMyweather().observe(getActivity(),item->{

            location.setText(item.getLocation());
            String strTemp = Integer.toString(item.getTemperature())+"\u2103";
            temperature.setText(strTemp);
            sunRizeTime.setText(item.getSinRiseTime());
            sunSetTime.setText(item.getSunSetTime());
            weatherInfo.setText(item.getWeatherInfo());
        });
        // current date time
        final Calendar c = Calendar.getInstance();
        String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        date.setText(Integer.toString(dd)+" "+month);
        year.setText(Integer.toString(yy));



    }

//    public void logout(View view) {
//        // this method will remove session and move to main activity
//        SessionManagement sessionManagement = new SessionManagement(getActivity());
//        sessionManagement.removeSession();
//        moveToLogInScreen();
//    }
    private void moveToLogInScreen(){
        Intent intent = new Intent(getActivity(), Login.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        finish();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}