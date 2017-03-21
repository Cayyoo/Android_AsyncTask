package com.example.asynctask.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.asynctask.R;
import com.example.asynctask.adapter.NewsListAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


public class ImageLoader {

    private ImageView mImageView;
    private String mUrl;

    private LruCache<String, Bitmap> mCaches;//一级缓存保存在内存中
    private ListView mListView;
    private Set<NewsAsyncTask> mTasks;

    public ImageLoader(ListView listView){
        this.mListView = listView;
        mTasks = new HashSet<>();

        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/4;//设置实际缓存大小

        mCaches = new LruCache<String, Bitmap>(cacheSize){
            /**
             * 在每次存入缓存的时候调用
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

    }

    /**
     * 增加到缓存
     */
    public void addBitmapToCache(String url, Bitmap bitmap){
        if(getBitmapFromCache(url)==null){

            mCaches.put(url, bitmap);
        }
    }

    /**
     * 从缓存中获取数据
     */
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(mImageView.getTag().equals(mUrl)){
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    public void showImageByThread(ImageView imageView, final String imageUrl){
        mImageView = imageView;
        mUrl = imageUrl;

        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap bmp = getBitmapFromUrl(imageUrl);
                Message message = Message.obtain();//使用现有的Messsage，提高Message的效率
                message.obj = bmp;
                handler.sendMessage(message);
            }
        }.start();

    }

    /**
     * 从网络下载图片
     */
    public Bitmap getBitmapFromUrl(String iurl){
        Bitmap bitmap = null;
        InputStream is = null;

        try {
            URL url = new URL(iurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    public void showImageByAsyncTask(ImageView imageView, final String imageUrl){
        //从缓存中获取图片，判断是否存在
        Bitmap bitmap = getBitmapFromCache(imageUrl);

        if(bitmap==null){
            //从网络下载图片
            imageView.setImageResource(R.mipmap.ic_launcher);
            //new NewsAsyncTask(imageView, imageUrl).execute(imageUrl);
        }else{
            //如果缓存中存在，则直接显示图片
            imageView.setImageBitmap(bitmap);
        }
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap>{
        //private ImageView mImageView;
        private String mUrl;

        public NewsAsyncTask(String url) {
            //this.mImageView = mImageView;
            this.mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //从网络获取图片
            Bitmap bitmap = getBitmapFromUrl(params[0]);

            if(bitmap!=null){
                //将Bitmap保存到缓存
                addBitmapToCache(params[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);

            if(imageView!=null && bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }

            mTasks.remove(this);
        }
    }

    /**
     * 用来加载从start到end的数据
     */
    public void loadImage(int start, int end){
        for (int i = start; i < end; i++) {
            String url = NewsListAdapter.URLS[i];
            //从缓存中获取图片，判断是否存在
            Bitmap bitmap = getBitmapFromCache(url);

            if(bitmap==null){
                //从网络下载图片
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTasks.add(task);
            }else{
                //如果缓存中存在，则直接显示图片
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }

    }

    public void cancelAllTasks(){
        if(mTasks!=null){
            for (NewsAsyncTask task : mTasks){
                task.cancel(true);
            }
        }
    }

}
