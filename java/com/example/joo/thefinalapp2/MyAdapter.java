package com.example.joo.thefinalapp2;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    Context context;
    List<Sensor> itens; // lista de sensores

    public MyAdapter(Context context, List<Sensor> itens) {
        this.itens = itens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView item = new TextView(context);
        item.setText(itens.get(position).getName()); // imprime o nome retornado por cada sensor
        item.setTextColor(Color.BLUE);
        item.setTextSize(30);
        return item;
    }
}
