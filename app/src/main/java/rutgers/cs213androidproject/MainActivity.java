package rutgers.cs213androidproject;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public EditText etInput;
    public FloatingActionButton fab;
    public ArrayList<String> albumnames;
    public ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView) findViewById(R.id.listview);
        etInput = (EditText) findViewById(R.id.input);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        albumnames = new ArrayList<String>();
        for(int i = 0; i<10; i++){
            albumnames.add("Test: " + i);
        }

        adapter = new ArrayAdapter(this, R.layout.album_name_text, albumnames);
        listview.setAdapter(adapter);




        String input = etInput.getText().toString().trim();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etInput.getText().toString().trim();
                albumnames.add(input);
                adapter.notifyDataSetChanged();
            }
        });

//        listview.setAdapter(adapter);


    }
}