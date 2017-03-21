package com.example.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片加载演示
 */
public class ImageAsyncTaskActivity extends AppCompatActivity {
    private ImageView imageView;
    private ProgressBar progressBar;

    private static String imageUrl = "http://img.hb.aicdn.com/761f1bce319b745e663fed957606b4b5d167b9bff70a-nfBc9N_fw580";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_asynctask);

        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //通过调用execute方法开始处理异步任务.相当于线程中的start方法.
        new MyAsyncTask().execute(imageUrl);
    }

    /**
     * 自定义网络请求异步任务
     */
    class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{

        /**
         * onPreExecute用于异步处理前的操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * 在doInBackground方法中进行异步任务的处理
         *
         * @param params 参数为URL
         * @return Bitmap对象
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];//获取传进来的参数
            Bitmap bitmap = null;
            URLConnection connection ;
            InputStream is;
            try {
                connection = new URL(url).openConnection();
                is = connection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                //休眠3秒防止加载太快，看不到加载效果
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //通过decodeStream方法解析输入流
                bitmap = BitmapFactory.decodeStream(bis);

                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         * onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值
         *
         * @param bitmap 网络图片
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            progressBar.setVisibility(View.GONE);

            if(bitmap!=null){
                imageView.setImageBitmap(bitmap);//更新imageView
            }
        }
    }

}
