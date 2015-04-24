package com.my.sumit.miniplusgoogle;


import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.PeopleFeed;
import com.google.api.services.plusDomains.model.Person;
import com.my.sumit.drawer.adapter.NavigationDrawerListAdapter;
import com.my.sumit.drawer.model.URLImageItem;
import com.my.sumit.models.CurrentCircle;
import com.my.sumit.models.CurrentFriends;
import com.my.sumit.singleton.MyPlusDomainAPI;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendListFragment extends Fragment {
    NavigationDrawerListAdapter navigationDrawerListAdapter;
    ListView friendList;
    String circle_id;
    PlusDomains plusDomains;

    ArrayList<URLImageItem> urlImageItems;
    ArrayList<String> imageUrls;
    ArrayList<String> friendNames;
    CurrentFriends currentFriends;

    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_friend_list, container, false);
        friendList = (ListView) view.findViewById(R.id.friend_list);
        urlImageItems = new ArrayList<URLImageItem>();

        imageUrls = new ArrayList<String>();
        friendNames = new ArrayList<String>();

        plusDomains = MyPlusDomainAPI.getPlusDomains();

        Bundle bundle = getArguments();
        int position = bundle.getInt("position");

        circle_id = CurrentCircle.getCircles().get(position).getId();

        new RetrieveFriends().execute();

        return view;
    }

    private class RetrieveFriends extends AsyncTask<Void, Void, ArrayList<URLImageItem>> {

        @Override
        protected ArrayList<URLImageItem> doInBackground(Void... params) {
            try {
                PlusDomains.People.ListByCircle listPeople = plusDomains.people().listByCircle(circle_id);
                listPeople.setMaxResults(100L);

                PeopleFeed peopleFeed = listPeople.execute();
                System.out.println("Google+ users circled:");

                if(peopleFeed.getItems() != null && peopleFeed.getItems().size() > 0 ) {
                    currentFriends = new CurrentFriends(peopleFeed.getItems());
                    for(Person person : peopleFeed.getItems()) {

                        urlImageItems.add(new URLImageItem(person.getDisplayName(),
                                person.getImage().getUrl()));
                    }
                }

            } catch (IOException e) {
                System.out.println("Circle Excep : ");
                e.printStackTrace();

            }
            return urlImageItems;
        }

        @Override
        protected void onPostExecute(ArrayList<URLImageItem> urlImageItems) {
            super.onPostExecute(urlImageItems);
            if(urlImageItems.size() == 0){
                return;
            }
            Log.i(MainActivity.TAG, "friends" + urlImageItems.get(0).getFriendName());

            navigationDrawerListAdapter =
                    new NavigationDrawerListAdapter(getActivity(), urlImageItems, true);

            friendList.setAdapter(navigationDrawerListAdapter);
            friendList.setOnItemClickListener(new ListMenuClickListener());
        }


    }

    //Slide Menu item click listener
    private class ListMenuClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);

            FriendFragment friendFragment = new FriendFragment();
            friendFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction =
                    getActivity().getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, friendFragment);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onDestroy()
    {
        friendList.setAdapter(null);
        super.onDestroy();
    }

}
