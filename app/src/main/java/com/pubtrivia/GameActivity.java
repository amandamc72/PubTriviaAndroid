package com.pubtrivia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mQuestionText;
    private Button mButtonOne;
    private Button mButtonTwo;
    private Button mButtonThree;
    private Button mButtonFour;
    private Button mNext;
    private ArrayList<QuestionsModel.QuestionsData> mQuestions;
    private int mCorrectIndex;
    private int mCurrentQuestion = 0;
    private int mNumberCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mQuestionText = (TextView) findViewById(R.id.questionText);
        mButtonOne = (Button) findViewById(R.id.answerOneButton);
        mButtonTwo = (Button) findViewById(R.id.answerTwoButton);
        mButtonThree = (Button) findViewById(R.id.answerThreeButton);
        mButtonFour = (Button) findViewById(R.id.answerFourButton);
        mNext = (Button) findViewById(R.id.nextButton);

        mButtonOne.setOnClickListener(this);
        mButtonTwo.setOnClickListener(this);
        mButtonThree.setOnClickListener(this);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setQuestionView(mQuestions.get(mCurrentQuestion));
            }
        });

        QuestionsModel questionsModel = getQuestionsModel();

        mButtonFour.setOnClickListener(this);

        if (questionsModel != null && questionsModel.getError() == 0) {
            mQuestions = questionsModel.getQuestions();
            setQuestionView(mQuestions.get(mCurrentQuestion));
        } else {
            AppUtils.showPopup(GameActivity.this, getString(R.string.error_loading));
        }
    }

    public void setQuestionView(QuestionsModel.QuestionsData question) {
        mNext.setVisibility(View.INVISIBLE);
        mQuestionText.setText(Html.fromHtml(question.getQuestion()).toString());
        ArrayList<String> answers = question.getAllAnswers();
        mButtonOne.setText(Html.fromHtml(answers.get(0)).toString());
        mButtonTwo.setText(Html.fromHtml(answers.get(1)).toString());
        mButtonThree.setText(Html.fromHtml(answers.get(2)).toString());
        mButtonFour.setText(Html.fromHtml(answers.get(3)).toString());

        mButtonOne.setBackground(ContextCompat.getDrawable(this, R.drawable.white_rounded_corners));
        mButtonTwo.setBackground(ContextCompat.getDrawable(this, R.drawable.white_rounded_corners));
        mButtonThree.setBackground(ContextCompat.getDrawable(this, R.drawable.white_rounded_corners));
        mButtonFour.setBackground(ContextCompat.getDrawable(this, R.drawable.white_rounded_corners));

        mButtonOne.setClickable(true);
        mButtonTwo.setClickable(true);
        mButtonThree.setClickable(true);
        mButtonFour.setClickable(true);

        for (int i = 0; i < 4; i++) {
            if(answers.get(i).equals(question.getCorrect_answer())) {
                mCorrectIndex = i;
                break;
            }
        }
    }

    public QuestionsModel getQuestionsModel() {
        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            return extras.getParcelable("questions");
        }
        return null;
    }

    public void checkAnswer(Button pressedButton, String answer, String rightAnswer) {
        MediaPlayer sound;
        if (answer.equals(rightAnswer)) {
            sound = MediaPlayer.create(this, R.raw.correct);
            mNumberCorrect++;
            pressedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.green_rounded_corners));
        } else {
            sound = MediaPlayer.create(this, R.raw.incorrect);
            pressedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rounded_corners));
            getCorrectButton(mCorrectIndex)
                    .setBackground(ContextCompat.getDrawable(this, R.drawable.green_rounded_corners));
        }
        sound.start();
        sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mCurrentQuestion++;
        if(mCurrentQuestion == mQuestions.size()){
            endGame();
        } else {
            mButtonOne.setClickable(false);
            mButtonTwo.setClickable(false);
            mButtonThree.setClickable(false);
            mButtonFour.setClickable(false);
            mNext.setVisibility(View.VISIBLE);
        }
    }

    public void endGame() {
        Intent i = new Intent(this, ScoreActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("score", mNumberCorrect);
        extras.putInt("total", mQuestions.size());
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        Button pressedButton = (Button)v;
        String answer = pressedButton.getText().toString();
        String correct = mQuestions.get(mCurrentQuestion).getCorrect_answer();
        checkAnswer(pressedButton, answer, correct);
    }


    public Button getCorrectButton(int pos) {
        Button button;
        switch (pos) {
            case 0:
                button = mButtonOne;
                break;
            case 1:
                button = mButtonTwo;
                break;
            case 2:
                button = mButtonThree;
                break;
            case 3:
                button = mButtonFour;
                break;
            default:
                button = null;
                break;
        }
        return button;
    }
}
