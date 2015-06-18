package vn.star.wallpapers;

import java.util.ArrayList;

import vn.star.activity.SettingActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class SdCardFragment extends Fragment {
	private CursorInterface cursorInterface;
	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	private ImageAdapter imageAdapter;
	private BaseAdapter adapter;
	GridView grid;
	private Cursor cursor;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			cursorInterface = (CursorInterface) activity;
		} catch (Exception e) {
			throw new RuntimeException(activity.getClass().getName()
					+ " must implement " + CursorInterface.class.getName());

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_sdcard, container, false);
		super.onCreate(savedInstanceState);
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(getActivity()));
		imageUrls = new ArrayList<String>();
		cursor = cursorInterface.getCursor();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int columIndex = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			imageUrls.add(cursor.getString(columIndex));
			Log.i("imageURL", "ImageURL" + cursor.getString(columIndex));
		}

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		adapter = new ImageAdapter(getActivity().getBaseContext(), imageUrls);
		grid = (GridView) view.findViewById(R.id.grid_sdcard);
		grid.setAdapter(adapter);

		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		grid.setNumColumns(cursorInterface.getNumberOfColumns());
		Toast.makeText(this.getActivity().getApplicationContext(),
				"width: " + grid.getWidth() + "height: " + grid.getHeight(),
				Toast.LENGTH_LONG).show();
		return view;
	}

	public void btnChoosePhotosClick(View v) {

		ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
	}

	/*
	 * private void startImageGalleryActivity(int position) { Intent intent =
	 * new Intent(this, ImagePagerActivity.class); intent.putExtra(Extra.IMAGES,
	 * imageUrls); intent.putExtra(Extra.IMAGE_POSITION, position);
	 * startActivity(intent); }
	 */

	public class ImageAdapter extends BaseAdapter {

		ArrayList<String> mList;
		LayoutInflater mInflater;
		Context mContext;
		SparseBooleanArray mSparseBooleanArray;

		public ImageAdapter(Context localContext, ArrayList<String> imageList) {
			// TODO Auto-generated constructor stub
			mContext = localContext;
			mSparseBooleanArray = new SparseBooleanArray();
			mList = new ArrayList<String>();
			mInflater = LayoutInflater.from(localContext);
			this.mList = imageList;

		}

		public ArrayList<String> getCheckedItems() {
			ArrayList<String> mTempArry = new ArrayList<String>();

			for (int i = 0; i < mList.size(); i++) {
				if (mSparseBooleanArray.get(i)) {
					mTempArry.add(mList.get(i));
				}
			}

			return mTempArry;
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view = convertView;
			if (view == null) {
				view = mInflater.inflate(R.layout.item_grid_sdcard, parent,
						false);
			}
			CheckBox mCheckBox = (CheckBox) view
					.findViewById(R.id.select_item_sdcard);

			final ImageView image = (ImageView) view
					.findViewById(R.id.item_img_grid_sdcard);
			image.getLayoutParams().height = cursorInterface.getHeightOfGrid();
			ImageLoader.getInstance().displayImage(
					"file://" + imageUrls.get(position), image, options,
					new SimpleImageLoadingListener() {
					});
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity()
							.getApplicationContext(), SettingActivity.class);
					intent.putExtra("POSITION", position);
					intent.putExtra("IMAGE_URL", imageUrls.get(position));
					startActivity(intent);
				}
			});
			return view;
		}

	}
}