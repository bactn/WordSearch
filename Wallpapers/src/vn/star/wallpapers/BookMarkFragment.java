package vn.star.wallpapers;

import java.util.ArrayList;
import java.util.List;

import vn.star.activity.SettingActivity;
import vn.star.utils.Bookmark;
import vn.star.utils.DatabaseHandler;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

;
public class BookMarkFragment extends Fragment {
	Util utils;
	ArrayList<String> img_url;
	GridView gridView;
	Menu menu;
	private DisplayImageOptions options;
	ImageAdapter adapter;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			utils = (Util) activity;
		} catch (Exception e) {
			throw new RuntimeException(activity.getClass().getName()
					+ " must implement " + Util.class.getName());

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_book_mark, container,
				false);
		super.onCreate(savedInstanceState);
		img_url = new ArrayList<String>();
		DatabaseHandler db = new DatabaseHandler(getActivity()
				.getApplicationContext());
		List<Bookmark> bookmarks = db.getAllBookmarks();
		int i = 0;
		for (Bookmark bm : bookmarks) {
			img_url.add(bm.getName());
			Log.e("img url", "img_url" + img_url.get(i));
			i++;
		}
		Toast.makeText(getActivity().getApplicationContext(),
				"url" + img_url.get(0), Toast.LENGTH_LONG).show();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		gridView = (GridView)view.findViewById(R.id.grid_bookmark);
		adapter = new ImageAdapter(this.getActivity().getApplicationContext(), img_url);
		gridView.setAdapter(adapter);
		
		return view;
	}

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
			return img_url.size();
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
				view = mInflater.inflate(R.layout.item_grid_bookmark, parent,
						false);
			}
			ViewHolder holder = new ViewHolder();
			holder.mCheckBox = (CheckBox) view
					.findViewById(R.id.select_item_bookmark);

			final ImageView image = (ImageView) view
					.findViewById(R.id.item_img_grid_bookmark);
			image.getLayoutParams().height = utils.getHeightOfGridItem();
			ImageLoader.getInstance().displayImage(img_url.get(position),
					image, options, new SimpleImageLoadingListener() {
					});
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity()
							.getApplicationContext(), SettingActivity.class);
					intent.putExtra("POSITION", position);
					intent.putExtra("ARRAY_IMAGE_URL", img_url.get(position));
					startActivity(intent);
				}
			});
			// holder.mCheckBox.setId(position);
			holder.mCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// CheckBox cb = (CheckBox) v;
					// int id = cb.getId();
					// if (thumbnailSelection[id]) {
					// cb.setChecked(false);
					// thumbnailSelection[id] = false;
					// menu.getItem(4).setEnabled(false);
					// } else {
					// cb.setChecked(true);
					menu.getItem(4).setEnabled(true);
					menu.getItem(4).setIcon(R.drawable.ic_action_play);
					// thumbnailSelection[id] = true;
					// }
				}
			});
			return view;
		}

	}

	class ViewHolder {
		// imageView;
		CheckBox mCheckBox;
		int id;
	}
}
