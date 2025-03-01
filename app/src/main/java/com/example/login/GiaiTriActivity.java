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

public class GiaiTriActivity extends AppCompatActivity {

    EditText editTextTien, editTextLoaiChiTieu;
    Button btnChonNgay, btnLuuThongTin, btnQuayLai;
    TextView textViewNgayThang;
    LinearLayout layoutResultContainer;

    String ngayThang = "";
    ArrayList<String> thongTinList = new ArrayList<>(); // Array to store the information

    // SharedPreferences
    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "GiaiTriPrefs";
    private static final String KEY_RESULT = "thongTinList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giaitri);

        // Initialize views
        editTextTien = findViewById(R.id.editTextTien);
        editTextLoaiChiTieu = findViewById(R.id.editTextLoaiChiTieu);
        btnChonNgay = findViewById(R.id.btnChonNgay);
        btnLuuThongTin = findViewById(R.id.btnLuuThongTin);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        textViewNgayThang = findViewById(R.id.textViewNgayThang);
        layoutResultContainer = findViewById(R.id.layoutResultContainer);

        // SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Back button
        btnQuayLai.setOnClickListener(v -> finish());

        // Date picker button
        btnChonNgay.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(GiaiTriActivity.this, (view, year1, month1, dayOfMonth) -> {
                ngayThang = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                textViewNgayThang.setText("Ngày tháng: " + ngayThang);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Save information button
        btnLuuThongTin.setOnClickListener(v -> {
            String tien = editTextTien.getText().toString();
            String loaiChiTieu = editTextLoaiChiTieu.getText().toString();

            if (tien.isEmpty() || loaiChiTieu.isEmpty() || ngayThang.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                String result = "Số tiền: " + tien +
                        "\nLoại chi tiêu: " + loaiChiTieu +
                        "\nNgày tháng: " + ngayThang;

                thongTinList.add(result);
                saveData();
                refreshLayout();
            }
        });

        // Load saved data
        loadSavedData();
    }

    // Save data to SharedPreferences
    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder stringBuilder = new StringBuilder();

        for (String item : thongTinList) {
            stringBuilder.append(item).append("##");
        }

        editor.putString(KEY_RESULT, stringBuilder.toString());
        editor.apply();
    }

    // Load saved data
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

    // Refresh layout to display the saved data
    private void refreshLayout() {
        layoutResultContainer.removeAllViews(); // Remove all old views

        for (int i = 0; i < thongTinList.size(); i++) {
            final int index = i; // Index of the information to delete

            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(16, 16, 16, 16);

            TextView textView = new TextView(this);
            textView.setText(thongTinList.get(i));
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            Button btnDelete = new Button(this);
            btnDelete.setText("Xóa");
            btnDelete.setOnClickListener(v -> {
                thongTinList.remove(index); // Remove information from the list
                saveData(); // Update SharedPreferences
                refreshLayout(); // Refresh layout
            });

            itemLayout.addView(textView);
            itemLayout.addView(btnDelete);

            layoutResultContainer.addView(itemLayout);
        }
    }
}
