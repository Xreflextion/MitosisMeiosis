package com.example.mitosis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> mitosis_advantages = new ArrayList<>(asList("Advantages of mitosis",
            "Can easily replace dead or old cells in a multicellular organism because all cells are identical", "Takes less energy and preparation time to reproduce"));
    ArrayList<String> mitosis_disadvantages = new ArrayList<>(asList("Disadvantages of mitosis", "No genetic variety",
            "Higher risk of extinction through negative changes in the environment as all organisms of that kind would be affected by that change."));

    public void toMeiosis(View view) {
        Intent intent = new Intent(getApplicationContext(), Meiosis.class);

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
        setContentView(R.layout.activity_main);

    }
}