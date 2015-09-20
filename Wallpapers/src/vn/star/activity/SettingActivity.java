package vn.star.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import vn.star.utils.Bookmark;
import vn.star.utils.DatabaseHandler;
import vn.star.wallpapers.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class SettingActivity extends Activity implements OnClickListener {
	public static ArrayList<String> image_urls;
	private DisplayImageOptions options;
	TextView txt_image_url;
	Button btn_set_time;
	// Dialog set time for Wallpaper
	NumberPicker[] numberPicker;
	Button btn_dialog_ok;
	Button btn_dialog_cancel;

	PendingIntent pendingIntent;

	static ListView listView; //
	static BaseAdapter adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);
		Intent intent = getIntent();
		image_urls = intent.getStringArrayListExtra("ARRAY_IMAGE_URL");
		if(image_urls.size() >0){
			Log.i("imgURL setting", "imgURl"+image_urls.get(0));
		}
		/* config ImageLoader Library */
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		/* list of image which be selected */
		listView = (ListView) findViewById(R.id.listView_setting);
		adapter = new ImageAdapter(this.getApplicationContext());

		listView.setAdapter(adapter);
		btn_set_time = (Button) findViewById(R.id.btn_set_time);

		// initialize for setting schedule
		Intent intent_schedule = new Intent(getBaseContext(),
				ScheduleReciver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, intent_schedule, 0);
	
		// click to download img to sdcard

		btn_set_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// show timer dialog for time setting
				// new setWallpaperTime().execute();
				/* show dialog to set Schedule for Wallpaper */
				showDialogSetTimer();

			}
		});

	}

	private void downloadImg(final int item) {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url_path = image_urls.get(item);
				URL url = null;
				try {
					url = new URL(url_path);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String filePath = Environment.getExternalStorageDirectory()
						.toString();
				new File(filePath + "/bbb/img").mkdirs();
				File image_name = new File(filePath + "/bbb/img/bbbb.jpg");
				try {

					InputStream input = url.openStream();
					Bitmap bmp = BitmapFactory.decodeStream(input);
					OutputStream output = new FileOutputStream(image_name);
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
					Log.e("OutPut Stream", "" + output);
					output.flush();
					output.close();
					MediaStore.Images.Media.insertImage(getContentResolver(),
							image_name.getAbsolutePath(), image_name.getName(),
							image_name.getName());

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

	private void showDialogSetTimer() { // show dialog set timer for wallpaper
		final Dialog dialog = new Dialog(SettingActivity.this);
		dialog.setTitle("Set Schedule for Wallpaper");
		dialog.setContentView(R.layout.activity_timer);

		numberPicker = new NumberPicker[3];

		numberPicker[0] = (NumberPicker) dialog.findViewById(R.id.np_day);
		numberPicker[1] = (NumberPicker) dialog.findViewById(R.id.np_hour);
		numberPicker[2] = (NumberPicker) dialog.findViewById(R.id.np_minute);

		btn_dialog_ok = (Button) dialog.findViewById(R.id.btn_ok);
		btn_dialog_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

		numberPicker[0].setMinValue(0);
		numberPicker[0].setMaxValue(365);
		// numberPicker[0].setWrapSelectorWheel(false);

		numberPicker[1].setMinValue(0);
		numberPicker[1].setMaxValue(24);
		// numberPicker[1].setWrapSelectorWheel(false);

		numberPicker[2].setMinValue(0);
		numberPicker[2].setMaxValue(60);
		// numberPicker[2].setWrapSelectorWheel(false);
		dialog.show();

		/* agree with set background for home screen */
		btn_dialog_ok.setOnClickListener(new OnClickListener() {

			@SuppressLint("ServiceCast")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlarmManager alarmMng = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				alarmMng.setRepeating(AlarmManager.ELAPSED_REALTIME,
						SystemClock.elapsedRealtime(),
						getMinuteTotal() * 1000 * 60, pendingIntent);
				dialog.cancel();
				Toast.makeText(
						getApplicationContext(),
						"day: " + numberPicker[0].getValue() + "hour: "
								+ numberPicker[1].getValue() + "minute: "
								+ numberPicker[2].getValue() + "total minutes "
								+ getMinuteTotal(), Toast.LENGTH_SHORT).show();

			}
		});

		btn_dialog_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

	}

	/* caculate total of minutes */
	private int getMinuteTotal() {
		int minutes = 0;
		minutes = 24 * 60 * numberPicker[0].getValue() + 60
				* numberPicker[1].getValue() + numberPicker[2].getValue();

		return minutes;
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
		Boolean isFavorited = false;

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
		public View getView(final int position, View converView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			CheckBox cb_favorite;
			CheckBox cb_download;
			Button btn_delete;

			// DatabaseHandler db = new
			// DatabaseHandler(getApplicationContext());
			// db.deleteAllBookmark(new Bookmark()); List<Bookmark> bookmarks =
			// db.getAllBookmarks(); for (Bookmark bm : bookmarks) {
			// String log = bm.getPhoneNumber() + bm.getName();
			// Log.d("bookmark", "bookmark" + log); }
			//
			if (converView == null) {
				converView = inflate.inflate(R.layout.item_list_setting,
						parent, false);
			}
			ImageView image = (ImageView) converView
					.findViewById(R.id.item_list_image);
			txt_image_url = (TextView) converView
					.findViewById(R.id.item_list_url_value);

			/* initialize favorite Button */
			cb_favorite = (CheckBox) converView.findViewById(R.id.btn_favorite);
			cb_favorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(),
							"click to button favorite", Toast.LENGTH_LONG)
							.show();
					/* query bookmark to sqlite */
					DatabaseHandler db = new DatabaseHandler(
							getApplicationContext());
					if (!isFavorited) {
						db.addBookmark(new Bookmark(image_urls.get(position)));
						isFavorited = true;
					} else {
						db.deleteBookmark(new Bookmark((db.getBookmarksCount())));
						isFavorited = false;
					}
				}
			});
			/* initialize delete button */
			btn_delete = (Button) converView.findViewById(R.id.btn_delete);
			btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					image_urls.remove(position);
					SettingActivity.adapter.notifyDataSetChanged();
				}
			});

			/* initialize download button */

			cb_download = (CheckBox) converView.findViewById(R.id.btn_download);
			cb_download.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					downloadImg(position);
					Toast.makeText(getApplicationContext(),
							"you download successed", Toast.LENGTH_LONG).show();
				}
			});

			if (image_urls != null) {
				txt_image_url.setText(image_urls.get(position));
			}
			ImageLoader.getInstance().displayImage(image_urls.get(position),
					image, options, new SimpleImageLoadingListener() {
					});
			return converView;
		}

	}

	public void setWallpaperTime() {

	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

//	class ScheduleReciver extends BroadcastReceiver {
//		// SettingActivity mainActivity;
//		@Override
//		public void onReceive(Context arg0, Intent arg1) {
//			// TODO Auto-generated method stub
//			new setWallpaperTime().execute();
//		}
//	}

}
