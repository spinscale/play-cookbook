package models;

import java.math.BigDecimal;

import org.apache.commons.lang.math.RandomUtils;

public class Order {
	public BigDecimal cost = new BigDecimal(RandomUtils.nextInt(50));
}
