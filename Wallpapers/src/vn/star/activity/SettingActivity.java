package vn.star.activity;

import vn.amobi.util.ads.AmobiAdView;
import vn.amobi.util.ads.AmobiAdView.WidgetSize;
import vn.star.wallpapers.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class SettingActivity extends Activity {
	private String imageUrls;
	private DisplayImageOptions options;
	TextView txt_image_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);

		Intent intent = getIntent();
		imageUrls = intent.getStringExtra("IMAGE_URL");

		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ListView listView = (ListView) findViewById(R.id.listView_setting);
		listView.setAdapter(new ImageAdapter(this.getApplicationContext()));

		AmobiAdView adview = (AmobiAdView) findViewById(R.id.setting_menu_adView);
		if(adview != null){
			adview.loadAd(WidgetSize.SMALL);
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class ImageAdapter extends BaseAdapter {
		LayoutInflater inflate;

		public ImageAdapter(Context cxt) {
			inflate = LayoutInflater.from(cxt);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public View getView(int arg0, View converView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (converView == null) {
				converView = inflate.inflate(R.layout.item_list_setting,
						parent, false);
			}
			ImageView image = (ImageView) converView
					.findViewById(R.id.item_list_image);
			txt_image_url = (TextView) converView.findViewById(R.id.item_list_url_value);
			if (imageUrls != null) {
				txt_image_url.setText(imageUrls);
			}
			ImageLoader.getInstance().displayImage("file://" + imageUrls,
					image, options, new SimpleImageLoadingListener() {
					});
			return converView;
		}

	}
}
