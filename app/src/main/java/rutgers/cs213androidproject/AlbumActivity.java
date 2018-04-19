package rutgers.cs213androidproject;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import model.Album;
import model.AlbumImageAdapter;
import model.CustomSpinner;
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
    public CustomSpinner customSpinner;
    public AlbumImageAdapter albumImageAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);


        gridView = (GridView) findViewById(R.id.gridview_album);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_album);
        albumImageAdapter = new AlbumImageAdapter(AlbumActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);

        customSpinner = (CustomSpinner) findViewById(R.id.spinner_album);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.photo_functions,android.R.layout.simple_spinner_dropdown_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(spinnerAdapter);
        customSpinner.setVisibility(View.INVISIBLE);

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


        //ON LONG PRESS SHOW OPTIONS
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                customSpinner.performClick();

                customSpinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i == 0){
                            MainActivity.session.getCurrentAlbum().deletePhoto(pos);
                            try {
                                User.save(MainActivity.session);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            update();
                            albumImageAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Photo Deleted", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                return true;
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
            MainActivity.session.getCurrentAlbum().addPhoto(photopath);

            try {
                User.save(MainActivity.session);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gridView = (GridView) findViewById(R.id.gridview_album);
            update();
            albumImageAdapter.notifyDataSetChanged();
            gridView.setAdapter(albumImageAdapter);

        }
    }

    public void update(){
        photoList.clear();
        photoList.addAll(MainActivity.session.getCurrentAlbum().getPhotos());
//        for(int i = 0; i < MainActivity.session.getCurrentAlbum().getPhotos().size(); i++){
//            photoList.add(MainActivity.session.getCurrentAlbum().getPhotos().get(i));
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
