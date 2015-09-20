package vn.star.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import vn.star.utils.Constants;
import vn.star.utils.Setting;
import vn.star.wallpapers.BookMarkFragment;
import vn.star.wallpapers.CategoriesFragment;
import vn.star.wallpapers.R;
import vn.star.wallpapers.SdCardFragment;
import vn.star.wallpapers.Util;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MainActivity extends Activity implements Util, Setting {
	GridView gridView;

	BaseAdapter adapter;
	static int numberOfColumns;
	static int heightOfGrid;
	private boolean thumbnailSelection[];
	Menu menu;
	SdCardFragment sdCardFragment;
	static int menuItem = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createGridView();
		new ReadJSONFeedTask()
				.execute("http://content.amobi.vn/detectface/renderphoto/render-photo?category=2");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		createGridView();
		if (menu != null) {
			menu.getItem(4).setEnabled(false);
			menu.getItem(4).setIcon(R.drawable.ic_action_play_light);
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menus) {
		this.menu = menus;
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_action, menu);
		menu.getItem(4).setEnabled(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int CONTENT_VIEW_ID = 3;
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		FrameLayout frame = new FrameLayout(this);
		frame.setId(CONTENT_VIEW_ID);
		setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		int id = item.getItemId();
		switch (id) {
		case R.id.action_highlight:
			menuItem = 0;
			createGridView();
			break;
		case R.id.action_category:
			menuItem = 1;
			Fragment newFragment = new CategoriesFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(CONTENT_VIEW_ID, newFragment).commit();
			return true;
		case R.id.action_sdcard:
			menuItem = 2;
			sdCardFragment = new SdCardFragment();
			FragmentTransaction sdcardTrans = getFragmentManager()
					.beginTransaction();
			sdcardTrans.add(CONTENT_VIEW_ID, sdCardFragment).commit();
			return true;
		case R.id.action_bookmark:
			menuItem = 3;
			Fragment bookmarkFragment = new BookMarkFragment();
			FragmentTransaction bmTransation = getFragmentManager()
					.beginTransaction();
			bmTransation.add(CONTENT_VIEW_ID, bookmarkFragment).commit();
			return true;
		case R.id.action_select:
			if (menuItem == 2) {
				sdCardFragment.startSettings();
				break;
			} else if (menuItem == 0) {
				startSettings();
				break;
			}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/* set Adapter for GridView in firstView */
	public void createGridView() {
		setContentView(R.layout.activity_main);
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		// Button btn = (Button) findViewById(R.id.btnchose);

		int gridViewWidth = getResources().getDimensionPixelSize(
				R.dimen.grip_view_width_size);
		int gridViewHeight = getResources().getDimensionPixelSize(
				R.dimen.grip_view_width_size);
		numberOfColumns = display.getWidth() / gridViewWidth;
		heightOfGrid = display.getHeight() / 3;

		adapter = new ImageAdapter(getBaseContext());
		// Set up an array of the Thumbnail Image ID column we want
		// Create the cursor pointing to the SDCard
		gridView = (GridView) findViewById(R.id.grid_main);
		gridView.setNumColumns(numberOfColumns + 1);
		gridView.setAdapter(adapter);
		thumbnailSelection = new boolean[gridView.getCount()];

	}

	public String readJSONFeed(String URL) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				Log.e("JSON", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				Log.i("JSON",
						"Number of surveys in feed: " + jsonArray.length());
				Log.e("JSONARRAY", "JSONARRAY" + jsonArray);

				// ---print out the content of the json feed---
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Log.e("JSONURL", "json url" + jsonArray.getString(i));

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* create adapter class for gridview */

	private class ImageAdapter extends BaseAdapter {

		public final String[] IMAGES = Constants.IMAGES;
		private DisplayImageOptions options;
		private LayoutInflater inflater;
		Context mContext;
		ImageView imageView;

		public ImageAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}

		// public void startSetting(int position) { // setting Wallpaper
		//
		// Intent intent = new Intent(mContext.getApplicationContext(),
		// SettingActivity.class);
		// intent.putExtra("POSITION", position);
		// intent.putExtra("IMAGE_URL", Constants.IMAGES[position]);
		// mContext.startActivity(intent);
		// }

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
			final int count_check_item = 0;
			final ViewHolder holder;
			// if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_gird_main, parent,
					false);
			imageView = (ImageView) convertView
					.findViewById(R.id.item_image_grid_main);
			imageView.getLayoutParams().height = heightOfGrid;
			holder.selectItem = (CheckBox) convertView
					.findViewById(R.id.main_item_checkBox);
			convertView.setTag(position);

			// }
			// else {
			// holder = (ViewHolder) convertView.getTag();
			// }
			holder.selectItem.setId(position);
			holder.selectItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					if (thumbnailSelection[id]) {
						holder.numberItemChecked--;
						holder.setCountChecked(holder.numberItemChecked);
						Log.e("numberItemChecked",
								"numberItemChecked" + holder.getCountChecked());

						cb.setChecked(false);
						thumbnailSelection[id] = false;
						menu.getItem(4).setEnabled(false);
						menu.getItem(4)
								.setIcon(R.drawable.ic_action_play_light);
					} else {
						holder.numberItemChecked++;
						holder.setCountChecked(holder.numberItemChecked);
						Log.e("numberItemChecked",
								"numberItemChecked" + holder.getCountChecked());
						cb.setChecked(true);
						menu.getItem(4).setEnabled(true);
						menu.getItem(4).setIcon(R.drawable.ic_action_play);
						thumbnailSelection[id] = true;
					}
				}
			});

			holder.selectItem.setTag(position);
			ImageLoader.getInstance().displayImage(IMAGES[position], imageView,
					options, new SimpleImageLoadingListener() {
					});
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// startSettings();
					Toast.makeText(mContext, "please check your image",
							Toast.LENGTH_LONG).show();
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		// imageView;
		CheckBox selectItem;
		int id;
		int numberItemChecked;

		int getCountChecked() {
			return numberItemChecked;
		}

		void setCountChecked(int count) {
			this.numberItemChecked = count;
		}
	}

	@Override
	public Cursor getCursor() {

		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_ADDED;
		Cursor imagecursor = this.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");
		return imagecursor;
	}

	@Override
	public int getNumberOfColumns() {
		// TODO Auto-generated method stub
		return numberOfColumns + 1;
	}

	@Override
	public int getHeightOfGridItem() {
		// TODO Auto-generated method stub
		return heightOfGrid;
	}

	public Menu getMenu() {
		return menu;
	}

	@Override
	public void startSettings() {
		ArrayList<String> image_url = new ArrayList<String>();
		String url;
		int len = thumbnailSelection.length;
		Log.e("count", "count" + len);
		for (int i = 0; i < len; i++) {
			if (thumbnailSelection[i]) {
				url = Constants.IMAGES[i];
				Log.i("url", "" + url);
				image_url.add(url);
				Toast.makeText(getApplicationContext(), "Item Clicked: " + i,
						Toast.LENGTH_SHORT).show();
			}
		}

		Intent intent = new Intent(this, SettingActivity.class);
		intent.putExtra("ARRAY_IMAGE_URL", image_url);
		startActivity(intent);

	}

	/*
	 * giải thuật sắp xếp nổi bọt
	 * 
	 * Bước 1: i=0; //Phần tử đầu tiên
	 * 
	 * Bước 2: Lần lượt so sánh phần tử đầu tiên với các phần tử sau đó. Nếu
	 * phần tử sau nhỏ hơn phần tử trước thì đổi chỗ.
	 * 
	 * Bước 3: i=i+1
	 * 
	 * Bước 4:
	 * 
	 * Nếu i < n, quay lại Bước 2.
	 * 
	 * Ngược lại, dừng, dãy đã cho đã sắp xếp đúng vị trí.
	 * 
	 * /************************************ cách khai báo C# int i,j,n; // khai
	 * báo các biến i,j,n theo dạng số nguyên int temp; // biến trung gian để
	 * đổi chỗ giữa 2 phần tử int A[n]; // khai bảo mảng A cần sắp xếp gồm n
	 * phần tử for(i=0; i< n-1; i++){ // Bắt đầu từ phần tử đầu tiên for(j=i+1;
	 * j< n; j++){ // Lăp so sánh phần tử thứ 2 if(A[j] < A[i]){ // so sánh phần
	 * tử đầu tiên với phần tử thứ 2. Nếu sau < đầu tiên temp = A[i]; // gán giá
	 * trị trung gian = phần tử đầu tiên A[i] = A[j]; // gán giá trị đầu tiền =
	 * giá trị sau (hay là đổi chỗ) A[j] = temp; // gán lại giá trị thứ 2 bằng
	 * temp(bằng luôn phần tử đầu tiên) // quay trở lại vòng lặp for(j=i+1; j<
	 * n; j++) } } }
	 */

}
