package com.example.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    private static final float TIEN_GOC = 0.0f;
    private float tongKhoanThu = TIEN_GOC;
    private List<PieEntry> entries; // Dữ liệu biểu đồ
    private PieChart pieChart;   // Biểu đồ thống kê
    private Button  btnBack;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        // Thêm nút back vào ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Khởi tạo biểu đồ và dữ liệu
        pieChart = findViewById(R.id.pieChart);
        entries = new ArrayList<>();
        entries.add(new PieEntry(100f, "Khoản thu")); // Khoản thu chiếm 100%

        // Khởi tạo các nút

        btnBack = findViewById(R.id.btnBack);

        // Gán sự kiện cho nút Back
        btnBack.setOnClickListener(v -> {
            // Quay lại Activity trước đó
            finish(); // Hoặc gọi onBackPressed() để quay lại Activity trước đó
        });

        // Gọi hàm load dữ liệu
        loadKhoanThuChiData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới dữ liệu mỗi khi trang được hiển thị lại
        loadKhoanThuChiData();
    }

    private void loadKhoanThuChiData() {
        // Lấy dữ liệu khoản thu từ SharedPreferences
        SharedPreferences sharedPreferencesKhoanThu = getSharedPreferences("KhoanThuPrefs", MODE_PRIVATE);
        String khoanThuData = sharedPreferencesKhoanThu.getString("thongTinList", "");
        float tongKhoanThu = TIEN_GOC;

        if (!khoanThuData.isEmpty()) {
            String[] items = khoanThuData.split("##");
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    // Parse số tiền từ dữ liệu
                    String[] parts = item.split("\n");
                    String moneyPart = parts[0].replace("Money: ", "").replace(",", "").trim();
                    try {
                        tongKhoanThu += Float.parseFloat(moneyPart);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Lấy dữ liệu khoản chi từ SharedPreferences
        SharedPreferences sharedPreferencesChiTieuKhac = getSharedPreferences("ChiTieuKhacPrefs", MODE_PRIVATE);
        String khoanChiData = sharedPreferencesChiTieuKhac.getString("thongTinList", "");
        float tongKhoanChi = TIEN_GOC; // Khởi tạo tổng khoản chi

        // Clear entries trước khi thêm dữ liệu mới
        entries.clear();
        entries.add(new PieEntry(100f, "Khoản thu")); // Khoản thu chiếm 100%

        if (!khoanChiData.isEmpty()) {
            String[] items = khoanChiData.split("##");
            int chiCount = 0; // Biến đếm số khoản chi
            for (String item : items) {
                if (!item.trim().isEmpty() && chiCount < 3) { // Chỉ thêm tối đa 3 khoản chi
                    // Parse số tiền và mô tả từ dữ liệu
                    String[] parts = item.split("\n");
                    String moneyPart = parts[0].replace("Money: ", "").replace(",", "").trim();
                    String description = parts.length > 1 ? parts[1].replace("Description: ", "").trim() : "Khoản chi"; // Mô tả nếu có

                    try {
                        tongKhoanChi += Float.parseFloat(moneyPart);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    // Tính phần trăm của mỗi khoản chi
                    float percentage = (Float.parseFloat(moneyPart) / tongKhoanThu) * 100;

                    // Thêm khoản chi vào danh sách với phần trăm thay vì mô tả
                    entries.add(new PieEntry(percentage, description + " (" + String.format("%.2f", percentage) + "%)")); // Hiển thị phần trăm trong mô tả
                    chiCount++; // Tăng số lượng khoản chi
                }
            }
        }

        // Cập nhật tổng khoản thu
        this.tongKhoanThu = tongKhoanThu;

        // Hiển thị phần trăm (Có thể dùng Toast hoặc TextView nếu cần)
        if (tongKhoanChi > 0) {
            Toast.makeText(this, "Khoản chi chiếm " + String.format("%.2f", (tongKhoanChi / tongKhoanThu) * 100) + "% trong khoản thu", Toast.LENGTH_SHORT).show();
        }

        // Nếu khoản chi chiếm đến 110% hoặc hơn, màu biểu đồ sẽ đổi thành đỏ
        if (tongKhoanChi >= tongKhoanThu) {
            // Nếu tổng khoản chi >= tổng khoản thu, cho tất cả các phần chiếm 100% và màu đỏ
            entries.clear();
            entries.add(new PieEntry(100f, "Khoản chi"));
        }

        // Cập nhật dữ liệu cho biểu đồ
        refreshChart();
    }

    private void refreshChart() {
        // Cập nhật dữ liệu cho biểu đồ
        PieDataSet dataSet = new PieDataSet(entries, "Thống kê tài chính");

        // Chia màu cho các phần (phân biệt các khoản chi với màu khác nhau)
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorKhoanThu, null)); // Màu cho khoản thu
        colors.add(getResources().getColor(R.color.colorKhoanChi1, null)); // Màu cho khoản chi 1
        colors.add(getResources().getColor(R.color.colorKhoanChi2, null)); // Màu cho khoản chi 2
        colors.add(getResources().getColor(R.color.colorKhoanChi3, null)); // Màu cho khoản chi 3
        colors.add(getResources().getColor(R.color.colorKhoanChi4, null)); // Màu cho khoản chi 4
        colors.add(getResources().getColor(R.color.colorKhoanChi5, null)); // Màu cho khoản chi 5
        colors.add(getResources().getColor(R.color.colorKhoanChi6, null)); // Màu cho khoản chi 6

        dataSet.setColors(colors); // Áp dụng màu cho các phần

        // Hiển thị phần trăm cho các phần trong biểu đồ
        dataSet.setDrawValues(true); // Hiển thị giá trị trên các phần
        dataSet.setValueTextSize(12f); // Kích thước chữ của giá trị
        dataSet.setValueTextColor(getResources().getColor(R.color.black, null)); // Màu chữ của giá trị thành màu đen

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Cập nhật phần mô tả dưới biểu đồ (Legend)
        pieChart.getLegend().setEnabled(true); // Hiển thị Legend (mô tả ô vuông dưới biểu đồ)
        pieChart.getLegend().setFormSize(12f); // Kích thước ô vuông
        pieChart.getLegend().setTextSize(12f); // Kích thước chữ của mô tả
        pieChart.getLegend().setTextColor(getResources().getColor(R.color.black, null)); // Màu chữ của mô tả thành màu đen

        // Cho phép mô tả xuống dòng nếu cần
        pieChart.getLegend().setWordWrapEnabled(true); // Bật tính năng xuống dòng cho Legend

        pieChart.invalidate(); // Làm mới biểu đồ
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Quay lại Activity trước đó
            finish();  // Hoặc gọi onBackPressed() để quay lại Activity trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Quay lại Activity trước đó
    }
}
