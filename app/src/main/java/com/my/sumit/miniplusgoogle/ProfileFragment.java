package com.my.sumit.miniplusgoogle;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.api.services.plusDomains.PlusDomains;
import com.my.sumit.DownloadImageTask;
import com.my.sumit.models.CurrentUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static String TAG = "Profile_Fragment";
    private PlusDomains plusDomains;
    private AuthorizedActivity authorizedActivity;
    private TextView profileName;
    private TextView profileAboutMe;
    private TextView profileOccupation;
    private TextView profileOrganization;
    private ImageView profileImageView;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = (TextView) view.findViewById(R.id.profile_name);
        profileAboutMe = (TextView) view.findViewById(R.id.profile_about_me);
        profileImageView = (ImageView) view.findViewById(R.id.profile_image);
        profileOccupation = (TextView) view.findViewById(R.id.profile_occupation);
        profileOrganization = (TextView) view.findViewById(R.id.profile_organization);

        profileName.setText(CurrentUser.getCurrentUserName());
        profileAboutMe.setText(CurrentUser.getCurrentUserAboutMe());

        if(CurrentUser.getCurrentUserOccupation() != null){
            profileOccupation.setText(CurrentUser.getCurrentUserOccupation());
        }

        if(CurrentUser.getCurrentUserOrganization() != null){
            profileOrganization.setText(CurrentUser.getCurrentUserOrganization());
        }

        new DownloadImageTask(profileImageView)
                .execute(CurrentUser.getCurrentUserImageURL());

        return view;
    }

    @Override
    public void onAttach(Activity myActivity){
        super.onAttach(myActivity);
        this.authorizedActivity = (AuthorizedActivity) myActivity;
    }
}
