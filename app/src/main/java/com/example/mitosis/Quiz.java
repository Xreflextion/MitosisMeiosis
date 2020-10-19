package com.example.mitosis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static java.util.Arrays.asList;

public class Quiz extends AppCompatActivity {
    private ConstraintLayout main;
    private ConstraintLayout cur_layout;
    private LinearLayout mc_layout;
    private TextView question_text_view;
    private TextView score_text_view;
    private TextView questions_left_text_view;
    private TextView response_text_view;
    private ImageView img_view;

    private ArrayList<ConstraintLayout> layouts;
    final private int[] total_questions = {10,2,1};

    final private ArrayList<String> phases = new ArrayList<>(asList("Interphase", "Prophase", "Metaphase", "Anaphase", "Telophase", "Prophase 1", "Metaphase 1",
            "Anaphase 1", "Telophase 1", "Prophase 2", "Metaphase 2", "Anaphase 2"));
    private HashMap<String, Integer> mc_questions = new HashMap<>();
    ArrayList<ArrayList<String>> diagrams = new ArrayList<>(asList( new ArrayList<>(asList("prophase1_diagram", "homologous chromosomes", "spindles/spindle fibres",
            "recombinant chromosomes/homologous chromosomes", "chromosome/sister chromatid")), new ArrayList<>(asList("daughter_cell_diagram", "centriole",
            "chromosome/chromatid", "nuclear membrane/nuclear envelope", "centromere")), new ArrayList<>(asList("meiosis_stages", "anaphase 2/anaphase2/anaphase two/anaphase ii",
            "telophase 1/telophase1/telophase one/telophase i", "metaphase 1/metaphase1/metaphase one/metaphase i", "prophase 1/prophase1/prophase one/prophase i")),
            new ArrayList<>(asList("mitosis_stages", "metaphase", "prophase", "anaphase", "telophase"))));
    final private ArrayList<ArrayList<String>> selection_questions = new ArrayList<>(asList(new ArrayList<>(asList("Meiosis", "Creates four haploid cells",
            "Germ cells do this process", "Creates gametes", "Daughter cells are not identical to parent", "Divides twice", "This process is done for sexual reproduction",
            "Gives genetic variety to a species")), new ArrayList<>(asList("Mitosis", "Creates two diploid cells", "Somatic cells do this process",
            "Daughter cells are identical to parent", "Divides once", "A form of asexual reproduction",
            "In multicellular organisms, this process can be done for replenishing old or dead cells", "In multicellular organisms, this process can be done for growth"))));
    ListView lst_view;
    ArrayAdapter<String> array_adapter;
    ArrayList<String> answers = new ArrayList<>();
    Random rand = new Random();

    private HashSet<String> seen = new HashSet<>();

    final private ArrayList<String> happy = new ArrayList<>(asList("( ´ ▽ ` )ﾉ", "（‐＾▽＾‐）", "d=(´▽｀)=b", "o(≧∇≦o)", "(*≧▽≦)", "(＾▽＾)", "~ヾ(＾∇＾)", "( •⌄• ू )✧"));
    final private ArrayList<String> sad = new ArrayList<>(asList("(╥_╥)", "(Ｔ▽Ｔ)", ".·´¯`(ू>︿< ू)´¯`·.", "(T＿T)", "(⋟﹏⋞)"));

    private String answer;

    private int cur_idx = 0;

    private int score = 0;
    private int total_score;
    private int total;
    private int questions_left;
    private int question_count;
    private int part_total;

    public void make_question(View view) {
        Button b = (Button) view;
        if (b.getTag() != null){
            switch_txt(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check_answer(v);
                }
            });
        }
        questions_left -= 1;
        question_count += 1;
        int num;
        switch (cur_idx) {
            case 1:
                try {
                    response_text_view.setText("");
                    int placeholder = rand.nextInt(4);
                    if (rand.nextBoolean()) {
                        answer = phases.get(rand.nextInt(phases.size()));
                        while (seen.contains(answer)) {
                            answer = phases.get(rand.nextInt(phases.size()));
                        }
                        seen.add(answer);
                        ArrayList<String> options = new ArrayList<>();
                        String str = "";
                        for (int i =0; i< mc_layout.getChildCount(); i++) {
                            if (i == placeholder) {
                                str = answer;
                            } else {
                                str = phases.get(rand.nextInt(phases.size()));
                                while (options.contains(str) || str.equals(answer)) {
                                    str = phases.get(rand.nextInt(phases.size()));
                                }
                            }
                            options.add(str);
                            Button button = (Button) mc_layout.getChildAt(i);
                            button.setText(str);
                        }
                        img_view.setImageResource(getResources().getIdentifier(answer.toLowerCase().replaceAll("\\s+",""), "drawable", getPackageName()));
                        question_text_view.setText(R.string.whatPhase);
                    } else {
                        ArrayList<String> arr = new ArrayList<>(mc_questions.keySet());
                        answer = arr.get(rand.nextInt(arr.size()));
                        while (seen.contains(answer)) {
                            answer = arr.get(rand.nextInt(arr.size()));
                        }
                        seen.add(answer);
                        ArrayList<Integer> options = new ArrayList<>();
                        String str = "";
                        for (int i = 0; i < mc_layout.getChildCount(); i++) {
                            if (i == placeholder) {
                                num = mc_questions.get(answer);
                            } else {
                                str = arr.get(rand.nextInt(arr.size()));
                                num = mc_questions.get(str);
                                while (options.contains(num) || num == mc_questions.get(answer)) {
                                    str = arr.get(rand.nextInt(arr.size()));
                                    num = mc_questions.get(str);
                                }
                            }
                            options.add(num);
                            Button button = (Button) mc_layout.getChildAt(i);
                            button.setText(Integer.toString(num));
                        }
                        question_text_view.setText(answer);
                        answer = Integer.toString(mc_questions.get(answer));
                        img_view.setImageResource(0);
                    }
                    break;
                } catch( Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            case 2:
                ImageView diagram_img = (ImageView) findViewById(R.id.diagramImgView);
                num = rand.nextInt(diagrams.size());
                while (seen.contains(Integer.toString(num))) {
                    num = rand.nextInt(diagrams.size());
                }
                answer = Integer.toString(num);
                seen.add(answer);
                ArrayList<String> diagram = diagrams.get(num);
                diagram_img.setImageResource(getResources().getIdentifier(diagram.get(0), "drawable", getPackageName()));
                LinearLayout layout = findViewById(R.id.editTextLayout);
                for (int i =0; i <layout.getChildCount(); i++) {
                    EditText edit_text = (EditText) layout.getChildAt(i);
                    edit_text.setText("");
                    edit_text.setFocusable(true);
                    edit_text.setFocusableInTouchMode(true);
                    edit_text.setTag(diagram.get(i+1));
                }
                question_text_view.setText(R.string.fillBlanks);
                break;
            case 3:
                try {
                    num = rand.nextInt(selection_questions.size());
                    while (seen.contains(Integer.toString(num))) {
                        num = rand.nextInt(selection_questions.size());
                    }
                    int num2;
                    if (num == 0) {
                        num2 = 1;
                    } else {
                        num2 = 0;
                    }
                    String str = selection_questions.get(num).get(0);
                    answer = Integer.toString(num);
                    seen.add(answer);

                    lst_view = (ListView) findViewById(R.id.listView);
                    ArrayList<String> select_all_options = new ArrayList<>();

                    for (int i=0; i<5; i++) {
                        String option = selection_questions.get(num).get(rand.nextInt(selection_questions.get(0).size()-1)+1);
                        while (select_all_options.contains(option)) {
                            option = selection_questions.get(num).get(rand.nextInt(selection_questions.get(0).size()-1)+1);
                        }
                        String option2 = selection_questions.get(num2).get(rand.nextInt(selection_questions.get(0).size()-1)+1);
                        while (select_all_options.contains(option2)) {
                            option2 = selection_questions.get(num2).get(rand.nextInt(selection_questions.get(0).size()-1)+1);
                        }
                        select_all_options.add(option);
                        select_all_options.add(option2);
                        answers.add(option);
                    }
                    Collections.shuffle(select_all_options);
                    question_text_view.setText("Choose all traits of " + str);

                    array_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, select_all_options);

                    lst_view.setAdapter(array_adapter);
                    lst_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (view.getTag() == null) {
                                view.setBackgroundColor(Color.GRAY);
                                view.setTag("chosen");
                            } else {
                                view.setBackgroundColor(Color.WHITE);
                                view.setTag(null);
                            }
                        }
                    });
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }

        }
        questions_left_text_view.setText(getString(R.string.questionsLeft, questions_left));
    }

    public void check_answer(View view) {
        Button button = (Button) view;
        if (button == findViewById(R.id.nextPgButton)) {
            button.setVisibility(View.INVISIBLE);
            mc_layout.setVisibility(View.VISIBLE);
            make_question(view);
        } else {
            switch (cur_idx) {
                case 1:
                    button = (Button) view;
                    if (button.getText().toString().equals(answer)) {
                        response_text_view.setText(getString(R.string.correct,  happy.get(rand.nextInt(happy.size()))));
                        response_text_view.setTextColor(Color.GREEN);
                        score += 1;
                    } else {
                        response_text_view.setText(getString(R.string.wrong, sad.get(rand.nextInt(sad.size())) + "\n Answer was: " + answer));
                        response_text_view.setTextColor(Color.RED);
                    }
                    button = (Button) findViewById(R.id.nextPgButton);
                    button.setVisibility(View.VISIBLE);
                    mc_layout.setVisibility(View.INVISIBLE);
                    if (question_count >= part_total) {
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch_layout(v);
                            }
                        });
                    }
                    break;
                case 2:
                    View v = getCurrentFocus();
                    if (v == null) {
                        v = view;
                    }
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    LinearLayout layout = findViewById(R.id.editTextLayout);
                    ArrayList<String> diagram = diagrams.get(Integer.parseInt(answer));
                    ImageView img_view = findViewById(R.id.diagramImgView);
                    boolean correct = false;
                    for (int i=0; i < layout.getChildCount(); i++) {
                        EditText edit_text = (EditText) layout.getChildAt(i);
                        if (edit_text.getTag() != null) {
                            String[] ans = edit_text.getTag().toString().split("/");
                            String input = ((EditText) layout.getChildAt(i)).getText().toString();
                            for (String an : ans) {
                                if (an.equals(input.trim().toLowerCase())) {
                                    edit_text.setText(getString(R.string.correct, happy.get(rand.nextInt(happy.size()))));
                                    correct = true;
                                    score += 1;
                                }
                            }
                            if (!correct){
                                String wrong_answer = edit_text.getText().toString();
                                edit_text.setText(getString(R.string.wrong, "You wrote: "+ wrong_answer));
                            }
                            correct = false;
                        }
                        edit_text.setFocusable(false);
                        edit_text.setFocusableInTouchMode(false);
                    }
                    String name = diagram.get(0)+"_answers";
                    img_view.setImageResource(getResources().getIdentifier(name, "drawable", getPackageName()));
                    button = findViewById(R.id.submitButton);
                    switch_txt(button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            make_question(v);
                        }
                    });
                    if (question_count >= part_total) {
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch_layout(v);
                            }
                        });
                    }
                    break;
                case 3:
                    String mark = "";
                    for (int i=0; i<lst_view.getChildCount();i ++) {
                        TextView txt = (TextView) lst_view.getChildAt(i);
                        String str = txt.getText().toString();
                        if (answers.contains(str) && txt.getTag() != null) {
                            mark += "Correct! \n \n";
                            score ++;
                        } else if (answers.contains(str) && txt.getTag() == null) {
                            mark += "Missed this. \n \n";
                        } else if (txt.getTag() != null) {
                            mark += "Incorrect. \n \n";
                            score --;
                        } else {
                            mark += "\n \n";
                        }
                    }
                    TextView text_view = (TextView) findViewById(R.id.markTextView);
                    text_view.setText(mark);
                    button = (Button) findViewById(R.id.selectAllSubmitButton);
                    if (question_count >= part_total) {
                        switch_txt(button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch_layout(v);
                            }
                        });
                    }
            }
            if (score < 0) {
                score = 0;
            }
            score_text_view.setText(getString(R.string.score, score));
        }

    }


    public void switch_layout(View v) {
        Button b = (Button) v;
        if (b.getTag() != null) {
            switch_txt(b);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check_answer(v);
                    }
                });
        }
        cur_layout.setVisibility(View.INVISIBLE);
        response_text_view.setText("");
        question_text_view.setText("");
        if (questions_left <= 0) {
            cur_layout = main;
            score_text_view.setText("Final Score: "+ score + "/" + total_score);
            Button button = (Button) findViewById(R.id.startButton);
            button.setText("Take the quiz again?");
        } else {
            seen = new HashSet<>();
            cur_layout = layouts.get(cur_idx);
            question_count = 0;
            part_total = total_questions[cur_idx];
            cur_idx += 1;
            make_question(v);
        }
        cur_layout.setVisibility(View.VISIBLE);
    }

    public void goBack(View view) {
        finish();
    }

    public void reset(View v) {
        if (questions_left <= 0) {
            cur_idx = 0;
            score = 0;
            seen = new HashSet<>();
            questions_left = total;

            mc_layout.setVisibility(View.VISIBLE);
            Button button = (Button) findViewById(R.id.nextPgButton);
            button.setVisibility(View.INVISIBLE);

            button = (Button) findViewById(R.id.submitButton);
            switch_txt(button);

            TextView txt_view = (TextView) findViewById(R.id.markTextView);
            txt_view.setText("");
            answers = new ArrayList<>();

            score_text_view.setText(getString(R.string.score, 0));
        }
        switch_layout(v);
    }

    public void switch_txt(Button button) {
        String txt = button.getText().toString();
        button.setText(button.getTag().toString());
        button.setTag(txt);
    }

    @Override
    public void onBackPressed() {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            if (cur_layout != main) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit the quiz?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            } else {
                finish();
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        main = (ConstraintLayout) findViewById(R.id.mainLayout);
        mc_layout = (LinearLayout) findViewById(R.id.mcButtonsLayout);
        cur_layout = main;

        layouts = new ArrayList<>(asList((ConstraintLayout) findViewById(R.id.multipleChoiceLayout), (ConstraintLayout) findViewById(R.id.fillBlanksLayout), (ConstraintLayout) findViewById(R.id.selectMultipleLayout)));

        question_text_view = (TextView) findViewById(R.id.questionTextView);
        questions_left_text_view = (TextView) findViewById(R.id.questionsLeftTextView);
        score_text_view = (TextView) findViewById(R.id.scoreTextView);
        response_text_view = (TextView) findViewById(R.id.responseTextView);
        img_view = (ImageView) findViewById(R.id.mcImageView);
        lst_view  = (ListView) findViewById(R.id.listView);

        for (int i: total_questions) {
            total += i;
        }

        questions_left = total;
        questions_left_text_view.setText(getString(R.string.questionsLeft, total));

        total_score = total_questions[0] + total_questions[1] * 4 + total_questions[2]*5;

        mc_questions.put("How many daughter cells does meiosis create?", 4);
        mc_questions.put("How many chromosomes are in a cell before mitosis?", 46);
        mc_questions.put("How many chromatids are in a cell during prophase?", 92);
        mc_questions.put("How many times does a cell divide in meiosis", 2);
        mc_questions.put("How many chromosomes are in the end product of one meiosis daughter cell?", 23);
        mc_questions.put("How many chromosomes are in each cell after Telophase 1", 23);
        mc_questions.put("How many sister chromatid pairs are in a daughter cell after cytokinesis", 0);
        mc_questions.put("How many parents are needed to do mitosis", 1);
        mc_questions.put("How many chromatids are in a cell during interphase", 46);
        mc_questions.put("How many homologous pairs are in a cell during prophase 1", 23);
    }
}