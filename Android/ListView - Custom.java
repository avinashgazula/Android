//create a new adapter(class) here bandAdapter

//here the custom list row has a row and an image
//band_list.xml is the layout resource file for the custom row

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

public class bandAdapter extends ArrayAdapter<String>{

    TextView bandText;
    ImageView bandImage;

	//constructor is used to call the adapter which helps in using the custom list row
    public bandAdapter(Context context, String[] bands) {
//---------->>>>> band_list is the name of the xml file which has the custom row
        super(context, R.layout.band_list, bands);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater bandInflater = LayoutInflater.from(getContext());
        View bandView = bandInflater.inflate(R.layout.band_list, parent, false);

        bandText = (TextView) bandView.findViewById(R.id.bandText);
        bandImage = (ImageView) bandView.findViewById(R.id.bandImage);
		
		//getItem(position) gets the name rom the string array bands at the position position
        String selectedBand = getItem(position);

        bandText.setText(selectedBand);
		//emo is an image in the drawable folder
        bandImage.setImageResource(R.drawable.emo);


        return bandView;
    }
}