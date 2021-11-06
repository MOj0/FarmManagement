package com.example.farmmanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class TaskDialog implements DatePickerDialog.OnDateSetListener
{
	private Activity activity;
	private Context context;
	private Fragment selectedFragment;
	private int taskId;

	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	private EditText taskName, taskDesc;
	private Button btnShowDatePicker;
	private FloatingActionButton fabTaskConfirm;
	private TextView txtDate;
	private Spinner spnAreaName;
	private Calendar deadlineDate;

	private String taskAreaName;

	public TaskDialog(Activity activity, Context context, Fragment selectedFragment, int taskId, Date currentDate,
					  String selectedArea)
	{
		this.activity = activity;
		this.context = context;
		this.selectedFragment = selectedFragment;
		this.taskId = taskId;

		if(currentDate == null)
		{
			currentDate = new Date();
		}

		dialogBuilder = new AlertDialog.Builder(context);
		final View contactPopupView = activity.getLayoutInflater().inflate(R.layout.add_task, null);

		initializeViews(contactPopupView);

		// Fill out the fields, if the user edits a task
		if(taskId != -1)
		{
			Task editTask = Utils.getInstance(context).getTaskById(taskId);
			if(editTask != null)
			{
				taskName.setText(editTask.getName());
				taskDesc.setText(editTask.getDescription());
				txtDate.setText(editTask.getDeadlineDateStr());
				taskAreaName = editTask.getAreaName();
			}
		}

		// Date stuff
		deadlineDate.setTime(currentDate);
		String currentDateStr = deadlineDate.get(Calendar.DAY_OF_MONTH) + "/" +
				(deadlineDate.get(Calendar.MONTH) + 1) + "/" +
				deadlineDate.get(Calendar.YEAR);
		txtDate.setText("Deadline date: " + currentDateStr);

		// Area stuff
		ArrayList<Area> areas = Utils.getInstance(context).getAreas();
		ArrayList<String> areaNames = areas.stream().map(Area::getName).collect(Collectors.toCollection(ArrayList::new));
		areaNames.add(0, "");
		ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, areaNames);
		spnAreaName.setAdapter(areaAdapter);
		spnAreaName.setSelection(selectedArea == null ? 0 : areaNames.indexOf(selectedArea));

		setOnClickListeners();

		dialogBuilder.setView(contactPopupView);
		dialog = dialogBuilder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void initializeViews(final View contactPopupView)
	{
		taskName = contactPopupView.findViewById(R.id.edt_task_name);
		taskDesc = contactPopupView.findViewById(R.id.edt_task_desc);
		txtDate = contactPopupView.findViewById(R.id.txt_date);
		btnShowDatePicker = contactPopupView.findViewById(R.id.btn_show_date_picker);
		fabTaskConfirm = contactPopupView.findViewById(R.id.fab_task_confirm);
		spnAreaName = contactPopupView.findViewById(R.id.spn_area_name);
		deadlineDate = Calendar.getInstance();
	}

	private void setOnClickListeners()
	{
		btnShowDatePicker.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showDatePickerDialog();
			}
		});

		// Add/Edit task
		fabTaskConfirm.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String name = taskName.getText().toString();
				String desc = taskDesc.getText().toString();
				String areaName = (String) spnAreaName.getSelectedItem();
				if(name.isEmpty())
				{
					taskName.setError("Missing data");
					taskName.setBackgroundResource(R.drawable.error_outline);
					return;
				}

				// Add / Edit task
				boolean editTask = taskId != -1;
				Task task = new Task(name, desc, deadlineDate, R.mipmap.farm, areaName);
				if(editTask)
				{
					task.setId(taskId); // Dangerous!
				}

				ArrayList<Area> areas = Utils.getInstance(context).getAreas();
				areas.stream().filter(a -> a.getName().equals(areaName)).findFirst().ifPresent(
						area -> area.addTask(task));
				Utils.getInstance(context).setAreas(areas);

				boolean operationSuccessful = editTask ?
						Utils.getInstance(context).editTask(taskId, task) : Utils.getInstance(context).addTask(task);
				if(operationSuccessful)
				{
					Toast.makeText(context, (editTask ? "Edited task: " : "Created new task: ") + name, Toast.LENGTH_SHORT).show();
				}

				// Update UI
				if(selectedFragment != null)
				{
					selectedFragment.onResume(); // used to call notifiedDataSetChanged()
				}

				dialog.dismiss();

				if(editTask)
					activity.onBackPressed();
			}
		});
	}

	private void showDatePickerDialog()
	{
		DatePickerDialog datePickerDialog = new DatePickerDialog(
				context,
				this,
				Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
	{
		deadlineDate.set(year, month, dayOfMonth);
		String date = dayOfMonth + "/" + (month + 1) + "/" + year;
		txtDate.setText("Deadline date: " + date);
	}
}
