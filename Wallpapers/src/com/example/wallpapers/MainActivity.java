package com.example.wallpapers;

import android.R.xml;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MainActivity extends Activity {
	GridView gridView;
	BaseAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new ImageAdapter(getBaseContext());
		// Set up an array of the Thumbnail Image ID column we want
		// Create the cursor pointing to the SDCard
		gridView = (GridView) findViewById(R.id.grid);
		gridView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.activity_main_action, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		FrameLayout frame = new FrameLayout(this);
		// frame.setId(CONTENT_VIEW_ID);
		setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		int id = item.getItemId();
		switch (id) {
		case R.id.action_highlight:
			setContentView(R.layout.activity_main);
			Toast.makeText(this, "highLight", Toast.LENGTH_LONG).show();
			gridView = (GridView) findViewById(R.id.grid);
			gridView.setAdapter(adapter);
			break;
		case R.id.action_subject:
			Fragment newFragment = new CatogoriesFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			// ft.add(CONTENT_VIEW_ID, newFragment).commit();
			break;
		case R.id.action_bookmark:
			Fragment bookmaFragment = new BookMarkFragment();
			FragmentTransaction bmTransation = getFragmentManager()
					.beginTransaction();
			// bmTransation.add(CONTENT_VIEW_ID, bookmaFragment).commit();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private static class ImageAdapter extends BaseAdapter {
		public final String[] IMAGES = Constants.IMAGES;
		private DisplayImageOptions options;
		private LayoutInflater inflater;

		public ImageAdapter(Context context) {
			// TODO Auto-generated constructor stub
			ImageLoader.getInstance().init(
					ImageLoaderConfiguration.createDefault(context));
			inflater = LayoutInflater.from(context);

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return IMAGES.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.item_gird_main, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view
						.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			ImageLoader.getInstance().displayImage(IMAGES[position],
					holder.imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {

						@Override
						public void onProgressUpdate(String arg0, View arg1,
								int current, int total) {
							// TODO Auto-generated method stub
							holder.progressBar.setProgress(Math.round(100.0f
									* current / total));
						}
					});
			return view;
		}

	}

	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
}
