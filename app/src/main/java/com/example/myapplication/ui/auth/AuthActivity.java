package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.ui.main.MainActivity;
import com.example.myapplication.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AuthActivity";

    private AuthViewModel viewModel;

    private EditText userId;
    private ProgressBar progressBar;

    @Inject
    ViewModelProviderFactory providerFactory;

    

    @Inject
    RequestManager requestManager;

    @Inject
    @Named("app_user")
    User userNumber1;

    @Inject
    @Named("auth_user")
    User userNumber2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        userId = findViewById(R.id.user_id_input);
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.login_button).setOnClickListener(this);

        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);



        subscribeObservers();

        Log.d(TAG, "onCreate: " + userNumber1);
        Log.d(TAG, "onCreate: " + userNumber2);
    }

    private void subscribeObservers(){
        viewModel.observeAuthState().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if(userAuthResource != null){
                    switch (userAuthResource.status){

                        case LOADING:{
                            showProgressBar(true);
                            break;
                        }

                        case AUTHENTICATED:{
                            showProgressBar(false);
                            Log.d(TAG, "onChanged: LOGIN SUCCESS: " + userAuthResource.data.getEmail());
                            onLoginSuccess();
                            break;
                        }

                        case ERROR:{
                            showProgressBar(false);
                            Toast.makeText(AuthActivity.this, userAuthResource.message
                                    + "\nDid you enter a number between 1 and 10?", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        case NOT_AUTHENTICATED:{
                            showProgressBar(false);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void onLoginSuccess(){
        Log.d(TAG, "onLoginSuccess: login successful!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible){
        if(isVisible){
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.GONE);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.login_button:{

                attemptLogin();
                break;
            }
        }
    }

    private void attemptLogin() {
        if(TextUtils.isEmpty(userId.getText().toString())){
            return;
        }
        viewModel.authenticateWithId(Integer.parseInt(userId.getText().toString()));
    }
}
