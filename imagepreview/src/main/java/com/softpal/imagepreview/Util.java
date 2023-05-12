package com.softpal.imagepreview;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

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
				if(fos != null)
				{
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		getAppFolder(context).getAbsolutePath();
	}
	
	public static File getAppFolder(Context context)
	{
		File photoDirectory = new File(getWorkingDirectory().getAbsolutePath(),getAppName(context.getApplicationContext()));
		if(! photoDirectory.exists())
		{
			photoDirectory.mkdir();
		}
		return photoDirectory;
	}
	
	private static File getWorkingDirectory()
	{
		File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),BuildConfig.LIBRARY_PACKAGE_NAME);
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
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/jpeg");
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
			File f = new File(getAppFolder(context), SystemClock.currentThreadTimeMillis() + ".jpg");
			FileOutputStream fo = null;
			try {
				boolean status = f.createNewFile();
				fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally
			{
				if(fo!=null){
					fo.flush();
					fo.close();
				}
			}
			File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"com.softpal.imagepreview/ImagesPreview/"+System.currentTimeMillis() + ".jpg");
			String path = folder.getPath();
			Uri myImagesdir = Uri.parse("content://" + path);
			
			share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			share.putExtra(Intent.EXTRA_STREAM, Uri.parse(f.getAbsolutePath()));
			//share.setDataAndType(myImagesdir,"image/jpeg");
			context.startActivity(share);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static void shareBitmap2(Context context,Bitmap bitmap){
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG1024.JPG");
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.IS_PENDING, 1);
		
		ContentResolver resolver = context.getContentResolver();
		Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
		Uri item = resolver.insert(collection, values);
		
		try (ParcelFileDescriptor pfd = resolver.openFileDescriptor(item,"w",null)) {
			// Write data into the pending image.
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Now that we're finished, release the "pending" status, and allow other apps
		// to view the image.
		values.clear();
		values.put(MediaStore.Images.Media.IS_PENDING, 0);
		resolver.update(item, values, null, null);
	}
	
	public static void shareBitmap3 (Context context, Bitmap bitmap) {
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		
		
		File f =  new File(context.getExternalCacheDir()+"/"+context.getResources().getString(R.string.app_name)+".png");
		Intent shareIntent;
		
		
		try {
			FileOutputStream outputStream = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
			
			outputStream.flush();
			outputStream.close();
			shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("image/jpg");
			
			Uri bmpUri = Uri.parse("file://"+f.getPath());
			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +context.getPackageName());
			shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			
			
		}catch (Exception e){
			throw new RuntimeException(e);
		}
		context.startActivity(Intent.createChooser(shareIntent,"Share Picture"));
	}
	
	private boolean isExternalStorageWritable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	// Checks if a volume containing external storage is available to at least read.
	private boolean isExternalStorageReadable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
		       Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
	}
	
	public static void shareImageWithFileProvider(Context context, Bitmap bitmap){
		try
		{
			File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"com.softpal.imagepreview/ImagesPreview/"+System.currentTimeMillis() + ".jpg");
			Intent intent = new Intent(Intent.ACTION_SEND);
			String path = folder.getPath();
			Uri myImagesdir = Uri.parse("content://" + path);
			intent.setDataAndType(myImagesdir,"images/jpg");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			context.startActivity(intent);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void dummyMethod(Context context, Bitmap bitmap){
		Intent shareIntent;
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
		OutputStream out = null;
		File file=new File(path);
		try {
			out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		path=file.getPath();
		Uri bmpUri = Uri.parse("file://"+path);
		shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
		shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +context.getPackageName());
		shareIntent.setType("image/png");
		context.startActivity(Intent.createChooser(shareIntent,"Share with"));
		
	}
	
	public static void shareImageUrl(final Context context){
		Picasso.get().load("https://cdn.pixabay.com/photo/2016/09/30/09/52/x-ray-1704855_960_720.jpg").into(new Target()
		{
			@Override
			public void onBitmapLoaded(Bitmap bitmap,Picasso.LoadedFrom from)
			{
				Log.v("Util", "=====> onBitmapLoaded");
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(context, bitmap));
				context.startActivity(Intent.createChooser(intent, "Share Image"));
			}
			
			@Override
			public void onBitmapFailed(Exception e,Drawable errorDrawable)
			{
				Log.v("Util", "=====> onBitmapFailed");
			}
			
			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable)
			{
				Log.v("Util", "=====> onPrepareLoad");
			}
		});
	}
	
	public static Uri getBitmapFromView(Context context, Bitmap bitmap){
		Uri bmpUri = null;
		try{
			File file = new File(context.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
			bmpUri = Uri.fromFile(file);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
		return bmpUri;
	}
}

