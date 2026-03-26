package my.edu.utar.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "PasswordManager.db";
  public static final int DATABASE_VERSION = 1;

  public static final String TABLE_NAME = "PASSWORD_TABLE";
  public static final String COLUMN_ID = "ID";
  public static final String COLUMN_SITE = "SITE_NAME";
  public static final String COLUMN_USER = "USERNAME";
  public static final String COLUMN_PASS = "PASSWORD";
  public static final String COLUMN_NOTES = "NOTES";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String createTableStatement = "CREATE TABLE " + TABLE_NAME + " ("
      + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_SITE + " TEXT, "
      + COLUMN_USER + " TEXT, "
      + COLUMN_PASS + " TEXT, "
      + COLUMN_NOTES + " TEXT"
      + ");";
    db.execSQL(createTableStatement);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }

  public boolean addOne(PasswordModel model) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    cv.put(COLUMN_SITE, model.getSiteName());
    cv.put(COLUMN_USER, model.getUsername());
    cv.put(COLUMN_PASS, model.getPassword());
    cv.put(COLUMN_NOTES, model.getNotes());

    long result = db.insert(TABLE_NAME, null, cv);
    db.close();
    return result != -1;
  }

  public boolean updateOne(PasswordModel model) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    cv.put(COLUMN_SITE, model.getSiteName());
    cv.put(COLUMN_USER, model.getUsername());
    cv.put(COLUMN_PASS, model.getPassword());
    cv.put(COLUMN_NOTES, model.getNotes());

    long result = db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(model.getId())});

    db.close();
    return result > 0;
  }

  public boolean deleteOne(int id) {
    SQLiteDatabase db = this.getWritableDatabase();

    long result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

    db.close();
    return result > 0;
  }

  public List<PasswordModel> getAll() {
    List<PasswordModel> returnList = new ArrayList<>();
    String queryString = "SELECT * FROM " + TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.rawQuery(queryString, null);

    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(0);
        String site = cursor.getString(1);
        String user = cursor.getString(2);
        String pass = cursor.getString(3);
        String notes = cursor.getString(4);

        PasswordModel newModel = new PasswordModel(id, site, user, pass, notes);
        returnList.add(newModel);
      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return returnList;
  }
}
