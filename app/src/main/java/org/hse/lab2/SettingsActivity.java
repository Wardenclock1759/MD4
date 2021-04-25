package org.hse.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {

    private static final String CAMERA_PERMISSION = "android.permission.CAMERA";
    private static final int CAMERA_PERMISSION_REQUEST = 48;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText name;
    private ImageView photo;
    private org.hse.lab2.PreferenceManager preferenceManager;
    private SensorManager sensorManager;
    private Sensor light;
    private TextView sensorLight;
    private ListView sensorList;

    private String imageFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sensorLight = findViewById(R.id.lightText);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        preferenceManager = new PreferenceManager(this);

        View saveButton = findViewById(R.id.saveButton);
        View takePictureButton = findViewById(R.id.imageButton);
        sensorList = (ListView) findViewById(R.id.sensorList);
        name = findViewById(R.id.name);
        photo = findViewById(R.id.studentImage);

        initData();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                saveSettings();
            }
        });
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkPermissions();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.e("request", "Access requested");
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1){
            loadPhoto();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPermissions() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, CAMERA_PERMISSION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_PERMISSION)){

            }
            else{
                ActivityCompat.requestPermissions(this, new String[] {CAMERA_PERMISSION}, CAMERA_PERMISSION_REQUEST);
                Log.e("request", "Access requested");
            }
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e("PhotoTaken", "Create file", e);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID
                        + ".provider", photoFile);
                takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                try {
                    startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Log.e("ActivityStarted", "Start activity", e);
                }
            }
        }
    }

    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        sensorLight.setText(lux + " lux");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void saveSettings(){
        preferenceManager.saveName(name.getText().toString());
        preferenceManager.savePicture(imageFilePath);
    }

    private void initData(){
        name.setText(preferenceManager.getName());
        imageFilePath = preferenceManager.getPicture();
        loadPhoto();
        loadSensors();
    }

    private void loadPhoto(){
        if(imageFilePath != null){
            Glide.with(this).load(imageFilePath).into(photo);
        }
    }

    private void loadSensors(){
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        List<String> sensorsNames = new ArrayList<String>();
        for (Sensor currentSensor : sensors ) {
            sensorsNames.add(currentSensor.getName());
        }
        sensorList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sensorsNames));
    }
}