/**
 * CS180 - Lab03 - Person
 * 
 * This is the construction class for Person
 *
 * All JavaDoc comments are from Wiki
 * 
 * @author hughe127
 * 
 * @lab 802
 * 
 * @date 19/9/14
 */

public class Person {

	String id;

	int age;

	int hdl;

	int ldl;

	int otherLipids;

	/**
	 * Person Class Constructor: Creates a new instance of the person class;
	 * Initializes instance variables within the given arguments
	 * 
	 * @param id
	 *            - this person's ID
	 * @param age
	 *            - this person's age
	 * @param hdl
	 *            - this person's LDL level
	 * @param ldl
	 *            - this person's HDL level
	 * @param otherLipids
	 *            - the person's other lipids level
	 */

	public Person(String id, int age, int hdl, int ldl, int otherLipids) {

		this.id = id; // initialize this person's ID

		this.age = age; // initialize this person's age

		this.ldl = ldl; // initialize this person's LDL level

		this.hdl = hdl; // initialize this persons's HDL level

		this.otherLipids = otherLipids; // initialize this persons's other
										// lipids level

	}

	public int getTotalCholesterol() {

		int tc = this.hdl + this.ldl + this.otherLipids;

		return tc;
	}

	/**
	 * determine if this person is an adult or a child
	 * 
	 * @return true if this person is an adult, false if they are a child
	 */

	public boolean isAdult() {

		return this.age >= 18;
	}

	/**
	 * @return true if the total cholesterol for this person is within the
	 *         healthy range, false otherwise
	 * 
	 *         A healthy TC level for an adult is less than 200 mg/dL. A healthy
	 *         TC level for a child is less than 170 mg/dL.
	 */

	public boolean hasGoodCholesterol() {
		int healthyLevel;

		if (this.isAdult()) {
			healthyLevel = 200;
		}

		else {
			healthyLevel = 170;
		}

		return this.getTotalCholesterol() < healthyLevel;
	}

	/**
	 * @return true if the LDL level is within the healthy range, false
	 *         otherwise.
	 * 
	 *         A healthy LDL level for an adult is less than or equal to 130
	 *         mg/dL. A healthy LDL level for a child is less than or equal to
	 *         110 mg/dL.
	 */

	public boolean hasGoodLDL() {

		if (this.isAdult()) {

			return this.ldl <= 130;
		}

		else {

			return this.ldl <= 110;

		}
	}

	/**
	 * @return true if the HDL level is within the healthy range, false
	 *         otherwise.
	 * 
	 *         A healthy range for HDL is greater than or equal to 40 mg/dL for
	 *         both adults and children.
	 */

	public boolean hasGoodHDL() {

		return this.hdl >= 40;
	}

	/**
	 * pretty print the person's cholesterol report.<br>
	 * this method is useful for debugging.
	 */
	
	public void printReport() {

		System.out.println(this.id + "'s Report");

		System.out.println("Age: " + this.age + " ("
				+ (isAdult() ? "Adult" : "Child") + ")");

		System.out.println("Total Cholesterol: " + getTotalCholesterol() + " ("
				+ (hasGoodCholesterol() ? "Good" : "Bad") + ")");

		System.out.println("LDL: " + this.ldl + " ("
				+ (hasGoodLDL() ? "Good" : "Bad") + ")");

		System.out.println("HDL: " + this.hdl + " ("
				+ (hasGoodHDL() ? "Good" : "Bad") + ")");
	}

}
