package com.example.asynctask.utils;

import android.os.AsyncTask;
import android.util.Log;


public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        Log.e("tag", "doInBackground");
        publishProgress();//传入进度值
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("tag", "onPostExecute");
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.e("tag", "onProgressUpdate");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        Log.e("tag", "onPreExecute");
        super.onPreExecute();
    }
}
