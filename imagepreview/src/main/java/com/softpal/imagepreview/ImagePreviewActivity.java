package com.softpal.imagepreview;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class ImagePreviewActivity extends AppCompatActivity implements OnItemClickListener<PreviewFile>
{
	
	public static String IMAGE_LIST = "intent_image_item";
	public static String CURRENT_ITEM = "current_item";
	public static String SHOULD_CACHE = "should_cache";
	public static String IS_ACCEPT_CANCEL_AVAILABLE = "is_accept_cancel_available";
	
	public static Toolbar toolbar;
	ViewPager vPager;
	PermissionListener permissionListener_save = null, permissionListener_share = null;
	private List<PreviewFile> mUriList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_preview);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		toolbar = findViewById(R.id.toolbar);
		vPager = findViewById(R.id.vPager);
		setSupportActionBar(toolbar);
		toolbar.setTitle("");
		toolbar.setSubtitle("");
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
		setUpViews();
	}
	
	private void setUpViews()
	{
		mUriList = (List<PreviewFile>)getIntent().getSerializableExtra(IMAGE_LIST);
		boolean shouldCache = getIntent().getBooleanExtra(SHOULD_CACHE,false);
		boolean isAcceptCancelAvailable = getIntent().getBooleanExtra(IS_ACCEPT_CANCEL_AVAILABLE,false);
		if(mUriList != null)
		{
			SlideAdapter slideAdapter = new SlideAdapter(this,mUriList,this,shouldCache,isAcceptCancelAvailable);
			vPager.setAdapter(slideAdapter);
			
			vPager.setCurrentItem(getIntent().getIntExtra(CURRENT_ITEM,0));
			
			vPager.setPageTransformer(false,new ViewPager.PageTransformer()
			{
				@Override
				public void transformPage(@NonNull View page,float position)
				{
					float normalizedPosition = Math.abs(Math.abs(position) - 1);
					page.setScaleX(normalizedPosition / 2 + 0.5f);
					page.setScaleY(normalizedPosition / 2 + 0.5f);
				}
			});
		}
	}
	
	@Override
	public boolean onSupportNavigateUp()
	{
		onBackPressed();
		return true;
	}
	
	@Override
	public void onItemClick(PreviewFile item)
	{
		toolbar.setVisibility(toolbar.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_save_share,menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.menu_save)
		{
			Glide.with(ImagePreviewActivity.this).asBitmap().load(mUriList.get(vPager.getCurrentItem()).getImageURL()).into(new CustomTarget<Bitmap>()
			{
				@Override
				public void onResourceReady(Bitmap resource,Transition<? super Bitmap> transition)
				{
					handleSavePermission(resource);
					// hideProgressDialog();
				}
				
				@Override
				public void onLoadCleared(@Nullable Drawable placeholder)
				{
				
				}
			});
		}
		else if(item.getItemId() == R.id.menu_share)
		{
			Glide.with(ImagePreviewActivity.this).asBitmap().load(mUriList.get(vPager.getCurrentItem()).getImageURL()).into(new CustomTarget<Bitmap>()
			{
				@Override
				public void onResourceReady(Bitmap resource,Transition<? super Bitmap> transition)
				{
					handleSharePermission(resource);
				}
				
				@Override
				public void onLoadCleared(@Nullable Drawable placeholder)
				{
				
				}
			});
		}
		return true;
	}
	
	private void handleSavePermission(final Bitmap resource)
	{
		permissionListener_save = new PermissionListener()
		{
			@Override
			public void onPermissionGranted()
			{
				Util.saveImageToGallery(ImagePreviewActivity.this,resource);
			}
			
			@Override
			public void onPermissionDenied(List<String> deniedPermissions)
			{
				return;
			}
		};
		TedPermission.with(ImagePreviewActivity.this).setPermissionListener(permissionListener_save).setRationaleMessage(getResources().getString(R.string.perm_storage_msg)).setDeniedMessage(getResources().getString(R.string.denied_message)).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).check();
	}
	
	private void handleSharePermission(final Bitmap resource)
	{
		permissionListener_share = new PermissionListener()
		{
			@Override
			public void onPermissionGranted()
			{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/jpeg");
				intent.putExtra(Intent.EXTRA_STREAM,Util.shareImage(ImagePreviewActivity.this,resource));
				startActivity(Intent.createChooser(intent,"Share Image"));
			}
			
			@Override
			public void onPermissionDenied(List<String> deniedPermissions)
			{
				return;
			}
		};
		TedPermission.with(ImagePreviewActivity.this).setPermissionListener(permissionListener_share).setRationaleMessage(getResources().getString(R.string.perm_storage_msg)).setDeniedMessage(getResources().getString(R.string.denied_message)).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).check();
	}
	
	private void showAlertDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ImagePreviewActivity.this).setPositiveButton("GO TO SETTING",new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog,int which)
			{
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package",getPackageName(),null);
				intent.setData(uri);
				startActivity(intent);
			}
		}).setNegativeButton("DENY",new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog,int which)
			{
				dialog.dismiss();
			}
		}).setTitle("Permission denied").setMessage("Without storage permission the app" + " is unable to open gallery or to save photos." + " Are you sure want to deny this permission?");
		builder.create().show();
	}
}
