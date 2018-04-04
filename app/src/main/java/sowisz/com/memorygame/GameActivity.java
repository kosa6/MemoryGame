package sowisz.com.memorygame;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

  private final int TIME = 500;

  private final MyDbHelper myDbHelper = SelectActivity.myDbHelper;

  private ImageView fullScreanPicture;
  private TableLayout gameBoard;
  private TextView scoreView;
  private int score;
  private int numberOfRows;
  private Map<String,Integer> indexOfImage;
  private List<String> randomImages;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);

    setUpindexsOfImages();
    setUpVariable();
    fillUpRandomImages();
    generateBoard();
  }

  private void setUpindexsOfImages()
  {
    indexOfImage = new HashMap<>();
    for(int i = 0; i < myDbHelper.getData().size(); i++)
    {
      indexOfImage.put(myDbHelper.getData().get(i).toString(), i);
    }
  }

  private void setUpVariable(){
    fullScreanPicture = findViewById(R.id.fullScreanPicture);
    gameBoard = findViewById(R.id.gameBoard);
    scoreView = findViewById(R.id.Score);
    fullScreanPictureOnClick();
  }

  private void fullScreanPictureOnClick(){
    fullScreanPicture.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        fullScreanPicture.setVisibility(View.GONE);
        gameBoard.setVisibility(View.VISIBLE);
      }
    });
  }

  private void fillUpRandomImages(){
    randomImages = new ArrayList<>();
    for(int i=0; i<numberOfPicture(); i++){
      randomImages.add(myDbHelper.getData().get(i).toString());
      randomImages.add(myDbHelper.getData().get(i).toString());
    }
  }

  private int numberOfPicture(){
    if(myDbHelper.getData().size()>10){
      return 10;
    }
    else if(myDbHelper.getData().size()%2 == 0){
      return myDbHelper.getData().size();
    }
    else{
      return myDbHelper.getData().size() - 1;
    }
  }

  private void generateBoard(){
    Random random = new Random();
    for(int i=0; i<numberOfPicture()/2; i++){
      TableRow tableRow = createTableRow(gameBoard);
      for(int j = 0; j < 4; j++)
      {
        int temp = random.nextInt(randomImages.size());
        createImageView(randomImages.get(temp), tableRow);
        randomImages.remove(temp);
      }
    }
  }

  private TableRow createTableRow(TableLayout myLayout)
  {
    TableRow newRow = new TableRow(this);
    TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.MATCH_PARENT);
    lp.weight = 1;
    myLayout.addView(newRow, lp);
    return newRow;
  }

  private ImageView createImageView(String uriPath, TableRow tableRow)
  {
    ImageView imageView = new ImageView(this);
    imageView.setImageResource(R.drawable.background);
    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
        TableRow.LayoutParams.WRAP_CONTENT);
    lp.setMargins(8,8,8,8);
    lp.weight = 1;
    playInMemory(imageView, uriPath);
    tableRow.addView(imageView, lp);
    return imageView;
  }

  private void playInMemory(ImageView imageView, final String uriPath)
  {
    final GameImageView miv = new GameImageView(imageView, uriPath, indexOfImage.get(uriPath));
    miv.setOnClickProgress(true);
    miv.setClick(false);
    miv.setMiv(null);
    imageView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        gameProgress(miv, uriPath);
      }
    });
  }

  private void gameProgress(final GameImageView miv, String uriPath)
  {
    if(miv.getOnClickProgress())
    {
      clickOnPicture(miv, uriPath);
    }
  }

  private void clickOnPicture(final GameImageView miv, String uriPath)
  {
    if(miv.getClick())
    {
      miv.setOnClickProgress(false);
      comparePicture(miv, uriPath);
    }
    else
    {
      miv.setClick(true);
      miv.setMiv(miv);
      miv.setOnImage();
    }
  }

  private void comparePicture(final GameImageView miv, String uriPath)
  {
    if(miv == miv.getMiv())
    {
      fullScreanPicture.setImageURI(Uri.parse(uriPath));
      gameBoard.setVisibility(View.GONE);
      fullScreanPicture.setVisibility(View.VISIBLE);
      miv.setOnClickProgress(true);
    }
    else
    {
      ratePlayer(miv);
    }
  }

  private void ratePlayer(final GameImageView miv)
  {
    if(miv.getId() == miv.getMiv().getId())
    {
      miv.setOnImage();
      Runnable mMyRunnable = new Runnable()
      {
        @Override
        public void run()
        {
          score += 2;
          scoreView.setText("Score: " + Integer.toString(score));
          miv.setInvisible();
          miv.getMiv().setInvisible();
          miv.setClick(false);
          miv.setOnClickProgress(true);
        }
      };
      Handler myHandler = new Handler();
      myHandler.postDelayed(mMyRunnable, TIME);

    }
    else
    {
      miv.setOnImage();
      Runnable mMyRunnable = new Runnable()
      {
        @Override
        public void run()
        {
          score--;
          scoreView.setText("Score: " + Integer.toString(score));
          miv.setOnMark();
          miv.getMiv().setOnMark();
          miv.setClick(false);
          miv.setOnClickProgress(true);
        }
      };
      Handler myHandler = new Handler();
      myHandler.postDelayed(mMyRunnable, TIME);
    }
  }


}
