package play.modules.searchhelp;

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.enhancers.Enhancer;

public class SearchHelperEnhancer extends Enhancer {

	@Override
	public void enhanceThisClass(ApplicationClass applicationClass) throws Exception {
		CtClass ctClass = makeClass(applicationClass);

        if (!ctClass.subtypeOf(classPool.get("play.modules.searchhelp.IndexedModel")) || 
        		!hasAnnotation(ctClass, "play.modules.search.Indexed")) {
            return;
        }
        
        CtMethod isIndexed = CtMethod.make("public static Boolean isIndexed() { return Boolean.TRUE; }", ctClass);
        ctClass.addMethod(isIndexed);

        List<String> fields = new ArrayList();
        for (CtField ctField : ctClass.getFields()) {
        	if (hasAnnotation(ctField, "play.modules.search.Field")) {
        		fields.add("\"" + ctField.getName() + "\"");
        	}
        }
        
        String method;
        if (fields.size() > 0) {
        	String fieldStr = fields.toString().replace("[", "").replace("]", "");
        	method = "public static java.util.List getIndexedFields() { return java.util.Arrays.asList(new String[]{" + fieldStr + "}); }";
        	CtMethod count = CtMethod.make(method, ctClass);
        	ctClass.addMethod(count);
        }
        
        applicationClass.enhancedByteCode = ctClass.toBytecode();
        ctClass.defrost();
	}
}
