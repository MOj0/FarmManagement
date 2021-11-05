package com.example.farmmanagement;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Utils
{
	private static final String TASK_KEY = "tasks";

	private static int taskId;
	private static boolean showCompletedTasks;

	private static Utils instance;
	private SharedPreferences sharedPreferences;

	private Utils(Context context)
	{
		sharedPreferences = context.getSharedPreferences("alternate_db", Context.MODE_PRIVATE);

		taskId = 0;
		showCompletedTasks = false;

		if(getTasks() == null)
		{
			setTasks(new ArrayList<>());
		}

		taskId = getTasks().stream().map(Task::getId).max(Comparator.naturalOrder()).orElse(-1) + 1;
	}

	public static Utils getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new Utils(context);
		}
		return instance;
	}

	public static int getTaskId()
	{
		return taskId++;
	}

	public static boolean isShowCompletedTasks()
	{
		return showCompletedTasks;
	}

	public static void setShowCompletedTasks(boolean showCompletedTasks)
	{
		Utils.showCompletedTasks = showCompletedTasks;
	}

	public ArrayList<Task> getTasks()
	{
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<Task>>()
		{
		}.getType();
		return gson.fromJson(sharedPreferences.getString(TASK_KEY, null), type);
	}

	public void setTasks(ArrayList<Task> tasks)
	{
		Gson gson = new Gson();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(TASK_KEY, gson.toJson(tasks));
		editor.apply();
	}

	public boolean addTask(Task task)
	{
		ArrayList<Task> tasks = getTasks();
		if(tasks != null && tasks.add(task))
		{
			Gson gson = new Gson();
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(TASK_KEY, gson.toJson(tasks));
			editor.apply();
			return true;
		}
		return false;
	}

	public boolean deleteTask(Task task)
	{
		ArrayList<Task> tasks = getTasks();
		if(tasks != null && tasks.removeIf(t -> t.getId() == task.getId()))
		{
			Gson gson = new Gson();
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(TASK_KEY, gson.toJson(tasks));
			editor.apply();
			return true;
		}
		return false;
	}

	public boolean editTask(int taskId, Task task)
	{
		ArrayList<Task> tasks = getTasks();
		if(tasks != null)
		{
			int index = getIndexById(taskId);
			tasks.set(index, task);

			Gson gson = new Gson();
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(TASK_KEY, gson.toJson(tasks));
			editor.apply();
			return true;
		}
		return false;
	}

	public boolean setTaskCompleted(Task task)
	{
		ArrayList<Task> tasks = getTasks();
		if(tasks != null)
		{
			// Using forEach, but should always only be 1
			tasks.stream().filter(t -> t.getId() == task.getId()).forEach(t -> t.setCompleted(true));
			Gson gson = new Gson();
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(TASK_KEY, gson.toJson(tasks));
			editor.apply();
			return true;
		}
		return false;
	}

	private int getIndexById(final int id)
	{
		ArrayList<Task> tasks = getTasks();
		return IntStream.range(0, tasks.size()).filter(i -> tasks.get(i).getId() == id).findFirst().orElse(-1);
	}

	public Task getTaskById(final int id)
	{
		return getTasks().stream().filter(task -> task.getId() == id).findFirst().orElse(null);
	}
}
