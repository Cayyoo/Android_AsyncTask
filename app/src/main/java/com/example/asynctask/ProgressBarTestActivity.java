package com.example.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;


/**
 * 水平进度条调用演示
 */
public class ProgressBarTestActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private MyAsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar_test);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //开启异步任务
        mTask = new MyAsyncTask();
        mTask.execute();
    }

    /**
     * 自定义异步任务类
     * 将异步任务的生命周期与Activity同步
     */
    class MyAsyncTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            //使用for循环来模拟进度条的进度.
            for (int i = 0; i < 100; i++) {
                //如果已经设置成cancel，则终止掉当前的进程
                if(mTask.isCancelled()){
                    break;
                }

                //调用publishProgress方法将自动触发onProgressUpdate方法来进行进度条的更新.
                publishProgress(i);

                try {
                    //通过线程休眠模拟耗时操作
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if(mTask.isCancelled()){
                return;
            }

            progressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTask!=null && mTask.getStatus()== AsyncTask.Status.RUNNING){
            //cancel方法只是将对应的AsyncTask状态标记为cancel，并没有真正的取消掉
            mTask.cancel(true);
        }
    }

}
