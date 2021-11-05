package com.example.farmmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity
{
	public static final String TASK_ID_KEY = "task_id";

	private ImageView imgTask;
	private TextView txtTaskName, txtDeadlineDate, txtDescription, txtArea;
	private Button btnCompleteTask, btnEditTask, btnDeleteTask;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		initViews();

		Intent intent = getIntent();
		if(intent != null)
		{
			int taskId = intent.getIntExtra(TASK_ID_KEY, -1);
			if(taskId != -1)
			{
				Task currentTask = Utils.getInstance(this).getTaskById(taskId);
				if(currentTask != null)
				{
					setData(currentTask);
					setButtonListeners(currentTask);
				}
			}
		}
	}

	private void initViews()
	{
		imgTask = findViewById(R.id.img_task_activity);
		txtTaskName = findViewById(R.id.txt_task_name1);
		txtDeadlineDate = findViewById(R.id.txt_deadline_date);
		txtDescription = findViewById(R.id.txt_description1);
		txtArea = findViewById(R.id.txt_area1);
		btnCompleteTask = findViewById(R.id.btn_complete_task);
		btnEditTask = findViewById(R.id.btn_edit_task);
		btnDeleteTask = findViewById(R.id.btn_delete_task);
	}

	private void setData(Task currentTask)
	{
		imgTask.setImageResource(currentTask.getResourceImageId());
		txtTaskName.setText(currentTask.getName());
		txtDeadlineDate.setText(currentTask.getDeadlineDateStr());
		txtDescription.setText(currentTask.getDescription());
		String areaName = currentTask.getAreaName();
		txtArea.setText(areaName != null ? "Area: " + areaName : "");
	}

	private void setButtonListeners(Task currentTask)
	{
		btnCompleteTask.setEnabled(!currentTask.isCompleted());
		btnCompleteTask.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(Utils.getInstance(TaskActivity.this).setTaskCompleted(currentTask))
				{
					Toast.makeText(TaskActivity.this, "Task " + currentTask.getName() + " completed!", Toast.LENGTH_SHORT).show();
					btnCompleteTask.setEnabled(false);
				}
			}
		});

		btnEditTask.setOnClickListener(v ->
				new TaskDialog(this, TaskActivity.this, null, currentTask.getId()));


		btnDeleteTask.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(Utils.getInstance(TaskActivity.this).deleteTask(currentTask))
				{
					Toast.makeText(TaskActivity.this, "Deleted task " + currentTask.getName(), Toast.LENGTH_SHORT).show();
					onBackPressed();
				}
			}
		});
	}
}
