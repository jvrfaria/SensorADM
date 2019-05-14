package com.example.joo.thefinalapp2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mySensorManager;
    private Sensor deviceSensor;
    private int sensorType;
    private TextView text1, text2, text3, text4, data1, data2, data3, data4;
    private ImageView coordImage; // imagem dos eixos XYZ
    private String unit = ""; // unidade de medida dos sensores
    private String desc = ""; // descrição de sensores não inerciais
    private boolean isInertialSensor; // flag que indica se o sensor é inercial (sensor de movimento com saída 3D)
    private String help = ""; // frase de ajuda no menu da toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text1 = findViewById(R.id.xTextXML);
        text2 = findViewById(R.id.yTextXML);
        text3 = findViewById(R.id.zTextXML);
        text4 = findViewById(R.id.absTextXML);
        data1 = findViewById(R.id.xDataXML);
        data2 = findViewById(R.id.yDataXML);
        data3 = findViewById(R.id.zDataXML);
        data4 = findViewById(R.id.absDataXML);
        coordImage = findViewById(R.id.imageXML);

        coordImage.setVisibility(View.GONE);
        text4.setVisibility(View.GONE);
        data4.setVisibility(View.GONE);

        Intent thatIntent = getIntent();
        sensorType = thatIntent.getIntExtra("tipo", 0);

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensor = mySensorManager.getDefaultSensor(sensorType);

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                unit = " m/s²";
                coordImage.setVisibility(View.VISIBLE);
                isInertialSensor = true;
                help = "Este sensor mede a aceleração do dispositivo em conjunto com a gravidade ao longo dos eixos XYZ representados na imagem.";
                break;
            case Sensor.TYPE_GYROSCOPE:
                unit = " rad/s";
                coordImage.setVisibility(View.VISIBLE);
                isInertialSensor = true;
                help = "Este sensor mede a velocidade angular do dispositivos ao redor dos eixos XYZ representados na imagem." +
                        "Rotação em sentido anti-horário é padronizada com velocidade angular positiva.";
                break;
            case Sensor.TYPE_GRAVITY:
                unit = " m/s²";
                coordImage.setVisibility(View.VISIBLE);
                isInertialSensor = true;
                text4.setVisibility(View.VISIBLE);
                data4.setVisibility(View.VISIBLE);
                help = "Este sensor mede a aceleração da gravidade ao longo dos eixos XYZ representados na imagem.";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                unit = " m/s²";
                coordImage.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);
                data4.setVisibility(View.VISIBLE);
                isInertialSensor = true;
                help = "Este sensor mede a aceleração do dispositivo ao longo dos eixos XYZ representados na imagem.";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                unit = " µT";
                coordImage.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);
                data4.setVisibility(View.VISIBLE);
                isInertialSensor = true;
                help = "Este sensor mede o campo magnético sobre o dispositivo ao longo dos eixos XYZ representados na imagem.";
                break;
            case Sensor.TYPE_LIGHT:
                unit = " lux";
                desc = "Luminosidade:";
                isInertialSensor = false;
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.GONE);
                data2.setVisibility(View.GONE);
                data3.setVisibility(View.GONE);
                help = "Este sensor mede a intensidade de luz incidente no dispositivo.";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                unit = " °C";
                desc = "Temperatura:";
                isInertialSensor = false;
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.GONE);
                data2.setVisibility(View.GONE);
                data3.setVisibility(View.GONE);
                help = "Este sensor mede a temperatura ambiente.";
                break;
            case Sensor.TYPE_PRESSURE:
                unit = " hPa";
                desc = "Pressão:";
                isInertialSensor = false;
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.GONE);
                data2.setVisibility(View.GONE);
                data3.setVisibility(View.GONE);
                help = "Este sensor mede a pressão atmosférica.";
                break;
            default:
                unit = "";
                desc = "";
                isInertialSensor = false;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this,deviceSensor,400000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // não executa nada quando há alteração na precisão do sensor
    }

    public void onSensorChanged(SensorEvent event) { // método executado quando há alteração na saída do sensor
        if(isInertialSensor) {
            text1.setText("Data X:");
            text2.setText("Data Y:");
            text3.setText("Data Z:");
            data1.setText(String.format("%.3f",event.values[0]) + unit);
            data2.setText(String.format("%.3f",event.values[1]) + unit);
            data3.setText(String.format("%.3f",event.values[2]) + unit);

            if(sensorType == Sensor.TYPE_GRAVITY || sensorType == Sensor.TYPE_LINEAR_ACCELERATION ||
                    sensorType == Sensor.TYPE_MAGNETIC_FIELD) { // utilização do quarto campo
                text4.setText("Intensidade:");
                data4.setText(String.format("%.3f",Math.sqrt(event.values[0]*event.values[0] + event.values[1]*event.values[1] +
                event.values[2]*event.values[2])) + unit); // calculo do módulo = sqrt(x²+y²+z²)
            }
        }
        else {
            text1.setText(desc);
            data1.setText(String.format("%.3f",event.values[0]) + unit);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) { // exibiçao do menu de ajuda
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(help).setTitle("Ajuda")
                    .setPositiveButton("OK", null).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
