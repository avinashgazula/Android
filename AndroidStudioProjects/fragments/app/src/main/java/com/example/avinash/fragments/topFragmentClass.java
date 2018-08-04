package com.example.avinash.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

public class topFragmentClass extends Fragment{

    public static EditText topText;
    public static EditText bottomText;

    topActivityListener topActivity;

    public interface topActivityListener{
        void createMeme(String top, String bottom);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            topActivity = (topActivityListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_fragment, container, false);

        topText = (EditText) view.findViewById(R.id.topText);
        bottomText = (EditText) view.findViewById(R.id.bottomText);

        final Button button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        topActivity.createMeme(topText.getText().toString(), bottomText.getText().toString());
                    }
                }
        );

        return view;
    }
}
