//    This file is part of Open WordSearch.
//
//    Open WordSearch is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Open WordSearch is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Open WordSearch.  If not, see <http://www.gnu.org/licenses/>.
//
//	  Copyright 2009, 2010 Brendan Dahl <dahl.brendan@brendandahl.com>
//	  	http://www.brendandahl.com

package com.dahl.brendan.wordsearch.view;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class WordSearchPreferencesCategory extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencescategory);
		this.updateCategorySummary();
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	private void updateCategorySummary() {
		String categorySum = this.getString(R.string.prefs_category_summary);
		Preference p = this.findPreference(this
				.getString(R.string.prefs_category));
		String category = p.getSharedPreferences().getString(p.getKey(),
				getString(R.string.RANDOM));
		List<String> catValues = Arrays.asList(this.getResources()
				.getStringArray(R.array.categories_list_values));
		String[] catLabels = this.getResources().getStringArray(
				R.array.categories_list_labels);
		int index = catValues.indexOf(category);
		categorySum = categorySum.replaceAll("%replaceme", catLabels[index]);
		p.setSummary(categorySum);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.preferences_options, menu);
		menu.findItem(R.id.menu_quit).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_quit:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (this.getString(R.string.prefs_category).equals(key)) {
			Log.e("category", "click to choose category");
			this.updateCategorySummary();
			final Intent intent_new = new Intent(this, WordSearchActivity.class);
			startActivity(intent_new);
		}

	}
}
