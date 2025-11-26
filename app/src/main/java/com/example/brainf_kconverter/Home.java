package com.example.brainf_kconverter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {
    DrawerLayout main;
    NavigationView navigationView;
    Toolbar toolbar;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        main =findViewById(R.id.main);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        loadFragment(new HomeFragment());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, main, toolbar, R.string.openDrawer, R.string.closeDrawer);
        main.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id==R.id.optHome){
                loadFragment(new HomeFragment());
                Toast.makeText(this, "Open home", Toast.LENGTH_SHORT).show();
            } else if (id==R.id.optNotes) {
                loadFragment(new NotesListFragment());
                Toast.makeText(this, "Open note", Toast.LENGTH_SHORT).show();
            } else if (id==R.id.optFollow){
                String webUrl = "https://github.com/Debabrata9k";
                Intent iFollow = new Intent(Intent.ACTION_VIEW);
                iFollow.setData(Uri.parse(webUrl));
                startActivity(Intent.createChooser(iFollow, "Open link with"));
            } else if (id==R.id.optShare) {
                Intent iShare = new Intent(Intent.ACTION_SEND);
                iShare.setType("text/plain");
                iShare.putExtra(Intent.EXTRA_TEXT, "Download our app from this link:- https://www.mediafire.com/folder/ox7taejckhusg/APPs");
                startActivity(Intent.createChooser(iShare, "SHARE Via"));
            } else if (id==R.id.optVersion) {
                String websiteUrl = "https://github.com/Debabrata9k/Text-Bomber/releases/tag/v1.0.2.4";
                Intent iVersion = new Intent(Intent.ACTION_VIEW);
                iVersion.setData(Uri.parse(websiteUrl));
                startActivity(Intent.createChooser(iVersion, "Open website with"));
            }

            main.closeDrawer(GravityCompat.START);

            return true;
        });

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (main.isDrawerOpen(GravityCompat.START)) {
                    main.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    dispatcher.onBackPressed();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}