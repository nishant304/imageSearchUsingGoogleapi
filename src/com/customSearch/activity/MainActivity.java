package com.customSearch.activity;

import com.customSearch.fragments.ImageViewerFragment;
import com.customSearch.utils.AppUtils;
import com.example.customimagesearch.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author nish
 * 
 *  takes search input from the user  
 *  and displays result in the fragment
 *  
 * 
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	private EditText edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_layout, new ImageViewerFragment(), "frag")
				.commit();
	}

	private void initView() {
		edit = (EditText) findViewById(R.id.editText1);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);
	}

	private ImageViewerFragment getAttachedFragment() {
		return (ImageViewerFragment) getSupportFragmentManager()
				.findFragmentByTag("frag");
	}


/**
 * @author nish
 *   
 *   sets new search query on every button click
 *  
 */
	
	@Override
	public void onClick(View arg0) {
		ImageViewerFragment fragment = getAttachedFragment();
		String searchQuery = edit.getText().toString()
				.replaceAll("[-+.^:,]", "");
		if (TextUtils.equals(searchQuery, "")) {
			AppUtils.makeToast("Please enter proper search query");
			return;
		}
		fragment.setNewSearchQuery(searchQuery);
	}


/**
 * @author nish
 *    dismiss popup window in case it is visible 
 *    
 *    otherwise destroy the activity
 *
 */
	@Override
	public void onBackPressed() {
		ImageViewerFragment fragment = getAttachedFragment();
		if (fragment.isPopWindowVisible()) {
			fragment.disimissPopUpWindow();
			return;
		}
		super.onBackPressed();
	}

}
