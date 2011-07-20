package models;

import play.modules.csv.CsvModel;

public class Car extends CsvModel {

	public Car() {}
	
	public Car(String brand, String type) {
		this.brand = brand;
		this.type = type;
	}
	
	public String brand;
	public String type;
	
	public String toString() {
		return brand + " " + type;
	}
}
