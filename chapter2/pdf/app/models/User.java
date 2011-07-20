package models;

import org.apache.commons.lang.RandomStringUtils;

public class User {
	public String name = "Alexander";
	public String description = "Random: " + RandomStringUtils.randomAlphanumeric(20);
}
