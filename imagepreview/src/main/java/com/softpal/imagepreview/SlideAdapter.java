package com.softpal.imagepreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.util.List;

public class SlideAdapter extends PagerAdapter
{
	private static String TAG = SlideAdapter.class.getSimpleName();
	private Context context;
	private List<PreviewFile> list;
	private OnItemClickListener<PreviewFile> listener;
	private boolean shouldCache;
	private boolean isAcceptCancelAvailable;
	
	public SlideAdapter(Context context,List<PreviewFile> list,OnItemClickListener<PreviewFile> listener,boolean shouldCache,boolean isAccpetRejectAvailable)
	{
		this.context = context;
		this.list = list;
		this.listener = listener;
		this.shouldCache = shouldCache;
		this.isAcceptCancelAvailable = isAccpetRejectAvailable;
		
		Log.v(TAG,"isAccpetRejectAvailable=="+isAccpetRejectAvailable);
	}
	
	@Override
	public int getCount()
	{
		int size = 0;
		if(list != null && list.size() > 0)
		{
			size = list.size();
		}
		return size;
	}
	
	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container,final int position)
	{
		
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		assert layoutInflater != null;
		View view = layoutInflater.inflate(R.layout.item_preview,container,false);
		
		PhotoView image = view.findViewById(R.id.iv_preview);
		ImageView accept = view.findViewById(R.id.iv_accept_image);
		ImageView cancel = view.findViewById(R.id.iv_reject_image);
		final TextView tvImageDescription = view.findViewById(R.id.tvImageDescription);
		
		final String uriImage = list.get(position).getImageURL();
		Bitmap bitmap;
		
		if(uriImage != null)
		{
			Uri uri = Uri.parse(uriImage);
			
			try
			{
				bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
				image.setImageBitmap(bitmap);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(shouldCache)
		{
			Glide.with(context).load(list.get(position).getImageURL()).into(image);
		}
		else
		{
			Glide.with(context).load(list.get(position).getImageURL()).apply(RequestOptions.skipMemoryCacheOf(true)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(image);
		}
		
		if(list.get(position).getImageDescription().isEmpty())
		{
			tvImageDescription.setVisibility(View.GONE);
		}
		else
		{
			tvImageDescription.setVisibility(View.VISIBLE);
			tvImageDescription.setText(list.get(position).getImageDescription());
		}
		
		image.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				listener.onItemClick(list.get(position));
				if(! list.get(position).getImageDescription().isEmpty())
				{
					tvImageDescription.setVisibility(tvImageDescription.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
				}
			}
		});
		
		if(isAcceptCancelAvailable)
		{
			accept.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			
			accept.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent sendIntent = new Intent();
					sendIntent.putExtra("imageOK",true);
					sendIntent.putExtra("imageUri",uriImage);
					((AppCompatActivity)context).setResult(Activity.RESULT_OK,sendIntent);
					((AppCompatActivity)context).finish();
				}
			});
			
			cancel.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent sendIntent = new Intent();
					sendIntent.putExtra("imageOK",false);
					((AppCompatActivity)context).setResult(Activity.RESULT_OK,sendIntent);
					((AppCompatActivity)context).finish();
				}
			});
		}
		else
		{
			accept.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
		}
		
		container.addView(view);
		return view;
	}
	
	@Override
	public void destroyItem(@NonNull ViewGroup container,int position,@NonNull Object object)
	{
		container.removeView((RelativeLayout)object);
	}
	
	@Override
	public boolean isViewFromObject(@NonNull View view,@NonNull Object object)
	{
		return view == object;
	}
}