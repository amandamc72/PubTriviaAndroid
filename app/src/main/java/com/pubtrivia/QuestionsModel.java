package com.pubtrivia;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionsModel implements Parcelable {

    @SerializedName("response_code")
    private int error;
    @SerializedName("results")
    private ArrayList<QuestionsData> questions = new ArrayList<>();

    public QuestionsModel(int error, ArrayList<QuestionsData> questions) {

        this.error = error;
        this.questions = questions;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public ArrayList<QuestionsData> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionsData> questions) {
        this.questions = questions;
    }

    public static class QuestionsData implements Parcelable {

        @SerializedName("category")
        private String category;
        @SerializedName("type")
        private String type;
        @SerializedName("difficulty")
        private String difficulty;
        @SerializedName("question")
        private String question;
        @SerializedName("correct_answer")
        private String correct_answer;
        @SerializedName("incorrect_answers")
        private  ArrayList<String> incorrect_answers;


        public String getCategory() {
            return category;
        }

        public String getType() {
            return type;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public String getQuestion() {
            return question;
        }

        public String getCorrect_answer() {
            return correct_answer;
        }

        public ArrayList<String> getIncorrect_answers() {

            return incorrect_answers;
        }

        public ArrayList<String> getAllAnswers() {
            ArrayList<String> answers = getIncorrect_answers();
            answers.add(getCorrect_answer());
            Collections.shuffle(answers);
            return answers;
        }

        public QuestionsData(String category, String type, String difficulty, String question, String correct_answer, ArrayList<String> incorrect_answers) {
            this.category = category;
            this.type = type;
            this.difficulty = difficulty;
            this.question = question;
            this.correct_answer = correct_answer;
            this.incorrect_answers = incorrect_answers;
        }

        protected QuestionsData(Parcel in) {
            category = in.readString();
            type = in.readString();
            difficulty = in.readString();
            question = in.readString();
            correct_answer = in.readString();
            if (in.readByte() == 0x01) {
                incorrect_answers = new ArrayList<String>();
                in.readList(incorrect_answers, String.class.getClassLoader());
            } else {
                incorrect_answers = null;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(category);
            dest.writeString(type);
            dest.writeString(difficulty);
            dest.writeString(question);
            dest.writeString(correct_answer);
            if (incorrect_answers == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeList(incorrect_answers);
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<QuestionsData> CREATOR = new Parcelable.Creator<QuestionsData>() {
            @Override
            public QuestionsData createFromParcel(Parcel in) {
                return new QuestionsData(in);
            }

            @Override
            public QuestionsData[] newArray(int size) {
                return new QuestionsData[size];
            }
        };
    }


    private QuestionsModel(Parcel in) {
        error = in.readInt();
        if (in.readByte() == 0x01) {
            questions = new ArrayList<QuestionsData>();
            in.readList(questions, QuestionsData.class.getClassLoader());
        } else {
            questions = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(error);
        if (questions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(questions);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuestionsModel> CREATOR = new Parcelable.Creator<QuestionsModel>() {
        @Override
        public QuestionsModel createFromParcel(Parcel in) {
            return new QuestionsModel(in);
        }

        @Override
        public QuestionsModel[] newArray(int size) {
            return new QuestionsModel[size];
        }
    };
}