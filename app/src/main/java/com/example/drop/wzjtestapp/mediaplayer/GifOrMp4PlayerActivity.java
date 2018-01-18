package com.example.drop.wzjtestapp.mediaplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.views.media.CustomVideoView;

public class GifOrMp4PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomVideoView videoview;
    private Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_or_mp4_player);
        //隐藏状态栏5.0以上可用
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.test_media));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                Toast.makeText(this,"进入了主页", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
