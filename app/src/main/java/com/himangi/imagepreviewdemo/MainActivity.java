package com.himangi.imagepreviewdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.himangi.imagepreview.ImagePreviewActivity;
import com.himangi.imagepreview.PreviewFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
	private final int TED_GALLERY_IMAGE_PREVIEW = 1122;
	private ImageView ivResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ivResult = findViewById(R.id.iv_result_image);
		
		ArrayList<PreviewFile> imageList = new ArrayList<>();
		imageList.add(new PreviewFile("http://u01.appmifile.com/images/2016/10/21/c31c157f-97a2-479c-a2a3-74baf9790bb9.jpg","Himangi Patel"));
		imageList.add(new PreviewFile("https://nebula.wsimg.com/12e33523b6e7341bb7045fa321cdd463?AccessKeyId=63190F15169737A11884&disposition=0&alloworigin=1",""));
		imageList.add(new PreviewFile("http://avantgallery.com/wp-content/uploads/2016/02/Nick-Veasey-Selfie-23x29.5.jpg",""));
		imageList.add(new PreviewFile("https://cdn.pixabay.com/photo/2016/09/30/09/52/x-ray-1704855_960_720.jpg","Main Activity"));
		imageList.add(new PreviewFile("http://cdn.emgn.com/wp-content/uploads/2015/08/X-ray-Balarina.jpg","Image Preview screen demo"));
		imageList.add(new PreviewFile("https://cdn.pixabay.com/photo/2016/09/30/09/52/x-ray-1704855_960_720.jpg",""));
		Intent intent = new Intent(MainActivity.this,ImagePreviewActivity.class);
		intent.putExtra(ImagePreviewActivity.IMAGE_LIST,imageList);
		intent.putExtra(ImagePreviewActivity.CURRENT_ITEM,3);
		intent.putExtra(ImagePreviewActivity.SHOULD_CACHE,true);
		
		// if you want to get uri of selected image file you should pass **IS_ACCEPT_CANCEL_AVAILABLE / isAcceptCancelAvailable** true
		boolean isAcceptCancelAvailable = true;
		
		if(isAcceptCancelAvailable)
		{
			intent.putExtra(ImagePreviewActivity.IS_ACCEPT_CANCEL_AVAILABLE,isAcceptCancelAvailable);
			startActivityForResult(intent,TED_GALLERY_IMAGE_PREVIEW);
		}
		else
		{
			startActivity(intent);
		}
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		super.onActivityResult(requestCode,resultCode,data);
		
		if(requestCode == TED_GALLERY_IMAGE_PREVIEW)
		{
			if(resultCode == RESULT_OK)
			{
				boolean result = data.getBooleanExtra("imageOK",false);
				if(result)
				{
					String uri = data.getStringExtra("imageUri");
					
					Uri ivUri = Uri.parse(uri);
					
					Glide.with(this)
					     .load(ivUri)
					     .into(ivResult);
				}
			}
		}
	}
}
