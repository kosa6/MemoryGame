package sowisz.com.memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button playButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpButton();
    }

    private void setUpButton(){
        playButton = findViewById(R.id.playButton);
        exitButton = findViewById(R.id.exitButton);
        playButtonOnClick();
        exitButtonOnClick();
    }

    private void playButtonOnClick(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectActivity.class);
                startActivity(intent);
            }
        });
    }

    private void exitButtonOnClick(){
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
