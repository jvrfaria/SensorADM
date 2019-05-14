package com.example.joo.thefinalapp2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView listaSensores;
    private SensorManager mySensorManager;
    private MyAdapter adapter;
    private List<Sensor> deviceSensores; // lista de sensores

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaSensores = findViewById(R.id.listaSensoresXML);

        try { // uso de try/catch pois é possível que getSystemService retorne null. Geraria NullPointerException com o método getSensorList!!
            mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // objeto SensorManager
            deviceSensores = mySensorManager.getSensorList(Sensor.TYPE_ALL); // retorna lista de sensores em variável List<Sensor>

            adapter = new MyAdapter(this, deviceSensores);
            listaSensores.setAdapter(adapter);
            listaSensores.invalidateViews();
            listaSensores.setOnItemClickListener(this);
        }
        catch(NullPointerException err) {
            Toast.makeText(this,"Erro na detecção dos sensores.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        Intent thisIntent = new Intent(ListActivity.this, SensorActivity.class);
        thisIntent.putExtra("tipo",deviceSensores.get(position).getType()); // envia para a activity "alvo" o tipo do sensor
        startActivity(thisIntent);
    }
}