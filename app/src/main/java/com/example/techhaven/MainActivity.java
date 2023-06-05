package com.example.techhaven;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.techhaven.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.techhaven.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FrameLayout mSearchBtn;
    private EditText mToolBar;
    private TextView mCancel;
    private LinearLayout mHeader;
    private FirebaseAuth mFirebaseAuth;
    private NavController navController;
    private BottomNavigationView navView;
    public static final int DETAIL_REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSearchBtn=findViewById(R.id.search_button);
        mToolBar=findViewById(R.id.toolbar_title);
        mCancel=findViewById(R.id.close_btn);
        mHeader=findViewById(R.id.header_container);
        mFirebaseAuth=FirebaseAuth.getInstance();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    navController.navigate(R.id.navigation_notifications);
                    mHeader.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                    break;
                case R.id.navigation_comment:
                    navController.navigate(R.id.navigation_comment);
                    mHeader.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_home:
                    navController.navigate(R.id.navigation_home);
                    mHeader.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        });

        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TIU","searchbtnclick");
                mToolBar.requestFocus();
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query= mToolBar.getText().toString();
                Log.d("Blee","okay ra ba "+query);
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                ((HomeFragment) fragment).getProducts(query);

            }
        });

        mToolBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    String query= mToolBar.getText().toString();
                    Log.d("Blee","okay ra ba "+query);
                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                    Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                    ((HomeFragment) fragment).getProducts(query);
                    return true;
                }
                return false;
            }
        });

        // Remove the title from the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.signInWithEmailAndPassword("fdc.johntiu@gmail.com", "admin123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TIU", "onFailure: ");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAIL_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                navController.navigate(R.id.navigation_notifications);
                mHeader.setVisibility(View.GONE);
            }
        }
    }
}