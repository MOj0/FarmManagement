package com.example.farmmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
	private Fragment selectedFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		ArrayList<Area> areas = Utils.getInstance(MainActivity.this).getAreas();
		if(areas == null || areas.isEmpty()) // Requires at least 1 area
		{
			Intent intent = new Intent(MainActivity.this, MapsActivity.class);
			startActivity(intent);
		}

		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		BottomNavigationView navView = findViewById(R.id.nav_view);
		navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
		{
			@SuppressLint("NonConstantResourceId")
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item)
			{
				switch(item.getItemId())
				{
					case R.id.navigation_map:
						selectedFragment = new MapsFragment();
						break;
					case R.id.navigation_dashboard:
						selectedFragment = new DashboardFragment();
						break;
					case R.id.navigation_calendar:
						selectedFragment = new CalendarFragment();
						break;
				}

				assert selectedFragment != null;
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
				return true;
			}
		});

		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.task_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item)
	{
		if(item.getItemId() == R.id.item_add_task)
		{
			new TaskDialog(this, MainActivity.this, selectedFragment, -1, null, null);
		}
		else if(item.getItemId() == R.id.item_show_completed_tasks)
		{
			boolean notShowCompletedTasks = !Utils.isShowCompletedTasks();
			item.setTitle((notShowCompletedTasks ? "Hide" : "Show") + " completed tasks");
			Utils.setShowCompletedTasks(notShowCompletedTasks);
			selectedFragment.onResume(); // used to call notifiedDataSetChanged()
		}
		return true;
	}
}