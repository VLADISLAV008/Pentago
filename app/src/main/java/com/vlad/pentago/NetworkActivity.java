package com.vlad.pentago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vlad.pentago.R;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Timer;
import java.util.TimerTask;

public class NetworkActivity extends AppCompatActivity {

    private EditText editText;
    private Button play;
    private String name;
    private NetworkClient networkClient;
    private ProgressBar progressBar;
    private boolean click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        networkClient = new NetworkClient(getApplicationContext());
        progressBar = findViewById(R.id.progressBar);
        editText = findViewById(R.id.nameOfGame);
        play = findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click) {
                    click = true;
                    name = editText.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    networkClient.publishQuestion(name);

                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (networkClient.getMqttAndroidClient().isConnected()) {
                                if (networkClient.getCountOfAnswer() == 0 && networkClient.getCountOfQuestion() == 1) {
                                    Runnable r = new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    };
                                    runOnUiThread(r);
                                    try {
                                        networkClient.getMqttAndroidClient().disconnect();
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                    GameActivity.mode = "network";
                                    GameActivity.network_player = 1;
                                    GameActivity.nameOfGame = name;
                                    startActivity(new Intent(NetworkActivity.this, GameActivity.class));
                                }
                                if ((networkClient.getCountOfAnswer() == 0 && networkClient.getCountOfQuestion() == 2) || (networkClient.getCountOfAnswer() == 1 && networkClient.getCountOfQuestion() == 1)) {
                                    Runnable r = new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    };
                                    runOnUiThread(r);
                                    try {
                                        networkClient.getMqttAndroidClient().disconnect();
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                    GameActivity.mode = "network";
                                    GameActivity.network_player = 2;
                                    GameActivity.nameOfGame = name;
                                    startActivity(new Intent(NetworkActivity.this, GameActivity.class));
                                }
                                if (networkClient.getCountOfAnswer() > 1 || networkClient.getCountOfQuestion() > 2 || (networkClient.getCountOfAnswer() == 1 && networkClient.getCountOfQuestion() == 2)) {
                                    Runnable r = new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(NetworkActivity.this, "Это имя занято. Введите другое имя.", Toast.LENGTH_LONG).show();
                                        }
                                    };
                                    runOnUiThread(r);
                                    click = false;
                                }
                            } else {
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(NetworkActivity.this, "Не получается подключиться к сервру.", Toast.LENGTH_LONG).show();
                                    }
                                };
                                runOnUiThread(r);
                                click = false;
                            }
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask, 3000);
                }

            }
        });
    }
}
