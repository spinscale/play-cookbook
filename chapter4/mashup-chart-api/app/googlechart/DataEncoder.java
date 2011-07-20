package googlechart;

import java.util.List;

import play.Logger;

public class DataEncoder {

	public static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.";
	public static int length = chars.length();

	public static String encode(List<Number> numbers, int maxValue) {
		String data = "";
		for (Number number : numbers) {
			double scaledVal = Math.floor(length * length * number.intValue() / maxValue);
			
			if (scaledVal > (length * length ) -1) {
				data += "..";
			} else if (scaledVal < 0) {
				data += "__";
			} else {
				int quotient = (int) Math.floor(scaledVal / length);
				int remainder = (int) scaledVal - (length * quotient);
				data += chars.charAt(quotient) + "" + chars.charAt(remainder);
			}
		}
		Logger.debug("Called with %s and %s => %s", numbers, maxValue, data);
		return data;
	}
	
	public static String encode(List<Number> numbers) {
		return encode(numbers, getMax(numbers));
	}
	
	public static int getMax(List<Number> numbers) {
		Number max = numbers.get(0);
		for (Number number : numbers.subList(1, numbers.size())) {
			if (number.doubleValue() > max.doubleValue()) {
				max = number;
			}
		}

		return (int) Math.ceil(max.doubleValue());
	}
	
}
