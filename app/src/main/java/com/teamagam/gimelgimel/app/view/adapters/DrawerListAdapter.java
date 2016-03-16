package com.teamagam.gimelgimel.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.view.DrawerListItem;

import java.util.List;

/**
 * Created by Gil on 3/14/2016.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerListItem> {

    private List<DrawerListItem> mDrawerItems;
    private Context mContext;

    public DrawerListAdapter(Context context, int resource, List<DrawerListItem> items) {
        super(context, resource, items);
        this.mDrawerItems = items;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.drawer_list_item, null);
        }

        DrawerListItem item = getItem(position);

        if (item != null) {
            //TODO: use viewholder
            TextView title = (TextView) view.findViewById(R.id.drawer_text);
            ImageView image = (ImageView) view.findViewById(R.id.drawer_icon);

            if (title != null) {
                title.setText(mDrawerItems.get(position).getTitle());
            }

            if (image != null) {

                image.setImageDrawable(mDrawerItems.get(position).getIcon());
            }
        }
        return view;
    }
}