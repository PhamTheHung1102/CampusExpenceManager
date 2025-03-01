package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DanhMucActivity extends AppCompatActivity {

    private Button btnKhoanThu, btnKhoanChi, btnThongKe, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhmuc);

        // Khởi tạo các nút trong danh mục
        btnKhoanThu = findViewById(R.id.btnKhoanThu);
        btnKhoanChi = findViewById(R.id.btnKhoanChi);
        btnThongKe = findViewById(R.id.btnThongKe);
        btnBack = findViewById(R.id.btnBack);  // Nút quay lại

        // Khi bấm vào "Khoản Thu"
        btnKhoanThu.setOnClickListener(v -> {
            Intent intent = new Intent(DanhMucActivity.this, KhoanThuActivity.class);
            startActivity(intent);
        });

        // Khi bấm vào "Khoản Chi"
        btnKhoanChi.setOnClickListener(v -> {
            Intent intent = new Intent(DanhMucActivity.this, KhoanChiActivity.class);
            startActivity(intent);
        });

        // Khi bấm vào "Thống Kê"
        btnThongKe.setOnClickListener(v -> {
            Intent intent = new Intent(DanhMucActivity.this, ThongKeActivity.class);
            startActivity(intent);
        });

        // Khi bấm vào nút "Quay lại"
        btnBack.setOnClickListener(v -> {
            finish();  // Quay lại màn hình trước đó
        });
    }
}
