package com.customSearch.adapters;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.customSerach.Response.Item;
import com.example.customimagesearch.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomSearchAdapter extends BaseAdapter {

	private ArrayList<Item> list = new ArrayList<Item>();
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Handler handler;

	public CustomSearchAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		initImageLoader();
		handler = new Handler(Looper.getMainLooper());
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_grid_layout, parent, false);
			holder.imagItem = (ImageView) view
					.findViewById(R.id.item_imageView);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				setView(holder, position);
			}
		});
		return view;
	}

	private void setView(final ViewHolder holder, int position) {
		imageLoader.displayImage(list.get(position).getImage()
				.getThumbnailLink(), holder.imagItem, options);
	}

	private void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	static class ViewHolder {
		ImageView imagItem;
	}

	public void clear() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addNewItems(List<Item> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}
}
