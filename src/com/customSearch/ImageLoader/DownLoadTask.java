package com.customSearch.ImageLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadTask implements Runnable {

	private String Url;
	private byte[] imageInBytes;

	private int UNKNOWN_READ_SIZE = 2 * 1024;

	public DownLoadTask(String url) {
		this.Url = url;
	}

	@Override
	public void run() {

		if (Thread.interrupted()) {
			return;
		}

		HttpURLConnection connection = null;
		try {
			URL url = new URL(Url);
			if (Thread.interrupted()) {
				return;
			}

			connection = (HttpURLConnection) url.openConnection();

			if (Thread.interrupted()) {
				return;
			}

			InputStream stream = new BufferedInputStream(
					connection.getInputStream());
			if (Thread.interrupted()) {
				return;
			}

			readFromStream(stream, connection.getContentLength());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}

	private void readFromStream(InputStream stream, int contentLength)
			throws InterruptedException {
		if (contentLength == -1) {
			downloadUnknowSizeStream(stream);
		} else {
			downLoadFixedSize(stream, contentLength);
		}
	}

	private void downLoadFixedSize(InputStream stream, int contentLength)
			throws InterruptedException {
		
		imageInBytes = new byte[contentLength];
		int remainingLength = contentLength;
		int byteOffset = 0;

		try {
			while (remainingLength > 0) {
				int read = stream.read(imageInBytes, byteOffset,
						remainingLength);
				byteOffset += read;
				remainingLength = remainingLength - read;
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void downloadUnknowSizeStream(InputStream stream) throws InterruptedException {

		byte[] tempBuf = new byte[UNKNOWN_READ_SIZE];
		int remainingBytes = UNKNOWN_READ_SIZE;
		int byteoffset = 0;

		while (true) {
			try {
				int read = stream.read(tempBuf, byteoffset, remainingBytes);
				if (read < 0) {
					break;
				}
				byteoffset += read;
				remainingBytes -= read;

				if (remainingBytes == 0) {
					int initialLength = tempBuf.length;
					byte[] temp = new byte[initialLength];
					System.arraycopy(tempBuf, 0, temp, 0, initialLength);
					int newReadSize = tempBuf.length + UNKNOWN_READ_SIZE;
					tempBuf = new byte[newReadSize];
					System.arraycopy(temp, 0, tempBuf, 0, initialLength);
				}
				
				if(Thread.interrupted()){
					throw new InterruptedException();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
