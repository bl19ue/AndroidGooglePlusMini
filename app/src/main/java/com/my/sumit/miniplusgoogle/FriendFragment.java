package com.my.sumit.miniplusgoogle;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.plusDomains.model.Person;
import com.my.sumit.DownloadImageTask;
import com.my.sumit.models.CurrentFriends;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private ImageView friendImage;
    private TextView friendName;
    private TextView friendOrganization;
    private TextView friendOccupation;
    private Person person;
    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_friend, container, false);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        int position = bundle.getInt("position");

        friendImage = (ImageView)view.findViewById(R.id.friend_image);
        friendName = (TextView)view.findViewById(R.id.friend_name);
        friendOccupation = (TextView)view.findViewById(R.id.friend_occupation);
        friendOrganization = (TextView)view.findViewById(R.id.friend_organization);

        person = CurrentFriends.getPersons().get(position);

        new DownloadImageTask(friendImage)
                .execute(person.getImage().getUrl());

        friendName.setText(person.getDisplayName());

        if(person.getOccupation() != null){
            friendOccupation.setText(person.getOccupation());
        }

        if(person.getOrganizations() != null){
            friendOccupation.setText(person.getOrganizations().get(0).getName());
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_friend_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        if(item.getTitle().equals("Email")) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            startActivity(emailIntent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}
