package vn.star.wallpapers;

import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;

public class DemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		
		Gson gson = new Gson();
		
	}

}
