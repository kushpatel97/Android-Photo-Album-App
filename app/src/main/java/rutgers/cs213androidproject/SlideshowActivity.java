package rutgers.cs213androidproject;

import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import model.Photo;

public class SlideshowActivity extends AppCompatActivity {


    public ImageButton previous, next;
    public ImageView imageView;
    public int currentindex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);


        previous = (ImageButton) findViewById(R.id.previous);
        next = (ImageButton) findViewById(R.id.next);
        imageView = (ImageView) findViewById(R.id.slideshowImage);

        int start = 0;
        final int end = MainActivity.session.getCurrentAlbum().getPhotos().size();
        int count = 0;
        currentindex = 0;

        openInitialImage();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentindex++;

                if(currentindex == end){
                    currentindex = 0;
                }

                System.out.println("=======================" + currentindex + "=======================");
                Photo photo = MainActivity.session.getCurrentAlbum().getPhotos().get(currentindex);
                Uri uri = Uri.parse(photo.getFilepath());
                imageView.setImageURI(uri);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentindex--;
                if(currentindex == -1){
                    currentindex = end-1;
                }
                System.out.println("=======================" + currentindex + "=======================");
                Photo photo = MainActivity.session.getCurrentAlbum().getPhotos().get(currentindex);
                Uri uri = Uri.parse(photo.getFilepath());
                imageView.setImageURI(uri);
            }
        });





    }

    public void openInitialImage(){
        Uri uri = Uri.parse(MainActivity.session.getCurrentAlbum().getPhotos().get(0).getFilepath());
        imageView.setImageURI(uri);
    }
}
