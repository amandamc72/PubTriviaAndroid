package com.pubtrivia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pubtrivia.R.array.category_values;

public class SettingsActivity extends AppCompatActivity {

    private Spinner mQuestionNumberSpinner;
    private Spinner mDifficultySpinner;
    private Spinner mCategoriesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mQuestionNumberSpinner = (Spinner) findViewById(R.id.questionCount);
        mCategoriesSpinner = (Spinner) findViewById(R.id.categorySpinner);
        mDifficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);
        ImageButton goButton = (ImageButton) findViewById(R.id.beginButton);

        ArrayAdapter<CharSequence> questionNumbersAdapter = ArrayAdapter.createFromResource(this,
                R.array.num_questions_array, R.layout.spinner_text);
        questionNumbersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mQuestionNumberSpinner.setAdapter(questionNumbersAdapter);

        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, R.layout.spinner_text);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategoriesSpinner.setAdapter(categoriesAdapter);

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, R.layout.spinner_text);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDifficultySpinner.setAdapter(difficultyAdapter);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numQuestions = mQuestionNumberSpinner.getSelectedItem().toString();
                int categoryPos = mCategoriesSpinner.getSelectedItemPosition();
                String[] categoryValueArray = getResources().getStringArray(category_values);
                String categoryValue = categoryValueArray[categoryPos];
                String selectedDifficulty = mDifficultySpinner.getSelectedItem().toString().toLowerCase();

                if (selectedInputs(numQuestions, categoryValue, selectedDifficulty)) {
                    Map<String, String> data = new HashMap<>();
                    data.put("type", "multiple");
                    data.put("amount", numQuestions);

                    if(!categoryValue.equals("1"))
                        data.put("category", categoryValue);
                    if (!selectedDifficulty.equals("any"))
                        data.put("difficulty", selectedDifficulty);

                    if (isNetworkAvailable()) {
                        getQuestions(data);
                    } else {
                        AppUtils.showPopup(SettingsActivity.this, getString(R.string.no_network));
                    }
                } else {
                    AppUtils.showPopup(SettingsActivity.this, getString(R.string.select_all));
                }
            }
        });

    }

    public boolean selectedInputs(String num, String cat, String diff) {
        return (!num.equals("Select Question number") &&
                !cat.equals("0") &&
                !diff.equals("Select Difficulty"));
    }

    public void getQuestions(Map<String, String> data) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<QuestionsModel> call = apiService.getQuestions(data);

        call.enqueue(new Callback<QuestionsModel>() {
            @Override
            public void onResponse(Call<QuestionsModel> call, Response<QuestionsModel> response) {
                QuestionsModel res = response.body();
                Intent intent = new Intent(SettingsActivity.this, GameActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("questions", res);
                intent.putExtras(extras);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
