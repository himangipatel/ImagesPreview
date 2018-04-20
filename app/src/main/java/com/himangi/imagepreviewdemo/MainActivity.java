package com.himangi.imagepreviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.himangi.imagepreview.ImagePreviewActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> imageList = new ArrayList<>();
        imageList.add("http://u01.appmifile.com/images/2016/10/21/c31c157f-97a2-479c-a2a3-74baf9790bb9.jpg");
        imageList.add("https://nebula.wsimg.com/12e33523b6e7341bb7045fa321cdd463?AccessKeyId=63190F15169737A11884&disposition=0&alloworigin=1");
        imageList.add("http://avantgallery.com/wp-content/uploads/2016/02/Nick-Veasey-Selfie-23x29.5.jpg");
        imageList.add("https://cdn.pixabay.com/photo/2016/09/30/09/52/x-ray-1704855_960_720.jpg");
        imageList.add("http://cdn.emgn.com/wp-content/uploads/2015/08/X-ray-Balarina.jpg");
        imageList.add("https://cdn.pixabay.com/photo/2016/09/30/09/52/x-ray-1704855_960_720.jpg");
        Intent intent = new Intent(MainActivity.this,
                ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.IMAGE_LIST,
                imageList);
        intent.putExtra(ImagePreviewActivity.CURRENT_ITEM, 3);
        startActivity(intent);

    }
}
