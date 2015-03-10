package com.my.sumit.drawer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.sumit.drawer.model.NavigationDrawerItem;
import com.my.sumit.miniplusgoogle.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class NavigationDrawerListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<NavigationDrawerItem> navigationDrawerItems;

    public NavigationDrawerListAdapter(Context context, ArrayList<NavigationDrawerItem> navigationDrawerItems){
        this.context = context;
        this.navigationDrawerItems = navigationDrawerItems;
    }

    @Override
    public int getCount() {
        return navigationDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater minflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = minflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(navigationDrawerItems.get(position).getIcon());
        txtTitle.setText(navigationDrawerItems.get(position).getTitle());

        return convertView;
    }
}

// Until now we are done creating all the required layouts, model and adapter class for
// navigation drawer. It’s time to move on to our MainActivity.java and start implementing
// the navigation drawer.

/*Following are the major steps we need take care of in the main activity.

        > Creating a NavDrawerListAdapter instance and adding list items.
        > Assigning the adapter to Navigation Drawer ListView
        > Creating click event listener for list items
        > Creating and displaying fragment activities on selecting list item.
*/