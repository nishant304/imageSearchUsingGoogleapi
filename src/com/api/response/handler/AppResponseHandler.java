package com.api.response.handler;

import java.lang.ref.WeakReference;
import android.content.Context;
import android.util.Log;
import com.customSearch.client.APIResponseHandler;
import com.customSearch.fragments.ImageViewerFragment;
import com.customSerach.Response.Response;

/**
 * @author nish wrapper for handling single type of response
 * 
 *         make this class singleton to overcome overhead of creating a new
 *         object again and again in case of rapid search request
 * 
 * 
 *         gives callback to classes observing this class
 * @param <T>
 */
public class AppResponseHandler<T> extends APIResponseHandler<T> {

	private WeakReference<ImageViewerFragment> weakRef;

	private static AppResponseHandler responsehandler;

	private OnRequestComplete callback;

	private AppResponseHandler(Context context) {
		super(((Class<T>) Response.class), context);
	}

	public static AppResponseHandler getInstance(Context context) {

		if (responsehandler == null) {
			synchronized (AppResponseHandler.class) {
				if (responsehandler == null) {
					responsehandler = new AppResponseHandler(
							context.getApplicationContext());
				}
			}
		}
		return responsehandler;
	}

	@Override
	public void onSuccess(Object t) {
		Response response = (Response) t;
		callback.onApiSuccess(response);
	}

	@Override
	public void onFailure() {
		callback.onApiFailure();
	}

	@Override
	protected void finalize() throws Throwable {
		Log.d("nishant", "object garbage collected");
		super.finalize();
	}

	public void RegisterForCallBacks(OnRequestComplete callback) {
		this.callback = callback;
	}

	public void removeCallBacks() {
		callback = null;
	}

	public interface OnRequestComplete {
		
		public void onApiSuccess(Response response);

		public void onApiFailure();
	}

}
