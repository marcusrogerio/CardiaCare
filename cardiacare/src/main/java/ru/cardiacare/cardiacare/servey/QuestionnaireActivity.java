package ru.cardiacare.cardiacare.servey;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.petrsu.cardiacare.smartcare.SmartCareLibrary;
import com.petrsu.cardiacare.smartcare.servey.Answer;
import com.petrsu.cardiacare.smartcare.servey.Feedback;
import com.petrsu.cardiacare.smartcare.servey.Question;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import ru.cardiacare.cardiacare.MainActivity;
import ru.cardiacare.cardiacare.R;

/* Отображение периодического опросника */

public class QuestionnaireActivity extends AppCompatActivity {

    RecyclerView QuestionnaireRecyclerView;
    RecyclerView.Adapter QuestionnaireAdapter;
    RecyclerView.LayoutManager QuestionnaireLayoutManager;
    public Context context = this;
    static public ImageButton buttonClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            FileInputStream fIn = openFileInput("feedback.json");
            String jsonFromFile = readSavedData();
            Gson json = new Gson();
            Feedback qst = json.fromJson(jsonFromFile, Feedback.class);
            MainActivity.feedback = qst;
        } catch (Exception e) {

        }
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_questionnaire);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.backgroundFlag = 1;
                onBackPressed();
            }
        });

        QuestionnaireRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        QuestionnaireLayoutManager = new LinearLayoutManager(getApplicationContext());
        QuestionnaireRecyclerView.setLayoutManager(QuestionnaireLayoutManager);

        LinkedList<Question> questionnaire = MainActivity.questionnaire.getQuestions();
        int[] Types = new int[questionnaire.size()];

        for (int i = 0; i < questionnaire.size(); i++) {
            Question question = questionnaire.get(i);
            Answer answer = question.getAnswer();
            switch (answer.getType()) {
                case "Text":
                    Types[i] = RecyclerViewAdapter.TextField;
                    break;
                case "MultipleChoise":
                    Types[i] = RecyclerViewAdapter.Multiplechoice;
                    break;
                case "SingleChoise":
                    Types[i] = RecyclerViewAdapter.Singlechoice;
                    break;
                case "BipolarQuestion":
                    Types[i] = RecyclerViewAdapter.Bipolarquestion;
                    break;
                case "Dichotomous":
                    Types[i] = RecyclerViewAdapter.Dichotomous;
                    break;
                case "GuttmanScale":
                    Types[i] = RecyclerViewAdapter.Guttmanscale;
                    break;
                case "LikertScale":
                    Types[i] = RecyclerViewAdapter.Likertscale;
                    break;
                case "ContinuousScale":
                    Types[i] = RecyclerViewAdapter.Continuousscale;
                    break;
                default:
                    Types[i] = RecyclerViewAdapter.DefaultValue;
            }
        }

        QuestionnaireAdapter = new RecyclerViewAdapter(MainActivity.questionnaire.getQuestions(), Types, context);
        QuestionnaireRecyclerView.setAdapter(QuestionnaireAdapter);

        buttonClean = (ImageButton) findViewById(R.id.buttonClean);
        buttonClean.setOnClickListener(new View.OnClickListener() {// Clean
            @Override // Clean
            public void onClick(View v) {
                MainActivity.backgroundFlag = 1;
                buttonClean.setEnabled(false);
                MainActivity.feedback = new Feedback("1 test", "Student", "feedback");
                Gson json = new Gson();
                String jsonStr = json.toJson(MainActivity.feedback);
                System.out.println(jsonStr);
                writeData(jsonStr);

                Intent intent = getIntent();
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }// Clean
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.backgroundFlag = 0;
        MainActivity.serveyButton.setEnabled(true);
//        MainActivity.QuestionnaireButton.setEnabled(true);//возвращаем состояние нажатия от повторного нажатия
//        buttonClean.setEnabled(true);//возвращаем состояние нажатия от повторного нажатия
//        MainActivity.alarmButton.setEnabled(true);//возвращаем состояние нажатия от повторного нажатия
    }

    public void writeData(String data) {
        try {
//            FileOutputStream fOut = openFileOutput (filename , MODE_PRIVATE );
            FileOutputStream fOut = context.openFileOutput("feedback.json", context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readSavedData() {
        StringBuilder datax = new StringBuilder("");
        try {
            FileInputStream fIn = openFileInput("feedback.json");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.append(readString);
                readString = buffreader.readLine();
            }
            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return datax.toString();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MainActivity.backgroundFlag = 0;
        MainActivity.ConnectToSmartSpace();
    }

    @Override
    public void onPause() {
        Gson json = new Gson();
        String jsonStr = json.toJson(MainActivity.feedback);
        System.out.println(jsonStr);
        writeData(jsonStr);
        // To SIB
        Long timestamp = System.currentTimeMillis() / 1000;
        String ts = timestamp.toString();
        SmartCareLibrary.sendFeedback(MainActivity.nodeDescriptor, MainActivity.patientUri, MainActivity.feedbackUri, ts);
        // To Server
        FeedbackPOST feedbackPOST = new FeedbackPOST(context);
        feedbackPOST.execute();
        super.onPause();
        if (MainActivity.backgroundFlag == 0) {
            MainActivity.DisconnectFromSmartSpace();
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.backgroundFlag = 1;
        super.onBackPressed();
    }
}