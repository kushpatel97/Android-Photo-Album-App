package rutgers.cs213androidproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleImageActivity extends AppCompatActivity {

    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);

        imageView = (ImageView) findViewById(R.id.singleImageView);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle == null){
        }
        else{
            int index = intent.getIntExtra("index",-1);
            Uri uri = Uri.parse(MainActivity.session.getCurrentAlbum().getPhotos().get(index).getFilepath());
            imageView.setImageURI(uri);
        }

    }
}
