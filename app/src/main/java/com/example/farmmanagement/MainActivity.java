package com.example.farmmanagement;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
	private Fragment selectedFragment;

	// Dialog for new task
	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	private EditText taskName, taskDesc;
	private Button btnShowDatePicker;
	private FloatingActionButton fabTaskConfirm;
	private TextView txtDate;
	private Spinner spnAreaName;
	private Calendar deadlineDate;


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
			new TaskDialog(this, MainActivity.this, selectedFragment, -1);
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