package com.customSearch.ImageLoader;

import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class ImageLoader  {

	
	PriorityBlockingQueue<Runnable> queue;
	PriorityQueue<Runnable> q = new PriorityQueue<Runnable>();

	private int CORE_SIZE= Runtime.getRuntime().availableProcessors();
	ThreadPoolExecutor threadpool; 
	
	private ImageLoader(){
	  queue = new PriorityBlockingQueue<Runnable>(30);	
	  threadpool = new ThreadPoolExecutor(CORE_SIZE, 8, 2, TimeUnit.SECONDS, queue);	
	}
	
	
	public void loadImage(String url,ImageAware img){ 
		
	
	}
	
	
	
	
}
