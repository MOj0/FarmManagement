package com.example.farmmanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.service.controls.templates.ControlButton;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

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


	public TaskDialog(Activity activity, Context context, Fragment selectedFragment, int taskId)
	{
		this.activity = activity;
		this.context = context;
		this.selectedFragment = selectedFragment;
		this.taskId = taskId;

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
//				taskAreaName = editTask.getAreaName(); //TODO
			}
		}

		// Date stuff
		String currentDate = deadlineDate.get(Calendar.DAY_OF_MONTH) + "/" +
				(deadlineDate.get(Calendar.MONTH) + 1) + "/" +
				deadlineDate.get(Calendar.YEAR);
		txtDate.setText(currentDate);

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
				if(name.isEmpty())
				{
					taskName.setError("Missing data");
					taskName.setBackgroundResource(R.drawable.error_outline);
					return;
				}

				boolean editTask = taskId != -1;
				Task task = new Task(name, desc, deadlineDate, R.mipmap.farm);
				if(editTask)
				{
					task.setId(taskId); // Dangerous!
				}

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
