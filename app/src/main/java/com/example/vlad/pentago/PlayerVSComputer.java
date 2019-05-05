package com.example.vlad.pentago;

import android.animation.Animator;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;


public class PlayerVSComputer extends AppCompatActivity implements View.OnClickListener {
    private Field field;
    private Move_Computer bot = new Move_Computer();
    private Move move_computer;
    private final String white_move = "Ход белых", black_move = "Ход черных";
    private int countOfGames = 0;
    private boolean arrow_pressed = true,
            isAnimate = false,
            game_is_over = false;
    private Button array[][] = new Button[6][6],
            arrow[] = new Button[8],
            play_again;
    private TableLayout left_top, right_top, left_bottom, right_bottom;
    private ImageView imageView;
    private TextView textView, move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        move = findViewById(R.id.move);
        play_again = findViewById(R.id.play_again);
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
        newGame();
    }

    private void newGame() {
        field = new Field();
        countOfGames++;
        textView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        if (countOfGames != 1) {
            imageView.setY(imageView.getY() - 510);
            textView.setY(textView.getY() - 510);
        }
        arrow_pressed = true;
        isAnimate = false;
        game_is_over = false;
        move_player();
        move.setVisibility(View.VISIBLE);
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                array[i][j].setOnClickListener(this);
                array[i][j].setBackground(getResources().getDrawable(R.drawable.button_red_blank));
            }
        for (int i = 0; i < 8; i++)
            arrow[i].setAlpha(0);
    }

    private void rotation_field_animate(final TableLayout tableLayout, final int kordX_ofLeftTop_cell, final int kordY_ofLeftTop_cell, final int duration, final int begin_translationX, final int begin_translationY, final int degree_of_rotation) {
        final Runnable rotation = new Runnable() {
            public void run() {
                Runnable move_field = new Runnable() {
                    public void run() {
                        tableLayout.animate().setDuration(duration).translationXBy(-begin_translationX).translationYBy(-begin_translationY).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                field.setPlayer();
                                move_player();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isAnimate = false;
                                is_game_over();

                                if (field.getPlayer() == 2 && !game_is_over) {
                                    move_computer = bot.move_computer(field.colourOfCell, 2);
                                    array[move_computer.get_KordX()][move_computer.get_KordY()].setBackground(getResources().getDrawable(R.drawable.button_black));
                                    field.colourOfCell[move_computer.get_KordX()][move_computer.get_KordY()] = 2;
                                    is_game_over();
                                    if (!game_is_over && move_computer.get_Rotation()) {
                                        int kordX, kordY;
                                        int degree_of_rotation;
                                        if (move_computer.get_Top()) kordX = 0;
                                        else kordX = 3;
                                        if (move_computer.get_Left()) kordY = 0;
                                        else kordY = 3;
                                        if (move_computer.get_Clockwise())
                                            degree_of_rotation = 90;
                                        else degree_of_rotation = -90;
                                        if (move_computer.get_Top()) {
                                            if (move_computer.get_Left())
                                                rotation_field_animate(left_top, kordX, kordY, 400, -50, -50, degree_of_rotation);
                                            else
                                                rotation_field_animate(right_top, kordX, kordY, 400, 50, -50, degree_of_rotation);
                                        } else {
                                            if (move_computer.get_Left())
                                                rotation_field_animate(left_bottom, kordX, kordY, 400, -50, 50, degree_of_rotation);
                                            else
                                                rotation_field_animate(right_bottom, kordX, kordY, 400, 50, 50, degree_of_rotation);
                                        }
                                        field.incCountOfBall();
                                        rotation_field(kordX, kordY, move_computer.get_Clockwise());
                                    }
                                }
                                arrow_pressed = true;
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
        if (field.getPlayer() == 1) setArrowVisible(View.INVISIBLE, 400, -100);

    }

    private void win_animate() {
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        Runnable translation = new Runnable() {
            public void run() {
                imageView.animate().setInterpolator(new OvershootInterpolator()).setDuration(600).translationYBy(510).setListener(new Animator.AnimatorListener() {
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
                textView.animate().setInterpolator(new OvershootInterpolator()).setDuration(600).translationYBy(510);
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
            move.setVisibility(View.INVISIBLE);
            play_again.setVisibility(View.VISIBLE);
            textView.setText("    Ничья");
            win_animate();
        } else {
            if (field.isWin(1)) {
                game_is_over = true;
                setArrowVisible(View.INVISIBLE, 400, -100);
                move.setVisibility(View.INVISIBLE);
                play_again.setVisibility(View.VISIBLE);
                textView.setText("Белые выграли!");
                win_animate();
            }
            if (field.isWin(2)) {
                game_is_over = true;
                setArrowVisible(View.INVISIBLE, 400, -100);
                move.setVisibility(View.INVISIBLE);
                play_again.setVisibility(View.VISIBLE);
                textView.setText("Черные выграли!");
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
        } else if (!game_is_over) {
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
                                    if (field.getPlayer() == 1) {
                                        v.setBackground(getResources().getDrawable(R.drawable.button_white));
                                        field.colourOfCell[i][j] = 1;
                                        is_game_over();
                                        if (!game_is_over)
                                            setArrowVisible(View.VISIBLE, 400, 100);
                                        arrow_pressed = false;
                                        field.incCountOfBall();
                                    } else {
                                        v.setBackground(getResources().getDrawable(R.drawable.button_black));
                                        field.colourOfCell[i][j] = 2;
                                        is_game_over();
                                        if (!game_is_over)
                                            setArrowVisible(View.VISIBLE, 400, 100);
                                        arrow_pressed = false;
                                        field.incCountOfBall();
                                    }
                                }
                    break;
            }
        }
    }
}
