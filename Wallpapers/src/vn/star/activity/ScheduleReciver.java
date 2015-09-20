package vn.star.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class ScheduleReciver extends BroadcastReceiver {
	static int img_position = 0;
	Context ctx;

	// SettingActivity mainActivity;
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		ctx = context;
		new setWallpaperTime().execute();
	}

	public class setWallpaperTime extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			// try {
			Log.e("img url", "img_url"+SettingActivity.image_urls);
			String url_path = SettingActivity.image_urls.get(img_position);
			img_position++;
			Log.e("setWallpaperTime", "setWallpaperTime" + img_position);
			if (img_position == SettingActivity.image_urls.size()) {
				img_position = 0;
			}
			WallpaperManager wpm = WallpaperManager.getInstance(ctx);
			InputStream ips;
			// Toast.makeText(getApplicationContext(),
			// "set wallpaper successed",
			// Toast.LENGTH_SHORT).show();
			try {
				ips = new URL(url_path).openStream();
				wpm.setStream(ips);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String feed) {

		}
	}
}
