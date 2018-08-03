package com.create.sidhu.movbox.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.create.sidhu.movbox.ProfileFollowers;
import com.create.sidhu.movbox.ProfileImage;
import com.create.sidhu.movbox.ProfileReviews;
import com.create.sidhu.movbox.R;
import com.create.sidhu.movbox.activities.FollowReviewActivity;
import com.create.sidhu.movbox.activities.MainActivity;
import com.create.sidhu.movbox.activities.SettingsActivity;
import com.create.sidhu.movbox.adapters.RecyclerViewAdapter;
import com.create.sidhu.movbox.models.MovieModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

//TODO: Fragment over fragment display issue for grid view of recycle adapter
//Reproduction steps:
//1: Make watchlist gridview and click on any movie.
//2: New fragment appears with elements of old fragment still visible.
/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    private ArrayList<MovieModel> movieModels;
    private Boolean bClick = true;
    private Context context;
    private String type;
    private Boolean isIdentity;
    private String image;
    private String name;

    View rootview;
    LinearLayout llSettings, llLogout, llWatched;
    TextView textViewName;
    CircleImageView imgProfile;
    ScrollView parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieModels = new ArrayList<>();
        final Bundle bundle = getArguments();
        type = bundle.getString("type");
        isIdentity = bundle.getBoolean("isIdentity");
        image = bundle.getString("image");
        name = bundle.getString("name");

        context = getActivity();
        rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        parentView = (ScrollView) rootview.findViewById(R.id.parentView) ;
        llSettings = (LinearLayout) rootview.findViewById(R.id.containerSettings);
        llLogout = (LinearLayout) rootview.findViewById(R.id.containerLogout);
        llWatched = (LinearLayout) rootview.findViewById(R.id.containerWatched);
        textViewName = rootview.findViewById(R.id.profile_name);
        imgProfile = rootview.findViewById(R.id.profile_image);
        TextView text_reviews = rootview.findViewById(R.id.profile_review_count);
        TextView text_followers = rootview.findViewById(R.id.profile_followers_count);
        TextView text_following = rootview.findViewById(R.id.profile_following_count);

        final Button btn_click = rootview.findViewById(R.id.btn_click);
        Button button_img = rootview.findViewById(R.id.btn_profile_image);

        textViewName.setText(name);
        Glide.with(context)
                .asBitmap()
                .load(image)
                .into(imgProfile);
        addData();
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("return_path", "ProfileFragment");
                Intent intent = new Intent(context, SettingsActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        llWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("return_path", "ProfileFragment");
                Intent intent = new Intent(context,FollowReviewActivity.class);
                intent.putExtra("type","watched");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        // onClickListener for Review count
        text_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("return_path", "ProfileFragment");
                Intent intent = new Intent(context,FollowReviewActivity.class);
                intent.putExtra("type","review");
                intent.putExtra("bundle", bundle);
                startActivity(intent);

            }
        });

        // onClickListener for Followers
        text_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("return_path", "ProfileFragment");
                Intent intent = new Intent(context,FollowReviewActivity.class);
                intent.putExtra("type","followers");
                intent.putExtra("bundle", bundle);
                startActivity(intent);

            }
        });

         // onClickListener for Followers
        text_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("return_path", "ProfileFragment");
                Intent intent = new Intent(context,FollowReviewActivity.class);
                intent.putExtra("type","following");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        // onClickListener for Profile Image

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileImage.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        };
        imgProfile.setOnClickListener(onClickListener);
        button_img.setOnClickListener(onClickListener);

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bClick = !bClick;       //achieves the toggle functionality
                initRecyclerView();
            }
        });
        return rootview;
    }

    private void addData() {
        MovieModel model = new MovieModel();
        model.setImage("https://www.topmovierankings.com/images/albums/photos/comrade-in-america-malayalam-movie-stills-poster-4503.jpg");
        model.setName("C.I.A");
        model.setRating("7");
        movieModels.add(model);

        model = new MovieModel();
        model.setImage("https://upload.wikimedia.org/wikipedia/ml/thumb/3/30/Parava_movie_poster.jpeg/220px-Parava_movie_poster.jpeg");
        model.setName("Parava");
        model.setRating("9");
        movieModels.add(model);

        model = new MovieModel();
        model.setImage("https://madaboutmoviez.files.wordpress.com/2016/03/kali-poster-2.jpg");
        model.setName("Kali");
        model.setRating("7");
        movieModels.add(model);

        model = new MovieModel();
        model.setImage("https://malayalam.samayam.com/img/64118605/Master.jpg");
        model.setName("Naam");
        model.setRating("7");
        movieModels.add(model);

        model = new MovieModel();
        model.setImage("https://madaboutmoviez.files.wordpress.com/2015/12/charlie-poster-3.jpg");
        model.setName("Charlie");
        model.setRating("9");
        movieModels.add(model);

        Log.d(TAG, "Data initiated");
        initRecyclerView();
    }

    private void initRecyclerView() {
        if (bClick) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);            // Calling the RecyclerView Adapter with a layout
            RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(layoutManager);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, movieModels, rootview, "profile");
            recyclerView.setAdapter(adapter);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            RecyclerView recyclerView = rootview.findViewById((R.id.recyclerView));
            recyclerView.setLayoutManager(gridLayoutManager);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, movieModels, rootview, "profile");
            recyclerView.setAdapter(adapter);
        }
    }
    public void OnClick(int position, Context context,View rootview, ArrayList<MovieModel> movieModels) {
        //Custom code
        this.context = context;
        parentView.removeAllViews();
        Bundle bundle = new Bundle();
        bundle.putString("type","movie");
        bundle.putString("name", movieModels.get(position).getName());
        bundle.putString("image", movieModels.get(position).getImage());
        bundle.putBoolean("isIdentity", false);
        ProfileFragment fragment2 = new ProfileFragment();
        fragment2.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.RelLayout1, fragment2, "fragmentdetails");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.show(fragment2);
        Toast.makeText(context,"Inside Profile",Toast.LENGTH_SHORT).show();

    }
}