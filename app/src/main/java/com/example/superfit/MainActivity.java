package com.example.superfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.common.base.Converter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import android.database.sqlite.SQLiteDatabase;

import java.util.function.ToIntFunction;

public class MainActivity extends AppCompatActivity{


    @NotEmpty
    Button btnSignUp;
    EditText etUserName,etEmail,etPin_code,etRepeat_code;
    private AwesomeValidation awesomeValidation;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etUserName = findViewById(R.id.txUserName);
        etEmail = findViewById(R.id.txEmail);
        etPin_code = findViewById(R.id.txCode);
        etRepeat_code = findViewById(R.id.txRepeatCode);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        dbHelper = new DBHelper(this);

        awesomeValidation.addValidation(this, R.id.txUserName, RegexTemplate.NOT_EMPTY,R.string.user_name);
        awesomeValidation.addValidation(this, R.id.txEmail, Patterns.EMAIL_ADDRESS,R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.txCode, ".{4,}",R.string.pin_cod);
        awesomeValidation.addValidation(this, R.id.txCode, "[1-9][1-9][1-9][1-9]",R.string.pin_cod2);
        awesomeValidation.addValidation(this, R.id.txRepeatCode,".{4,}", R.string.repeat_pin_cod);
        awesomeValidation.addValidation(this, R.id.txRepeatCode,R.id.txCode,R.string.pin_code_match);

        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){


                    SQLiteDatabase database = dbHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_NAME,etUserName.getText().toString());
                    contentValues.put(DBHelper.KEY_MAIL,etEmail.getText().toString());
                    contentValues.put(DBHelper.KEY_CODE,etPin_code.getText().toString());

                    database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                    Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                    if(cursor.moveToNext()){
                        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                        int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                        int codeIndex = cursor.getColumnIndex(DBHelper.KEY_CODE);
                        do{

                            Log.d("Log", "ID = " + cursor.getInt(idIndex)
                                            + "\n"
                                    + "Name = " + cursor.getString(nameIndex)
                                    + "\n"
                                    + "Email = " + cursor.getString(emailIndex)
                                    + "\n"
                                    + "Code = " + cursor.getString(codeIndex)
                                    + "\n"
                                    );

                        }while (cursor.moveToNext());
                    }else {
                        Log.d("Error Log", "0 rows");
                    }

                    cursor.close();
                    dbHelper.close();

                    Intent intent = new Intent(MainActivity.this, mainScreen.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void SignIn(View view) {
        Intent intent = new Intent(this, Authorization.class);
        startActivity(intent);
    }
}