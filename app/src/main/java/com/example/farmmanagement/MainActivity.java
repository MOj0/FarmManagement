package com.example.farmmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

//		ArrayList<Area> areas = Utils.getInstance(MainActivity.this).getAreas();
//		if(areas == null || areas.isEmpty()) // Requires at least 1 area
//		{
//			Intent intent = new Intent(MainActivity.this, MapsActivity.class);
//			startActivity(intent);
//		}

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
				Fragment selectedFragment = null;
				switch(item.getItemId())
				{
					case R.id.navigation_home:
						selectedFragment = new MapsFragment();
						break;
					case R.id.navigation_dashboard:
						selectedFragment = new DashboardFragment();
						break;
					case R.id.navigation_notifications:
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
//		if(item.getItemId() == R.id.itemAddTask)
//		{
//			startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
//		}
//		else if(item.getItemId() == R.id.itemShowCompletedTasks)
//		{
//			boolean notShowCompletedTasks = !Utils.isShowCompletedTasks();
//			item.setTitle((notShowCompletedTasks ? "Hide" : "Show") + " completed tasks");
//			Utils.setShowCompletedTasks(notShowCompletedTasks);
//		}
//		return true;

		return false;
	}
}