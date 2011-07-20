package play.modules.solr;

import javassist.CtClass;
import javassist.CtMethod;
import play.Logger;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.enhancers.Enhancer;

public class SolrEnhancer extends Enhancer {

	@Override
	public void enhanceThisClass(ApplicationClass applicationClass) throws Exception {
        CtClass ctClass = makeClass(applicationClass);

        if (!ctClass.subtypeOf(classPool.get("play.modules.solr.SearchModel"))) {
            return;
        }

        String method = "public static play.modules.solr.Query search(String query, String[] values) { return search("+applicationClass.name+".class, query, values); }";
        CtMethod count = CtMethod.make(method, ctClass);
        ctClass.addMethod(count);
        
        // Done.
        applicationClass.enhancedByteCode = ctClass.toBytecode();
        ctClass.defrost();
        
        Logger.info("Enhanced search of %s", applicationClass.name);
	}

}
