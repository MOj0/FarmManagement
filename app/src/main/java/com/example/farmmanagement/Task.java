package com.example.farmmanagement;

import java.util.Calendar;

public class Task
{
	private int id;
	private String name, description, areaName;
	private Calendar deadlineDate;
	private boolean isExpanded, isCompleted;
	private int resourceImageId;

	public Task(String name, String description, Calendar deadlineDate, int resourceImageId)
	{
		this.id = Utils.getTaskId();
		this.name = name;
		this.description = description;
		this.deadlineDate = deadlineDate;
		this.resourceImageId = resourceImageId;
		this.isExpanded = false;
		this.isCompleted = false;
		this.areaName = null;
	}

	public Task(String name, String description, Calendar deadlineDate, int resourceImageId, String areaName)
	{
		this.id = Utils.getTaskId();
		this.name = name;
		this.description = description;
		this.deadlineDate = deadlineDate;
		this.resourceImageId = resourceImageId;
		this.areaName = areaName;
		this.isExpanded = false;
		this.isCompleted = false;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAreaName()
	{
		return areaName;
	}

	public void setAreaName(String areaName)
	{
		this.areaName = areaName;
	}

	public Calendar getDeadlineDate()
	{
		return deadlineDate;
	}

	public void setDeadlineDate(Calendar deadlineDate)
	{
		this.deadlineDate = deadlineDate;
	}

	public boolean isExpanded()
	{
		return isExpanded;
	}

	public void setExpanded(boolean expanded)
	{
		isExpanded = expanded;
	}

	public boolean isCompleted()
	{
		return isCompleted;
	}

	public void setCompleted(boolean completed)
	{
		isCompleted = completed;
	}

	public int getResourceImageId()
	{
		return resourceImageId;
	}

	public void setResourceImageId(int resourceImageId)
	{
		this.resourceImageId = resourceImageId;
	}

	@Override
	public String toString()
	{
		return "Task{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", areaName='" + areaName + '\'' +
				", deadlineDate=" + deadlineDate +
				", isExpanded=" + isExpanded +
				", isCompleted=" + isCompleted +
				", resourceImageId=" + resourceImageId +
				'}';
	}

	public String getDeadlineDateStr()
	{
		return deadlineDate.get(Calendar.DAY_OF_MONTH) + "/" +
				(deadlineDate.get(Calendar.MONTH) + 1) + "/" +
				deadlineDate.get(Calendar.YEAR);
	}
}
