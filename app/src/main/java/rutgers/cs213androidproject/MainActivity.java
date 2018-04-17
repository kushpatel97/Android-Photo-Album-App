package rutgers.cs213androidproject;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public EditText etInput;
    public FloatingActionButton fab;
    public ArrayList<String> albumnames;
    public ArrayAdapter adapter;
    public ListView listview;

    public User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
//        etInput = (EditText) findViewById(R.id.input);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        albumnames = new ArrayList<String>();
        adapter = new ArrayAdapter(this, R.layout.album_name_text, albumnames);
        listview.setAdapter(adapter);




//        String input = etInput.getText().toString().trim();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAlbum(v);
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();

        try {
            User.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            User.load();
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
            User.load();
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addAlbum(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

                if(user.albumExists(albumname)){
                    Context context = getApplicationContext();
                    CharSequence text = "Album already exists!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                    return;
                }


                user.addAlbum(albumname);
                albumnames.add(albumname);
                adapter.notifyDataSetChanged();
//                etInput.setText("");
            }
        });
        alertDialogBuilder.show();



    }
}
