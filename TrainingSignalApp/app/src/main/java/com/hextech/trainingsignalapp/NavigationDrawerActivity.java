package com.hextech.trainingsignalapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NavigationDrawerActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    TextView loginName, loginEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        loginName = findViewById(R.id.loginName);
        loginEmail = findViewById(R.id.loginEmail);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth != null && mAuth.getCurrentUser() != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            loginName = (TextView) headerView.findViewById(R.id.loginName);
            loginEmail = (TextView) headerView.findViewById(R.id.loginEmail);
            final ImageView profileImageView = headerView.findViewById(R.id.profileImageView);

            loginName.setText(mAuth.getCurrentUser().getDisplayName().toString());
            loginEmail.setText(mAuth.getCurrentUser().getEmail().toString());

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_screeners, R.id.nav_news, R.id.nav_chat, R.id.nav_search, R.id.nav_watchlists)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout_button) {
            mAuth.signOut();
            startActivity(new Intent(NavigationDrawerActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}