package sowisz.com.memorygame;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

public class GameImageView {
  private final static int MARK = R.drawable.background;
  private ImageView imageView;
  private String urlImage;
  private int id;
  private static boolean click = false;
  private static GameImageView miv;
  private static boolean onClickProgress = true;

  public GameImageView(ImageView imageView, String urlImage, int id) {
    this.imageView = imageView;
    this.urlImage = urlImage;
    this.id = id;
  }

  public boolean getOnClickProgress() {
    return onClickProgress;
  }

  public void setOnClickProgress(boolean onClickProgress) {
    GameImageView.onClickProgress = onClickProgress;
  }

  public void setInvisible() {
    imageView.setVisibility(View.INVISIBLE);
  }

  public GameImageView getMiv() {
    return miv;
  }

  public void setMiv(GameImageView miv) {
    GameImageView.miv = miv;
  }

  public void setOnImage() {
    this.imageView.setImageURI(Uri.parse(urlImage));
  }

  public void setOnMark() {
    this.imageView.setImageResource(MARK);
  }

  public int getId() {
    return this.id;
  }

  public boolean getClick() {
    return click;
  }

  public void setClick(boolean b) {
    click = b;
  }
}
