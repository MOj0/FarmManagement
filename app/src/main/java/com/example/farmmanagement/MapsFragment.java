package com.example.farmmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.stream.Collectors;

public class MapsFragment extends Fragment
{
	private GoogleMap mMap;
//	private ArrayList<Area> areas;

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

//			// Map areas to polygonOptions and add them to map
//			areas = Utils.getInstance(getContext()).getAreas();
//
//			ArrayList<PolygonOptions> polygonOptions = Utils.getInstance(getContext()).getPolygonOptions();
//			ArrayList<Polygon> polygons = polygonOptions.stream().map(mMap::addPolygon).collect(Collectors.toCollection(ArrayList::new));
//
//			// Set listeners for click events
//			mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener()
//			{
//				@Override
//				public void onPolygonClick(@NonNull Polygon polygon)
//				{
//					final int polygonIndex = polygons.indexOf(polygon);
//
//					Area selectedArea = areas.stream().filter(a -> a.getPolygonIndex() == polygonIndex).findFirst().orElse(null);
//					assert selectedArea != null;
//					ArrayList<Task> tasksInArea = selectedArea.getTasks();
//
//					BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
//					bottomSheetDialog.setContentView(R.layout.bottom_sheet_task_dialog);
//					bottomSheetDialog.setCanceledOnTouchOutside(true);
//
//					TextView txtAreaName = bottomSheetDialog.findViewById(R.id.txtAreaName1);
//					assert txtAreaName != null;
//					txtAreaName.setText(areas.get(polygonIndex).getName());
//
//					ArrayList<String> taskNames = tasksInArea.stream().map(Task::getName).collect(Collectors.toCollection(ArrayList::new));
//					ArrayAdapter<String> taskNameAdapter =
//							new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, taskNames);
//
//					ListView listView = bottomSheetDialog.findViewById(R.id.listViewTasks);
//					assert listView != null;
//					listView.setAdapter(taskNameAdapter);
//
//					listView.setOnItemClickListener((parent, view1, position, id) -> {
//						Intent intent = new Intent(requireContext(), TaskActivity.class);
//						Task clickedTask = tasksInArea.get(position);
//						intent.putExtra(TaskActivity.TASK_ID_KEY, clickedTask.getId());
//						startActivity(intent);
//					});
//
//					Button btnAddTask = bottomSheetDialog.findViewById(R.id.btnAddTask);
//					assert btnAddTask != null;
//					btnAddTask.setOnClickListener(v -> {
//						Intent intent = new Intent(requireContext(), AddTaskActivity.class);
//						intent.putExtra("area", selectedArea.getName());
//						startActivity(intent);
//					});
//					bottomSheetDialog.show();
//				}
//			});
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
}