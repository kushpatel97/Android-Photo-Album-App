package rutgers.cs213androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;

import model.AlbumImageAdapter;
import model.CustomSpinner;
import model.Photo;
import model.Tag;

public class SearchActivity extends AppCompatActivity {

    public ImageButton search;
    public EditText editText;
    public CustomSpinner customSpinner;
    public GridView gridView;

    public ArrayList<Photo> photoList = new ArrayList<>();
    public AlbumImageAdapter albumImageAdapter;

    public String[] spinneroptions = {"Person", "Location"};
    public int option;
    public ArrayList<Tag> tagArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        search = (ImageButton) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.tagName);
        customSpinner = (CustomSpinner) findViewById(R.id.tagspinner);
        gridView = (GridView) findViewById(R.id.gridview_search);

        albumImageAdapter = new AlbumImageAdapter(SearchActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(SearchActivity.this, R.array.tag_functions, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(spinnerAdapter);

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                option = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gridView = (GridView) findViewById(R.id.gridview_search);
        albumImageAdapter = new AlbumImageAdapter(SearchActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagArrayList.clear();
                String tagkey = spinneroptions[option];
                String tagvalue = editText.getText().toString();
                tagArrayList.add(new Tag(tagkey, tagvalue));
                photoList = MainActivity.session.searchTags(tagArrayList);

                gridView = (GridView) findViewById(R.id.gridview_search);
                albumImageAdapter.notifyDataSetChanged();
                gridView.setAdapter(albumImageAdapter);

            }
        });


    }
}
