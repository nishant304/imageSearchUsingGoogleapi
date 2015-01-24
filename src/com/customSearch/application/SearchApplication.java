package com.customSearch.application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.app.Application;

public class SearchApplication extends Application {

	private static SearchApplication appInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		appInstance = this;
		initImageLoader();
	}

	private void initImageLoader() {
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(appInstance));
	}

	public static SearchApplication getAppInstance() {
		return appInstance;
	}
}
