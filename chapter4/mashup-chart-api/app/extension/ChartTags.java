package extension;

import googlechart.DataEncoder;
import groovy.lang.Closure;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;

@FastTags.Namespace("chart")
public class ChartTags extends FastTags {

    public static void _lc(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) throws Exception {
        out.print("<img src=\"http://chart.apis.google.com/chart?");
        out.print("cht=lc");
        Logger.info("%s", template.getBinding().getVariables().keySet());

        out.print("&chs=" + get("width", "400", args) + "x" + get("height", "200", args));
        out.print("&chtt=" + get("title", "Standard title", args));
        out.print("&chco=" + get("colors", "3D7930", args));

        String labelX = StringUtils.join((List<String>)args.get("labelX"), "|");
        String labelY = StringUtils.join((List<String>)args.get("labelY"), "|");
        out.println("&chxl=0:|"+ labelX + "|1:|" + labelY);
        
        List<Object> data = (List<Object>) args.get("data");
        
        String fieldName = args.get("field").toString();

        List<Number> xValues = new ArrayList<Number>();
        for (Object obj : data) {
            Class clazz = obj.getClass();
            Field field = clazz.getField(fieldName);
        	Number currentX = (Number) field.get(obj);
        	xValues.add(currentX);
         }
        
        String dataString = DataEncoder.encode(xValues);
        
        out.print("&chd=e:" + dataString);
        out.print("&chxs=0,00AA00,14,0.5,l,676767");
        out.print("&chxt=x,y");
        out.print("&chxr=0,0," + DataEncoder.getMax(xValues));
        out.print("&chg=20,25");
        out.print("&chls=1,6,3");
        out.print("\">");
    }
    
    private static String get(String key, String defaultValue, Map<?,?> args) {
    	if (args.containsKey(key)) {
    		return args.get(key).toString();
    	}
    	return defaultValue;
    }
}
