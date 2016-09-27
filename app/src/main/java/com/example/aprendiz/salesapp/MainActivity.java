package com.example.aprendiz.salesapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aprendiz.salesapp.fragments.LoginFragment;
import com.example.aprendiz.salesapp.fragments.PublicationFragment;
import com.example.aprendiz.salesapp.fragments.RegisterUserFragment;
import com.example.aprendiz.salesapp.models.Publication;
import com.example.aprendiz.salesapp.utils.PrefUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RegisterUserFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        PublicationFragment.OnListFragmentInteractionListener {

    public String loggedInUserEmail;
    public String loggedInUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadActivity();
    }

    public void loadActivity() {
        Fragment fragment;

        loggedInUserEmail = PrefUtils.getFromPrefs(MainActivity.this, PrefUtils.PREFS_LOGIN_EMAIL_KEY, "");
        loggedInUserPassword = PrefUtils.getFromPrefs(MainActivity.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (loggedInUserEmail.equals("") || loggedInUserPassword.equals("")) {
            fragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        } else {
            fragment = new PublicationFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        boolean FragmentTransaction = false;
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_publications) {
            fragment = new PublicationFragment();
            FragmentTransaction = true;

        } else if (id == R.id.nav_send) {

        }

        if (FragmentTransaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Publication item) {

    }
}
