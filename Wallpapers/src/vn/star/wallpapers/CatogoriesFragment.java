package vn.star.wallpapers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.star.wallpapers.R;

public class CatogoriesFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_catogories, container,
				false);
		 super.onCreate(savedInstanceState);
		return view;
	}
}
