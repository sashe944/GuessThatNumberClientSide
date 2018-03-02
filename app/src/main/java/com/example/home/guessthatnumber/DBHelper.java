package com.example.home.guessthatnumber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Home on 5.2.2018 г..
 */

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public static final String DB_NAME = "GuessGame.db";
    public static final int DB_VERSION = 1;

    public static final String USER_ID = "_id";
    public static final String USER_USERNAME = "username";
    public static final String USER_PASSWORD = "password";
    public static final String USER_EMAIL = "email";
    public static final String USER_GENDER = "gender";
    public static final String USER_AVATAR = "avatar";

    public static final String POINTS_ID="points_id";
    public static final String POINTS="Points";
    public static final String FK_USER_ID="user_id";

    public static final String CREATE_TABLE_USER =
            "CREATE TABLE user ('" +
                    USER_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "'" + USER_USERNAME + "' TEXT NOT NULL UNIQUE," +
                    "'" + USER_PASSWORD + "' TEXT NOT NULL," +
                    "'" + USER_EMAIL + "' TEXT NOT NULL UNIQUE," +
                    "'" + USER_GENDER + "' TEXT," +
                    "'" + USER_AVATAR + "' TEXT DEFAULT 'noimage.png')";

    public static final String CREATE_TABLE_POINTS =
    "CREATE TABLE points ('" + POINTS_ID +"'INTEGER PRIMARY KEY AUTOINCREMENT," +
            "'" +POINTS + "' TEXT NOT NULL," +
            "'" +FK_USER_ID + "'INTEGER,FOREIGN KEY(FK_USER_ID) REFERENCES user(_id) ";

    public DBHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_POINTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS points");
        onCreate(db);

    }

    public boolean registerUser(User user) {
        try {
            db = getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(USER_USERNAME, user.username);
            cv.put(USER_PASSWORD, user.password);
            cv.put(USER_EMAIL, user.email);
            cv.put(USER_AVATAR, user.imagePath);
            cv.put(USER_GENDER, user.gender);

            if (db.insertOrThrow("user", null, cv) == -1) {
                return false;
            }

        } catch (SQLException e) {
            Log.e("DB_EXCEPTION", e.getMessage());
            return false;
        } finally {
            if (db != null)
                db.close();
        }
        return true;
    }

    public User loginUser(String username, String password) {
        User user = null;
        Cursor c = null;

        try {
            db = getReadableDatabase();
            String query = "SELECT * FROM user WHERE " +
                    USER_USERNAME + " LIKE ? AND " +
                    USER_PASSWORD + " LIKE ?";

            c = db.rawQuery(query, new String[]
                    {
                            username,
                            password
                    });
            if (c.moveToFirst()) {
                user = new User();
                user.username = username;
                user.password = password;

                int emailIndex = c.getColumnIndex(USER_EMAIL);
                user.email = c.getString(emailIndex);

                user.gender = c.getString(
                        c.getColumnIndex(USER_GENDER));

                return user;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", e.getMessage());
        } finally {
            if (c != null)
                c.close();

            if (db != null)
                db.close();
        }

        return user;
    }
}