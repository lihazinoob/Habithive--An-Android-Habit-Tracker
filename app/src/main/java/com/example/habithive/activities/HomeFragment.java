package com.example.habithive.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.habithive.R;
import com.example.habithive.activities.database.AppDatabase;
import com.example.habithive.activities.model.User;
import com.example.habithive.activities.model.UserManagerSingleton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ShapeableImageView profileImageView;
    private TextView grettingUserNameText;
    private AppDatabase appDatabase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        appDatabase = AppDatabase.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

//        Bind the UI components
        profileImageView = view.findViewById(R.id.profileImageGreetingView);
        grettingUserNameText = view.findViewById(R.id.GreetingUserNameText);

//        /Load the User data of the header here

        User currentUser = UserManagerSingleton.getInstance().getCurrentUser();

        if(currentUser == null && FirebaseAuth.getInstance().getCurrentUser() != null)
        {
//            Fetch from Room database if Singleton is null but user is authenticated
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            new Thread(()->
            {
               User userFromRoomDb = appDatabase.userDao().getUserById(userId);
               if(userFromRoomDb != null)
               {
                   UserManagerSingleton.getInstance().setCurrentUser(userFromRoomDb);
                   updateUI(userFromRoomDb);
               }
            }).start();
        }
        else if(currentUser != null)
        {
            updateUI(currentUser);
        }
        return view;
    }
    private void updateUI(User user)
    {
        requireActivity().runOnUiThread(() -> {
            grettingUserNameText.setText(user.getUsername());
            String imageUrl = user.getImageURL(); // Fixed method name to match User class
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl)
                        .placeholder(R.drawable.user_tie_solid)
                        .error(R.drawable.user_tie_solid)
                        .into(profileImageView);
            }
        });
    }
}