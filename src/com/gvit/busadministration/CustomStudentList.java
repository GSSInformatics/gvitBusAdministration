/**
 * 
 */
package com.gvit.busadministration;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gvit.busadministration.student.IStudentPass;

/**
 * @author AjaykumarVasireddy
 * @version 1.0
 *
 */
public class CustomStudentList extends ArrayAdapter<IStudentPass> {

	private Activity context;
	private List<IStudentPass> studentPasses;

	public CustomStudentList(Activity context, List<IStudentPass> studentPasses) {
		super(context, R.layout.student_list_row, studentPasses);
		this.context = context;
		this.studentPasses = studentPasses;
	}

	@Override
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.student_list_row, null, true);
		ImageView studImage = (ImageView) rowView.findViewById(R.id.image);
		studImage.setImageResource(R.drawable.ic_launcher);

		TextView studFirstName = (TextView) rowView
				.findViewById(R.id.firstName);
		studFirstName.setText(studentPasses.get(position).getFirstName());

		TextView studLastName = (TextView) rowView.findViewById(R.id.lastName);
		studLastName.setText(studentPasses.get(position).getLastName());

		TextView studentId = (TextView) rowView.findViewById(R.id.studentsID);
		studentId.setText(studentPasses.get(position).getRegNumber());

		return rowView;
	}

}
