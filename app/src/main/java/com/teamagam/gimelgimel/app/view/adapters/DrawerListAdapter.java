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
import com.teamagam.gimelgimel.app.model.ViewsModels.DrawerListItem;

import java.util.List;

/**
 * Drawer list adapter used by out drawer.
 * Creates each drawer item's view based on underlying list on item data
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerListItem> {

    /**
     * Underlying list of items data
     */
    private List<DrawerListItem> mDrawerItems;
    private Context mContext;

    public DrawerListAdapter(Context context, int resource, List<DrawerListItem> items) {
        super(context, resource, items);
        mDrawerItems = items;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.drawer_list_item, null);
        }

        DrawerListItem item = getItem(position);

        if (item != null) {
            //TODO: use ViewHolder
            TextView itemName = (TextView) view.findViewById(R.id.drawer_text);
            ImageView image = (ImageView) view.findViewById(R.id.drawer_icon);

            if (itemName != null) {
                itemName.setText(mDrawerItems.get(position).getName());
            }

            if (image != null) {

                image.setImageDrawable(mDrawerItems.get(position).getIcon());
            }
        }
        return view;
    }
}