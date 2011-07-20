package play.modules.csv;

import javassist.CtClass;
import javassist.CtMethod;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.enhancers.Enhancer;

public class CsvEnhancer extends Enhancer {

	@Override
	public void enhanceThisClass(ApplicationClass applicationClass) throws Exception {
        CtClass ctClass = makeClass(applicationClass);

        if (!ctClass.subtypeOf(classPool.get("play.modules.csv.CsvModel"))) {
            return;
        }

        CtMethod findById = CtMethod.make("public static play.modules.csv.CsvModel findById(Long id) { return findById(" + applicationClass.name + ".class, id); }", ctClass);
        ctClass.addMethod(findById);

        CtMethod find = CtMethod.make("public static play.modules.csv.CsvQuery find(String query, Object[] fields) { return find(" + applicationClass.name + ".class, query, fields); }", ctClass);
        ctClass.addMethod(find);
        
        applicationClass.enhancedByteCode = ctClass.toBytecode();
        ctClass.defrost();
	}

}
