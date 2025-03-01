package com.example.login; // Đảm bảo khai báo đúng package

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Đảm bảo R đã được import đúng

        Button btnDanhMuc = findViewById(R.id.btnDanhMuc);

        btnDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DanhMucActivity.class); // Đảm bảo DanhMucActivity đã tồn tại
                startActivity(intent);
            }
        });

        Button btnLogOut = findViewById(R.id.btnLogOut);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class); // Đảm bảo LoginActivity đã tồn tại
                startActivity(intent);
                finish();
            }
        });
    }
}
