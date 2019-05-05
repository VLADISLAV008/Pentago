package com.example.vlad.pentago;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button two_players, one_player, rules, network_game;
    private MediaPlayer bgMp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        two_players = findViewById(R.id.two_players);
        one_player = findViewById(R.id.one_player);
        network_game = findViewById(R.id.network_game);
        rules = findViewById(R.id.rules);
        bgMp = MediaPlayer.create(this, R.raw.music_pentago);
        bgMp.setLooping(true);
        bgMp.start();

        two_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TwoPlayers.class));
            }
        });
        one_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PlayerVSComputer.class));
            }
        });
        network_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Network_game.class));
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
        bgMp = MediaPlayer.create(this, R.raw.music_pentago);
        bgMp.setLooping(true);
        bgMp.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bgMp.setLooping(false);
        bgMp.stop();
    }
}
