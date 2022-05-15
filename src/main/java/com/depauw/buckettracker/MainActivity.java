package com.depauw.buckettracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import com.depauw.buckettracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener{

    private ActivityMainBinding binding;
    private static final int DEFAULT_NUM_MINS = 20;
    private static final int MILLIS_PER_MIN = 60000;
    private static final int MILLIS_PER_SEC = 1000;
    private static final int SECS_PER_MIN = 60;
    private static final int COUNTDOWN_START = 0;
    private CountDownTimer countDownTimer;
    private long timeRemaining = DEFAULT_NUM_MINS * MILLIS_PER_MIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonAddScore.setOnLongClickListener(this);
        binding.buttonSetTime.setOnClickListener(this);
        binding.switchGameClock.setOnCheckedChangeListener(this);
        binding.toggleIsGuest.setOnCheckedChangeListener(this);
        changeHomeSettings();
    }

    public CountDownTimer getNewTimer(long timerTotalLength, long timerTickLength){
        return new CountDownTimer(timerTotalLength,timerTickLength) {
            @Override
            public void onTick(long l) {
                long minuteValue = (l / MILLIS_PER_SEC) / SECS_PER_MIN;
                long secondValue = (l / MILLIS_PER_SEC) % SECS_PER_MIN;
                binding.textviewTimeRemaining.setText(minuteValue + ":" + secondValue);
            }

            @Override
            public void onFinish() {
                binding.switchGameClock.setChecked(false);
            }
        };
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case R.id.switch_game_clock:{
                if (binding.switchGameClock.isChecked()) {
                    countDownTimer = getNewTimer(timeRemaining, MILLIS_PER_SEC);
                    countDownTimer.start();
                } else {
                    String label = binding.textviewTimeRemaining.getText().toString();
                    String[] temp = label.split(":");
                    long minutesRemaining = Integer.valueOf(temp[0]) * MILLIS_PER_MIN; //0 denotes the first index in our temp array
                    long secondsRemaining = Integer.valueOf(temp[1]) * MILLIS_PER_SEC; //1 denotes the second index in our temp array
                    timeRemaining = minutesRemaining + secondsRemaining;
                    countDownTimer.cancel();
                }
            }
            case R.id.toggle_is_guest: {
                if (binding.toggleIsGuest.isChecked()) {
                    changeGuestSettings();
                } else {
                    changeHomeSettings();
                }
            }
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()) { //switch added so its easier to add more views later if needed
            case R.id.button_set_time:{
                if (!TextUtils.isEmpty(binding.edittextNumMins.getText().toString()) && !TextUtils.isEmpty(binding.edittextNumSecs.getText().toString())){
                    String editTextMinutes = binding.edittextNumMins.getText().toString();
                    String editTextSeconds = binding.edittextNumSecs.getText().toString();
                    int minutes = Integer.valueOf(editTextMinutes);
                    int seconds = Integer.valueOf(editTextSeconds);

                    if(minutes >= COUNTDOWN_START && minutes < DEFAULT_NUM_MINS && seconds >= COUNTDOWN_START && seconds < SECS_PER_MIN) {
                        binding.switchGameClock.setChecked(false);

                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        long minutesRemaining = minutes * MILLIS_PER_MIN;
                        long secondsRemaining = seconds * MILLIS_PER_SEC;
                        timeRemaining = minutesRemaining + secondsRemaining;
                        long minuteValue = (timeRemaining / MILLIS_PER_SEC) / SECS_PER_MIN;
                        long secondValue = (timeRemaining / MILLIS_PER_SEC) % SECS_PER_MIN;
                        binding.textviewTimeRemaining.setText(minuteValue + ":" + secondValue);
                    }
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v){
        if(!binding.checkboxAddOne.isChecked() && !binding.checkboxAddTwo.isChecked() && !binding.checkboxAddThree.isChecked()){
            return true;
        }

        int sum = 0;
        if(binding.checkboxAddOne.isChecked()){
            sum = sum + 1;
        }
        if(binding.checkboxAddTwo.isChecked()){
            sum = sum + 2;
        }
        if(binding.checkboxAddThree.isChecked()){
            sum = sum + 3;
        }

        if(binding.toggleIsGuest.isChecked()){
            int before = Integer.valueOf(binding.textviewGuestScore.getText().toString());
            int after = before + sum;
            binding.textviewGuestScore.setText(String.valueOf(after));
        }
        else{
            int before = Integer.valueOf(binding.textviewHomeScore.getText().toString());
            int after = before + sum;
            binding.textviewHomeScore.setText(String.valueOf(after));
        }

        binding.checkboxAddOne.setChecked(false);
        binding.checkboxAddTwo.setChecked(false);
        binding.checkboxAddThree.setChecked(false);

        if(binding.toggleIsGuest.isChecked()){
            binding.toggleIsGuest.setChecked(false);
            changeHomeSettings();
        }
        else{
            binding.toggleIsGuest.setChecked(true);
            changeGuestSettings();
        }
        return true;
    }

    public void changeHomeSettings(){
        binding.labelHome.setTextColor(getResources().getColor(R.color.red, getTheme()));
        binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
        binding.labelGuest.setTextColor(getResources().getColor(R.color.black, getTheme()));
        binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.black, getTheme()));
    }

    public void changeGuestSettings(){
        binding.labelGuest.setTextColor(getResources().getColor(R.color.red, getTheme()));
        binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
        binding.labelHome.setTextColor(getResources().getColor(R.color.black, getTheme()));
        binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.black, getTheme()));
    }

}