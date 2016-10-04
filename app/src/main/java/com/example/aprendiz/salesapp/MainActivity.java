package com.example.aprendiz.salesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprendiz.salesapp.fragments.AboutFragment;
import com.example.aprendiz.salesapp.fragments.LoginFragment;
import com.example.aprendiz.salesapp.fragments.PublicationCreate;
import com.example.aprendiz.salesapp.fragments.PublicationDetailFragment;
import com.example.aprendiz.salesapp.fragments.PublicationFragment;
import com.example.aprendiz.salesapp.fragments.PublicationUpdate;
import com.example.aprendiz.salesapp.fragments.RegisterCommentaryFragment;
import com.example.aprendiz.salesapp.fragments.RegisterUserFragment;
import com.example.aprendiz.salesapp.fragments.UpdateCommentaryFragment;
import com.example.aprendiz.salesapp.fragments.UpdateUserFragment;
import com.example.aprendiz.salesapp.models.Publication;
import com.example.aprendiz.salesapp.models.User;
import com.example.aprendiz.salesapp.utils.PrefUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RegisterUserFragment.OnFragmentInteractionListener,
        UpdateUserFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        PublicationFragment.OnListFragmentInteractionListener,
        PublicationCreate.OnFragmentInteractionListener,
        RegisterCommentaryFragment.OnFragmentInteractionListener,
        UpdateCommentaryFragment.OnFragmentInteractionListener,
        PublicationUpdate.OnFragmentInteractionListener,
        PublicationDetailFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener {

    public String loggedInUserEmail;
    public String loggedInUserPassword;
    public String loggedInUserData;
    final String TAG = this.getClass().getName();

    ImageView mUserImage;
    TextView mUserFullName;
    TextView mUserEmail;

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
        loggedInUserData = PrefUtils.getFromPrefs(MainActivity.this, PrefUtils.PREFS_USER_KEY, "");

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


            showUserHeader(navigationView);
        }
    }

    public void showUserHeader(NavigationView navigationView) {
        Gson gson = new Gson();
        User user = gson.fromJson(loggedInUserData, User.class);

        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_main);

        mUserImage = (ImageView) headerLayout.findViewById(R.id.image_user);
        mUserFullName = (TextView) headerLayout.findViewById(R.id.fullname_user);
        mUserEmail = (TextView) headerLayout.findViewById(R.id.emai_user);

        mUserFullName.setText(user.getFullName());
        mUserEmail.setText(user.getEmail());

        // Proceso carga imagen
        if (!user.getPhoto().isEmpty()) {
            Picasso.with(getApplication())
                    .load(user.getPhoto())
                    .into(mUserImage);
        }
    }

    boolean twice = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            if (twice) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }

            twice = true;
            Toast.makeText(MainActivity.this, "Presione atrás de nuevo para salir", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    twice = false;
                }
            }, 4000);
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

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

        } else if (id == R.id.nav_account_logout) {
            PrefUtils.saveToPrefs(MainActivity.this, PrefUtils.PREFS_LOGIN_EMAIL_KEY, "");
            PrefUtils.saveToPrefs(MainActivity.this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "");
            PrefUtils.saveToPrefs(MainActivity.this, PrefUtils.PREFS_USER_KEY, "");
            MainActivity.this.finish();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_account) {
            fragment = new UpdateUserFragment();
            FragmentTransaction = true;

        } else if (id == R.id.nav_publications_create) {
            fragment = new PublicationCreate();
            FragmentTransaction = true;

        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
            FragmentTransaction = true;

        }
        /*else if (id == R.id.nav_publications_update) {
            fragment = new PublicationUpdate();
            FragmentTransaction = true;

        } else if (id == R.id.nav_commentary_create) {
            fragment = new RegisterCommentaryFragment();
            FragmentTransaction = true;

        } else if (id == R.id.nav_commentary_update) {
            fragment = new UpdateCommentaryFragment();
            FragmentTransaction = true;
        }*/

        if (FragmentTransaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
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


   /* @Override
    public void enviarInformacion(String title) {
        PublicationUpdate publicationUpdate= (PublicationUpdate) getSupportFragmentManager().findFragmentById(R.id.content_publication_update);
        publicationUpdate.recibir(title);
    }*/
}
