package com.vlad.pentago;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.vlad.pentago.R;

import org.eclipse.paho.client.mqttv3.MqttException;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    static public String mode = "";
    private Field field;
    private String white_move, black_move, waitSecondPlayer;
    private final String p2 = "2p", p1 = "1p", network = "network";
    private int countOfGames = 0, kordX, kordY;
    private float value;
    private boolean arrow_pressed = true,
            isAnimate = false,
            game_is_over = false;
    private Button array[][] = new Button[6][6],
            arrow[] = new Button[8],
            play_again;
    private TableLayout left_top, right_top, left_bottom, right_bottom;
    private ImageView imageView;
    private TextView textView, move;
    private ProgressBar progressBar;
    private AdView mAdView;

    static public int network_player = 0;
    static public String nameOfGame;
    private NetworkClient networkClient;
    private TextView textViewColourOfPlayer;
    private ImageView imageViewColourOfPlayer;
    private boolean arrivedMove = false, wait = false;

    private Move_Computer bot = new Move_Computer();
    private Move move_computer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        networkClient = new NetworkClient(getApplicationContext(), network_player, nameOfGame);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        textViewColourOfPlayer = findViewById(R.id.textViewColourOfPlayer);
        imageViewColourOfPlayer = findViewById(R.id.imageViewColourOfPlayer);
        move = findViewById(R.id.move);
        play_again = findViewById(R.id.play_again);
        progressBar = findViewById(R.id.progressBar);
        play_again.setOnClickListener(this);

        arrow[0] = findViewById(R.id.arrow_left_top_clockwise);
        arrow[1] = findViewById(R.id.arrow_right_top_counter_clockwise);
        arrow[2] = findViewById(R.id.arrow_right_top_clockwise);
        arrow[3] = findViewById(R.id.arrow_right_bottom_counter_clockwise);
        arrow[4] = findViewById(R.id.arrow_right_bottom_clockwise);
        arrow[5] = findViewById(R.id.arrow_left_bottom_counter_clockwise);
        arrow[6] = findViewById(R.id.arrow_left_bottom_clockwise);
        arrow[7] = findViewById(R.id.arrow_left_top_counter_clockwise);
        for (int i = 0; i < 8; i++)
            arrow[i].setOnClickListener(this);

        left_top = findViewById(R.id.left_top);
        right_top = findViewById(R.id.right_top);
        left_bottom = findViewById(R.id.left_bottom);
        right_bottom = findViewById(R.id.right_bottom);

        array[0][0] = findViewById(R.id.btn1);
        array[0][1] = findViewById(R.id.btn2);
        array[0][2] = findViewById(R.id.btn3);
        array[0][3] = findViewById(R.id.btn19);
        array[0][4] = findViewById(R.id.btn20);
        array[0][5] = findViewById(R.id.btn21);
        array[1][0] = findViewById(R.id.btn4);
        array[1][1] = findViewById(R.id.btn5);
        array[1][2] = findViewById(R.id.btn6);
        array[1][3] = findViewById(R.id.btn22);
        array[1][4] = findViewById(R.id.btn23);
        array[1][5] = findViewById(R.id.btn24);
        array[2][0] = findViewById(R.id.btn7);
        array[2][1] = findViewById(R.id.btn8);
        array[2][2] = findViewById(R.id.btn9);
        array[2][3] = findViewById(R.id.btn25);
        array[2][4] = findViewById(R.id.btn26);
        array[2][5] = findViewById(R.id.btn27);
        array[3][0] = findViewById(R.id.btn10);
        array[3][1] = findViewById(R.id.btn11);
        array[3][2] = findViewById(R.id.btn12);
        array[3][3] = findViewById(R.id.btn28);
        array[3][4] = findViewById(R.id.btn29);
        array[3][5] = findViewById(R.id.btn30);
        array[4][0] = findViewById(R.id.btn13);
        array[4][1] = findViewById(R.id.btn14);
        array[4][2] = findViewById(R.id.btn15);
        array[4][3] = findViewById(R.id.btn31);
        array[4][4] = findViewById(R.id.btn32);
        array[4][5] = findViewById(R.id.btn33);
        array[5][0] = findViewById(R.id.btn16);
        array[5][1] = findViewById(R.id.btn17);
        array[5][2] = findViewById(R.id.btn18);
        array[5][3] = findViewById(R.id.btn34);
        array[5][4] = findViewById(R.id.btn35);
        array[5][5] = findViewById(R.id.btn36);

        white_move = getResources().getString(R.string.moveWight);
        black_move = getResources().getString(R.string.moveBlack);
        waitSecondPlayer = getResources().getString(R.string.waitingSecondPlayer);

        newGame();
        networkClient.setMoveCallback(new NetworkClient.MoveCallback() {
            @Override
            public void onOpponentMove(Move move) {
                arrivedMove = true;
                array[move.get_KordX()][move.get_KordY()].callOnClick();
                if (move.get_Rotation()) {
                    if (move.get_Left() && move.get_Top() && move.get_Clockwise())
                        arrow[0].callOnClick();
                    if (!move.get_Left() && move.get_Top() && !move.get_Clockwise())
                        arrow[1].callOnClick();
                    if (!move.get_Left() && move.get_Top() && move.get_Clockwise())
                        arrow[2].callOnClick();
                    if (!move.get_Left() && !move.get_Top() && !move.get_Clockwise())
                        arrow[3].callOnClick();
                    if (!move.get_Left() && !move.get_Top() && move.get_Clockwise())
                        arrow[4].callOnClick();
                    if (move.get_Left() && !move.get_Top() && !move.get_Clockwise())
                        arrow[5].callOnClick();
                    if (move.get_Left() && !move.get_Top() && move.get_Clockwise())
                        arrow[6].callOnClick();
                    if (move.get_Left() && move.get_Top() && !move.get_Clockwise())
                        arrow[7].callOnClick();
                }
                arrivedMove = false;
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onOpponentReady() {
                move_player();
                wait = false;
            }
        });
    }

    private void newGame() {
        field = new Field();
        countOfGames++;
        if (mode.equals(network)) {
            arrivedMove = false;
            wait = false;
        }
        textView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        if (countOfGames != 1) {
            imageView.setY(imageView.getY() - value);
            textView.setY(textView.getY() - value);
        }

        if (mode.equals(network)) {
            textViewColourOfPlayer.setVisibility(View.VISIBLE);
            if (network_player == 1)
                imageViewColourOfPlayer.setBackground(getResources().getDrawable(R.drawable.button_white));
            else
                imageViewColourOfPlayer.setBackground(getResources().getDrawable(R.drawable.button_black));
            imageViewColourOfPlayer.setVisibility(View.VISIBLE);
        }

        arrow_pressed = true;
        isAnimate = false;
        game_is_over = false;
        if (mode.equals(network) && network_player == 1) {
            move.setText(waitSecondPlayer);
            wait = true;
        } else move_player();

        move.setVisibility(View.VISIBLE);
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                array[i][j].setOnClickListener(this);
                array[i][j].setBackground(getResources().getDrawable(R.drawable.button_red_blank));
            }
        for (int i = 0; i < 8; i++)
            arrow[i].setAlpha(0);

        if(mode.equals(network))
        {
            if (network_player == 2)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void rotation_field_animate(final TableLayout tableLayout, final int kordX_ofLeftTop_cell, final int kordY_ofLeftTop_cell, final int duration, final int begin_translationX, final int begin_translationY, final int degree_of_rotation) {
        if (mode.equals(p2) || (mode.equals(network) && field.getPlayer() == network_player))
            setArrowVisible(View.INVISIBLE, 400, -100);
        if (mode.equals(p1))
            if (field.getPlayer() == 1) setArrowVisible(View.INVISIBLE, 400, -100);

        final Runnable rotation = new Runnable() {
            public void run() {
                Runnable move_field = new Runnable() {
                    public void run() {
                        tableLayout.animate().setDuration(duration).translationXBy(-begin_translationX).translationYBy(-begin_translationY).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                if (mode.equals(p1) || mode.equals(network)) {
                                    field.setPlayer();
                                    move_player();
                                    if (mode.equals(p1) && field.getPlayer() == 2 && !game_is_over)
                                        progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (mode.equals(p2)) {
                                    isAnimate = false;
                                    field.setPlayer();
                                    is_game_over();
                                    move_player();
                                    arrow_pressed = true;
                                }
                                if (mode.equals(p1)) {

                                    isAnimate = false;
                                    is_game_over();
                                    arrow_pressed = true;

                                    if (field.getPlayer() == 2 && !game_is_over) {
                                        move_computer = bot.move_computer(field.colourOfCell, 2);
                                        array[move_computer.get_KordX()][move_computer.get_KordY()].callOnClick();
                                        if (move_computer.get_Left() && move_computer.get_Top() && move_computer.get_Clockwise())
                                            arrow[0].callOnClick();
                                        if (!move_computer.get_Left() && move_computer.get_Top() && !move_computer.get_Clockwise())
                                            arrow[1].callOnClick();
                                        if (!move_computer.get_Left() && move_computer.get_Top() && move_computer.get_Clockwise())
                                            arrow[2].callOnClick();
                                        if (!move_computer.get_Left() && !move_computer.get_Top() && !move_computer.get_Clockwise())
                                            arrow[3].callOnClick();
                                        if (!move_computer.get_Left() && !move_computer.get_Top() && move_computer.get_Clockwise())
                                            arrow[4].callOnClick();
                                        if (move_computer.get_Left() && !move_computer.get_Top() && !move_computer.get_Clockwise())
                                            arrow[5].callOnClick();
                                        if (move_computer.get_Left() && !move_computer.get_Top() && move_computer.get_Clockwise())
                                            arrow[6].callOnClick();
                                        if (move_computer.get_Left() && move_computer.get_Top() && !move_computer.get_Clockwise())
                                            arrow[7].callOnClick();
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                if (mode.equals(network)) {
                                    isAnimate = false;
                                    is_game_over();
                                    arrow_pressed = true;
                                    if (network_player != field.getPlayer()) {
                                        networkClient.sendMove(new Move(kordX, kordY, true, degree_of_rotation == 90, kordY_ofLeftTop_cell == 0, kordX_ofLeftTop_cell == 0));
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                    if (game_is_over)
                                        progressBar.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                };
                tableLayout.animate().setDuration(duration).rotationBy(degree_of_rotation).withEndAction(move_field);

                for (int i = kordX_ofLeftTop_cell; i < kordX_ofLeftTop_cell + 3; i++)
                    for (int j = kordY_ofLeftTop_cell; j < kordY_ofLeftTop_cell + 3; j++) {
                        array[i][j].animate().setDuration(duration).rotationBy(-degree_of_rotation);
                    }
            }
        };
        tableLayout.animate().setDuration(duration).translationXBy(begin_translationX).translationYBy(begin_translationY).withEndAction(rotation).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void win_animate() {
        textViewColourOfPlayer.setVisibility(View.INVISIBLE);
        imageViewColourOfPlayer.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        value = arrow[5].getY() - imageView.getY() + 40;
        Runnable translation = new Runnable() {
            public void run() {
                imageView.animate().setInterpolator(new OvershootInterpolator()).setDuration(600).translationYBy(value).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimate = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                textView.animate().setInterpolator(new OvershootInterpolator()).setDuration(600).translationYBy(value);
            }
        };
        imageView.animate().setDuration(400).alphaBy(100).withEndAction(translation).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        textView.animate().setDuration(400).alphaBy(100);
    }

    private void is_game_over() {
        if ((field.isWin(1) && field.isWin(2)) || field.getCountOfBall() == 36) {
            game_is_over = true;
            setArrowVisible(View.INVISIBLE, 400, -100);
            if (!mode.equals(network))
                move.setVisibility(View.INVISIBLE);
            else
                move.setText(R.string.gameIsOver);
            if (!mode.equals(network))
                play_again.setVisibility(View.VISIBLE);
            textView.setText(R.string.draw);
            win_animate();
        } else {
            if (field.isWin(1)) {
                game_is_over = true;
                setArrowVisible(View.INVISIBLE, 400, -100);
                if (!mode.equals(network))
                    move.setVisibility(View.INVISIBLE);
                else
                    move.setText(R.string.gameIsOver);
                if (!mode.equals(network))
                    play_again.setVisibility(View.VISIBLE);
                textView.setText(R.string.wightWins);
                win_animate();
            }
            if (field.isWin(2)) {
                game_is_over = true;
                setArrowVisible(View.INVISIBLE, 400, -100);
                if (!mode.equals(network))
                    move.setVisibility(View.INVISIBLE);
                else
                    move.setText(R.string.gameIsOver);
                if (!mode.equals(network))
                    play_again.setVisibility(View.VISIBLE);
                textView.setText(R.string.blackWins);
                win_animate();
            }
        }
    }

    private void rotation_field(int kordX_ofLeftTop_cell, int kordY_ofLeftTop_cell, boolean clockwise) {
        Button small_field_btn[][] = new Button[3][3];
        for (int i = kordX_ofLeftTop_cell; i < kordX_ofLeftTop_cell + 3; i++)
            for (int j = kordY_ofLeftTop_cell; j < kordY_ofLeftTop_cell + 3; j++) {
                if (clockwise) small_field_btn[j % 3][2 - i % 3] = array[i][j];
                else small_field_btn[2 - j % 3][i % 3] = array[i][j];
            }
        for (int i = kordX_ofLeftTop_cell; i < kordX_ofLeftTop_cell + 3; i++)
            for (int j = kordY_ofLeftTop_cell; j < kordY_ofLeftTop_cell + 3; j++)
                array[i][j] = small_field_btn[i % 3][j % 3];

        field.rotation_field(kordX_ofLeftTop_cell, kordY_ofLeftTop_cell, clockwise);
    }

    private void setArrowVisible(int visibility, int duration, int alpha) {
        for (int t = 0; t < 8; t++) {
            arrow[t].setVisibility(visibility);
            arrow[t].animate().setDuration(duration).alphaBy(alpha);
        }
    }

    public void move_player() {
        if (field.getPlayer() == 1) move.setText(white_move);
        else move.setText(black_move);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_again && !isAnimate) {
            newGame();
            play_again.setVisibility(View.INVISIBLE);
        } else if (!game_is_over && !wait) {
            switch (v.getId()) {
                case R.id.arrow_left_top_clockwise:
                    rotation_field(0, 0, true);
                    rotation_field_animate(left_top, 0, 0, 400, -50, -50, 90);
                    break;
                case R.id.arrow_right_top_clockwise:
                    rotation_field(0, 3, true);
                    rotation_field_animate(right_top, 0, 3, 400, 50, -50, 90);
                    break;
                case R.id.arrow_right_bottom_clockwise:
                    rotation_field(3, 3, true);
                    rotation_field_animate(right_bottom, 3, 3, 400, 50, 50, 90);
                    break;
                case R.id.arrow_left_bottom_clockwise:
                    rotation_field(3, 0, true);
                    rotation_field_animate(left_bottom, 3, 0, 400, -50, 50, 90);
                    break;
                case R.id.arrow_left_top_counter_clockwise:
                    rotation_field(0, 0, false);
                    rotation_field_animate(left_top, 0, 0, 400, -50, -50, -90);
                    break;
                case R.id.arrow_right_top_counter_clockwise:
                    rotation_field(0, 3, false);
                    rotation_field_animate(right_top, 0, 3, 400, 50, -50, -90);
                    break;
                case R.id.arrow_right_bottom_counter_clockwise:
                    rotation_field(3, 3, false);
                    rotation_field_animate(right_bottom, 3, 3, 400, 50, 50, -90);
                    break;
                case R.id.arrow_left_bottom_counter_clockwise:
                    rotation_field(3, 0, false);
                    rotation_field_animate(left_bottom, 3, 0, 400, -50, 50, -90);
                    break;
                default:
                    if (arrow_pressed && !isAnimate)
                        for (int i = 0; i < 6; i++)
                            for (int j = 0; j < 6; j++)
                                if (v.getId() == array[i][j].getId()) {
                                    if (field.colourOfCell[i][j] == 0) {
                                        if (field.getPlayer() == 1) {
                                            if (!(mode.equals(network) && network_player == 2 && !arrivedMove)) {
                                                v.setBackground(getResources().getDrawable(R.drawable.button_white));
                                                field.colourOfCell[i][j] = 1;
                                                kordX = i;
                                                kordY = j;
                                                is_game_over();
                                                if (game_is_over)
                                                    networkClient.sendMove(new Move(kordX, kordY, false, true, true, true));
                                                if ((!game_is_over && (mode.equals(p2) || mode.equals(p1) || (mode.equals(network) && network_player == 1))))
                                                    setArrowVisible(View.VISIBLE, 400, 100);
                                                arrow_pressed = false;
                                                field.incCountOfBall();
                                            }
                                        }
                                        if (field.getPlayer() == 2) {
                                            if (!(mode.equals(network) && network_player == 1 && !arrivedMove)) {
                                                v.setBackground(getResources().getDrawable(R.drawable.button_black));
                                                field.colourOfCell[i][j] = 2;
                                                kordX = i;
                                                kordY = j;
                                                is_game_over();
                                                if (game_is_over)
                                                    networkClient.sendMove(new Move(kordX, kordY, false, true, true, true));
                                                if (!game_is_over && (mode.equals(p2) || (mode.equals(network) && network_player == 2)))
                                                    setArrowVisible(View.VISIBLE, 400, 100);
                                                arrow_pressed = false;
                                                field.incCountOfBall();
                                            }
                                        }
                                    }
                                }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
        if (mode.equals(network)) {
            try {
                networkClient.getMqttAndroidClient().disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdView.resume();
    }

    @Override
    protected void onPause() {
        mAdView.pause();

        super.onPause();
    }
}

