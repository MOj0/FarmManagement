package com.example.farmmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

public class TaskRecViewAdapter extends RecyclerView.Adapter<TaskRecViewAdapter.ViewHolder>
{
	private ArrayList<Task> tasks;
	private Context mContext;

	public TaskRecViewAdapter(Context mContext)
	{
		this.mContext = mContext;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position)
	{
		final Task currentTask = tasks.get(position);

		holder.cardView.setBackground(holder.defaultCardViewBackground);
		if(currentTask.isCompleted())
		{
			holder.parentRelLayout.setVisibility(Utils.isShowCompletedTasks() ? View.VISIBLE : View.GONE);
			holder.cardView.setBackgroundResource(R.color.light_green);
		}

		holder.txtTaskName.setText(currentTask.getName());
		holder.imgTask.setImageResource(currentTask.getResourceImageId());
		holder.txtDate.setText(currentTask.getDeadlineDateStr());
		holder.txtDescription.setText(currentTask.getDescription());

		boolean isTaskExpanded = currentTask.isExpanded();
		holder.expandedRelLayout.setVisibility(isTaskExpanded ? View.VISIBLE : View.GONE);

		// Clicked on the specific task
		holder.cardView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(currentTask.isExpanded())
				{
					Intent intent = new Intent(mContext, TaskActivity.class);
					intent.putExtra(TaskActivity.TASK_ID_KEY, tasks.get(holder.getAdapterPosition()).getId());
					mContext.startActivity(intent);
				}
				else
				{
					currentTask.setExpanded(true);
					notifyItemChanged(tasks.indexOf(currentTask));
				}
			}
		});

		TransitionManager.beginDelayedTransition(holder.cardView);
	}

	@Override
	public int getItemCount()
	{
		return tasks.size();
	}

	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasks = tasks;
		notifyDataSetChanged();
	}

	protected class ViewHolder extends RecyclerView.ViewHolder
	{
		private CardView cardView;
		private ImageView imgTask;
		private TextView txtTaskName, txtDescription, txtCollapse, txtDate;
		private RelativeLayout parentRelLayout, expandedRelLayout;
		private Drawable defaultCardViewBackground;

		@SuppressLint("ClickableViewAccessibility")
		public ViewHolder(@NonNull View itemView)
		{
			super(itemView);

			cardView = itemView.findViewById(R.id.card_view);
			imgTask = itemView.findViewById(R.id.img_task);
			txtTaskName = itemView.findViewById(R.id.txt_task_name);
			txtDescription = itemView.findViewById(R.id.txt_description);
			txtCollapse = itemView.findViewById(R.id.txt_collapse);
			txtDate = itemView.findViewById(R.id.txt_date1);
			parentRelLayout = itemView.findViewById(R.id.parent_rel_layout);
			expandedRelLayout = itemView.findViewById(R.id.expanded_rel_layout);

			defaultCardViewBackground = cardView.getBackground();

			txtCollapse.setOnClickListener(v -> {
				Task currentTask = tasks.get(getAdapterPosition());
				currentTask.setExpanded(false);
				notifyItemChanged(tasks.indexOf(currentTask));
				expandedRelLayout.setVisibility(View.GONE);
			});
		}
	}
}
