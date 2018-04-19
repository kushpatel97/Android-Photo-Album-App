package rutgers.cs213androidproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import model.CustomSpinner;
import model.Tag;
import model.User;

public class SingleImageActivity extends AppCompatActivity {

    public ImageView imageView;
    public FloatingActionButton floatingActionButton;
    public ListView listView;
    public CustomSpinner customSpinner;

    public String[] tagOptions = {"Person", "Location"};

    public ArrayList<String> taglist = new ArrayList<>();
    public ArrayAdapter<String> tagAdapter;

    /*
    Possible image slideshow
    https://www.youtube.com/watch?v=nL0k2usU7I8
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        //Load content
        update();
        //End Load content
        imageView = (ImageView) findViewById(R.id.singleImageView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_tag);
        listView = (ListView) findViewById(R.id.taglistview);
        customSpinner = (CustomSpinner) findViewById(R.id.spinner_tag);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(SingleImageActivity.this, android.R.layout.simple_spinner_dropdown_item, tagOptions);

        tagAdapter = new ArrayAdapter<>(this, R.layout.album_name_text, taglist);
        listView.setAdapter(tagAdapter);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(spinnerAdapter);
        customSpinner.setVisibility(View.INVISIBLE);
        openImage();


        // Add tag to a photo
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customSpinner.performClick();
                Toast.makeText(SingleImageActivity.this, tagOptions[0] + "afdasdfas", Toast.LENGTH_SHORT);
                customSpinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        // IF PERSON TAG IS SELECTED
                        if(i == 0){
//                            Toast.makeText(getApplicationContext(), "afdasdfas", Toast.LENGTH_SHORT);
                            System.out.println(customSpinner.getSelectedItem().toString());

                            AlertDialog.Builder alert = new AlertDialog.Builder(SingleImageActivity.this);
                            alert.setTitle("Person Tag");
                            alert.setMessage("Please Enter a value for this tag");

                            final EditText input = new EditText(SingleImageActivity.this);
                            alert.setView(input);


                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String renamed = input.getText().toString();
//                                    if(session.albumExists(renamed)){
//                                        Context context = getApplicationContext();
//                                        CharSequence text = "Album already exists. Try another name.";
//                                        int duration = Toast.LENGTH_SHORT;
//                                        Toast.makeText(context, text, duration).show();
//                                        return;
//                                    }
                                    if(renamed.isEmpty()){
                                        Context context = getApplicationContext();
                                        CharSequence text = "Field cannot be blank";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, text, duration).show();
                                        return;
                                    }
//                                    Tag tag = new Tag(customSpinner.getSelectedItem().toString(), renamed);
                                    MainActivity.session.getCurrentAlbum().getCurrentPhoto().addTag(customSpinner.getSelectedItem().toString(),renamed);

                                    try {
                                        User.save(MainActivity.session);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    update();
                                    tagAdapter.notifyDataSetChanged();
                                }
                            });


                            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();
                        }
                        // IF LOCATION TAG IS SELECTED
                        else if (i == 1){
//                            Toast.makeText(getApplicationContext(), "afdasdfas", Toast.LENGTH_SHORT);
                            System.out.println(customSpinner.getSelectedItem().toString());

                            AlertDialog.Builder alert = new AlertDialog.Builder(SingleImageActivity.this);
                            alert.setTitle("Location Tag");
                            alert.setMessage("Please Enter a value for this tag");

                            final EditText input = new EditText(SingleImageActivity.this);
                            alert.setView(input);


                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String renamed = input.getText().toString();
//                                    if(session.albumExists(renamed)){
//                                        Context context = getApplicationContext();
//                                        CharSequence text = "Album already exists. Try another name.";
//                                        int duration = Toast.LENGTH_SHORT;
//                                        Toast.makeText(context, text, duration).show();
//                                        return;
//                                    }
                                    if(renamed.isEmpty()){
                                        Context context = getApplicationContext();
                                        CharSequence text = "Field cannot be blank";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, text, duration).show();
                                        return;
                                    }
//                                    Tag tag = new Tag(customSpinner.getSelectedItem().toString(), renamed);
                                    MainActivity.session.getCurrentAlbum().getCurrentPhoto().addTag(customSpinner.getSelectedItem().toString(),renamed);

                                    try {
                                        User.save(MainActivity.session);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    update();
                                    tagAdapter.notifyDataSetChanged();
                                }
                            });


                            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });



            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                System.out.println(adapterView.getSelectedItemPosition());
                return true;
            }
        });






    }

    private void openImage() {

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

    private void update(){
        taglist.clear();
        for(int i = 0; i < MainActivity.session.getCurrentAlbum().getCurrentPhoto().getTaglist().size(); i++){
            taglist.add(MainActivity.session.getCurrentAlbum().getCurrentPhoto().getTaglist().get(i).key + " | " + MainActivity.session.getCurrentAlbum().getCurrentPhoto().getTaglist().get(i).value);
        }
    }
}
