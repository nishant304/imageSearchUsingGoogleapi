package com.customSearch.utils;

import android.widget.Toast;
import com.customSearch.application.SearchApplication;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nish
 * 
 *         json parser using gson libraray
 */
public class AppUtils {

	public static <T> T getObject(String json, Class<T> classOfT) {
		return new Gson().fromJson(json, classOfT);
	}

	public static <T> T getObject(JsonElement json, Class<T> classOfT) {
		return new Gson().fromJson(json, classOfT);
	}

	public static JsonObject getJsonObject(String json) {
		return new JsonParser().parse(json).getAsJsonObject();
	}

	public static void makeToast(String string) {
		Toast.makeText(SearchApplication.getAppInstance(), string,
				Toast.LENGTH_SHORT).show();
	}
}
