package com.gvit.busadministration;

import java.util.List;

import com.gvit.busadministration.database.DatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BusRoute extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_route);
		
		final String busNumber = getIntent().getStringExtra("BusNum");
		
		final ListView busRouteList = (ListView) findViewById(R.id.busRoutes);
		DatabaseHandler handler = new DatabaseHandler(this);
		List<String> busRoutes = handler.getBusRoutes(busNumber);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, busRoutes);
		busRouteList.setAdapter(adapter);
		busRouteList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				String busRoute = (String) (busRouteList
						.getItemAtPosition(position));

				Intent intent = new Intent(BusRoute.this,
						StudentListActivity.class);
				intent.putExtra("BusRoute", busRoute);
				intent.putExtra("BusNum", busNumber);
				
				startActivity(intent);
			}
		});
		
		Button skipButton = (Button) findViewById(R.id.skipButton);
		skipButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BusRoute.this,
						StudentListActivity.class);
				intent.putExtra("BusNum", busNumber);
				startActivity(intent);
			}
		});
	}
}
