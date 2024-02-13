package com.dam.proyectoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.proyectoandroid.Database.DatabaseAux;
import com.dam.proyectoandroid.Database.User;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView titulo = findViewById(R.id.titulo);

        Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom);
        titulo.startAnimation(zoomAnimation);

    }
    public void signUpNewUser(View v){
        TextView nameTextView = findViewById(R.id.usuarioreg);
        TextView emailTextView = findViewById(R.id.mailreg);
        TextView passTextView = findViewById(R.id.passreg);

        String nameString = nameTextView.getText().toString();
        String emailString = emailTextView.getText().toString();
        String passString = passTextView.getText().toString();

        DatabaseAux aux = new DatabaseAux(Register.this);
        SQLiteDatabase db = aux.getWritableDatabase();

       if(db != null && !nameString.isEmpty() && !emailString.isEmpty() && !passString.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("name", nameString);
            values.put("email", emailString);
            values.put("pass", passString);

            long res = db.insert("users", null, values);
            System.out.println(res);
            if(res >= 0) {
                Toast.makeText(this, "Insertado correctamente", Toast.LENGTH_LONG).show();
                nameTextView.setText("");
                emailTextView.setText("");
                passTextView.setText("");
            }
            else {
                Toast.makeText(this, "Fallo al insertar", Toast.LENGTH_LONG).show();
            }
            db.close();
        }
        Intent nIntent = new Intent(Register.this, Login.class);
        startActivity(nIntent);
    }
}