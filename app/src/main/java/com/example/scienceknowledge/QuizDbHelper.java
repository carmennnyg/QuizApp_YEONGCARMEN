package com.example.scienceknowledge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.scienceknowledge.QuizContract.*;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ScienceKnowledge.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("On what scale are there 180 degrees between freezing point & boiling point?",
                "Celsius Scale", "Fahrenheit Scale", "Kelvin Scale", 2);
        addQuestion(q1);
        Question q2 = new Question("The remains of prehistoric organisms that have been preserved in rocks are called______",
                "Fossilization", "Optometry", "Fossils", 3);
        addQuestion(q2);
        Question q3 = new Question("The process of splitting atoms is called ", "Fission", "Fusion",
                "Audacity", 1);
        addQuestion(q3);
        Question q4 = new Question("Can lasers cut diamonds", "Yes", "Under The Right Conditions",
                "No", 1);
        addQuestion(q4);
        Question q5 = new Question("What is 'mpd'?", "Multiple Personality Disorder",
                "Music Player Daemon", "Metropolitan Police Department", 1);
        addQuestion(q5);
        Question q6 = new Question("What gas did Joseph Priestley discover in 1774?", "Axiom",
                "Nitrogen", "Oxygen", 3);
        addQuestion(q6);
        Question q7 = new Question("The study of word origins is called what?", "Etymology",
                "Etiology", "Epidemiology", 1);
        addQuestion(q7);
        Question q8 = new Question("What does an odometer measure", "Height Above Sea Level",
                "Height Below Sea Level", "Both", 1);
        addQuestion(q8);
        Question q9 = new Question("Can there be lightning without rain", "Only During Daytime",
                "Yes", "No", 2);
        addQuestion(q9);
        Question q10 = new Question("What is a lipid?", "Fat", "Calories",
                "Muscle", 1);
        addQuestion(q10);

    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER, question.getAnswer());
        db.insert(QuestionsTable.TABLE_NAME,null, cv);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

}
