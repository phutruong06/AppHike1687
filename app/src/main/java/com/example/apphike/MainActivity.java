package com.example.apphike;


import android.Manifest;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraBounds;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapControllable;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;


public class MainActivity extends AppCompatActivity {

    MapView mapView;
    FloatingActionButton floatingActionButton;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission not Granted!", Toast.LENGTH_SHORT).show();
            }
        }
    });


    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener =  new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {

             mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };

    private  final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(20.0).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
        }
    };

    private  final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
             getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
             getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
             getGestures(mapView).removeOnMoveListener(onMoveListener);
             floatingActionButton.show();

        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        floatingActionButton = findViewById(R.id.forcuslocation);
        floatingActionButton.hide();

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        mapView.getMapboxMap().loadStyleUri(Style.SATELLITE, new Style.OnStyleLoaded() {
           @Override
           public void onStyleLoaded(@NonNull Style style) {
               mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());
               LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
               locationComponentPlugin.setEnabled(true);
               LocationPuck2D locationPuck2D = new LocationPuck2D();
               locationPuck2D.setBearingImage(AppCompatResources.getDrawable(MainActivity.this, R.drawable.baseline_place_24));
               locationComponentPlugin.setLocationPuck(locationPuck2D);
               locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
               locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
               getGestures(mapView).addOnMoveListener(onMoveListener);

               floatingActionButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                       locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                       getGestures(mapView).addOnMoveListener(onMoveListener);

                       floatingActionButton.hide();
                   }
               });
           }
       });
    }

}