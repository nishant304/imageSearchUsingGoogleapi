package com.customSearch.fragments;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.api.response.handler.AppResponseHandler;
import com.api.response.handler.AppResponseHandler.OnRequestComplete;
import com.customSearch.adapters.CustomSearchAdapter;
import com.customSearch.client.AppHttpClient;
import com.customSearch.utils.AppConstants;
import com.customSerach.Response.Item;
import com.customSerach.Response.Response;
import com.example.customimagesearch.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * @author nish
 * 
 * displays search result in a gridview 
 * handles touch event on items and 
 * zoom functionality 
 * 
 * observes  Appresponsehandler for new search results
 * 
 */

public class ImageViewerFragment extends Fragment implements OnScrollListener,
		OnRequestComplete, OnItemClickListener {

	private GridView gridview;
	private String searchParameter;
	private Handler handler;
	private boolean isLoading;
	private CustomSearchAdapter adapter;
	private View popUpView;
	private ImageView imgview;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private PopupWindow window;
	private PhotoViewAttacher mattacher;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_layout, container, false);
		initView(view);
		setupListeners();
		initImageLoader();
		return view;
	}

	

/**
 * @author nish
 *   register for callbacks
 *   add observer here 
 */
	
	@Override
	public void onStart() {
		super.onStart();
		AppResponseHandler.getInstance(getActivity())
				.RegisterForCallBacks(this);
	}

	private void initView(View view) {
		gridview = (GridView) view.findViewById(R.id.gridView);
		adapter = new CustomSearchAdapter(getActivity());
		handler = new Handler(Looper.getMainLooper());
	}

	private void setupListeners() {
		gridview.setAdapter(adapter);
		gridview.setOnScrollListener(this);
		gridview.setOnItemClickListener(this);
	}


/**
 * @author nish
 *  
 *  loads data on a drag or appropriate scroll
 *  using get request from rest client 
 *   
 */
	private void loadMore() {
		AppHttpClient.getClient().get(getUrl(), getHandler());
	}

	private AppResponseHandler<Response> getHandler() {
		return AppResponseHandler.getInstance(getActivity());
	}

	private String getUrl() {
		return AppConstants.URL + "&q=" + searchParameter + "&searchType=image"
				+ "&start=" + String.valueOf(adapter.getCount() + 1);
	}

/**
 * @author nish
 *   new search query from user 
 *   clear the adapter .....remove previous data 
 *   make new search request in a messge queue
 */
	
	public void setNewSearchQuery(String searchParameter) {
		this.searchParameter = searchParameter;
		onNewQuery();
	}

	private void onNewQuery() {
		adapter.clear();
		queueLoadRequest();
	}

	private void queueLoadRequest() {
		isLoading = true;
		handler.post(new Runnable() {
			@Override
			public void run() {
				loadMore();
			}
		});
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	@Override
	public void onScroll(AbsListView listview, int arg1, int arg2, int arg3) {

	}

/**
 * @author nish
 *  load more data in case we reach end of the screen 
 *  
 */
	
	@Override
	public void onScrollStateChanged(AbsListView arg0, int state) {
		if (state == SCROLL_STATE_TOUCH_SCROLL) {
			if (shouldLoadMore() && !isLoading()) {
				queueLoadRequest();
			}
		}
	}

	private boolean shouldLoadMore() {
		if (gridview.getLastVisiblePosition() == adapter.getCount() - 1) {
			return true;
		}
		return false;
	}

	@Override
	public void onApiSuccess(Response response) {
		setLoading(false);
		adapter.addNewItems(response.getItems());
	}

	@Override
	public void onApiFailure() {
		setLoading(false);
	}

	/**
	 * @author nish make sure callback is removed in onstop
	 */

	@Override
	public void onStop() {
		super.onStop();
		AppResponseHandler.getInstance(getActivity()).removeCallBacks();
	}

	/**
	 * @author nish display image in full window initializes view for the first
	 *         click only
	 * 
	 */

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position,
			long arg3) {
		Item item = (Item) adapter.getItem(position);
		getPopupWindow().showAtLocation(view, Gravity.CENTER, 0, 0);
		displayFullSizeImage(item, window);
	}

	/**
	 * @author nish Lazy initialization of views
	 * 
	 *         Dont initilaize it initially as the user might not click on any
	 *         image to view it Keep the reference to avoid heavy findvievbyid
	 *         call
	 */

	private PopupWindow getPopupWindow() {
		if (window == null) {
			window = new PopupWindow(getPopUpView(), LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			window.setAnimationStyle(android.R.anim.fade_in);
		}
		return window;
	}

	public boolean isPopWindowVisible() {
		return window != null && window.isShowing();
	}

	public void disimissPopUpWindow() {
		window.dismiss();
		mattacher=null;
	}


/**
 * @author nish
 *   PhotoViewAttacher will handle zoom in functionality 
 *   in a pop up window
 */
	
	private void displayFullSizeImage(Item item, final PopupWindow window) {
		mattacher  = new PhotoViewAttacher(getImageViewForWindow());
		imageLoader.displayImage(item.getImage().getThumbnailLink(),
				getImageViewForWindow(), options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						window.update();
						mattacher.update();
					}
				});
	}

	private void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	private ImageView getImageViewForWindow() {
		if (imgview == null) {
			imgview = (ImageView) getPopUpView().findViewById(
					R.id.item_imageView_window);
		}
		return imgview;
	}

	private View getPopUpView() {
		if (popUpView == null) {
			popUpView = getInFlater().inflate(R.layout.popup_window, null);
		}
		return popUpView;
	}

	private LayoutInflater getInFlater() {
		return getActivity().getLayoutInflater();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		adapter = null;
		gridview = null;
		handler = null;
		imageLoader = null;
		imgview = null;
		popUpView = null;
	}
}
