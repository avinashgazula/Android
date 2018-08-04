package com.example.avinash.listview;

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

    public bandAdapter(Context context, String[] bands) {
        super(context, R.layout.band_list, bands);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater bandInflater = LayoutInflater.from(getContext());
        View bandView = bandInflater.inflate(R.layout.band_list, parent, false);

        bandText = (TextView) bandView.findViewById(R.id.bandText);
        bandImage = (ImageView) bandView.findViewById(R.id.bandImage);
        String selectedBand = getItem(position);

        bandText.setText(selectedBand);
        if (selectedBand.equals("fob"))
            bandImage.setImageResource(R.drawable.fob);
        else if (selectedBand.equals("top"))
            bandImage.setImageResource(R.drawable.top);
        else if (selectedBand.equals("mcr"))
            bandImage.setImageResource(R.drawable.mcr);
        else if (selectedBand.equals("panic!"))
            bandImage.setImageResource(R.drawable.panic);
        else if (selectedBand.equals("bmth"))
            bandImage.setImageResource(R.drawable.bmth);
        else
            bandImage.setImageResource(R.drawable.emo);


        return bandView;
    }
}
