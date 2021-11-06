package com.example.farmmanagement;

import java.util.ArrayList;

public class Area
{
	private int polygonIndex;
	private String name;
	private ArrayList<Task> tasks;

	public Area(String name, ArrayList<Task> tasks)
	{
		this.polygonIndex = -1; // has to be set later
		this.name = name;
		this.tasks = tasks;
	}

	public int getPolygonIndex()
	{
		return polygonIndex;
	}

	public void setPolygonIndex(int polygonIndex)
	{
		this.polygonIndex = polygonIndex;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<Task> getTasks()
	{
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasks = tasks;
	}

	public boolean addTask(Task task)
	{
		return tasks.add(task);
	}
}
