package com.gvit.busadministration.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.gvit.busadministration.student.IStudentPass;
import com.gvit.busadministration.student.StudentPass.StudentPassBuilder;

/**
 * @author AjaykumarVasireddy
 * @version 1.0
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	/** Name of the database. */
	private static final String DATABASE_NAME = "Busadministration";

	/** Version of the database. */
	private static final int VERSION = 1;

	private static final String TABLE_NAME = "Students";

	private static final String KEY_ID = "ID";

	private static final String KEY_BUSNO = "BUSNUM";

	private static final String KEY_BUSROUTE = "BUSROUTE";

	private static final String KEY_STUDID = "STUDENTID";

	private static final String KEY_FNAME = "FIRSTNAME";

	private static final String KEY_LNAME = "LASTNAME";

	/**
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTableSQL = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_BUSNO + " TEXT," + KEY_BUSROUTE
				+ " TEXT," + KEY_STUDID + " TEXT," + KEY_FNAME + " TEXT,"
				+ KEY_LNAME + " TEXT" + " )";
		db.execSQL(createTableSQL);
	}

	public void insertDTOs(List<IStudentPass> studentPasses) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (IStudentPass studentPass : studentPasses) {
			ContentValues values = new ContentValues();
			values.put(KEY_BUSNO, studentPass.getBusNumber());
			values.put(KEY_BUSROUTE, studentPass.getBusRouteName());
			values.put(KEY_FNAME, studentPass.getFirstName());
			values.put(KEY_STUDID, studentPass.getRegNumber());
			values.put(KEY_LNAME, studentPass.getLastName());
			// Inserting Row
			db.insert(TABLE_NAME, null, values);
		}
		// 2nd argument is String containing nullColumnHack
		db.close(); // Closing database connection
	}

	public List<String> getBusNumbers() {
		SQLiteDatabase database = this.getReadableDatabase();
		String[] columns = { KEY_BUSNO };
		Cursor cursor = database.query(true, TABLE_NAME, columns, null, null,
				null, null, KEY_BUSNO, null);
		List<String> busList = new ArrayList<String>(cursor.getCount());
		while (cursor.moveToNext()) {
			String busNo = cursor.getString(cursor.getColumnIndex(KEY_BUSNO));
			busList.add(busNo);
		}
		return busList;
	}

	public List<String> getBusRoutes(String busNumber) {
		SQLiteDatabase database = this.getReadableDatabase();
		String[] columns = { KEY_BUSROUTE };
		String[] selectionArgs = { busNumber };
		Cursor cursor = database.query(true, TABLE_NAME, columns, KEY_BUSNO
				+ "=?", selectionArgs, null, null, null, null);
		List<String> routeList = new ArrayList<String>(cursor.getCount());
		while (cursor.moveToNext()) {
			String busroute = cursor.getString(cursor
					.getColumnIndex(KEY_BUSROUTE));
			routeList.add(busroute);
		}
		return routeList;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}
	
	public void deleteAllEntries(){
		SQLiteDatabase database = this.getWritableDatabase();
		int delete = database.delete(TABLE_NAME, null, null);
	}

	public List<IStudentPass> getStudentsWithPass(String busNum, String busRoute) {
		List<IStudentPass> studentPasses = new ArrayList<IStudentPass>();

		SQLiteDatabase database = this.getReadableDatabase();
		String[] columns = { KEY_BUSNO, KEY_BUSROUTE, KEY_FNAME, KEY_LNAME,
				KEY_STUDID };
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setDistinct(true);
		qBuilder.setTables(TABLE_NAME);
		StringBuilder sBuilder = new StringBuilder(KEY_BUSNO);
		sBuilder.append("=?");
		String[] selectionArgs = new String[1];
		selectionArgs[0] = busNum;
		if (busRoute != null && busRoute.length() != 0) {
			selectionArgs = new String[2];
			selectionArgs[0] = busNum;
			sBuilder.append("AND ").append(KEY_BUSROUTE).append("=?");
			selectionArgs[1] = busRoute;
		}
		Cursor cursor = qBuilder.query(database, columns, sBuilder.toString(),
				selectionArgs, null, null, KEY_ID);

		while (cursor.moveToNext()) {
			StudentPassBuilder passBuilder = new StudentPassBuilder();

			String busNumber = cursor.getString(cursor
					.getColumnIndex(KEY_BUSNO));
			passBuilder.busNumber(busNumber);

			String busRouter = cursor.getString(cursor
					.getColumnIndex(KEY_BUSROUTE));
			passBuilder.busRoute(busRouter);

			String firstName = cursor.getString(cursor
					.getColumnIndex(KEY_FNAME));
			passBuilder.firstName(firstName);

			String lastName = cursor
					.getString(cursor.getColumnIndex(KEY_LNAME));
			passBuilder.lastName(lastName);

			String studentID = cursor.getString(cursor
					.getColumnIndex(KEY_STUDID));
			passBuilder.regNo(studentID);
			
			studentPasses.add(passBuilder.build());
		}

		return studentPasses;
	}

}
