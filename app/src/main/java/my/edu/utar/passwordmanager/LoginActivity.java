package my.edu.utar.passwordmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
  TextInputEditText etMasterPass;
  Button btnLogin;
  TextView tvLoginTitle;
  SharedPreferences pref;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    etMasterPass = findViewById(R.id.etPassword);
    btnLogin = findViewById(R.id.btnLogin);
    tvLoginTitle = findViewById(R.id.tvTitle);

    pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String savedMaster = pref.getString("master_password", "");

    if (savedMaster.isEmpty()) {
      tvLoginTitle.setText("Set Your Master Password");
      btnLogin.setText("Set & Login");
    } else {
      tvLoginTitle.setText("Enter Master Password");
      btnLogin.setText("Login");
    }

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String entered = etMasterPass.getText().toString().trim();

        if (entered.isEmpty()) {
          Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
          return;
        }

        String currentSavedPassword = pref.getString("master_password", "");

        if (currentSavedPassword.isEmpty()) {
          pref.edit().putString("master_password", entered).apply();
          Toast.makeText(LoginActivity.this, "Master Password Saved.", Toast.LENGTH_SHORT).show();
          navigateToMain();
        } else {
          if (entered.equals(currentSavedPassword)) {
            navigateToMain();
          } else {
            Toast.makeText(LoginActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
          }
        }
      }
    });
  }

  private void navigateToMain() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }
}