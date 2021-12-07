package com.storozhevykh.forexfourieranalyzer.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.storozhevykh.forexfourieranalyzer.Constants;
import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.view.ChartActivity;

import java.util.ArrayList;
import java.util.List;

public class MoreSpinnerAdapter extends ArrayAdapter<MoreSpinnerObject> {
    private Context mContext;
    private ArrayList<MoreSpinnerObject> listState;
    private MoreSpinnerAdapter myAdapter;
    private boolean isFromView = false;
    private SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

    public MoreSpinnerAdapter(Context context, int resource, List<MoreSpinnerObject> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<MoreSpinnerObject>) objects;
        this.myAdapter = this;
        setSelectionFromPreferences();
    }

    private void setSelectionFromPreferences() {
        listState.get(0).setSelected(preferences.getBoolean(Constants.PREFERENCES_SHOW_MAIN, false));
        listState.get(1).setSelected(preferences.getBoolean(Constants.PREFERENCES_SHOW_BASEL, false));
        listState.get(2).setSelected(preferences.getBoolean(Constants.PREFERENCES_SHOW_HARMS, false));
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.more_spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.moreSpinnerText);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.moreSpinnerCheckbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isFromView) {
                listState.get(position).setSelected(isChecked);
                SharedPreferences.Editor editor = preferences.edit();
                switch (position) {
                    case 0:
                        editor.putBoolean(Constants.PREFERENCES_SHOW_MAIN, isChecked);
                        break;
                    case 1:
                        editor.putBoolean(Constants.PREFERENCES_SHOW_BASEL, isChecked);
                        break;
                    case 2:
                        editor.putBoolean(Constants.PREFERENCES_SHOW_HARMS, isChecked);
                        break;
                }
                editor.apply();
                ((ChartActivity) mContext).updateBarList();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
