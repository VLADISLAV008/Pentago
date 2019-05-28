package com.vlad.pentago;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.vlad.pentago.R;

public class MainActivity extends AppCompatActivity {

    private Button two_players, one_player, rules, network_game, setting;
    private boolean musicOn = true;
    private MediaPlayer bgMp;
    SharedPreferences sPref;
    private final String MUSIC = "music";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //MobileAds.initialize(this, "ca-app-pub-7091507023849858~6034969978");

        two_players = findViewById(R.id.two_players);
        one_player = findViewById(R.id.one_player);
        network_game = findViewById(R.id.network_game);
        setting = findViewById(R.id.setting);
        rules = findViewById(R.id.rules);

        sPref = getPreferences(MODE_PRIVATE);
        musicOn = sPref.getBoolean(MUSIC,true);

        if (musicOn) {
            bgMp = MediaPlayer.create(this, R.raw.music_pentago);
            bgMp.setLooping(true);
            bgMp.start();
        }

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_setting, null);
                dialogBuilder.setView(dialogView);
                final Button music = (Button) dialogView.findViewById(R.id.music);
                if (musicOn)
                    music.setBackground(getResources().getDrawable(R.drawable.music_icon));
                else
                    music.setBackground(getResources().getDrawable(R.drawable.music_icon_off));
                music.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (musicOn) {
                            music.setBackground(getResources().getDrawable(R.drawable.music_icon_off));
                            musicOn = false;
                            bgMp.setLooping(false);
                            bgMp.stop();
                        } else {
                            music.setBackground(getResources().getDrawable(R.drawable.music_icon));
                            musicOn = true;
                            bgMp = MediaPlayer.create(MainActivity.this, R.raw.music_pentago);
                            bgMp.setLooping(true);
                            bgMp.start();
                        }
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
        two_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.mode = "2p";
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        one_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.mode = "1p";
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        network_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NetworkActivity.class));
            }
        });
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Rules.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (musicOn) {
            bgMp = MediaPlayer.create(this, R.raw.music_pentago);
            bgMp.setLooping(true);
            bgMp.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicOn) {
            bgMp.setLooping(false);
            bgMp.stop();
        }
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(MUSIC, musicOn);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
