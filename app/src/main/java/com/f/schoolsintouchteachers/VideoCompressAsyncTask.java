package com.f.schoolsintouchteachers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

/**
 * Created by admin on 2/3/2018.
 */

class  VideoCompressAsyncTask extends AsyncTask<String, String, String> {

    Context mContext;
    private ProgressDialog mProgressDialog;

    public VideoCompressAsyncTask(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("waiting 1 minutes..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
//            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_photo_camera_white_48px));
//            compressionMsg.setVisibility(View.VISIBLE);
//            picDescription.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(String... paths) {
        String filePath = null;
        int progress=0;
//        try {
//
//        //    filePath = SiliCompressor.with(mContext).compressVideo(Uri.parse(paths[0]), paths[1]);
//
//            for (int i=0;i<60000;i+=1000){
//               progress=(i/60000)*100;
//                publishProgress("" + String.valueOf(progress));
//            }
//
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        return  filePath;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        mProgressDialog.setProgress(Integer.parseInt(values[0]));

    }

    @Override
    protected void onPostExecute(String compressedFilePath) {
        mProgressDialog.dismiss();
        super.onPostExecute(compressedFilePath);
        File imageFile = new File(compressedFilePath);
        float length = imageFile.length() / 1024f; // Size in KB
        String value;
        if(length >= 1024)
            value = length/1024f+" MB";
        else
            value = length+" KB";
        String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", mContext.getString(R.string.video_compression_complete), imageFile.getName(), value);
//            compressionMsg.setVisibility(View.GONE);
//            picDescription.setVisibility(View.VISIBLE);
//            picDescription.setText(text);
        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
        Log.i("Silicompressor", "Path: "+value);
    }

}
