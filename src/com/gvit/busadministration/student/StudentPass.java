/**
 * 
 */
package com.gvit.busadministration.student;

/**
 * @author AjaykumarVasireddy
 * @version 1.0
 *
 */
public class StudentPass implements IStudentPass {

	private String regno;
	private String firstName;
	private String lastName;
	private String busNumber;
	private String busRoute;

	public StudentPass(StudentPassBuilder studentPassBuilder) {
		this.regno = studentPassBuilder.regno;
		this.firstName = studentPassBuilder.firstName;
		this.lastName = studentPassBuilder.lastName;
		this.busNumber = studentPassBuilder.busno;
		this.busRoute = studentPassBuilder.busRoute;
	}

	public static class StudentPassBuilder {
		private String regno;
		private String busno;
		private String busRoute;
		private String firstName;
		private String lastName;

		public StudentPassBuilder regNo(String regno) {
			this.regno = regno;
			return this;
		}
		
		public StudentPassBuilder firstName(String fname) {
			this.firstName = fname;
			return this;
		}
		
		public StudentPassBuilder lastName(String lName) {
			this.lastName = lName;
			return this;
		}
		
		public StudentPassBuilder busNumber(String busNo) {
			this.busno = busNo;
			return this;
		}
		
		public StudentPassBuilder busRoute(String busRoute) {
			this.busRoute = busRoute;
			return this;
		}

		public StudentPass build() {
			StudentPass pass = new StudentPass(this);
			return pass;
		}

	}

	@Override
	public String getRegNumber() {
		return regno;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getBusRouteName() {
		return busRoute;
	}

	@Override
	public String getBusNumber() {
		return busNumber;
	}

}
