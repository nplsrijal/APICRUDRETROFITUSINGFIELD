package com.example.apicrudretrofitusingfield;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    private Button btnshow,btnadd,btnsearch,btnedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        btnshow=findViewById(R.id.btnshow);
        btnadd=findViewById(R.id.btnadd);
        btnsearch=findViewById(R.id.btnsearch);
        btnedit=findViewById(R.id.btnedit);

        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this,SearchActivity.class);
                startActivity(intent);

            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this,UpdateActivity.class);
                startActivity(intent);
            }
        });
    }
}
