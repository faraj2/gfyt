package com.f.schoolsintouchteachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;


/**
 * Created by admin on 2/17/2018.
 */

public class MyFfmeg {
//    FFmpeg ffmpeg;
//    Context context;
//
//    public MyFfmeg(Context context) {
//        this.context = context;
//    }
//
//    public void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                Log.d("FFMPEG", "ffmpeg : null");
//                ffmpeg = FFmpeg.getInstance(context);
//            }
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onFailure() {
//                    showUnsupportedExceptionDialog();
//                    Log.d("FFMPEG",  " not supported :");
//                }
//
//                @Override
//                public void onSuccess() {
//                    Log.d("FFMPEG", "ffmpeg : correct Loaded");
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            Log.d("FFMPEG",  "EXception not supported :" + e);
//        }
//    }
//
//    public void showUnsupportedExceptionDialog() {
//        AlertDialog.Builder builder=new AlertDialog.Builder(context);
//        builder.setMessage("Unsupported ffmpeg");
//        AlertDialog alertDialog=builder.create();
//        alertDialog.show();
//    }
//
//    public void execFFmpegBinary(final String[] command) {
//        final ProgressDialog progressDialog=new ProgressDialog(context);
//       // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.show();
//        try {
//            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onFailure(String s) {
//                    progressDialog.dismiss();
//                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//                    Log.d("FFMPEG", "FAILED with output : " + s);
//                }
//
//                @Override
//                public void onSuccess(String s) {
//                    progressDialog.dismiss();
//                    Toast.makeText(context, "Success"+s, Toast.LENGTH_SHORT).show();
//                    Log.d("FFMPEG", "SUCCESS with output : " + s);
////Perform action on success
//                }
//
//
//            @Override
//            public void onProgress(String s) {
//                    progressDialog.setMessage("Progress"+s);
//                Log.d("FFMPEG", "progress : " + s);
//            }
//
//            @Override
//            public void onStart() {
//                Toast.makeText(context, "Started", Toast.LENGTH_SHORT).show();
//                Log.d("FFMPEG", "Started command : ffmpeg " + command);
//            }
//
//            @Override
//            public void onFinish() {
//                   // progressDialog.dismiss();
//                Toast.makeText(context, "OnFinished", Toast.LENGTH_SHORT).show();
//                Log.d("FFMPEG", "Finished command : ffmpeg " + command);
//
//            }
//        });
//    } catch (FFmpegCommandAlreadyRunningException e) {
//e.printStackTrace();
//    }
//}

}
