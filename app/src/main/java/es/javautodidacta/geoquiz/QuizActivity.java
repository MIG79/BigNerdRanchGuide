package es.javautodidacta.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toastTrue = Toast.makeText(QuizActivity.this, R.string.incorrect, Toast.LENGTH_SHORT);
                toastTrue.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                toastTrue.show();
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toastFalse = Toast.makeText(QuizActivity.this, R.string.correct, Toast.LENGTH_SHORT);
                toastFalse.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                toastFalse.show();
            }
        });
    }
}
