package com.example.anatomyofmobile_group3_nt213;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.anatomyofmobile_group3_nt213.model.User;

public class RegisterActivity extends AppCompatActivity {
    private SQLiteConnector dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbConnector = new SQLiteConnector(this);

        EditText edtUser = findViewById(R.id.edtUserReg);
        EditText edtPass = findViewById(R.id.edtPassReg);
        EditText edtEmail = findViewById(R.id.edtEmailReg);
        Button btnRegister = findViewById(R.id.btnRegister);

        //Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                String email = edtEmail.getText().toString();

                if (user.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    String hashedPassword = PasswordHasher.hashPassword(pass);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Gọi API
                            String result = NetworkUtils.postRequest("register.php", user, email, hashedPassword);

                            // Cập nhật giao diện phải về lại Main UI Thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.equals("success")) {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        finish(); // Quay lại màn hình login
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
}