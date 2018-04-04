package sowisz.com.memorygame;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectActivity extends AppCompatActivity {

    private static final int PICK_PHOTO_CODE = 100;
    private static final int REQUEST_TAKE_PHOTO = 1;

    public static MyDbHelper myDbHelper;
    private String mCurrentPhotoPath;
    private Button capturePicture;
    private Button useOldPicturesButton;
    private Button startGame;
    private GridView gridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        setUpMyDbHelper();
        setUpGridView();
        setUpButton();
    }

    private void setUpMyDbHelper(){
      myDbHelper = new MyDbHelper(this);
      myDbHelper.deleteData();
    }
    private void setUpGridView(){
      imageAdapter = new ImageAdapter(this);
      gridView = findViewById(R.id.gridview);
      gridView.setAdapter(imageAdapter);
    }

    private void setUpButton(){
      capturePicture = findViewById(R.id.button);
      useOldPicturesButton = findViewById(R.id.button2);
      startGame = findViewById(R.id.startGame);
      capturePictureOnClick();
      useOldPicturesSetOnClick();
      startGameOnClick();
    }

  private void capturePictureOnClick() {
      capturePicture.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          try{
            dispatchTakePictureIntent();
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
      });
  }

  private void dispatchTakePictureIntent()
  {
      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      // Ensure that there's a camera activity to handle the intent
      if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        // Create the File where the photo should go
        File photoFile = createImageFileException();
        // Continue only if the File was successfully created
        checkIfValidFileAndAddItToDatabase(photoFile, takePictureIntent);
      }
  }

  private File createImageFileException(){
      try{
        return createImageFile();
      }
      catch(IOException e){
        Log.e("Error: ", e.getMessage());
        return null;
      }
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp =
        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );
    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private void checkIfValidFileAndAddItToDatabase(File photoFile, Intent takePictureIntent){
    if (photoFile != null) {
      mCurrentPhotoPath = photoFile.getAbsolutePath();
      Uri photoURI = FileProvider.getUriForFile(this,
          "com.sowisz.memory",
          photoFile);
      takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
      startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }
  }

  private void useOldPicturesSetOnClick(){
      useOldPicturesButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          openGallery();
        }
      });
    }

    private void openGallery(){
      Intent gallery = new Intent(Intent.ACTION_PICK, Media.INTERNAL_CONTENT_URI);
      startActivityForResult(gallery, PICK_PHOTO_CODE);
    }

  private void startGameOnClick() {
      startGame.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if(myDbHelper.getData().size() >= 4){
            Intent intent = new Intent(v.getContext(), GameActivity.class);
            startActivity(intent);
          }else{
            Toast.makeText(SelectActivity.this,"Number of pictures must be at least 4", Toast.LENGTH_LONG).show();
          }
        }
      });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_PHOTO_CODE) {
          myDbHelper.insertData(data.getData().toString());
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
          galleryAddPic();
        }
  }

  private void galleryAddPic() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(mCurrentPhotoPath);
    Uri contentUri = Uri.fromFile(f);
    myDbHelper.insertData(contentUri.toString());
    mediaScanIntent.setData(contentUri);
    this.sendBroadcast(mediaScanIntent);
  }
}
