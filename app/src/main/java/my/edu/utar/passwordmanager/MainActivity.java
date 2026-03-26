package my.edu.utar.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  RecyclerView rvPasswordList;
  DatabaseHelper dbHelper;
  PasswordAdapter adapter;
  Button btnAdd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    rvPasswordList = findViewById(R.id.rvPasswordList);
    btnAdd = findViewById(R.id.btnAdd);
    dbHelper = new DatabaseHelper(MainActivity.this);

    rvPasswordList.setLayoutManager(new LinearLayoutManager(this));

    refreshData();

    btnAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();

    refreshData();
  }

  public void refreshData() {
    List<PasswordModel> allPasswords = dbHelper.getAll();
    adapter = new PasswordAdapter(allPasswords, MainActivity.this);
    rvPasswordList.setAdapter(adapter);
  }
}