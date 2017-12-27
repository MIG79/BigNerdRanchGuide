package es.javautodidacta.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.canberra_is_the_capital_of_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private int mRightAnswers = 0;
    private boolean mIsCheater;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d(TAG, "onCreate(Bundle) called");

        mQuestionTextView = findViewById(R.id.question_text_view);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this,
                        answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswers(true);
                hideButtons();
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswers(false);
                hideButtons();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    /**
     * Saves info shown on screen to retrieve it when the activity is destroyed when
     * the device is turned side up.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void hideButtons() {
        mFalseButton.setVisibility(View.INVISIBLE);
        mTrueButton.setVisibility(View.INVISIBLE);

        mCurrentIndex++;
        updateQuestion();
    }

    private void showButtons() {
        mFalseButton.setVisibility(View.VISIBLE);
        mTrueButton.setVisibility(View.VISIBLE);
    }

    /**
     * Updates question shown adding 1 to current index.
     */
    private void updateQuestion() {
        if(mCurrentIndex < mQuestionBank.length) {
            int question = mQuestionBank[mCurrentIndex].getTextResId();
            mQuestionTextView.setText(question);
            showButtons();
        } else {
            int porcentaje = (100 / mQuestionBank.length) * mRightAnswers;
            Toast.makeText(this, porcentaje + "% RIGHT", Toast.LENGTH_SHORT).show();
            mQuestionTextView.setText(porcentaje + "% RIGHT");
            mQuestionTextView.setTextColor(Color.BLUE);
        }
        mIsCheater = false;
    }

    /**
     * Checks whether answer is true or false.
     */
    private void checkAnswers(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if(mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct;
                mRightAnswers++;
            } else {
                messageResId = R.string.incorrect;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }
}
