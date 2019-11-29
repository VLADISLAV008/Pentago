package com.vlad.pentago;

import android.content.Intent;
import android.media.MediaPlayer;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ModeWithComputerActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    private String modeComputer = "hard";
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_with_computer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button play = findViewById(R.id.play);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        mp = MediaPlayer.create(this, R.raw.click);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_easy:
                        buttonSound();
                        modeComputer = "easy";
                        break;
                    case R.id.radio_middle:
                        buttonSound();
                        modeComputer = "middle";
                        break;
                    case R.id.radio_hard:
                        buttonSound();
                        modeComputer = "hard";
                        break;
                    default:
                        modeComputer = "hard";
                        break;
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound();
                Move_Computer.mode = modeComputer;
                startActivity(new Intent(ModeWithComputerActivity.this, GameActivity.class));
            }
        });
    }

    private void buttonSound()
    {
        if (MainActivity.musicOn) {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(this, R.raw.click);
            }
            mp.start();
        }
    }
}
