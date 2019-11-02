package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnLogin = (Button) findViewById(R.id.btnHomeLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });

        final Button btnRegister = (Button) findViewById(R.id.btnHomeRegister);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

    }
}
