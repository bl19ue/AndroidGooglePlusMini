package com.my.sumit.miniplusgoogle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.api.services.plusDomains.PlusDomains;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.plusDomains.model.Circle;
import com.google.api.services.plusDomains.model.CircleFeed;
import com.google.api.services.plusDomains.model.Person;
import com.my.sumit.drawer.adapter.NavigationDrawerListAdapter;
import com.my.sumit.drawer.model.NavigationDrawerItem;
import com.my.sumit.models.CurrentCircle;
import com.my.sumit.models.CurrentUser;
import com.my.sumit.singleton.MyPlusDomainAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AuthorizedActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = "my_google_plus_mini";
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment fragment = null;

    //Navigation Drawer Title
    private CharSequence drawerTitle;

    //To store app title
    private CharSequence title;

    //Slide menu items;
    private String[] menuTitles;
    private TypedArray menuIcons;

    private ArrayList<NavigationDrawerItem> navDrawerItems;
    private NavigationDrawerListAdapter naviDrawerListAdapter;

    private PlusDomains plusDomainsApi;
    private CurrentUser currentUserObject;
    private Person user = null;
    private List<Circle> circles = null;
    private CurrentCircle myCircles = null;
    private PlusDomains.Circles.List listCircles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized);

        plusDomainsApi = MyPlusDomainAPI.getPlusDomains();
        if(currentUserObject == null) {
            new RetrievePerson().execute();
        }
        new RetrieveCircle().execute();
        title = drawerTitle = getTitle();

        //Load slide menu items
        menuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        //Navigation drawer icons from resources
        menuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.list_slider_menu);

        navDrawerItems = new ArrayList<NavigationDrawerItem>();

        //For adding each item in the drawer
        for(int i = 0; i < menuTitles.length; i++){
            navDrawerItems.add(new NavigationDrawerItem(menuTitles[i], menuIcons.getDrawable(i)));
        }


        //Recycle typed array
        menuIcons.recycle();

        //Creating a click listener on the sliding drawer
        drawerList.setOnItemClickListener(new SlideMenuClickListener());

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



        /*
        fragment = new ProfileFragment();
        attachFragment(0);
        */
        //new RetrievePerson().execute();
    }

    private class RetrievePerson extends AsyncTask<Void, Void, Person> {

        @Override
        protected Person doInBackground(Void... params) {
            try {
                // To Get User Profile
                user = plusDomainsApi.people().get("me").execute();
                Log.i(TAG, "User Name : " + user.getDisplayName());
                Log.i(TAG,"Image URL : " + user.getImage().getUrl());

                /*
                // To Get Circle Feed
                listCircles = plusDomainsApi.circles().list("me");
                listCircles.setMaxResults(5L);
                CircleFeed circleFeed = listCircles.execute();
                List<Circle> circles = circleFeed.getItems();

                // Loop until no additional pages of results are available.
                for (Circle circle : circles) {
                    System.out.println(circle.getDisplayName());
                    Log.i(TAG,"Circle Id : " + circle.getId());
                }
                */
            } catch (IOException e) {
                System.out.println("User Name Excep : ");
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(Person user) {
            super.onPostExecute(user);
            Log.i(TAG,"User::" + user.getName());

            currentUserObject = new CurrentUser(user.getDisplayName(),
                    user.getAboutMe(), user.getImage().getUrl(),
                    user.getOccupation(), user.getOrganizations().get(0).toString());

            displayView(0);
        }
    }

    private class RetrieveCircle extends AsyncTask<Void, Void, List<Circle>> {

        @Override
        protected List<Circle> doInBackground(Void... params) {
            try {
                listCircles = plusDomainsApi.circles().list("me");
                listCircles.setMaxResults(5L);
                CircleFeed circleFeed = listCircles.execute();
                circles = circleFeed.getItems();

                // Loop until no additional pages of results are available.
                for (Circle circle : circles) {
                    System.out.println(circle.getDisplayName());
                    Log.i(TAG,"Circle Id : " + circle.getId());
                }


            } catch (IOException e) {
                System.out.println("Circle Excep : ");
                e.printStackTrace();
            }
            return circles;
        }

        @Override
        protected void onPostExecute(List<Circle> circles) {
            super.onPostExecute(circles);
            Log.i(TAG,"circles::" + circles.get(0).getDisplayName());

            myCircles = new CurrentCircle(circles);

        }


    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //Slide Menu item click listener
    private class SlideMenuClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    //Displaying fragment view for selected item in the drawer
    private void displayView(int position){
        //Update content by replacing fragments
        fragment = null;
        switch (position){
            case 0: {

                fragment = new ProfileFragment();
                break;
            }
            case 1: {
                fragment = new CirclesFragment();
                break;
            }
            default:{
                fragment = new ProfileFragment();
                break;
            }
        }

        attachFragment(position);

    }

    private void attachFragment(int position){

        if(fragment != null){

            //Getting activity's fragment manager
            FragmentManager fragmentManager = getFragmentManager();

            //Replacing the frame container with our new fragment and commiting it.
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            //Updating the selected item and title, and then closing the drawer
            drawerList.setItemChecked(position, true);
            drawerList.setSelection(position);
            setTitle(menuTitles[position]);
            drawerLayout.closeDrawer(drawerList);
        }
        else{
            //error, could not even get a fragment
            Log.e("AuthorizedActivity", "Error in creating fragment");
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

            return super.onOptionsItemSelected(item);

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

    public Person getMyPerson(){
        return user;
    }

}