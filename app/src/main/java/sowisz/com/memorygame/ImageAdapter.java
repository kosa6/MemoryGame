package sowisz.com.memorygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import java.io.IOException;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

  private Context mContext;

  public ImageAdapter(Context c) {
    mContext = c;
  }

  public int getCount() {
    return SelectActivity.myDbHelper.getData().size();
  }

  public Object getItem(int position) {
    return SelectActivity.myDbHelper.getData().get(position);
  }

  public long getItemId(int position) {
    return 0;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;

    if (convertView == null) {
      imageView = new ImageView(mContext);
      imageView.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
      imageView.setScaleType(ScaleType.FIT_XY);
      imageView.setAdjustViewBounds(true);
      imageView.setPadding(8, 8, 8, 8);
    } else {
      imageView = (ImageView) convertView;
    }

    Uri imageUri = Uri.parse(SelectActivity.myDbHelper.getData().get(position).toString());
    imageView.setImageBitmap(Bitmap.createScaledBitmap(uriToBitmap(imageUri), 300, 300, false));
    return imageView;
  }

  private Bitmap uriToBitmap(Uri uri){
    try{
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
      return bitmap;
    }catch (IOException e){
      e.printStackTrace();
      return null;
    }
  }
}
