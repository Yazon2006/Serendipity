package com.example.bel.softwarefactory.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bel.softwarefactory.entities.LeftMenuItem;
import com.example.bel.softwarefactory.R;

import java.util.ArrayList;

public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LeftMenuItem> leftMenuItems;

    public DrawerListAdapter(Context context, ArrayList<LeftMenuItem> leftMenuItems) {
        this.context = context;
        this.leftMenuItems = leftMenuItems;
    }

    @Override
    public int getCount() {
        return leftMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return leftMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.description);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText(leftMenuItems.get(position).getTitle());
        subtitleView.setText( leftMenuItems.get(position).getDescription());
        iconView.setImageResource(leftMenuItems.get(position).getIcon());

        return view;
    }
}