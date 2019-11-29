package com.vlad.pentago;

import java.util.ArrayList;
import java.util.Random;

class Move_Computer {

    private Move move, m;
    private Random random = new Random();
    private ArrayList<Move> list = new ArrayList<>();
    private ArrayList<Move> smallList = new ArrayList<>();
    static String mode;

    Move move_computer(int[][] colourOfCell, int player) {
        list.clear();
        smallList.clear();
        move = null;
        if (win_move(colourOfCell, player)) return move;
        else {
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 6; j++) {
                    if (colourOfCell[i][j] == 0) {
                        m = new Move(i, j, true, true, true, true);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                        m = new Move(i, j, true, true, true, false);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                        m = new Move(i, j, true, true, false, true);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                        m = new Move(i, j, true, false, true, true);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                        m = new Move(i, j, true, true, false, false);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                        m = new Move(i, j, true, false, true, false);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                        m = new Move(i, j, true, false, false, true);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                        m = new Move(i, j, true, false, false, false);
                        if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                            list.add(m);
                        }
                    }
                }
        }

        if (mode.equals("easy"))
            if (list.size() != 0) {
                int length = list.size();
                for (int p = length - 1; p >= 0; p--) {
                    if (empty_four_balls_empty_ofOpponent_after_the_move(colourOfCell, list.get(p), 2, 1))
                        list.remove(p);
                }
            }

        if (list.size() != 0) {
            move = list.get(random.nextInt(list.size()));
            if (mode.equals("easy")) return list.get(random.nextInt(list.size()));
        }

        if (mode.equals("hard") || mode.equals("middle")) {
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (empty_four_balls_empty_after_the_move(colourOfCell, list.get(i), 2))
                        return list.get(i);
                }

                int length = list.size();
                for (int p = length - 1; p >= 0; p--) {
                    if (empty_four_balls_empty_ofOpponent_after_the_move(colourOfCell, list.get(p), 2, 1))
                        list.remove(p);
                }

                if (mode.equals("hard")) {
                    if (list.size() != 0) {
                        int kol = 0;
                        for (int i = 0; i < 6; i++)
                            for (int j = 0; j < 6; j++)
                                if (colourOfCell[i][j] != 0)
                                    kol++;
                        move = list.get(random.nextInt(list.size()));
                        if (kol > 24) {
                            length = list.size();
                            for (int p = length - 1; p >= 0; p--)
                                if (!opportunity_to_block_opponent_win(colourOfCell, list.get(p), 2, 1))
                                    list.remove(p);
                        }
                        if (list.size() == 0) return move;
                    }
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (player_opportunity_to_win_after_the_move(colourOfCell, list.get(i), 2, 1))
                                return list.get(i);
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (!three_balls_line_ofOpponent_after_the_move(colourOfCell, list.get(i), 2, 1) && three_balls_line_in_five_cells(colourOfCell, 1))
                            return list.get(i);
                    }
                }

                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (blocking_creation_of_three_balls(colourOfCell, list.get(i), 1))
                            return list.get(i);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (blocking_creation_of_three_balls(colourOfCell, list.get(i), 2))
                            return list.get(i);
                    }
                }
            }

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get_KordX() == 1 && list.get(i).get_KordY() == 1)
                    smallList.add(list.get(i));
                if (list.get(i).get_KordX() == 1 && list.get(i).get_KordY() == 4)
                    smallList.add(list.get(i));
                if (list.get(i).get_KordX() == 4 && list.get(i).get_KordY() == 1)
                    smallList.add(list.get(i));
                if (list.get(i).get_KordX() == 4 && list.get(i).get_KordY() == 4)
                    smallList.add(list.get(i));
            }

            if (smallList.size() != 0) return smallList.get(random.nextInt(smallList.size()));
            if (list.size() != 0) return list.get(random.nextInt(list.size()));
        }

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                if (colourOfCell[i][j] == 0) {
                    m = new Move(i, j, true, true, true, true);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                    m = new Move(i, j, true, true, true, false);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                    m = new Move(i, j, true, true, false, true);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                    m = new Move(i, j, true, false, true, true);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                    m = new Move(i, j, true, true, false, false);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                    m = new Move(i, j, true, false, true, false);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                    m = new Move(i, j, true, false, false, true);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                    m = new Move(i, j, true, false, false, false);
                    if (!opponent_victory_after_the_move(colourOfCell, m, 2, 1)) {
                        return m;
                    }
                }
            }

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                if (colourOfCell[i][j] == 0) {
                    return new Move(i, j, true, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
                }
            }
        return move;
    }

    private boolean opportunity_to_block_opponent_win(int[][] colourOfCell, Move mv, int player, int opponent) {
        int kordX, kordY;
        if (mv.get_Top()) kordX = 0;
        else kordX = 3;
        if (mv.get_Left()) kordY = 0;
        else kordY = 3;
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = player;
        rotation_field(colourOfCell, kordX, kordY, mv.get_Clockwise());
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                if (colourOfCell[i][j] == 0) {
                    m = new Move(i, j, true, true, true, true);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }

                    m = new Move(i, j, true, false, true, true);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, true, true, false);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, false, true, false);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, true, false, true);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, false, false, true);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, true, false, false);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, false, false, false);
                    if (player_opportunity_to_win_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                }
            }
        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
        return true;
    }

    private boolean player_opportunity_to_win_after_the_move(int[][] colourOfCell, Move mv, int player, int opponent) {
        int kordX, kordY;
        if (mv.get_Top()) kordX = 0;
        else kordX = 3;
        if (mv.get_Left()) kordY = 0;
        else kordY = 3;
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = player;
        rotation_field(colourOfCell, kordX, kordY, mv.get_Clockwise());
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                if (colourOfCell[i][j] == 0) {
                    m = new Move(i, j, true, true, true, true);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }

                    m = new Move(i, j, true, false, true, true);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, true, true, false);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, false, true, false);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, true, false, true);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, false, false, true);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, true, false, false);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                    m = new Move(i, j, true, false, false, false);
                    if (!opportunity_opponent_victory_after_the_move(colourOfCell, m, opponent, player)) {
                        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
                        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
                        return false;
                    }
                }
            }
        rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
        return true;
    }

    private boolean blocking_creation_of_three_balls(int[][] colourOfCell, Move mv, int opponent) {
        if (mv.get_KordX() % 3 == 0 && mv.get_KordY() % 3 == 0) {
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY() + 1] == opponent && colourOfCell[mv.get_KordX() + 2][mv.get_KordY() + 2] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() + 2][mv.get_KordY()] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() + 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() + 2] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 0 && mv.get_KordY() % 3 == 1) {
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() + 1] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() + 2][mv.get_KordY()] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 0 && mv.get_KordY() % 3 == 2) {
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX() + 2][mv.get_KordY() - 2] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() + 2][mv.get_KordY()] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() - 2] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 1 && mv.get_KordY() % 3 == 0) {
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() - 1][mv.get_KordY()] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() + 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() + 2] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 1 && mv.get_KordY() % 3 == 1) {
            if (colourOfCell[mv.get_KordX() - 1][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX() + 1][mv.get_KordY() - 1] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() - 1][mv.get_KordY() + 1] == opponent && colourOfCell[mv.get_KordX() + 1][mv.get_KordY() - 1] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() - 1][mv.get_KordY()] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() + 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() - 1] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 1 && mv.get_KordY() % 3 == 2) {
            if (colourOfCell[mv.get_KordX() + 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() - 1][mv.get_KordY()] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() - 2] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 2 && mv.get_KordY() % 3 == 0) {
            if (colourOfCell[mv.get_KordX() - 1][mv.get_KordY() + 1] == opponent && colourOfCell[mv.get_KordX() - 2][mv.get_KordY() + 2] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() - 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() - 2][mv.get_KordY()] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() + 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() + 2] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 2 && mv.get_KordY() % 3 == 1) {
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() + 1] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() - 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() - 2][mv.get_KordY()] == opponent)
                return true;
        }
        if (mv.get_KordX() % 3 == 2 && mv.get_KordY() % 3 == 2) {
            if (colourOfCell[mv.get_KordX() - 1][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX() - 2][mv.get_KordY() - 2] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX() - 1][mv.get_KordY()] == opponent && colourOfCell[mv.get_KordX() - 2][mv.get_KordY()] == opponent)
                return true;
            if (colourOfCell[mv.get_KordX()][mv.get_KordY() - 1] == opponent && colourOfCell[mv.get_KordX()][mv.get_KordY() - 2] == opponent)
                return true;
        }
        return false;
    }

    private boolean opponent_victory_after_the_move(int[][] colourOfCell, Move mv, int player, int opponent) {
        int kordX, kordY;
        if (mv.get_Top()) kordX = 0;
        else kordX = 3;
        if (mv.get_Left()) kordY = 0;
        else kordY = 3;
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = player;
        rotation_field(colourOfCell, kordX, kordY, mv.get_Clockwise());
        if (isWin(colourOfCell, opponent)) {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return true;
        } else {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return false;
        }
    }

    private boolean opportunity_opponent_victory_after_the_move(int[][] colourOfCell, Move mv, int player, int opponent) {
        int kordX, kordY;
        if (mv.get_Top()) kordX = 0;
        else kordX = 3;
        if (mv.get_Left()) kordY = 0;
        else kordY = 3;
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = player;
        rotation_field(colourOfCell, kordX, kordY, mv.get_Clockwise());
        if (win_move(colourOfCell, opponent)) {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return true;
        } else {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return false;
        }
    }

    private boolean move_for_empty_four_balls_empty(int[][] colourOfCell, int player) {
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                if (colourOfCell[i][j] == 0) {
                    m = new Move(i, j, true, true, true, true);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                    m = new Move(i, j, true, false, true, true);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                    m = new Move(i, j, true, true, true, false);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                    m = new Move(i, j, true, false, true, false);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                    m = new Move(i, j, true, true, false, true);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                    m = new Move(i, j, true, false, false, true);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                    m = new Move(i, j, true, true, false, false);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                    m = new Move(i, j, true, false, false, false);
                    if (empty_four_balls_empty_after_the_move(colourOfCell, m, player))
                        return true;
                }
            }
        return false;
    }

    private boolean empty_four_balls_empty_ofOpponent_after_the_move(int[][] colourOfCell, Move mv, int player, int opponent) {
        int kordX, kordY;
        if (mv.get_Top()) kordX = 0;
        else kordX = 3;
        if (mv.get_Left()) kordY = 0;
        else kordY = 3;
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = player;
        rotation_field(colourOfCell, kordX, kordY, mv.get_Clockwise());
        if (move_for_empty_four_balls_empty(colourOfCell, opponent)) {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return true;
        } else {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return false;
        }
    }


    private boolean empty_four_balls_empty_after_the_move(int[][] colourOfCell, Move mv, int player) {
        int kordX, kordY;
        if (mv.get_Top()) kordX = 0;
        else kordX = 3;
        if (mv.get_Left()) kordY = 0;
        else kordY = 3;
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = player;
        rotation_field(colourOfCell, kordX, kordY, mv.get_Clockwise());
        if (empty_four_balls_empty(colourOfCell, player)) {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return true;
        } else {
            rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return false;
        }
    }


    private boolean empty_four_balls_empty(int[][] colourOfCell, int player) {

        for (int i = 0; i < 6; i++) {
            int countOfTheSame = 0;
            if (colourOfCell[i][0] == 0 && colourOfCell[i][5] == 0) {
                for (int j = 0; j < 6; j++)
                    if (player == colourOfCell[i][j]) countOfTheSame++;
                if (countOfTheSame == 4) return true;
            }
        }

        for (int i = 0; i < 6; i++) {
            int countOfTheSame = 0;
            if (colourOfCell[0][i] == 0 && colourOfCell[5][i] == 0) {
                for (int j = 0; j < 6; j++) {
                    if (player == colourOfCell[j][i]) countOfTheSame++;
                }
                if (countOfTheSame == 4) return true;
            }
        }


        if (colourOfCell[0][0] == 0 && colourOfCell[5][5] == 0) {
            int countOfTheSame = 0;
            for (int i = 0; i < 6; i++) {
                if (player == colourOfCell[i][i]) countOfTheSame++;
                if (countOfTheSame == 4) return true;
            }
        }

        if (colourOfCell[0][5] == 0 && colourOfCell[5][0] == 0) {
            int countOfTheSame = 0;
            for (int i = 0; i < 6; i++) {
                if (player == colourOfCell[i][5 - i]) countOfTheSame++;
                if (countOfTheSame == 4) return true;
            }
        }
        return false;
    }

    private boolean win_move(int[][] colourOfCell, int player) {
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                if (colourOfCell[i][j] == 0) {
                    colourOfCell[i][j] = player;
                    if (isWin(colourOfCell, player)) {
                        move = new Move(i, j, true, true, true, true);
                        colourOfCell[i][j] = 0;
                        return true;
                    } else {
                        if (win_rotation(colourOfCell, 0, 0, true, player, i, j) ||
                                win_rotation(colourOfCell, 0, 0, false, player, i, j) ||
                                win_rotation(colourOfCell, 0, 3, true, player, i, j) ||
                                win_rotation(colourOfCell, 0, 3, false, player, i, j) ||
                                win_rotation(colourOfCell, 3, 0, true, player, i, j) ||
                                win_rotation(colourOfCell, 3, 0, false, player, i, j) ||
                                win_rotation(colourOfCell, 3, 3, true, player, i, j) ||
                                win_rotation(colourOfCell, 3, 3, false, player, i, j)) {
                            colourOfCell[i][j] = 0;
                            return true;
                        }
                        colourOfCell[i][j] = 0;
                    }
                }
            }
        return false;
    }

    private boolean win_rotation(int[][] colourOfCell, int kordX_ofLeftTop_cell, int kordY_ofLeftTop_cell, boolean clockwise, int player, int kordX_of_cell, int kordY_of_cell) {
        rotation_field(colourOfCell, kordX_ofLeftTop_cell, kordY_ofLeftTop_cell, clockwise);
        if (isWin(colourOfCell, player)) {
            rotation_field(colourOfCell, kordX_ofLeftTop_cell, kordY_ofLeftTop_cell, !clockwise);
            move = new Move(kordX_of_cell, kordY_of_cell, true, clockwise, kordY_ofLeftTop_cell == 0, kordX_ofLeftTop_cell == 0);
            return true;
        } else
            rotation_field(colourOfCell, kordX_ofLeftTop_cell, kordY_ofLeftTop_cell, !clockwise);
        return false;
    }

    private boolean isWin(int[][] colourOfCell, int colourOfPlayer) {

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = j; t < j + 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[i][t]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 6; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = i; t < i + 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[t][j]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = 0; t < 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[i + t][j + t]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        for (int i = 4; i < 6; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = 0; t < 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[i - t][j + t]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        return false;
    }

    private void rotation_field(int[][] colourOfCell, int kordX_ofLeftTop_cell, int kordY_ofLeftTop_cell, boolean clockwise) {
        int small_field[][] = new int[3][3];
        for (int i = kordX_ofLeftTop_cell; i < kordX_ofLeftTop_cell + 3; i++)
            for (int j = kordY_ofLeftTop_cell; j < kordY_ofLeftTop_cell + 3; j++) {
                if (clockwise) small_field[j % 3][2 - i % 3] = colourOfCell[i][j];
                else small_field[2 - j % 3][i % 3] = colourOfCell[i][j];
            }
        for (int i = kordX_ofLeftTop_cell; i < kordX_ofLeftTop_cell + 3; i++)
            for (int j = kordY_ofLeftTop_cell; j < kordY_ofLeftTop_cell + 3; j++)
                colourOfCell[i][j] = small_field[i % 3][j % 3];
    }

    private boolean three_balls_line_ofOpponent_after_the_move(int[][] colourOfCell, Move mv, int player, int opponent) {
        int kordX, kordY;
        if (mv.get_Top()) kordX = 0;
        else kordX = 3;
        if (mv.get_Left()) kordY = 0;
        else kordY = 3;
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = player;
        if (three_balls_line_in_five_cells(colourOfCell, opponent)) {
            colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
            return true;
        }
//        else {
//            rotation_field(colourOfCell, kordX, kordY, mv.get_Clockwise());
//            if (three_balls_line_in_five_cells(colourOfCell, opponent)) {
//                rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
//                colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
//                return true;
//            } else {
//                rotation_field(colourOfCell, kordX, kordY, !mv.get_Clockwise());
//                colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
//                return false;
//            }
//        }
        colourOfCell[mv.get_KordX()][mv.get_KordY()] = 0;
        return false;
    }

    private boolean three_balls_line_in_five_cells(int[][] colourOfCell, int player) {

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                for (int t = j; t < j + 5; t++)
                    if (player == colourOfCell[i][t]) countOfTheSame++;
                    else if (colourOfCell[i][t] != 0) countOfTheSame = -100;
                if (countOfTheSame >= 3) return true;
            }
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 6; j++) {
                int countOfTheSame = 0;
                for (int t = i; t < i + 5; t++)
                    if (player == colourOfCell[t][j]) countOfTheSame++;
                    else if (colourOfCell[t][j] != 0) countOfTheSame = -100;
                if (countOfTheSame >= 3) return true;
            }
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                for (int t = 0; t < 5; t++)
                    if (player == colourOfCell[i + t][j + t]) countOfTheSame++;
                    else if (colourOfCell[i + t][j + t] != 0) countOfTheSame = -100;
                if (countOfTheSame >= 3) return true;
            }
        for (int i = 4; i < 6; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                for (int t = 0; t < 5; t++)
                    if (player == colourOfCell[i - t][j + t]) countOfTheSame++;
                    else if (colourOfCell[i - t][j + t] != 0) countOfTheSame = -100;
                if (countOfTheSame >= 3) return true;
            }
        return false;
    }
}
