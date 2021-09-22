package com.tnrlab.travelassistant.loader;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.tnrlab.travelassistant.R;


public class Loader {

    Activity activity;
    Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public Loader(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loading_layout);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */


        //...now load that gif which we put inside the drawable folder here with the help of Glide


        DrawableImageViewTarget drawableImageViewTarget = new DrawableImageViewTarget(gifImageView);


        Glide.with(activity)
                .load(R.raw.splash_animation)
                .into(drawableImageViewTarget);

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
