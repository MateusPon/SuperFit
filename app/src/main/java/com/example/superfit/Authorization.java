package com.example.superfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class Authorization extends AppCompatActivity {

    String userName, pin_code;
    private AwesomeValidation awesomeValidation;
    boolean hasUser;
    EditText etName, etEmail;
    DBHelper dbHelper;
    TextView txSingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        etName = findViewById(R.id.txUserName);
        TextView txSignIn = findViewById(R.id.txSignIn);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        dbHelper = new DBHelper(this);

        awesomeValidation.addValidation(this, R.id.txUserName, RegexTemplate.NOT_EMPTY,R.string.user_name);

        txSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    Cursor cursor  = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                    userName = etName.getText().toString();
                    hasUser = false;
                    if(cursor.moveToFirst()){
                        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                        int codeIndex = cursor.getColumnIndex(DBHelper.KEY_CODE);
                        do{

                            if(cursor.getString(nameIndex).equals(userName)){
                                hasUser = true;
                                pin_code = cursor.getString(codeIndex);
                                break;
                            }
                        }
                        while (cursor.moveToNext());
                    }
                    cursor.close();

                    if(hasUser){
                        Intent i = new Intent(Authorization.this, AuthorizationCode.class);
                        i.putExtra("userName", userName);
                        i.putExtra("code", pin_code);
                        startActivity(i);
                    }
                    else Toast.makeText(Authorization.this, "Пользователя с таким именем не существует!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void SignUp(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}