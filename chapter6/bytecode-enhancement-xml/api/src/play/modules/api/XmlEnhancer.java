package play.modules.api;

import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.enhancers.Enhancer;

public class XmlEnhancer extends Enhancer {

	@Override
	public void enhanceThisClass(ApplicationClass applicationClass) throws Exception {
        CtClass ctClass = makeClass(applicationClass);

        if (!ctClass.subtypeOf(classPool.get("play.db.jpa.JPABase"))) {
            return;
        }

        if (!hasAnnotation(ctClass, "javax.persistence.Entity")) {
            return;
        }
        
        ConstPool constpool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        
        if (!hasAnnotation(ctClass, "javax.xml.bind.annotation.XmlAccessorType")) {
        	Annotation annot = new Annotation("javax.xml.bind.annotation.XmlAccessorType", constpool);
        	EnumMemberValue enumValue = new EnumMemberValue(constpool);
        	enumValue.setType("javax.xml.bind.annotation.XmlAccessType");
        	enumValue.setValue("FIELD");
        	annot.addMemberValue("value", enumValue);
        	attr.addAnnotation(annot);
        	ctClass.getClassFile().addAttribute(attr);
        }
        
        if (!hasAnnotation(ctClass, "javax.xml.bind.annotation.XmlRootElement")) {
        	Annotation annot = new Annotation("javax.xml.bind.annotation.XmlRootElement", constpool);
        	String entityName = ctClass.getName();
        	String entity = entityName.substring(entityName.lastIndexOf('.') + 1).toLowerCase();
        	annot.addMemberValue("name", new StringMemberValue(entity, constpool));
        	attr.addAnnotation(annot);
        	ctClass.getClassFile().addAttribute(attr);
        }
        
        applicationClass.enhancedByteCode = ctClass.toBytecode();
        ctClass.defrost();
	}
}
