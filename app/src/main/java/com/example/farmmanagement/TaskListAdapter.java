package com.example.farmmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class TaskListAdapter extends ArrayAdapter<String>
{
	private final int mResource;
	private List<Integer> completed;

	public TaskListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, List<Integer> completed)
	{
		super(context, resource, objects);
		mResource = resource;
		this.completed = completed;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Get the data item for this position
		String taskName = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if(convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
		}

		TextView txtTaskName = (TextView) convertView.findViewById(android.R.id.text1);
		txtTaskName.setText(taskName);

		// Display completed tasks or not
		if(completed.contains(position))
		{
			convertView.setVisibility(Utils.isShowCompletedTasks() ? View.VISIBLE : View.GONE);
			convertView.setBackgroundColor(completed.contains(position) ? 0xFF00FF75 : 0x00000000);
		}

		// Return the completed view to render on screen
		return convertView;
	}

	public void setCompleted(List<Integer> completed)
	{
		this.completed = completed;
	}
}