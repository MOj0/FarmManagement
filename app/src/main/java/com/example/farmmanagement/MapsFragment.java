package com.example.farmmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapsFragment extends Fragment
{
	private GoogleMap mMap;
	private ArrayList<Area> areas;
	private Area selectedArea;
	private TaskListAdapter taskListAdapter;
	private ListView listView;
	private ArrayList<Task> tasksInArea;

	private OnMapReadyCallback callback = new OnMapReadyCallback()
	{
		/**
		 * Manipulates the map once available.
		 * This callback is triggered when the map is ready to be used.
		 * This is where we can add markers or lines, add listeners or move the camera.
		 */
		@Override
		public void onMapReady(@NonNull GoogleMap googleMap)
		{
			mMap = googleMap;

			areas = Utils.getInstance(getContext()).getAreas();

			// Map areas to polygonOptions and add them to map
			ArrayList<PolygonOptions> polygonOptions = Utils.getInstance(getContext()).getPolygonOptions();
			ArrayList<Polygon> polygons = polygonOptions.stream().map(mMap::addPolygon).collect(Collectors.toCollection(ArrayList::new));

			// Set listeners for click events
			mMap.setOnPolygonClickListener(polygon ->
			{
				final int polygonIndex = polygons.indexOf(polygon);
				ArrayList<Task> tasks = Utils.getInstance(requireContext()).getTasks();

				selectedArea = areas.stream().filter(a -> a.getPolygonIndex() == polygonIndex).findFirst().orElse(null);
				assert selectedArea != null;
				tasksInArea = tasks.stream().filter(t -> t.getAreaName().equals(selectedArea.getName())).collect(Collectors.toCollection(ArrayList::new));
				ArrayList<String> taskNames = tasksInArea.stream().map(Task::getName).collect(Collectors.toCollection(ArrayList::new));
				List<Integer> indexesCompleted = IntStream.range(0, tasksInArea.size()).filter(i -> tasksInArea.get(i).isCompleted()).boxed().collect(Collectors.toList());

				taskListAdapter = new TaskListAdapter(requireContext(), android.R.layout.simple_list_item_1, taskNames, indexesCompleted);

				BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
				bottomSheetDialog.setContentView(R.layout.bottom_sheet_task_dialog);
				bottomSheetDialog.setCanceledOnTouchOutside(true);

				TextView txtAreaName = bottomSheetDialog.findViewById(R.id.txt_area_name1);
				assert txtAreaName != null;
				txtAreaName.setText(areas.get(polygonIndex).getName());

				listView = bottomSheetDialog.findViewById(R.id.list_view_tasks);
				assert listView != null;
				listView.setAdapter(taskListAdapter);

				listView.setOnItemClickListener((parent, view1, position, id) -> {
					Intent intent = new Intent(requireContext(), TaskActivity.class);
					intent.putExtra(TaskActivity.TASK_ID_KEY, tasksInArea.get(position).getId());
					startActivity(intent);
				});

				Button btnAddTask = bottomSheetDialog.findViewById(R.id.btn_add_task);
				assert btnAddTask != null;
				btnAddTask.setOnClickListener(v ->
						new TaskDialog(requireActivity(),
								requireContext(),
								requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container),
								-1,
								null,
								selectedArea.getName()));
				bottomSheetDialog.show();
			});
		}
	};


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
							 @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_maps, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		SupportMapFragment mapFragment =
				(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
		if(mapFragment != null)
		{
			mapFragment.getMapAsync(callback);
		}
	}

	@Override
	public void onResume() // Called when user closes the "make new task" dialog, selected area is known
	{
		super.onResume();

		if(selectedArea != null)
		{
			ArrayList<Task> tasks = Utils.getInstance(requireContext()).getTasks();
			tasksInArea = tasks.stream().filter(t -> t.getAreaName().equals(selectedArea.getName())).collect(Collectors.toCollection(ArrayList::new));

			ArrayList<String> taskNames = tasksInArea.stream().map(Task::getName)
					.collect(Collectors.toCollection(ArrayList::new));
			List<Integer> indexesCompleted = IntStream.range(0, tasksInArea.size()).filter(i -> tasksInArea.get(i).isCompleted()).boxed().collect(Collectors.toList());

			taskListAdapter.clear();
			taskListAdapter.addAll(taskNames);
			taskListAdapter.setCompleted(indexesCompleted);
			taskListAdapter.notifyDataSetChanged();
		}
	}
}