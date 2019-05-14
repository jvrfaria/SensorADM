package com.example.joo.thefinalapp2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;

public class SensorActivity extends AppCompatActivity {
    private int sensorType;
    private SensorManager mySensorManager;
    private ArrayList<String> infoArray = new ArrayList<String>(); // ArrayList com Strings sobre o sensor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ListView sensorInfo = (ListView) findViewById(R.id.sensorInfoXML);

        Intent thatIntent = getIntent();
        sensorType = thatIntent.getIntExtra("tipo", 0); // recebe o tipo de sensor provindo da Activity lançadora

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,infoArray);
        sensorInfo.setAdapter(itemAdapter);

        Button testButton = (Button) findViewById(R.id.testSensorXML);

        if(sensorType==0) { // caso haja falha na passagem do tipo de sensor pela Intent
            infoArray.add("Erro na identificação do sensor.");
            sensorInfo.invalidateViews();
        }
        else {
            Sensor deviceSensor = mySensorManager.getDefaultSensor(sensorType); // recebe sensor padrão do tipo especificado
            toolbar.setTitle(deviceSensor.getName());
            setSupportActionBar(toolbar);

            infoArray.add("Nome: " + deviceSensor.getName());
            infoArray.add("Versão: " + deviceSensor.getVersion());
            infoArray.add("Fornecedor: " + deviceSensor.getVendor());
            infoArray.add("Consumo: " + String.format("%.3f",deviceSensor.getPower()) + " mA"); // impressão das características do sensor
            sensorInfo.invalidateViews();
        }
        if(sensorType == Sensor.TYPE_ACCELEROMETER || sensorType == Sensor.TYPE_GRAVITY ||
                sensorType == Sensor.TYPE_MAGNETIC_FIELD || sensorType == Sensor.TYPE_LIGHT ||
                sensorType == Sensor.TYPE_LINEAR_ACCELERATION || sensorType == Sensor.TYPE_AMBIENT_TEMPERATURE ||
                sensorType == Sensor.TYPE_GYROSCOPE || sensorType == Sensor.TYPE_PRESSURE) {  // sensores que permitem teste

            testButton.setVisibility(View.VISIBLE);
            testButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent thisIntent = new Intent(SensorActivity.this,TestActivity.class);
                    thisIntent.putExtra("tipo",sensorType);
                    startActivity(thisIntent);

                }
            });
        }
        else // botão de teste desativado para sensores que não admitem teste
            testButton.setVisibility(View.INVISIBLE);
    }
}