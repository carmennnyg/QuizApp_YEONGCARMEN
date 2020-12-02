package com.example.scienceknowledge;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private int answer;

    public Question(){
    }

    // Constructor
    public Question(String question, String option1, String option2, String option3, int answer) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answer = answer;
    }

    // Reading method
    protected Question(Parcel in) {
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        answer = in.readInt();
    }

    // Create the object
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    // Getter and Setter
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getOption1() {
        return option1;
    }
    public void setOption1(String option1) {
        this.option1 = option1;
    }
    public String getOption2() {
        return option2;
    }
    public void setOption2(String option2) {
        this.option2 = option2;
    }
    public String getOption3() {
        return option3;
    }
    public void setOption3(String option3) {
        this.option3 = option3;
    }
    public int getAnswer() {
        return answer;
    }
    public void setAnswer(int answer)
    {
        this.answer = answer;
    }

    // Return 0 of object
    @Override
    public int describeContents() {
        return 0;
    }

    // Write object values to parcel for storage
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeInt(answer);
    }
}