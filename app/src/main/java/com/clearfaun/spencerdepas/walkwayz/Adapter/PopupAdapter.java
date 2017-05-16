package com.clearfaun.spencerdepas.walkwayz.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clearfaun.spencerdepas.walkwayz.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by SpencerDepas on 5/15/17.
 */

public class PopupAdapter implements GoogleMap.InfoWindowAdapter {

    private View popup=null;
    private LayoutInflater inflater=null;
    private static int starCounter = 0;

    public PopupAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.map_marker_popup, null);
        }

        ImageView icon=(ImageView)popup.findViewById(R.id.icon);
        TextView tv=(TextView)popup.findViewById(R.id.title);
        TextView snippet =(TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());


        if(marker.getTitle().equals("Agressive indiduel") ||  marker.getTitle().equals("Recent mugging") ){
            icon.setVisibility(View.VISIBLE);
        }else{
            icon.setVisibility(View.INVISIBLE);
        }


        return(popup);
    }
}