//ListView of just Text

//import statements
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


String[] bands = {"fob","mcr","top", "panic!", "bmth"};
//Adapter converts array to list items
ListAdapter bandsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bands);

bandsListView = (ListView) findViewById(R.id.bandsListView);
bandsListView.setAdapter(bandsAdapter);


//on Touching a list view item
bandsListView.setOnItemClickListener(
		new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//parent is item selected and each item in the list gets a position 0,1,2...
				String band = String.valueOf(parent.getItemAtPosition(position));
				Toast.makeText(MainActivity.this, band, Toast.LENGTH_SHORT).show();
			};
		}
);