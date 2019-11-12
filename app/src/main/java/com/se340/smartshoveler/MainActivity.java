package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView skiptoMap =  findViewById(R.id.tvSkipToMaps);

        skiptoMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(mapsIntent);
            }
        }); 

        final Button btnLogin = findViewById(R.id.btnHomeLogin);
        final Button btnSkipToSendWork = findViewById(R.id.btnHomeSkipToSendWork);

        btnSkipToSendWork.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent workIntent = new Intent(MainActivity.this, SubmitWorkRequestActivity.class);
                MainActivity.this.startActivity(workIntent);
            }}
        );

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });

        final Button btnRegister = findViewById(R.id.btnHomeRegister);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });
    }
}
