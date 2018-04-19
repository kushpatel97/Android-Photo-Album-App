package rutgers.cs213androidproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.CustomSpinner;
import model.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public final Context pub_context = this;

    public FloatingActionButton fab;

    public ArrayList<String> albumnames = new ArrayList<String>();
    public ArrayAdapter adapter;
    public ListView listview;
    public CustomSpinner spinner;
    public File filename = new File("/data/data/rutgers.cs213androidproject/files/data.dat");

    public static User session = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            session = User.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!filename.exists()){
            Context context = this;
            File file = new File(context.getFilesDir(), "data.dat");
            try {
                file.createNewFile();
            }
            catch (IOException e){

            }
        }
        update();

        // Link XML values to controller
        listview = (ListView) findViewById(R.id.listview);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        //Spinner Stuff
        spinner = (CustomSpinner) findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.album_functions,android.R.layout.simple_spinner_dropdown_item);


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setVisibility(View.INVISIBLE);

        //Listview Stuff
        adapter = new ArrayAdapter(this, R.layout.album_name_text, albumnames);
        listview.setAdapter(adapter);

        //Button functions
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAlbum(v);
            }
        });


        //============= On item long press
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long id) {
                Toast.makeText(getApplicationContext(),"long clicked, "+"pos: " + pos, Toast.LENGTH_SHORT).show();
                spinner.performClick();


                spinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i == 0) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("Rename");
                            alert.setMessage("Rename me");

                            final EditText input = new EditText(MainActivity.this);
                            alert.setView(input);


                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String renamed = input.getText().toString();
                                    if(session.albumExists(renamed)){
                                        Context context = getApplicationContext();
                                        CharSequence text = "Album already exists. Try another name.";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, text, duration).show();
                                        return;
                                    }
                                    if(renamed.isEmpty()){
                                        Context context = getApplicationContext();
                                        CharSequence text = "Field cannot be blank";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, text, duration).show();
                                        return;
                                    }
                                    session.getAlbums().get(pos).setAlbumName(renamed);

                                    try {
                                        User.save(session);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    update();
                                    adapter.notifyDataSetChanged();
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
                        else if (i == 1){
                            session.deleteAlbum(pos);
                            try {
                                User.save(session);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            update();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Album Deleted", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                return true;
            }
        });

        //When Clicked should redirect you to another activity with that albums photos
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Album currentAlbum = session.getAlbums().get(i);
                session.setCurrentAlbum(currentAlbum);
                Intent goToCurrentAlbum = new Intent(MainActivity.this, AlbumActivity.class);
                startActivity(goToCurrentAlbum);
                Toast.makeText(getApplicationContext(), adapterView.getItemAtPosition(i) + " Page", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_icon) {
            Intent goToSearch = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(goToSearch);
            Toast.makeText(getApplicationContext(), "Onto Search", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();

        try {
            User.save(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            session = User.load();
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            session = User.load();
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addAlbum(View view){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enter the name of the album: ");

        final EditText input = new EditText(this);
//        final Context context = getApplicationContext();
//        final int duration = Toast.LENGTH_SHORT;
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumname = input.getText().toString().trim();
                if(albumname.isEmpty() || albumname == null){
                    Context context = getApplicationContext();
                    CharSequence text = "Field cannot be blank";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                    return;
                }

                if(session.albumExists(albumname)){
                    Context context = getApplicationContext();
                    CharSequence text = "Album already exists!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                    return;
                }

                try {
                    User.save(session);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Album album = new Album(albumname);
                session.addAlbum(album);

                try {
                    User.save(session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                update();
                adapter.notifyDataSetChanged();
//                etInput.setText("");
            }
        });
        alertDialogBuilder.show();



    }

    public void update(){
        albumnames.clear();
        for(int i=0; i <session.getAlbums().size(); i++){
            albumnames.add(session.getAlbums().get(i).getAlbumName());
        }
//        adapter.notifyDataSetChanged();
    }
}
