package com.gvit.busadministration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

import com.csvreader.CsvReader;
import com.gvit.busadministration.database.DatabaseHandler;
import com.gvit.busadministration.student.IStudentPass;
import com.gvit.busadministration.student.StudentPass;
import com.gvit.busadministration.student.StudentPass.StudentPassBuilder;

public class BusNumber extends Activity {

	private DatabaseHandler handler;

	public BusNumber() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_number);

		handler = new DatabaseHandler(this);
//		List<IStudentPass> studentPasses = createStudentPasses();
//		handler.insertDTOs(studentPasses);

		final ListView busNumberListView = (ListView) findViewById(R.id.busNumbers);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, handler.getBusNumbers());
		busNumberListView.setAdapter(adapter);

		busNumberListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				String busNumber = (String) (busNumberListView
						.getItemAtPosition(position));

				Intent intent = new Intent(BusNumber.this, BusRoute.class);
				intent.putExtra("BusNum", busNumber);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.global, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.upload:
			FileChooserDialog dialog = new FileChooserDialog(this);
			dialog.setShowFullPath(true);
			dialog.setFolderMode(false);
			dialog.setShowConfirmation(true, false);
			dialog.show();
			// Define the filter for select images.
			dialog.setFilter(".*csv");
			dialog.setShowOnlySelectable(false);
			dialog.addListener(new OnFileSelectedListener() {

				@Override
				public void onFileSelected(Dialog source, File folder,
						String name) {

				}

				@Override
				public void onFileSelected(Dialog source, File file) {
					source.hide();
					Toast toast = Toast.makeText(BusNumber.this,
							"File selected: " + file.getName(),
							Toast.LENGTH_LONG);
					toast.show();
					persisToDB(file.getAbsolutePath());
				}
			});
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void persisToDB(String filePath) {
		handler.deleteAllEntries();
		ArrayList<IStudentPass> passList = new ArrayList<>();
		try {
			CsvReader reader = new CsvReader(filePath);
			reader.readHeaders();
			while (reader.readRecord()) {
				StudentPassBuilder passBuilder = new StudentPass.StudentPassBuilder();
				String busNumber = reader.get("BusNumber");
				passBuilder.busNumber(busNumber);

				String route = reader.get("Route");
				passBuilder.busRoute(route);

				String studentId = reader.get("StudentId");
				passBuilder.regNo(studentId);

				String firstName = reader.get("StudentFirstName");
				passBuilder.firstName(firstName);

				String lastName = reader.get("StudentLastName");
				passBuilder.lastName(lastName);
				passList.add(passBuilder.build());
			}
			handler.insertDTOs(passList);

		} catch (FileNotFoundException e) {
			Log.e("LOAD ERROR", e.getMessage());
		} catch (IOException e) {
			Log.e("Read ERROR", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
