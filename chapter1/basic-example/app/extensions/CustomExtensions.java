package extensions;

import java.util.Date;

import play.i18n.Messages;
import play.templates.JavaExtensions;

public class CustomExtensions extends JavaExtensions {

	private static final int MIN = 60;
	private static final int HOUR = MIN * 60;
	private static final int DAY= HOUR * 24;
	private static final int MONTH= DAY * 30;
	private static final int YEAR= DAY * 365;
	
	public static String pretty(Date date) {
		Date now = new Date();
		if (date.after(now)) {
	        long delta = (date.getTime() - now.getTime()) / 1000;
	        if (delta < 60) {
	            return Messages.get("in.seconds", delta, pluralize(delta));
	        }
	        if (delta < HOUR) {
	            long minutes = delta / MIN;
	            return Messages.get("in.minutes", minutes, pluralize(minutes));
	        }
	        if (delta < DAY) {
	            long hours = delta / HOUR;
	            return Messages.get("in.hours", hours, pluralize(hours));
	        }
	        if (delta < MONTH) {
	            long days = delta / DAY;
	            return Messages.get("in.days", days, pluralize(days));
	        }
	        if (delta < YEAR) {
	            long months = delta / MONTH;
	            return Messages.get("in.months", months, pluralize(months));
	        }
	        long years = delta / YEAR;
	        return Messages.get("in.years", years, pluralize(years));
			
		} else {
			return JavaExtensions.since(date);
		}
	}
}
