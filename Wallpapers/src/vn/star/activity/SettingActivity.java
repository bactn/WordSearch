package vn.star.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import vn.amobi.util.Utils;
import vn.star.wallpapers.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class SettingActivity extends Activity implements OnClickListener {
	private String imageUrls;
	private ArrayList<String> image_urls;
	private DisplayImageOptions options;
	TextView txt_image_url;
	Button btn_favorite, btn_download;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);

		Intent intent = getIntent();
		image_urls = intent.getStringArrayListExtra("ARRAY_IMAGE_URL");

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
		btn_favorite = (Button) findViewById(R.id.btn_set_favorite);
		btn_favorite.setOnClickListener(this);
		btn_download = (Button) findViewById(R.id.btn_download);
		btn_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String url_path = image_urls.get(0);
						URL url = null;
						try {
							url = new URL(url_path);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String filePath = Environment
								.getExternalStorageDirectory().toString();
						// File dir = new File(filePath.getAbsolutePath());
						// dir.mkdir();
						new File(filePath + "/bbb/img").mkdirs();
						File image_name = new File(filePath
								+ "/bbb/img/bbbb.jpg");
						try {

							InputStream input = url.openStream();
							Bitmap bmp = BitmapFactory.decodeStream(input);
							OutputStream output = new FileOutputStream(
									image_name);
							bmp.compress(Bitmap.CompressFormat.JPEG, 100,
									output);
							Log.e("OutPut Stream", "" + output);
							output.flush();
							output.close();
							MediaStore.Images.Media.insertImage(
									getContentResolver(),
									image_name.getAbsolutePath(),
									image_name.getName(), image_name.getName());

						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
		});
		// AmobiAdView adview = (AmobiAdView)
		// findViewById(R.id.setting_menu_adView);
		// if (adview != null) {
		// adview.loadAd(WidgetSize.SMALL);
		//
		// }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// if (v.getId() == R.id.btn_set_favorite) {
		//
		// if (btn_favorite.isPressed()) {
		// btn_favorite
		// .setBackgroundResource(R.drawable.ic_action_favorite);
		// btn_favorite.setPressed(false);
		// } else {
		// btn_favorite
		// .setBackgroundResource(R.drawable.ic_action_favorite_light);
		// btn_favorite.setPressed(true);
		// }
		// }
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
			return image_urls.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return image_urls.size();
		}

		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (converView == null) {
				converView = inflate.inflate(R.layout.item_list_setting,
						parent, false);
			}
			ImageView image = (ImageView) converView
					.findViewById(R.id.item_list_image);
			txt_image_url = (TextView) converView
					.findViewById(R.id.item_list_url_value);
			if (image_urls != null) {
				txt_image_url.setText(image_urls.get(position));
			}

			ImageLoader.getInstance().displayImage(image_urls.get(position),
					image, options, new SimpleImageLoadingListener() {
					});
			return converView;
		}

	}

}
