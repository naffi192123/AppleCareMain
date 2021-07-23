package com.naffi.applecare.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.github.drjacky.imagepicker.ImagePicker;
import com.naffi.applecare.Detection;
import com.naffi.applecare.Login;
import com.naffi.applecare.MainActivityViewModel;
import com.naffi.applecare.R;
import com.naffi.applecare.SessionManagement;
import com.naffi.applecare.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

import static android.app.Activity.RESULT_OK;

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

        Button capture = root.findViewById(R.id.btn_takePicture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(getActivity())
                        .crop()
//                        .cropOval()
                        .maxResultSize(512, 512, true)
                        .createIntentFromDialog((Function1) (new Function1() {
                            public Object invoke(Object var1) {
                                this.invoke((Intent) var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it) {
                                Intrinsics.checkNotNullParameter(it, "it");
                                launcher.launch(it);
                            }
                        }));
            }
        });



        return root;
    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                   // Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Detection.class);
                    intent.putExtra("imageUri", uri.toString());
                    startActivity(intent);
                    // Use the uri to load the image
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });

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
//        viewModel.getTempTest().observe(getActivity(),item->{
//            temperature.setText(item);
//        });
        // current date time
        final Calendar c = Calendar.getInstance();
        String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        date.setText(Integer.toString(dd)+" "+month);
        year.setText(Integer.toString(yy));

        // Camera Button functionality start

        // Camera Button functionality end

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