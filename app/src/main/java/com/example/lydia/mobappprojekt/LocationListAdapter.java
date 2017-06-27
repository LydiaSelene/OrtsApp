package com.example.lydia.mobappprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lydia on 19.07.2016.
 * Für ListViews
 */
public class LocationListAdapter extends BaseAdapter {

    private ArrayList<LocationData> locationList;
    private LayoutInflater layoutInflater;

    public LocationListAdapter(Context aContext, ArrayList<LocationData> locations) {
        locationList = locations;
        layoutInflater = LayoutInflater.from(aContext);
    }

    /** This method returns the total number of row counts for the listview.
     * Typically this contains the size of the list you passing as input.
     * @return
     */
    @Override
    public int getCount() {
        return locationList.size();
    }

    /** Returns object representing data for each row. */
    @Override
    public Object getItem(int position) {
        return locationList.get(position);
    }

    /** This returns the unique integer id that represents each row item. Let us return the integer position value. */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /** The getView() method returns a view instance that represents a single row in ListView item.
     * Here you can inflate your own layout and update values on list row.*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.locations_listview_item, null);

            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.title);
            holder.idView = (TextView) convertView.findViewById(R.id.id);

            /** mit setTag kann man sachen für views speichern */
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleView.setText(locationList.get(position).getName());
        holder.idView.setText(locationList.get(position).getId()+"");
        return convertView;
    }

    /** Das ViewHilder-Pattern verbessert die Performance */
    static class ViewHolder {
        TextView titleView;
        TextView idView;
    }

}
