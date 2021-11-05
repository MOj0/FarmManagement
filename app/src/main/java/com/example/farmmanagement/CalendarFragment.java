package com.example.farmmanagement;

import android.content.Intent;
import android.graphics.Color;
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

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CalendarFragment extends Fragment
{
	private SimpleDateFormat dateFormatMonth;
	private TextView txtYearMonth;

	private CompactCalendarView compactCalendarView;
	private ArrayAdapter<String> taskNameAdapter;
	private Date mDateClicked;
	private ArrayList<Task> tasks;
	private ListView listView;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_calendar, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		dateFormatMonth = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault());

		txtYearMonth = view.findViewById(R.id.txtYearMonth);
		txtYearMonth.setText(dateFormatMonth.format(new Date()));

		compactCalendarView = view.findViewById(R.id.compactCalendarView);
		compactCalendarView.setUseThreeLetterAbbreviation(true);

		getEvents();

		compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener()
		{
			@Override
			public void onDayClick(Date dateClicked)
			{
				mDateClicked = dateClicked;
				BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
				bottomSheetDialog.setContentView(R.layout.bottom_sheet_task_dialog);
				bottomSheetDialog.setCanceledOnTouchOutside(true);

				List<Event> events = compactCalendarView.getEvents(dateClicked);
				tasks = events.stream().map(e -> (Task) e.getData()).collect(Collectors.toCollection(ArrayList::new));

				ArrayList<String> taskNames = tasks.stream().map(Task::getName).collect(Collectors.toCollection(ArrayList::new));
				taskNameAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, taskNames);

				listView = bottomSheetDialog.findViewById(R.id.list_view_tasks);
				assert listView != null;
				listView.setAdapter(taskNameAdapter);

				listView.setOnItemClickListener((parent, view1, position, id) -> {
					Intent intent = new Intent(requireContext(), TaskActivity.class);
					intent.putExtra(TaskActivity.TASK_ID_KEY, tasks.get(position).getId());
					startActivity(intent);
				});

				Button btnAddTask = bottomSheetDialog.findViewById(R.id.btn_add_task);
				assert btnAddTask != null;
				btnAddTask.setOnClickListener(v ->
						new TaskDialog(requireActivity(), requireContext(), requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container), -1, dateClicked));
				bottomSheetDialog.show();
			}

			@Override
			public void onMonthScroll(Date firstDayOfNewMonth)
			{
				txtYearMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
			}
		});
	}

	private void getEvents()
	{
		compactCalendarView.removeAllEvents();
		ArrayList<Task> tasks = Utils.getInstance(getActivity()).getTasks();
		tasks.forEach(t ->
				compactCalendarView.addEvent(new Event(Color.BLUE, t.getDeadlineDate().getTimeInMillis(), t)));
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if(taskNameAdapter != null)
		{
			getEvents();
			List<Event> eventsOnDate = compactCalendarView.getEvents(mDateClicked);
			tasks = eventsOnDate.stream().map(e -> (Task) e.getData()).collect(Collectors.toCollection(ArrayList::new));

			taskNameAdapter.add(tasks.get(tasks.size() - 1).getName());
			taskNameAdapter.notifyDataSetChanged();
			listView.setAdapter(taskNameAdapter);
		}
	}
}