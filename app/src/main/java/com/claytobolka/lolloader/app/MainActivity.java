package com.claytobolka.lolloader.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {

    private ImageView lolCatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lolCatView = (ImageView) findViewById(R.id.imageView);

        if (savedInstanceState != null)
        {
            lolCatView.setImageBitmap((Bitmap) savedInstanceState.getParcelable("catImage"));
        }
        else {
            new LoadLolCat(this, lolCatView).execute();
        }


        lolCatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadLolCat(v.getContext(), lolCatView).execute();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("catImage", ((BitmapDrawable) lolCatView.getDrawable()).getBitmap());
        super.onSaveInstanceState(outState);
    }

}
