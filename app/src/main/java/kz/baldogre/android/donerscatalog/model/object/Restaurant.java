package kz.baldogre.android.donerscatalog.model.object;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by lol on 08.03.2018.
 */

public class Restaurant {
    String name;
    String info;
    private LatLng latLng;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
