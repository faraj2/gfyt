package com.f.schoolsintouchteachers.trails;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.f.schoolsintouchteachers.R;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TestRecord extends AppCompatActivity {

    private CountDownTimer countdon;
    private static final int RequestPermissionCode = 51;
    private String AudioSavePathInDevice;
    private MediaRecorder mediaRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_record);
        final ImageButton button=findViewById(R.id.send);
        EditText editText=findViewById(R.id.messagepost);
        final RelativeLayout relativeLayout=findViewById(R.id.reltv);
        final RecordView recordView = findViewById(R.id.record_view);
        final RecordButton recordButton = findViewById(R.id.record_button);


        //IMPORTANT
        recordButton.setRecordView(recordView);
        recordButton.setListenForRecord(true);
        recordView.setLessThanSecondAllowed(false);
        if(checkPermission()) {

            MediaRecorderReady();
        } else {
            requestPermission();
        }
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                relativeLayout.setVisibility(View.GONE);
               countdon= new CountDownTimer(300000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        Toast.makeText(TestRecord.this, String.valueOf(millisUntilFinished/1000), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);


                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                    Toast.makeText(TestRecord.this, "Recording started",
                            Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancel() {


countdon.cancel();
relativeLayout.setVisibility(View.VISIBLE);
mediaRecorder.reset();
MediaRecorderReady();
            }

            @Override
            public void onFinish(long recordTime) {

                countdon.cancel();

                    mediaRecorder.stop();
                   // mediaPlayer.release();
                    MediaRecorderReady();

              //  Toast.makeText(TestRecord.this, String.valueOf(System.currentTimeMillis()/1000), Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLessThanSecond() {
                countdon.cancel();
               // recordButton.setListenForRecord(false);
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });
        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
relativeLayout.setVisibility(View.GONE);
//recordButton.setListenForRecord(true);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recordButton.setClickable(false);
recordButton.setVisibility(View.INVISIBLE);
button.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
if (s.length()<=0){

    recordButton.setClickable(true);
    recordButton.setVisibility(View.VISIBLE);
    button.setVisibility(View.GONE);
}
            }
        });
    }

    private void MediaRecorderReady() {
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                        String.valueOf(System.currentTimeMillis()) + "AudioRecording.3gp";
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(TestRecord.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        MediaRecorderReady();
                        Toast.makeText(TestRecord.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TestRecord.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}
