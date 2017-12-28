package es.javautodidacta.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "es.javautodidacta.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "es.javautodidacta.geoquiz.answer_shown";
    private boolean mAnswerIsTrue;
    private static final String KEY_INDEX = "index";
    private static final String KEY_INDEX_INT = "index_int";
    private static final String KEY_ANSWER = "key_answer";
    private static final String HIDDEN_BUTTON = "hidden_button";

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mApiLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mApiLevel = findViewById(R.id.API_level);
        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);

        if(savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_INDEX, false);
            QuizActivity.tries = savedInstanceState.getInt(KEY_INDEX_INT, 0);

            if(QuizActivity.tries <= 3) {
                mApiLevel.setText("Tries: " + QuizActivity.tries);
            }
            
            String answer = savedInstanceState.getString(KEY_ANSWER, "");
            mAnswerTextView.setText(answer);
            boolean hiddenButton = savedInstanceState.getBoolean(HIDDEN_BUTTON, false);
            if(hiddenButton) mShowAnswerButton.setVisibility(View.INVISIBLE);

        } else {
            mAnswerIsTrue = getIntent()
                    .getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        }

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ++QuizActivity.tries;

                if(QuizActivity.tries <= 3) {
                    mApiLevel.setText("Tries: " + QuizActivity.tries);
                    if (mAnswerIsTrue) {
                        mAnswerTextView.setText(R.string.true_button);
                    } else {
                        mAnswerTextView.setText(R.string.false_button);
                    }
                    setAnswerShownResult(true);
                } else {
                    mAnswerTextView.setText(R.string.cheating_is_prohibited);
                }

                int cx = mShowAnswerButton.getWidth() / 2;
                int cy = mShowAnswerButton.getHeight() / 2;
                float radius = mShowAnswerButton.getWidth();
                Animator anim = ViewAnimationUtils
                        .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            }
        });
    }


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        if(QuizActivity.tries <= 3) {
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED, data);
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
        outState.putBoolean(KEY_INDEX, mAnswerIsTrue);
        outState.putInt(KEY_INDEX_INT, QuizActivity.tries);
        outState.putString(KEY_ANSWER, mAnswerTextView.getText().toString());
        outState.putBoolean(HIDDEN_BUTTON, mShowAnswerButton.getVisibility() == View.INVISIBLE);
    }
}
