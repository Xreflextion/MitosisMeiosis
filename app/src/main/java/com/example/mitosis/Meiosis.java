package com.example.mitosis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Meiosis extends AppCompatActivity {

    public void toMitosis(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }

    public void toQuiz(View view) {
        Intent intent = new Intent(getApplicationContext(), Quiz.class);

        startActivity(intent);
    }

    public void checkAnswer(View view){
        Button button = (Button) view;
        if (button.getTag() != null && button.getTag().toString().equals("correct")) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meiosis);
    }
}