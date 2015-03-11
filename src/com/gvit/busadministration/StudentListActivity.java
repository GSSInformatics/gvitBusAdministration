package com.gvit.busadministration;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.gvit.busadministration.database.DatabaseHandler;
import com.gvit.busadministration.student.IStudentPass;

public class StudentListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_list);
		ListView studentList = (ListView) findViewById(R.id.list);
		final String busNumber = getIntent().getStringExtra("BusNum");
		final String busRoute = getIntent().getStringExtra("BusRoute");

		DatabaseHandler handler = new DatabaseHandler(this);
		List<IStudentPass> studentPasses = handler.getStudentsWithPass(
				busNumber, busRoute);
		CustomStudentList adapter = new CustomStudentList(
				StudentListActivity.this, studentPasses);
		studentList.setAdapter(adapter);
	}
}
