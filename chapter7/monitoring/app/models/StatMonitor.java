package models;

public class StatMonitor {

	public String name;
	public Long hits;
	public Long min;
	public Long max;
	public Double avg;
	
	public String toString() {
		return name + "/" + hits + "/" + min + "/" + max + "/" + avg;
	}
}
