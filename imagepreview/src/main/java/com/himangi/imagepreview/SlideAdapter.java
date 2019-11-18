package com.himangi.imagepreview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class SlideAdapter extends PagerAdapter
{
	
	private Context context;
	private List<PreviewFile> list;
	private OnItemClickListener<PreviewFile> listener;
	private boolean shouldCache;
	
	public SlideAdapter(Context context,List<PreviewFile> list,OnItemClickListener<PreviewFile> listener,boolean shouldCache)
	{
		this.context = context;
		this.list = list;
		this.listener = listener;
		this.shouldCache = shouldCache;
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
		final TextView tvImageDescription = view.findViewById(R.id.tvImageDescription);
		
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