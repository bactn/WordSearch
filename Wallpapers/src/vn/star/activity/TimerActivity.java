package vn.star.activity;

import vn.star.wallpapers.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

public class TimerActivity extends Activity {
	NumberPicker[] numberPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		
		Button btnDialog = (Button) findViewById(R.id.btnDialog);
		
		btnDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				show();
			}
		});
	}
	private void show(){
		Dialog dialog = new Dialog(TimerActivity.this);
		dialog.setTitle("Set Schedule for Wallpaper");
		dialog.setContentView(R.layout.activity_timer);

		numberPicker = new NumberPicker[3];

		numberPicker[0] = (NumberPicker) dialog.findViewById(R.id.np_day);
		numberPicker[1] = (NumberPicker) dialog.findViewById(R.id.np_hour);
		numberPicker[2] = (NumberPicker) dialog.findViewById(R.id.np_minute);

		numberPicker[0].setMinValue(0);
		numberPicker[0].setMaxValue(10);
//		numberPicker[0].setWrapSelectorWheel(false);

		numberPicker[1].setMinValue(0);
		numberPicker[1].setMaxValue(24);
//		numberPicker[1].setWrapSelectorWheel(false);

		numberPicker[2].setMinValue(0);
		numberPicker[2].setMaxValue(1000);
//		numberPicker[2].setWrapSelectorWheel(false);
		dialog.show();

	}
}
