package com.example.login;

// ThuongActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ThuongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuong);

        Button btnBackHome = findViewById(R.id.btnBackHomeThuong);

        // Khi nhấn vào nút quay lại trang Home
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại trang HomeActivity
                Intent intent = new Intent(ThuongActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Đảm bảo không giữ lại ThuongActivity trong lịch sử
            }
        });
    }
}

