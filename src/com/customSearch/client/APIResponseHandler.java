package com.customSearch.client;

import org.apache.http.Header;
import android.content.Context;
import com.customSearch.utils.AppUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;



/**
 * @author nish
 *
 * @param <T>  wrapper on response handler    
 * will return response in abstract methods 
 */
public abstract class APIResponseHandler<T> extends AsyncHttpResponseHandler {

	/**
	 * shows loading image on Screen from where api call has been made based on
	 * this flag
	 */
	/**
	 * response handler shall assume the response to be of this class
	 */
	private Class<T> clazz;
	

	public APIResponseHandler(Class<T> clazz, Context context) {
		this.clazz = clazz;
	}



	@Override
	final public void onFailure(int statusCode, Header[] headers,
			byte[] errorResponse, Throwable arg3) {
		// called when response HTTP status is "4XX" (eg. 401, 403, 404)
		AppUtils.makeToast("Api Failed");
		onFailure();
	}

	@Override
	final public void onSuccess(int statusCode, Header[] headers,
			byte[] response) {
		// called when response HTTP status is "200 OK"
		String sResp=null;
		if(response!=null){
			sResp = new String(response);	
		}
		onSuccess(sResp!=null ?AppUtils.getObject(sResp, clazz):null);
	}

	/**
	 * called when api call succeeds with status 1
	 * 
	 * @param t
	 */
	abstract public void onSuccess(T t);

	abstract public void onFailure();

}
