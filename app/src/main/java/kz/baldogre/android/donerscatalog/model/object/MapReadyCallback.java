package kz.baldogre.android.donerscatalog.model.object;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import kz.baldogre.android.donerscatalog.presentation.presenter.MainActivityPresenter;

/**
 * Created by lol on 08.03.2018.
 */

public class MapReadyCallback implements OnMapReadyCallback {
    Context context;
    MainActivityPresenter presenter;
    GoogleMap mGoogleMap;

    public MapReadyCallback(Context context, MainActivityPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                TextView textView = new TextView(context);
                textView.setText(marker.getTitle());
                return textView;
            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                presenter.onMarkerPressed(marker.getSnippet());
                return false;
            }
        });

        presenter.mapReady(mGoogleMap);
    }

}
