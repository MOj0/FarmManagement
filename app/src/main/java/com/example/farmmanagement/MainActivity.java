package com.example.farmmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity
{
	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	private EditText edtTaskName;
	private Button btnConfirmTask, btnCancelTask;


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
		if(item.getItemId() == R.id.item_add_task)
		{
			//TODO .............................
//			startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
		}
//		else if(item.getItemId() == R.id.itemShowCompletedTasks)
//		{
//			boolean notShowCompletedTasks = !Utils.isShowCompletedTasks();
//			item.setTitle((notShowCompletedTasks ? "Hide" : "Show") + " completed tasks");
//			Utils.setShowCompletedTasks(notShowCompletedTasks);
//		}
		return true;
	}


	private void createNewTaskDialog()
	{
		dialogBuilder = new AlertDialog.Builder(this);
		final View contactPopupView = getLayoutInflater().inflate(R.layout.add_task, null);

//		edtAreaName = contactPopupView.findViewById(R.id.edtAreaName);
//		btnConfirmArea = contactPopupView.findViewById(R.id.btnConfirmArea);
//		btnCancelArea = contactPopupView.findViewById(R.id.btnCancelArea);

		dialogBuilder.setView(contactPopupView);
		dialog = dialogBuilder.create();
		dialog.show();
	}
}