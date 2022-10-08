package com.app.awaaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.awaaz.Utils.Constance;
import com.app.awaaz.Utils.Message;
import com.app.awaaz.databinding.ActivityMainBinding;
import com.app.awaaz.databinding.ActivityMainBindingImpl;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView drawer;
    int bottom_nav_id = R.id.nav_home;
    int drawer_nav_id;
    View nav_header;
    TextView navFullname, navEmail;
    CircleImageView imageView, toolbarImageView;
    SharedPreferences sharedPreferences;
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        changeFragment(new home_fragnment());
        sharedPreferences = getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, MODE_PRIVATE);

        //Taking Global Instance
        drawerLayout = findViewById(R.id.parent_drawer_layout);
        drawer = findViewById(R.id.navigation_drawer);
        nav_header = mainBinding.navigationDrawer.getHeaderView(0);
        toolbarImageView = findViewById(R.id.drawer_open_profile_pic);

        //Setting Nav Header
        String fname = sharedPreferences.getString(Constance.USER_FN, "");
        String lname = sharedPreferences.getString(Constance.USER_LN, "");
        String email = sharedPreferences.getString(Constance.USER_EM, "");
        String profile = sharedPreferences.getString(Constance.USER_PP, "");

        navFullname = nav_header.findViewById(R.id.nav_user_fullname);
        navEmail = nav_header.findViewById(R.id.nav_user_email);
        imageView = nav_header.findViewById(R.id.nav_profile_pic);
        navFullname.setText(fname + " " + lname);
        navEmail.setText(email);

        if (!profile.equals("")) {
            Picasso.get().load(Constance.LOGIN_API + profile).into(imageView);
            Picasso.get().load(Constance.LOGIN_API + profile).into(toolbarImageView);
        }

        //Opening Drawer On Click Of Image
        mainBinding.drawerOpenProfilePic
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBinding.parentDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mainBinding.headerDonaPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragments = new DonateThingFragment();
                FragmentManager fragmentManagers = getSupportFragmentManager();
                FragmentTransaction fragmentTransactions = fragmentManagers.beginTransaction();
                fragmentTransactions.add(R.id.full_frame, fragments);
                fragmentTransactions.addToBackStack(null);
                fragmentTransactions.commit();
            }
        });


        //Handeling Navigation Drawer
        mainBinding.navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer_nav_id = item.getItemId();
                item.setChecked(true);
                mainBinding.parentDrawerLayout.closeDrawer(GravityCompat.START);
                switch (drawer_nav_id) {
                    case R.id.drawer_home:
                        changeFragment(new home_fragnment());
                        bottom_nav_id = R.id.nav_home;
                        mainBinding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        break;

                    case R.id.drawer_events:
                        changeFragment(new Event_fragnment());
                        bottom_nav_id = R.id.nav_events;
                        mainBinding.bottomNavigationView.setSelectedItemId(R.id.nav_events);
                        break;


                    case R.id.drawer_profile:
                        changeFragment(new Profile_fragment());
                        bottom_nav_id = R.id.nav_profile;
                        mainBinding.bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;


                    case R.id.drawer_thingd:
                        Fragment fragments = new DonateThingFragment();
                        FragmentManager fragmentManagers = getSupportFragmentManager();
                        FragmentTransaction fragmentTransactions = fragmentManagers.beginTransaction();
                        fragmentTransactions.add(R.id.full_frame, fragments);
                        fragmentTransactions.addToBackStack(null);
                        fragmentTransactions.commit();
                        break;


                    case R.id.drawer_moneyd:
                        openLink("https://awaazngo.business.site/");
                        break;


                    case R.id.drawer_fb:
                        openLink(Constance.FOLLOW_ON_FB);
                        break;


                    case R.id.drawer_insta:
                        openLink(Constance.FOLLOW_ON_INSTA);
                        break;


                    case R.id.drawer_twitter:
                        openLink(Constance.FOLLOW_ON_TWITTER);
                        break;


                    case R.id.drawer_youtube:
                        openLink(Constance.FOLLOW_ON_YOUTUBE);
                        break;


                    case R.id.drawer_about:
                        Fragment fragment = new AboutUs();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.full_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;


                    case R.id.drawer_logout:
                        Logout();
                        break;

                }


                return true;
            }
        });


        //Handeling BottomNavigation
        mainBinding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottom_nav_id = item.getItemId();
                switch (bottom_nav_id) {
                    case R.id.nav_home:
                        changeFragment(new home_fragnment());
                        drawer_nav_id = R.id.drawer_home;
                        mainBinding.navigationDrawer.setCheckedItem(R.id.drawer_home);
                        break;

                    case R.id.nav_events:
                        changeFragment(new Event_fragnment());
                        drawer_nav_id = R.id.drawer_events;
                        mainBinding.navigationDrawer.setCheckedItem(R.id.drawer_events);
                        break;

                    case R.id.nav_profile:
                        changeFragment(new Profile_fragment());
                        drawer_nav_id = R.id.drawer_profile;
                        mainBinding.navigationDrawer.setCheckedItem(R.id.drawer_profile);
                        break;
                }
                return true;
            }
        });
    }


    //Change Fragment Funcion
    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainactivity_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    //Logout Function
    public void Logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent gotoLogin = new Intent(MainActivity.this, LoginActivity.class);
        //gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gotoLogin);
        finish();
    }

    //Open Follow Link Function
    void openLink(String url) {
        Uri uri = Uri.parse(url);
        Intent open = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(open);
    }

    //Handeling back pressed
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.close();
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    //Setting Navigation Drawer Menu
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (bottom_nav_id == R.id.nav_home) {
            drawer.setCheckedItem(R.id.drawer_home);
        } else if (bottom_nav_id == R.id.nav_events) {
            drawer.setCheckedItem(R.id.drawer_events);
        } else if (bottom_nav_id == R.id.nav_profile) {
            drawer.setCheckedItem(R.id.drawer_profile);
        }
    }

}