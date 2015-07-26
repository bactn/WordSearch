package vn.star.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import vn.star.utils.Constants;
import vn.star.utils.Setting;
import vn.star.wallpapers.BookMarkFragment;
import vn.star.wallpapers.CatogoriesFragment;
import vn.star.wallpapers.Util;
import vn.star.wallpapers.R;
import vn.star.wallpapers.SdCardFragment;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createGridView();

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
			createGridView();
			break;
		case R.id.action_subject:
			Toast.makeText(this, "action_subject", Toast.LENGTH_LONG).show();
			Fragment newFragment = new CatogoriesFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(CONTENT_VIEW_ID, newFragment).commit();
			return true;
		case R.id.action_sdcard:
			Fragment sdCardFragment = new SdCardFragment();
			FragmentTransaction sdcardTrans = getFragmentManager()
					.beginTransaction();
			sdcardTrans.add(CONTENT_VIEW_ID, sdCardFragment).commit();
			return true;
		case R.id.action_bookmark:
			Fragment bookmaFragment = new BookMarkFragment();
			FragmentTransaction bmTransation = getFragmentManager()
					.beginTransaction();
			bmTransation.add(4, bookmaFragment);
			return true;
		case R.id.action_select:
			startSettings();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void createGridView() { // set Adapter for GridView
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
		// int gridViewSpacing = getResources().getDimensionPixelSize(
		// R.dimen.grip_view_spacing);

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
					Toast.makeText(mContext, "334", Toast.LENGTH_LONG).show();
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
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
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

}
