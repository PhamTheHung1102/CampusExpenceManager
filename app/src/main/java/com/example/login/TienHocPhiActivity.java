package com.example.login;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class TienHocPhiActivity extends AppCompatActivity {

    EditText editTextSoTien, editTextKhoaHoc, editTextLopHoc;
    Button btnChonNgay, btnLuuThongTin, btnQuayLai;
    TextView textViewNgayThang;
    LinearLayout layoutResultContainer;

    String ngayThang = "";
    ArrayList<String> thongTinList = new ArrayList<>(); // Mảng lưu thông tin

    // SharedPreferences
    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TienHocPhiPrefs";
    private static final String KEY_RESULT = "thongTinList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienhocphi);

        // Ánh xạ các view
        editTextSoTien = findViewById(R.id.editTextSoTien);
        editTextKhoaHoc = findViewById(R.id.editTextKhoaHoc);
        editTextLopHoc = findViewById(R.id.editTextLopHoc);
        btnChonNgay = findViewById(R.id.btnChonNgay);
        btnLuuThongTin = findViewById(R.id.btnLuuThongTin);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        textViewNgayThang = findViewById(R.id.textViewNgayThang);
        layoutResultContainer = findViewById(R.id.layoutResultContainer);

        // SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Nút Quay Lại
        btnQuayLai.setOnClickListener(v -> finish());

        // Nút chọn ngày
        btnChonNgay.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(TienHocPhiActivity.this, (view, year1, month1, dayOfMonth) -> {
                ngayThang = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                textViewNgayThang.setText("Ngày tháng: " + ngayThang);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Nút Lưu Thông Tin
        btnLuuThongTin.setOnClickListener(v -> {
            String soTien = editTextSoTien.getText().toString();
            String khoaHoc = editTextKhoaHoc.getText().toString();
            String lopHoc = editTextLopHoc.getText().toString();

            if (soTien.isEmpty() || khoaHoc.isEmpty() || lopHoc.isEmpty() || ngayThang.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Đảm bảo ngày tháng đã được gán chính xác
                String result = "Số tiền: " + soTien +
                        "\nKhóa học: " + khoaHoc +
                        "\nLớp học: " + lopHoc +
                        "\nNgày tháng: " + ngayThang;

                thongTinList.add(result);
                saveData();
                refreshLayout();
            }
        });

        // Tải dữ liệu
        loadSavedData();
    }

    // Lưu dữ liệu vào SharedPreferences
    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder stringBuilder = new StringBuilder();

        for (String item : thongTinList) {
            stringBuilder.append(item).append("##");
        }

        editor.putString(KEY_RESULT, stringBuilder.toString());
        editor.apply();
    }

    // Tải dữ liệu đã lưu
    private void loadSavedData() {
        String savedData = sharedPreferences.getString(KEY_RESULT, "");

        if (!savedData.isEmpty()) {
            String[] items = savedData.split("##");

            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    thongTinList.add(item);
                }
            }
            refreshLayout();
        }
    }

    // Xóa dữ liệu và làm mới layout
    private void refreshLayout() {
        layoutResultContainer.removeAllViews(); // Xóa toàn bộ view cũ

        for (int i = 0; i < thongTinList.size(); i++) {
            final int index = i; // Chỉ số của thông tin cần xóa

            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(16, 16, 16, 16);

            TextView textView = new TextView(this);
            textView.setText(thongTinList.get(i));
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            Button btnDelete = new Button(this);
            btnDelete.setText("Xóa");
            btnDelete.setOnClickListener(v -> {
                thongTinList.remove(index); // Xóa thông tin khỏi danh sách
                saveData(); // Cập nhật SharedPreferences
                refreshLayout(); // Làm mới giao diện
            });

            itemLayout.addView(textView);
            itemLayout.addView(btnDelete);

            layoutResultContainer.addView(itemLayout);
        }
    }
}
