class Car implements Vehicle {

	private String color;
	private String make;
	
	public Car(String color, String make) {
		this.color = color;
		this.make = make;
	}
	
	@Override
	public String getMake() {
		return make;
	}

	@Override
	public String getColor() {
		return color;
	}
}