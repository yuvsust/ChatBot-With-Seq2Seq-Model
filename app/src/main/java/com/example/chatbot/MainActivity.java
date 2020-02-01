package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAccessorsAdapter mTabAccessorsAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootReference = FirebaseDatabase.getInstance().getReference();

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("SUST Feculty");

        mViewPager = findViewById(R.id.main_tabs_pager);
        mTabAccessorsAdapter = new TabsAccessorsAdapter(getSupportFragmentManager(), 4);
        mViewPager.setAdapter(mTabAccessorsAdapter);

        mTabLayout = findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {

        super.onStart();

        if(currentUser == null)
            sendUserToLoginActivity();
        else {
            verifyUserExistence();
        }
    }

    private void verifyUserExistence() {
        String CurrentUserId = mAuth.getCurrentUser().getUid();
        RootReference.child("Users").child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists())) {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                }
                else {
                    sendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

//        if(item.getItemId() == R.id.main_find_teachers) {
//            sendUserToFindFriendsActivity();
//        }
        if(item.getItemId() == R.id.main_setitngs_option) {
            sendUserToSettingsActivity();
        }
        if(item.getItemId() == R.id.main_logout_option) {
            mAuth.signOut();
            sendUserToLoginActivity();
        }
        return  true;
    }

    private void sendUserToLoginActivity() {
        Intent mainInten = new Intent(MainActivity.this, LoginActivity.class);
        mainInten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainInten);
        finish();
    }

    private void sendUserToSettingsActivity() {
        Intent mainInten = new Intent(MainActivity.this, SettingsActivity.class);
        mainInten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainInten);
        finish();
    }

//    private void sendUserToFindFriendsActivity() {
//        Intent mainIntent = new Intent(MainActivity.this, FindFriendsActivity.class);
//        startActivity(mainIntent);
//    }
}
