package com.dahl.brendan.wordsearch.view;

import com.dahl.brendan.wordsearch.view.WordSearchActivity.DialogHighScoresLocalShowListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {
	Button btn_newGame;
	Button btn_sizeofGrid;
	Button btn_highScore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordlist_text_view);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final Intent intent_new = new Intent(this, WordSearchActivity.class);
		final Intent intent_size = new Intent(this, WordSearchPreferencesSize.class);
		final Intent intent_category = new Intent(this, WordSearchPreferencesCategory.class);
		
		btn_newGame = (Button) findViewById(R.id.btn_newGame);
		btn_newGame.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(intent_category);
			}
		});
			
		btn_sizeofGrid = (Button) findViewById(R.id.btn_size);
		btn_sizeofGrid.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(intent_size); 
			}
		});
		
		
		btn_highScore = (Button) findViewById(R.id.btn_HighScore);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
}
