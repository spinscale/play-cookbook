package models;

import java.util.List;

import play.modules.csv.CsvModel;


public class User extends CsvModel {

	public String name;
	public Car currentCar;
	
	public String toString() {
		return name + "/" + id;
	}
}
