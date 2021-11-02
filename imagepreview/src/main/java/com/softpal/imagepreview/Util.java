package com.softpal.imagepreview;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 Created by Himangi on 20/4/18
 */
public class Util
{
	
	public static void saveImageToGallery(Context context,Bitmap bitmap)
	{
		File photo = new File(getAppFolder(context),SystemClock.currentThreadTimeMillis() + ".jpg");
		try
		{
			/*FileOutputStream fos = new FileOutputStream(photo.getPath());
			bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
			fos.close();*/
			if(photo.exists())
			{
				ContentValues values = new ContentValues();
				
				values.put(MediaStore.Images.Media.DATE_TAKEN,System.currentTimeMillis());
				values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
				values.put(MediaStore.MediaColumns.DATA,photo.getAbsolutePath());
				
				context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
				Toast.makeText(context,"Image saved to Gallery",Toast.LENGTH_SHORT).show();
			}
		}
		catch(Exception e)
		{
			Log.e("Picture","Exception in photoCallback",e);
		}
	}
	
	
	public static void saveToInternalStorage(Context context,Bitmap bitmapImage){
		
		File mypath=new File(getAppFolder(context),SystemClock.currentThreadTimeMillis() + ".jpg");
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(mypath);
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			Toast.makeText(context,"Image saved to Gallery",Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		getAppFolder(context).getAbsolutePath();
	}
	
	public static File getAppFolder(Context context)
	{
		Log.v("Util", "getAppFolder path : "+getWorkingDirectory().getAbsolutePath());
		File photoDirectory = new File(getWorkingDirectory().getAbsolutePath(),getAppName(context.getApplicationContext()));
		if(! photoDirectory.exists())
		{
			photoDirectory.mkdir();
		}
		return photoDirectory;
	}
	
	private static File getWorkingDirectory()
	{
		File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),BuildConfig.LIBRARY_PACKAGE_NAME);
		if(! directory.exists())
		{
			directory.mkdir();
		}
		return directory;
	}
	
	private static String getAppName(Context context)
	{
		ApplicationInfo ai = null;
		String appLabel = "(unknown)";
		if(context != null)
		{
			final PackageManager pm = context.getApplicationContext().getPackageManager();
			try
			{
				ai = pm.getApplicationInfo(context.getPackageName(),0);
				appLabel = pm.getApplicationLabel(ai).toString();
			}
			catch(final PackageManager.NameNotFoundException e)
			{
				ai = null;
			}
		}
		return ai != null ? appLabel : "(unknown)";
	}
	
	public static Uri shareImage(Context context,Bitmap bitmap)
	{
		File photo = new File(getAppFolder(context),SystemClock.currentThreadTimeMillis() + ".jpg");
		try
		{
			FileOutputStream fos = new FileOutputStream(photo.getPath());
			bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
			fos.close();
			if(photo.exists())
			{
				ContentValues values = new ContentValues();
				
				values.put(MediaStore.Images.Media.DATE_TAKEN,System.currentTimeMillis());
				values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
				values.put(MediaStore.MediaColumns.DATA,photo.getAbsolutePath());
				return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
				//https://cdn.pixabay.com/photo/2016/09/30/09/52/x-ray-1704855_960_720.jpg
			}
		}
		catch(Exception e)
		{
			Log.e("Picture","Exception in photoCallback",e);
		}
		return null;
	}
	
	public static void shareBitmap (Context context, Bitmap bitmap) {
		try {
			Bitmap icon = bitmap;
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/jpeg");
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			File f = new File(getAppFolder(context), SystemClock.currentThreadTimeMillis() + ".jpg");
			try {
				f.createNewFile();
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			share.putExtra(Intent.EXTRA_STREAM, Uri.parse(f.getAbsolutePath()));
			context.startActivity(share);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

