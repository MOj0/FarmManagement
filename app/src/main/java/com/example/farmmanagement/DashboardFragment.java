package com.example.farmmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment
{
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_dashboard, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
	{
//		RecyclerView recyclerView = view.findViewById(R.id.taskView);
//		TaskRecViewAdapter adapter = new TaskRecViewAdapter(getContext());
//		adapter.setTasks(Utils.getInstance(getContext()).getTasks());
//
//		recyclerView.setAdapter(adapter);
//		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
	}
}