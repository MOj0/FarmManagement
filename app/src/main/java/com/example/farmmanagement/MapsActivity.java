package com.example.farmmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.farmmanagement.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener
{
	private static final int PATTERN_GAP_LENGTH_PX = 20;
	private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

	private static final int PATTERN_DASH_LENGTH_PX = 20;
	private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

	// Create a stroke pattern of a gap followed by a dash.
	private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

	private ActivityMapsBinding binding;
	private GoogleMap mMap;
	private FloatingActionButton fabConfirm;
	private FloatingActionButton fabCancel;

	private ArrayList<LatLng> points;
	private ArrayList<PolygonOptions> polygonOptionsArrayList;
	private ArrayList<Polygon> polygons;
	private ArrayList<Area> areas;

	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	private EditText edtAreaName;
	private Button btnConfirmArea, btnCancelArea;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		binding = ActivityMapsBinding.inflate(getLayoutInflater());
		// Retrieve the content view that render the map
		setContentView(binding.getRoot());

		points = new ArrayList<>();
		polygons = new ArrayList<>();
		polygonOptionsArrayList = new ArrayList<>();
		areas = new ArrayList<>();

		fabConfirm = findViewById(R.id.fab_confirm);
		fabConfirm.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(points.isEmpty())
				{
					return;
				}
				if(points.size() < 3)
				{
					Toast.makeText(MapsActivity.this, "Use at least 3 points for the area!", Toast.LENGTH_SHORT).show();
					points.clear();
					mMap.clear();

					// Add all polygons to map, save them to ArrayList polygons
					polygons = polygonOptionsArrayList.stream().map(mMap::addPolygon).collect(Collectors.toCollection(ArrayList::new));
					return;
				}
				createAreaNameInputDialog();
			}
		});

		fabConfirm.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				if(areas.isEmpty())
				{
					Toast.makeText(getBaseContext(), "You need to add at least 1 area!", Toast.LENGTH_SHORT).show();
					return true;
				}
				// Set correct indexes
				IntStream.range(0, areas.size()).forEach(i -> areas.get(i).setPolygonIndex(i));

				Utils.getInstance(getApplicationContext()).setPolygonOptions(polygonOptionsArrayList);
				Utils.getInstance(getApplicationContext()).setAreas(areas);

				Intent intent = new Intent(MapsActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				return true;
			}
		});

		fabCancel = findViewById(R.id.fab_cancel);
		fabCancel.setOnClickListener(v -> {
			// Clear current points
			points.clear();
			mMap.clear();
			polygonOptionsArrayList.forEach(mMap::addPolygon);
		});

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		assert mapFragment != null;
		mapFragment.getMapAsync(this);
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(@NonNull GoogleMap googleMap)
	{
		mMap = googleMap;

		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
		{
			@Override
			public void onMapClick(@NonNull LatLng latLng)
			{
				points.add(latLng);
				mMap.addMarker(new MarkerOptions().position(latLng));
				polygons.forEach(p -> p.setStrokePattern(null));
			}
		});

//		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.684, 143.903), 4));

		// Set listeners for click events
		mMap.setOnPolygonClickListener(this);
	}

	@Override
	public void onPolygonClick(@NonNull Polygon polygon)
	{
		polygons.forEach(p -> p.setStrokePattern(null));
		if(polygon.getStrokePattern() == null)
		{
			polygon.setStrokePattern(PATTERN_POLYGON_ALPHA);
		}

		BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
		bottomSheetDialog.setContentView(R.layout.delete_area_dialog);
		bottomSheetDialog.setCanceledOnTouchOutside(true);

		final int polygonIndex = polygons.indexOf(polygon);

		TextView txtAreaName = bottomSheetDialog.findViewById(R.id.txt_area_name);
		assert txtAreaName != null;
		txtAreaName.setText(areas.get(polygonIndex).getName());

		Button btnDeleteArea = bottomSheetDialog.findViewById(R.id.btn_delete_area);
		assert btnDeleteArea != null;
		btnDeleteArea.setOnClickListener(v -> {
			polygonOptionsArrayList.remove(polygonIndex);
			areas.remove(polygonIndex);
			polygons.remove(polygon);
			polygon.remove();

			bottomSheetDialog.dismiss();
		});

		bottomSheetDialog.show();
	}


	private void createAreaNameInputDialog()
	{
		dialogBuilder = new AlertDialog.Builder(this);
		final View contactPopupView = getLayoutInflater().inflate(R.layout.area_popup_name, null);

		edtAreaName = contactPopupView.findViewById(R.id.edt_area_name);
		btnConfirmArea = contactPopupView.findViewById(R.id.btn_confirm_area);
		btnCancelArea = contactPopupView.findViewById(R.id.btn_cancel_area);

		dialogBuilder.setView(contactPopupView);
		dialog = dialogBuilder.create();
		dialog.show();

		btnConfirmArea.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!goodAreaName(edtAreaName.getText().toString()))
				{
					Toast.makeText(getBaseContext(), "Area with that name already exists!", Toast.LENGTH_SHORT).show();
					return;
				}

				addPolygon();

				// Add area
				Toast.makeText(getBaseContext(), "Area name: " + edtAreaName.getText().toString(), Toast.LENGTH_SHORT).show();
				Area newArea = new Area(edtAreaName.getText().toString(), new ArrayList<>());
				areas.add(newArea);

				dialog.dismiss();
			}
		});

		btnCancelArea.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
	}

	private boolean goodAreaName(String areaName)
	{
		return areas.isEmpty() || areas.stream().noneMatch(a -> a.getName().equals(areaName));
	}


	private void addPolygon()
	{
		PolygonOptions polygonOptions = new PolygonOptions().clickable(true);
		points.forEach(polygonOptions::add); // Define points for polygon
		polygonOptionsArrayList.add(polygonOptions); // Add current polygon to polygonOptions

		// Reset all
		points.clear();
		mMap.clear();

		// Add all polygons to map, save them to ArrayList polygons
		polygons = polygonOptionsArrayList.stream().map(mMap::addPolygon).collect(Collectors.toCollection(ArrayList::new));
	}
}