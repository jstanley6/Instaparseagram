package com.jasonstanl3y.instaparseagram.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jasonstanl3y.instaparseagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 20;
    private static final String TAG = "LoginActivity";
    private EditText edUsername;
    private EditText edPassword;
    private Button btnLogin;
    private TextView tvCreateAccount;
    private DonutProgress pBar;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        pBar = findViewById(R.id.donut_progress);
        pBar.setVisibility(CircleProgress.INVISIBLE);

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                login(username, password);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean a = false;
                                if (a) {
                                    ObjectAnimator anim = ObjectAnimator.ofInt(pBar, "progress", 0, 10);
                                    anim.setInterpolator(new DecelerateInterpolator());
                                    anim.setDuration(500);
                                    anim.start();
                                } else {
                                    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(LoginActivity.this, R.animator.progress_anim);
                                    set.setInterpolator(new DecelerateInterpolator());
                                    set.setTarget(pBar);
                                    set.start();
                                }
                            }
                        });
                    }
                }, 0, 2000);
                pBar.setVisibility(CircleProgress.VISIBLE);
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                 alertDisplayer("Wrong Credentials", "Wrong username/password. Press \"Ok\" to re-enter your credentials.");
                    Log.e(TAG, "Issue with Login");
                    pBar.setVisibility(CircleProgress.INVISIBLE);
                    e.printStackTrace();
                    return;
                }


                goMainActivity();
            }
        });
    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    private void goMainActivity() {
        Log.d(TAG, "Navigating to Main Activity");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
