package com.example.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;


import com.example.asynctask.adapter.NewsListAdapter;
import com.example.asynctask.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NewsListActivity extends AppCompatActivity {

    private ListView mListView;
    private String mUrl = "http://www.imooc.com/api/teacher?type=4&num=30";//数据源链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        mListView = (ListView) findViewById(R.id.listView);
        new NewsAsyncTask().execute(mUrl);
    }

    /**
     * 实现网络的异步访问
     */
    class NewsAsyncTask extends AsyncTask<String, Void, List<News>>{

        @Override
        protected List<News> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<News> list) {
            super.onPostExecute(list);
            NewsListAdapter adapter = new NewsListAdapter(NewsListActivity.this, list, mListView);
            mListView.setAdapter(adapter);
        }
    }

    private List<News> getJsonData(String url) {
        List<News> list = new ArrayList<>();

        try {
            String jsonString = readStream(new URL(url).openStream());
            Log.e("tag", "jsonString:"+jsonString);
            JSONObject jsonObject;
            News news;

            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    news = new News();
                    news.newsIconUrl = jsonObject.getString("picSmall");
                    news.newsTitle = jsonObject.getString("name");
                    news.newsContent = jsonObject.getString("description");
                    list.add(news);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private String readStream(InputStream is){
        InputStreamReader isr;
        String result = "";

        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8");//字节流转化为字符流
            BufferedReader br = new BufferedReader(isr);

            while((line = br.readLine())!=null){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
