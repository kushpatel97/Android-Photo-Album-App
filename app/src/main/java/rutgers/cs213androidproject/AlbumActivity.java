package rutgers.cs213androidproject;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

import model.Album;
import model.AlbumImageAdapter;
import model.Photo;
import model.Tag;
import model.User;

public class AlbumActivity extends AppCompatActivity {

    /*
    Links:
        Adding a photo
        https://www.youtube.com/watch?v=6beVvhCFiKA
     */

    public final int REQUEST_CODE = 0;
    public ArrayList<Photo> photoList = new ArrayList<>();
    public FloatingActionButton fab;
    public GridView gridView;
    public AlbumImageAdapter albumImageAdapter;
    public static User user = MainActivity.user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);


        gridView = (GridView) findViewById(R.id.gridview_album);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_album);
        albumImageAdapter = new AlbumImageAdapter(AlbumActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);


        update();

        // Add a photo
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                addPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                addPhoto.setType("image/*");
                startActivityForResult(addPhoto, REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            Uri uri = null;
            if(data != null){
                uri = data.getData();
            }
            String photopath = uri.toString();
            user.getCurrentAlbum().addPhoto(photopath);

            try {
                User.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }

            update();
            albumImageAdapter.notifyDataSetChanged();
            gridView.setAdapter(albumImageAdapter);

        }
    }

    public void update(){
        photoList.clear();
        for(int i = 0; i < user.getCurrentAlbum().getPhotos().size(); i++){
            photoList.add(user.getCurrentAlbum().getPhotos().get(i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
