package com.vlad.pentago;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class NetworkClient {

    private MqttAndroidClient mqttAndroidClient;
    private String serverUri = "tcp://159.224.87.241";
    private String clientId = "ExampleAndroidClient";
    private String commonTopicQuestion = "Pentago_Question";
    private String commonTopicAnswer = "Pentago_Answer";
    private String commonTopicReady = "Pentago_Ready_To_Play";
    private String topic;
    private String nameOfGame = "ExampleAndroidName ";
    private int countOfAnswer, countOfQuestion;
    private MoveCallback moveCallback;

    void setMoveCallback(MoveCallback moveCallback) {
        this.moveCallback = moveCallback;
    }

    public interface MoveCallback {
        void onOpponentMove(Move move);

        void onOpponentReady();
    }

    NetworkClient(Context context, final int player, String name) {
        topic = name + String.valueOf(player);
        nameOfGame = name;
        clientId = clientId + System.currentTimeMillis();
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);

        try {
            mqttAndroidClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (player == 1) subscribeToTopic(nameOfGame + "2", 1);
                else subscribeToTopic(nameOfGame + "1", 1);
                subscribeToTopic(commonTopicQuestion, 1);
                subscribeToTopic(commonTopicReady, 1);
                publishTopic(commonTopicReady, nameOfGame + Integer.toString(player));
            }

            @Override
            public void connectionLost(Throwable cause) {
                //addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String m = message.toString();
                if (topic.equals(commonTopicQuestion)) {
                    if (m.equals(nameOfGame))
                        publishTopic(commonTopicAnswer, nameOfGame);
                } else {
                    if (topic.equals(commonTopicReady)) {
                        if (player == 1 && m.equals(nameOfGame + "2")) {
                            moveCallback.onOpponentReady();
                        }
                        if (player == 2 && m.equals(nameOfGame + "1")) {
                            publishTopic(commonTopicReady, nameOfGame + "2");
                        }
                    } else {
                        boolean clockwise, left, top;
                        if (m.charAt(2) == 't') {
                            clockwise = m.charAt(3) == 't';
                            left = m.charAt(4) == 't';
                            top = m.charAt(5) == 't';
                            moveCallback.onOpponentMove(new Move(Character.getNumericValue(m.charAt(0)), Character.getNumericValue(m.charAt(1)), true, clockwise, left, top));
                        } else
                            moveCallback.onOpponentMove(new Move(Character.getNumericValue(m.charAt(0)), Character.getNumericValue(m.charAt(1)), false, true, true, true));
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    NetworkClient(Context context) {
        clientId = clientId + System.currentTimeMillis();
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        try {
            mqttAndroidClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                subscribeToTopic(commonTopicAnswer, 1);
                subscribeToTopic(commonTopicQuestion, 1);
            }

            @Override
            public void connectionLost(Throwable cause) {
                //addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String m = message.toString();
                if (topic.equals(commonTopicQuestion)) {
                    if (m.equals(nameOfGame))
                        countOfQuestion++;
                } else {
                    if (m.equals(nameOfGame))
                        countOfAnswer++;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    void publishQuestion(String name) {
        nameOfGame = name;
        countOfAnswer = 0;
        countOfQuestion = 0;
        publishTopic(commonTopicQuestion, name);
    }

    void sendMove(Move move) {
        String message = String.valueOf(move.get_KordX()) + String.valueOf(move.get_KordY());
        if (move.get_Rotation()) {
            message += "t";
            if (move.get_Clockwise()) message += "t";
            else message += "f";
            if (move.get_Left()) message += "t";
            else message += "f";
            if (move.get_Top()) message += "t";
            else message += "f";
        } else message += "f";
        publishTopic(topic, message);
    }

    private void publishTopic(String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        try {
            mqttAndroidClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToTopic(String topic, int qos) {
        try {
            mqttAndroidClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    int getCountOfAnswer() {
        return countOfAnswer;
    }

    int getCountOfQuestion() {
        return countOfQuestion;
    }

    MqttAndroidClient getMqttAndroidClient() {
        return mqttAndroidClient;
    }
}
