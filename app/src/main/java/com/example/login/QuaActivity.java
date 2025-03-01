package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class QuaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qua);

        Button btnBackHome = findViewById(R.id.btnBackHomeQua);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiện tại chưa có sự kiện quay lại Home
                // Nếu bạn muốn thêm sự kiện quay lại Home, uncomment dòng dưới
                // Intent intent = new Intent(QuaActivity.this, HomeActivity.class);
                // startActivity(intent);
                // finish();
            }
        });
    }
}
