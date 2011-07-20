package models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

public class Customer {

	public String name;
	public List<Order> orders = new ArrayList<Order>();
	
	public Customer() {
		name = RandomStringUtils.randomAlphabetic(10);
		for (int i = 0 ; i < 6 ; i++) {
			orders.add(new Order());
		}
	}
	
}
