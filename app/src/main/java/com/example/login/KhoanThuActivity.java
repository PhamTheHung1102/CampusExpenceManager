package com.example.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class KhoanThuActivity extends AppCompatActivity {

    private EditText edtSoTien, edtMoTaKhoanThu, edtNgayThang;
    private Button btnLuuKhoanThu, btnBack;
    private LinearLayout layoutResultContainer;

    SharedPreferences sharedPreferencesKhoanThu;

    private static final String PREFS_NAME_KHOAN_THU = "KhoanThuPrefs";
    private static final String KEY_RESULT = "thongTinList";

    private ArrayList<String> thongTinList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoanthu);

        // Ánh xạ View
        edtSoTien = findViewById(R.id.edtSoTien);
        edtMoTaKhoanThu = findViewById(R.id.edtMoTaKhoanThu);
        edtNgayThang = findViewById(R.id.edtNgayThang);
        btnLuuKhoanThu = findViewById(R.id.btnLuuKhoanThu);
        btnBack = findViewById(R.id.btnBack);  // Nút Back
        layoutResultContainer = findViewById(R.id.layoutResultContainer);

        // Khởi tạo SharedPreferences
        sharedPreferencesKhoanThu = getSharedPreferences(PREFS_NAME_KHOAN_THU, MODE_PRIVATE);

        btnLuuKhoanThu.setOnClickListener(v -> {
            String soTien = edtSoTien.getText().toString().trim();
            String moTa = edtMoTaKhoanThu.getText().toString().trim();
            String ngayThang = edtNgayThang.getText().toString().trim();

            if (soTien.isEmpty() || moTa.isEmpty() || ngayThang.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Lưu thông tin vào danh sách (bao gồm số tiền, mô tả, ngày tháng)
                String result = "Money: " + soTien +
                        "\ntype: " + moTa +
                        "\nDay: " + ngayThang;

                thongTinList.add(result);
                saveData();
                refreshLayout();
            }
        });

        // Tải dữ liệu đã lưu
        loadSavedData();


        // Sự kiện nút Back
        btnBack.setOnClickListener(v -> {
            finish();  // Đóng activity và quay lại màn hình trước đó
        });

        // TextWatcher để định dạng số tiền với dấu phẩy
        edtSoTien.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false; // Cờ để kiểm tra trạng thái nhập liệu

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không làm gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Chỉ xử lý nếu không phải đang thay đổi lại từ TextWatcher
                if (!isEditing) {
                    isEditing = true;
                    String input = charSequence.toString().replaceAll(",", ""); // Xóa dấu phẩy cũ
                    if (!input.isEmpty()) {
                        try {
                            // Chuyển đổi thành số và định dạng lại với dấu phẩy
                            double value = Double.parseDouble(input);
                            String formattedValue = NumberFormat.getInstance(Locale.getDefault()).format(value);
                            edtSoTien.setText(formattedValue);
                            edtSoTien.setSelection(formattedValue.length()); // Đảm bảo con trỏ ở cuối văn bản
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    isEditing = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không làm gì ở đây
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferencesKhoanThu.edit();
        StringBuilder stringBuilder = new StringBuilder();

        // Lưu tất cả thông tin thu (bao gồm số tiền, mô tả, ngày tháng)
        for (String item : thongTinList) {
            stringBuilder.append(item).append("##");
        }

        // Lưu vào SharedPreferences
        editor.putString(KEY_RESULT, stringBuilder.toString());
        editor.apply();

        // Kiểm tra đã lưu thành công
        Log.d("KhoanThuActivity", "Data saved: " + stringBuilder.toString());
    }

    private void loadSavedData() {
        // Lấy dữ liệu đã lưu từ SharedPreferences
        String savedData = sharedPreferencesKhoanThu.getString(KEY_RESULT, "");

        // Kiểm tra nếu có dữ liệu lưu trữ
        if (!savedData.isEmpty()) {
            // Chia dữ liệu theo dấu phân cách "##"
            String[] items = savedData.split("##");

            // Thêm các mục đã lưu vào danh sách thongTinList
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    thongTinList.add(item);
                }
            }

            // Kiểm tra dữ liệu đã được tải từ SharedPreferences
            Log.d("KhoanThuActivity", "Loaded data: " + savedData);

            // Làm mới giao diện để hiển thị các khoản thu đã lưu
            refreshLayout();
        } else {
            // Thông báo nếu không có dữ liệu đã lưu
            Log.d("KhoanThuActivity", "No data found in SharedPreferences.");
        }
    }

    private void refreshLayout() {
        layoutResultContainer.removeAllViews(); // Xóa toàn bộ view cũ

        for (int i = 0; i < thongTinList.size(); i++) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(16, 16, 16, 16);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            itemLayout.setWeightSum(1);

            // TextView để hiển thị thông tin khoản thu
            TextView textView = new TextView(this);
            textView.setText(thongTinList.get(i));  // Hiển thị thông tin khoản thu (bao gồm số tiền, mô tả, ngày tháng)
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            textView.setMinHeight(150);  // Đặt chiều cao tối thiểu là 150dp

            // Sử dụng final variable để tránh lỗi lambda
            final int position = i;

            // Nút Xóa
            Button btnDelete = new Button(this);
            btnDelete.setText("Xóa");
            btnDelete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btnDelete.setPadding(2, 2, 2, 2);  // Giảm padding của nút Xóa để nhỏ hơn
            btnDelete.setTextSize(10);  // Chỉnh kích thước chữ của nút Xóa nhỏ hơn

            // Khi bấm nút Xóa
            btnDelete.setOnClickListener(v -> {
                thongTinList.remove(position); // Xóa thông tin khỏi danh sách
                saveData(); // Cập nhật SharedPreferences
                refreshLayout(); // Làm mới giao diện
            });

            // Thêm TextView và Button vào itemLayout
            itemLayout.addView(textView);
            itemLayout.addView(btnDelete);

            // Thêm itemLayout vào container
            layoutResultContainer.addView(itemLayout);
        }
    }
}
