package com.example.asynctask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asynctask.R;
import com.example.asynctask.model.News;
import com.example.asynctask.utils.ImageLoader;

import java.util.List;


public class NewsListAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private Context context;
    private List<News> list;
    private LayoutInflater mInflater;

    private ImageLoader mImageLoader;

    private int mStart, mEnd;
    public static String [] URLS;

    private boolean mFirstIn;

    public NewsListAdapter(Context context, List<News> list, ListView listView) {
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);

        mImageLoader = new ImageLoader(listView);
        URLS = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            URLS[i] = list.get(i).newsIconUrl;
        }

        listView.setOnScrollListener(this);//绑定监听事件
        mFirstIn = true;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;

        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.news_list_item, null);

            viewHolder.image_iv = (ImageView) convertView.findViewById(R.id.image_iv);
            viewHolder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.image_iv.setImageResource(R.mipmap.ic_launcher);
        String url = list.get(position).newsIconUrl;

        viewHolder.image_iv.setTag(url);//设置tag，防止图片错乱

        //mImageLoader.showImageByThread(viewHolder.image_iv, url);
        mImageLoader.showImageByAsyncTask(viewHolder.image_iv, url);

        viewHolder.title_tv.setText(list.get(position).newsTitle);
        viewHolder.content_tv.setText(list.get(position).newsContent);

        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){//滑动停止状态，加载可见项
            mImageLoader.loadImage(mStart, mEnd);
        }else{//停止加载可见项
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;

        //第一次显示的时候调用
        if(mFirstIn && totalItemCount>0){
            mFirstIn = false;
            mImageLoader.loadImage(mStart, mEnd);
        }
    }

    static class ViewHolder{
        TextView title_tv, content_tv;
        ImageView image_iv;
    }

}
