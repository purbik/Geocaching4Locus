package com.arcao.geocaching4locus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		return true;
	}

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_header, target);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static Intent createIntent(Context context) {
		return new Intent(context, SettingsActivity.class);
	}

	public static Intent createIntent(Context context, String preferenceFragment) {
		return createIntent(context).putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT, preferenceFragment);
	}

	public static Intent createIntent(Context context, Class<?> preferenceFragment) {
		return createIntent(context, preferenceFragment.getName());
	}
}