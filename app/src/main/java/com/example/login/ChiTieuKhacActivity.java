package com.example.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChiTieuKhacActivity extends AppCompatActivity {

    private EditText edtNoiDungChiTieuKhac;
    private Button btnLuuChiTieuKhac;
    SharedPreferences sharedPreferencesChiTieuKhac;

    private static final String PREFS_NAME_CHI_TIEU_KHAC = "ChiTieuKhacPrefs";
    private static final String KEY_RESULT = "thongTinList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitieukhac);

        // Ánh xạ View
        edtNoiDungChiTieuKhac = findViewById(R.id.edtNoiDungChiTieuKhac);
        btnLuuChiTieuKhac = findViewById(R.id.btnLuuChiTieuKhac);

        // Khởi tạo SharedPreferences
        sharedPreferencesChiTieuKhac = getSharedPreferences(PREFS_NAME_CHI_TIEU_KHAC, MODE_PRIVATE);

        // Xử lý sự kiện khi bấm nút Lưu
        btnLuuChiTieuKhac.setOnClickListener(v -> {
            String noiDung = edtNoiDungChiTieuKhac.getText().toString().trim();

            if (!noiDung.isEmpty()) {
                // Lưu nội dung vào SharedPreferences
                saveChiTieuKhac(noiDung);

                // Hiển thị thông báo
                Toast.makeText(this, "Đã lưu chi tiêu!", Toast.LENGTH_SHORT).show();

                // Xóa nội dung trong EditText
                edtNoiDungChiTieuKhac.setText("");
            } else {
                Toast.makeText(this, "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChiTieuKhac(String noiDung) {
        // Lấy dữ liệu đã lưu trước đó
        String existingData = sharedPreferencesChiTieuKhac.getString(KEY_RESULT, "");

        // Thêm nội dung mới vào dữ liệu cũ
        String updatedData = existingData + "##" + noiDung;

        // Lưu lại dữ liệu
        SharedPreferences.Editor editor = sharedPreferencesChiTieuKhac.edit();
        editor.putString(KEY_RESULT, updatedData);
        editor.apply();
    }
}
