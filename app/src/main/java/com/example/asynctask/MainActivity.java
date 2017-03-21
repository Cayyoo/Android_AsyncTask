package com.example.asynctask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.asynctask.utils.MyAsyncTask;


/**
 * 使用AsyncTask的注意事项
 * ① 必须在UI线程中创建AsyncTask的实例.
 * ② 只能在UI线程中调用AsyncTask的execute方法.
 * ③ AsyncTask被重写的四个方法是系统自动调用的,不应手动调用.
 * ④ 每个AsyncTask只能被执行(execute方法)一次,多次执行将会引发异常.
 * ⑤ AsyncTask的四个方法,只有doInBackground方法是运行在其他线程中,其他三个方法都运行在UI线程中,也就说其他三个方法都可以进行UI的更新操作.
 *
 * 1、不会重复执行已完成的任务。
 *   图片加载中，如果图片都已经加载成功了，再按“加载图片”按钮时，没有任何反应，不会重新获取图片数据。
 *   execute(Param)源码中会判断该任务的状态。RUNNING或FINISHED。
 * 2、AsyncTask的cancel(boolean)只是标记了取消状态，并不能真正地取消任务。
 *   要想取消任务，必须在doInBackground中通过isCancelled()判断本任务是否被取消，如果为ture，则进行相应逻辑来退出本任务。
 */

/**
 * Android异步任务AsyncTask：
 *
 * 一、概述
 * Android是单线程模型，耗时的操作必须放在非主线程中执行，对此，我们需要使用多线程/线程池或者AsyncTask等来完成异步加载任务。
 *
 * 二、AsyncTask
 * AsyncTask<Params, Progress, Result>是一个抽象类，通常用于被继承，继承AsyncTask需要指定如下三个泛型参数：
 * Params：启动任务时输入参数的类型
 * Progress：后台任务执行中返回进度值的类型
 * Result：后台执行任务完成后返回结果的类型
 *
 * AsyncTask子类的回调方法：
 * doInBackground：必须重写，异步执行后台线程将要完成的任务
 * onPreExecute：执行后台耗时操作前被调用，通常用户完成一些初始化操作
 * onPostExecute：当doInBackground()完成后系统会自动调用该方法，并将doInBackground方法返回的值传给该方法
 * onProgressUpdate：在doInBackground()方法中调用publishProgress()方法更新任务的执行进度后，就会触发该方法。
 */

/**
 * AsyncTask和Handler两种异步方式区别：
 * 一、AsyncTask
 * 它是Android提供的轻量级的异步类，可以直接继承AsyncTask。
 * 在类中实现异步操作，并提供接口来反馈当前异步执行的程度，也就是所谓的进度更新，最后反馈执行的结果给UI主线程。
 * 这种方法使用起来简单快捷，过程清晰明了而且便于控制。
 * 不足的是在使用多个异步操作和并需要进行Ui变更时，就变得复杂起来，代码也看起来比较臃肿。
 *
 * 二、Handler
 * 异步消息处理机制。它是通过Handler, Looper, Message,Thread四个对象之间的联系来进行处理消息的。
 * 这种方式在功能上比较清晰，有多个后台任务的时候代码看起来比较有序。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab!=null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public void loadImage(View view){
        Intent it = new Intent(MainActivity.this, ImageAsyncTaskActivity.class);
        startActivity(it);
    }

    public void showProgressBar(View view){
        Intent it = new Intent(MainActivity.this, ProgressBarTestActivity.class);
        startActivity(it);
    }

    public void goNewsList(View view){
        Intent it = new Intent(MainActivity.this, NewsListActivity.class);
        startActivity(it);
    }
}
