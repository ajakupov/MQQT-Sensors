package com.example.root.mqqtsensors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.mqqtsensors.helpers.MQTTHelper;
import com.example.root.mqqtsensors.helpers.MQTTPublishHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class WelcomeActivity extends AppCompatActivity {

    public MQTTHelper mqttHelper;
    public MQTTPublishHelper mqttPublishHelper;
    public TextView subscribeTextView;
    public EditText publishText;
    public Button publishButton;
    public TextView redirect;
    private int ledState=0;
    public String updated = "Romain Vadam update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        subscribeTextView = (TextView) findViewById(R.id.subscribeTextView);
        publishText = (EditText) findViewById(R.id.publishText);


        //Initialieation of the MQTTHelper for the subscription to listen topic on the broker
        starMQTT();

        //publishHelper initialization
        mqttPublishHelper = new MQTTPublishHelper(getApplicationContext());
        //initialize button
        publishButton = (Button) findViewById(R.id.publishButton);
        //add action to the button
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMQTT();
            }
        });
    }

    private void starMQTT() {
        mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.i("Debug",mqttMessage.toString());
                subscribeTextView.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void publishMQTT () {
        //mqttPublishHelper = new MQTTPublishHelper(getApplicationContext());
        String operatorName=publishText.getText().toString();

        if(operatorName.equals("")) {
            Toast.makeText(this,"Veuillez saisir votre nom",Toast.LENGTH_LONG);
        }else {
            mqttPublishHelper.publishToTopic(publishText.getText().toString(),ledState);
        }
        
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonON:
                if (checked)
                    ledState=1;
                    break;
            case R.id.radioButtonOFF:
                if (checked)
                    ledState=0;
                    break;
        }
    }
}
