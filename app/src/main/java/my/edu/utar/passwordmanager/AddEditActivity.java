package my.edu.utar.passwordmanager;

import android.os.Bundle;
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

public class AddEditActivity extends AppCompatActivity {

  int editId = -1;
  TextInputEditText etSite, etUser, etPass, etNotes;
  TextInputLayout tilSiteName, tilUsername, tilPassword, tilNotes;
  Button btnSave;
  TextView tvTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_edit);

    etSite = findViewById(R.id.etSiteName);
    etUser = findViewById(R.id.etUsername);
    etPass = findViewById(R.id.etPassword);
    etNotes = findViewById(R.id.etNotes);
    tilSiteName = findViewById(R.id.tilSiteName);
    tilUsername = findViewById(R.id.tilUsername);
    tilPassword = findViewById(R.id.tilPassword);
    tilNotes = findViewById(R.id.tilNotes);
    btnSave = findViewById(R.id.btnSave);
    tvTitle = findViewById(R.id.tvTitle);

    if (getIntent().hasExtra("ID")) {
      editId = getIntent().getIntExtra("ID", -1);

      etSite.setText(getIntent().getStringExtra("SITE"));
      etUser.setText(getIntent().getStringExtra("USER"));
      etPass.setText(getIntent().getStringExtra("PASS"));
      etNotes.setText(getIntent().getStringExtra("NOTES"));

      btnSave.setText(R.string.btn_update);
      tvTitle.setText(R.string.add_edit_title_update);
    }

    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String site = etSite.getText().toString().trim();
        String user = etUser.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (site.isEmpty()) {
          tilSiteName.setError("Site Name is required");
        } else {
          tilSiteName.setError(null);
        }

        if (user.isEmpty()) {
          tilUsername.setError("Username is required");
        } else {
          tilUsername.setError(null);
        }

        if (pass.isEmpty()) {
          tilPassword.setError("Password is required");
        } else {
          tilPassword.setError(null);
        }

        if (!site.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {
          DatabaseHelper databaseHelper = new DatabaseHelper(AddEditActivity.this);

          if (editId == -1) {
            PasswordModel newEntry = new PasswordModel(site, user, pass, notes);
            boolean success = databaseHelper.addOne(newEntry);

            if (success) {
              Toast.makeText(AddEditActivity.this, "Password Added Successfully", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(AddEditActivity.this, "Failed to add password.", Toast.LENGTH_SHORT).show();
            }
          } else {
            PasswordModel updatedEntry = new PasswordModel(editId, site, user, pass, notes);
            boolean success = databaseHelper.updateOne(updatedEntry);

            if (success) {
              Toast.makeText(AddEditActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(AddEditActivity.this, "Failed to update password.", Toast.LENGTH_SHORT).show();
            }
          }

          finish();
        }
      }
    });
  }
}