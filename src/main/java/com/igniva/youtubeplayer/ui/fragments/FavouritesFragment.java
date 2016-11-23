package com.igniva.youtubeplayer.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igniva.youtubeplayer.R;
import com.igniva.youtubeplayer.db.DatabaseHandler;
import com.igniva.youtubeplayer.libs.FloatingActionButton;
import com.igniva.youtubeplayer.libs.FloatingActionMenu;
import com.igniva.youtubeplayer.model.DataGalleryPojo;
import com.igniva.youtubeplayer.model.DataYoutubePojo;
import com.igniva.youtubeplayer.ui.activities.MainActivity;
import com.igniva.youtubeplayer.ui.adapters.CategoryListAdapter;
import com.igniva.youtubeplayer.ui.adapters.FavouriteListAdapter;
import com.igniva.youtubeplayer.utils.UtilsUI;

import org.mozilla.javascript.tools.debugger.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class FavouritesFragment extends BaseFragment implements FloatingActionMenu.OnMenuToggleListener {
    View mView;
    public static RecyclerView mRvCategories;
    private SharedPreferences sharedPreferences;
    ArrayList<String> small_images_url, medium_images_url, large_images_url;
    List<DataYoutubePojo> mAllData;
    public static TextView message;
    public static RelativeLayout no_data_found_layout;
    DatabaseHandler mDatabaseHandler;


    public static ArrayList<String> channels_name, channel_thumb, listCategories, listDuration, listNames, listRating, listFavourite;

    public static FloatingActionMenu menu_fab;
    FloatingActionButton fab1, fab2, fab3;
    MainActivity main = new MainActivity();
    List<DataGalleryPojo> mAllImages = main.getMyImages();

    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category, container, false);

        MainActivity.menu_fab.setVisibility(View.GONE);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        channels_name = new ArrayList<>();
        channel_thumb = new ArrayList<>();

        small_images_url = new ArrayList<>();
        medium_images_url = new ArrayList<>();
        large_images_url = new ArrayList<>();

        listCategories = new ArrayList<String>();
        listDuration = new ArrayList<String>();
        listNames = new ArrayList<String>();
        listRating = new ArrayList<String>();
        listFavourite = new ArrayList<String>();

        // Reading all contacts

        message = (TextView) mView.findViewById(R.id.iv_message);
        no_data_found_layout = (RelativeLayout)mView.findViewById(R.id.no_data_found_layout);


        mDatabaseHandler = new DatabaseHandler(getActivity());

        fetchLatestVideos();


        return mView;
    }

    @Override
    public void setUpLayouts() {

        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);
        try {
            mRvCategories.setVisibility(View.VISIBLE);
            mRvCategories.setAdapter(new FavouriteListAdapter(getActivity(), listCategories, listNames, listDuration, listRating, listFavourite, 1));
            mRvCategories.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            mRvCategories.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataInViewLayouts() {
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
}
    private  void clear() {
        mAllData .clear();
        channel_thumb.clear();
        channels_name.clear();

        listCategories.clear();
        listNames.clear();
        listDuration.clear();
        listRating.clear();
        listFavourite.clear();

        FavouritesFragment.listCategories.clear();
        FavouritesFragment.listNames.clear();
        FavouritesFragment.listDuration.clear();
        FavouritesFragment.listRating.clear();
        FavouritesFragment.listFavourite.clear();

        FavouritesFragment.no_data_found_layout.setVisibility(View.INVISIBLE);

        menu_fab.close(true);

    }

    public void fetchLatestVideos(){
        mAllData = mDatabaseHandler.getAllContacts();

        for (DataYoutubePojo cn : mAllData) {

            String log = "video_Id_favourite: " + cn.getVideo_no() + " , Video_Title: " + cn.getVideo_title() + " Video_id" + cn.getVideo_id() + "Video_channel" + cn.getVideo_channel() +
                    " ,Duration: " + cn.getVideo_duration() + " Rating: " + cn.getVideo_rating() + " Thumb: " + cn.getVideo_thumb() + " Playlist: " + cn.getVideo_playlist() +
                    " order: " + cn.getVideo_order() + " Favourite= " + cn.getVideo_favourite();

            if (cn.getVideo_favourite().equals("1")) {
                listCategories.add(cn.getVideo_id().toString());
                listNames.add(cn.getVideo_title().toString());
                listDuration.add(cn.getVideo_duration().toString());
                listRating.add("" + cn.getVideo_rating());
                listFavourite.add(cn.getVideo_favourite());
            }

            // Writing Contacts to log
            Log.e("Name: ", log);

        }
        if (listCategories.size() == 0) {
            no_data_found_layout.setVisibility(View.VISIBLE);
            ;
        }

        for (DataGalleryPojo cn : mAllImages) {

            large_images_url.add(cn.getImage_link().toString());

        }
        setUpLayouts();

    }

    @Override
    public void onMenuToggle(boolean opened) {

        if(opened){

            menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_white_24dp));

        }else {

            menu_fab.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_filter));

        }

    }



}