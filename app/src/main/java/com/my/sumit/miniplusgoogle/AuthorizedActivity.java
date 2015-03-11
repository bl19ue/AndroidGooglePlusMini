package com.my.sumit.miniplusgoogle;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.my.sumit.drawer.adapter.NavigationDrawerListAdapter;
import com.my.sumit.drawer.model.NavigationDrawerItem;

import java.util.ArrayList;


public class AuthorizedActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    //Navigation Drawer Title
    private CharSequence drawerTitle;

    //To store app title
    private CharSequence title;

    //Slide menu items;
    private String[] menuTitles;
    private TypedArray menuIcons;

    private ArrayList<NavigationDrawerItem> navDrawerItems;
    private NavigationDrawerListAdapter naviDrawerListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized);

        title = drawerTitle = getTitle();

        //Load slide menu items
        menuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        //Navigation drawer icons from resources
        menuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.list_slider_menu);

        navDrawerItems = new ArrayList<NavigationDrawerItem>();

        //For adding each item in the drawer
        for(int i=0;i<menuTitles.length;i++){
            navDrawerItems.add(new NavigationDrawerItem(menuTitles[i], menuIcons.getResourceId(i, -1)));
        }


        //Recycle typed array
        menuIcons.recycle();

        

        //Setting drawer list adapter
        naviDrawerListAdapter = new NavigationDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        drawerList.setAdapter(naviDrawerListAdapter);

        //// enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                new Toolbar(AuthorizedActivity.this),
                R.string.app_name, //Drawer Open
                R.string.app_name) //Drawer Close
        {
            public void onDrawerClosed(View v){
                getSupportActionBar().setTitle(title);

                //Calls onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle(title);

                //Calls onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if(savedInstanceState == null){
            //When the user opens the app for the first time, show list 0
            //-> Call displayView in MainActivity
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_authorized, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(drawerToggle.onOptionsItemSelected(item) ||  //Toggle drawer on selecting action bar icon/title
                item.getItemId() == R.id.action_settings){ //handle action bar clicks
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Called when invalidOptionsMenu() is triggered
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        //If drawer is opened, hide the action items
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence new_title){
        title = new_title;
        getSupportActionBar().setTitle(title);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
