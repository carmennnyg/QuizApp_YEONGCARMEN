package com.example.scienceknowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWER = "keyAnswer";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView mQuestionTV, mScoreTV, mQuestionCountTV, mTimeCountTV;
    private Button mBtnConfirm;
    private RadioButton mRB1, mRB2, mRB3;
    private RadioGroup mRadioGroup;

    private ColorStateList mTextColorRb;
    private ColorStateList mTextColorCb;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private int questionCounter;
    private int questionCountTotal;

    private Question currentQuestion;
    private ArrayList<Question> questionList;

    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTV = findViewById(R.id.text_view_question);
        mScoreTV = findViewById(R.id.score_text_view);
        mQuestionCountTV = findViewById(R.id.question_text_view);
        mTimeCountTV = findViewById(R.id.timeCountdown_text_view);
        mRadioGroup = findViewById(R.id.radio_group);
        mRB1 = findViewById(R.id.radio_button1);
        mRB2 = findViewById(R.id.radio_button2);
        mRB3 = findViewById(R.id.radio_button3);
        mBtnConfirm = findViewById(R.id.confirm_button);

        mTextColorRb = mRB1.getHintTextColors();
        mTextColorCb = mTimeCountTV.getTextColors();

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();

        } else{
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWER);

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }


        mBtnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (mRB1.isChecked() || mRB2.isChecked() || mRB3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please Select An Answer!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        mRB1.setTextColor(mTextColorRb);
        mRB2.setTextColor(mTextColorRb);
        mRB3.setTextColor(mTextColorRb);
        mRadioGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            mQuestionTV.setText(currentQuestion.getQuestion());
            mRB1.setText(currentQuestion.getOption1());
            mRB2.setText(currentQuestion.getOption2());
            mRB3.setText(currentQuestion.getOption3());

            questionCounter++;
            mQuestionCountTV.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            mBtnConfirm.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTimeCountTV.setText(timeFormatted);
        if (timeLeftInMillis < 10000) {
            mTimeCountTV.setTextColor(Color.RED);
        } else {
            mTimeCountTV.setTextColor(mTextColorCb);
        }
    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(mRadioGroup.getCheckedRadioButtonId());
        int answer = mRadioGroup.indexOfChild(rbSelected) + 1;
        if (answer == currentQuestion.getAnswer()) {
            score++;
            mScoreTV.setText("Score: " + score);
        }
        showSolution();
    }

    private void showSolution() {
        mRB1.setTextColor(Color.BLACK);
        mRB2.setTextColor(Color.BLACK);
        mRB3.setTextColor(Color.BLACK);
        switch (currentQuestion.getAnswer()) {
            case 1:
                mRB1.setTextColor(Color.WHITE);
                mQuestionTV.setText("Option 1 is correct");
                break;
            case 2:
                mRB2.setTextColor(Color.WHITE);
                mQuestionTV.setText("Option 2 is correct");
                break;
            case 3:
                mRB3.setTextColor(Color.WHITE);
                mQuestionTV.setText("Option 3 is correct");
                break;
        }
        if (questionCounter < questionCountTotal) {
            mBtnConfirm.setText("Next");
        } else {
            mBtnConfirm.setText("Finish");
        }
    }
    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed(){
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press Back Again To Finish The Quiz!", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWER, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}
