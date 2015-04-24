package com.my.sumit.miniplusgoogle;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.services.plusDomains.model.Circle;
import com.my.sumit.models.CurrentCircle;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CirclesFragment extends Fragment {

    ListView categoryList;
    private Fragment fragment;
    private List<String> circleList;

    public CirclesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_circles, container, false);
        categoryList = (ListView) v.findViewById(R.id.category_list);

        circleList = new ArrayList<String>();
        List<Circle> circles = CurrentCircle.getCircles();
        for(Circle circle : circles){
            circleList.add(circle.getDisplayName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, circleList);

        categoryList.setAdapter(arrayAdapter);
        categoryList.setOnItemClickListener(new ListMenuClickListener());
        return v;
    }

    //Slide Menu item click listener
    private class ListMenuClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            FriendListFragment friendListFragment = new FriendListFragment();
            friendListFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction =
                    getActivity().getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, friendListFragment);
            fragmentTransaction.commit();

        }
    }

}
