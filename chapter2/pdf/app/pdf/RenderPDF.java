package pdf;

import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import play.Logger;
import play.Play;
import play.classloading.enhancers.LocalvariablesNamesEnhancer.LocalVariablesNamesTracer;
import play.data.validation.Validation;
import play.mvc.Http;
import play.mvc.Scope;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.Result;
import play.templates.Template;
import play.templates.TemplateLoader;
import play.vfs.VirtualFile;

public class RenderPDF extends Result {

	private static FopFactory fopFactory = FopFactory.newInstance();
	private static TransformerFactory tFactory = TransformerFactory.newInstance();
	private VirtualFile templateFile;

	public static void renderPDF(Object... args) {
		throw new RenderPDF(args);
	}

	public RenderPDF(Object ... args) {
		templateFile = getTemplateFile(args);
		populateRenderArgs(args);
	}

	@Override
	public void apply(Request request, Response response) {
		Template template = TemplateLoader.load(templateFile);

		response.setHeader("Content-Disposition", "inline; filename=\"" + request.actionMethod + ".pdf\"");
		setContentTypeIfNotSet(response, "application/pdf");

		try {
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, response.out);
			Transformer transformer = tFactory.newTransformer();
			Scope.RenderArgs args = Scope.RenderArgs.current();
			String content = template.render(args.data);
			Source src = new StreamSource(IOUtils.toInputStream(content));
			javax.xml.transform.Result res = new SAXResult(fop.getDefaultHandler());
			transformer.transform(src, res);
		} catch (FOPException e) {
			Logger.error(e, "Error creating pdf");
		} catch (TransformerException e) {
			Logger.error(e, "Error creating pdf");
		}
	}

	private void populateRenderArgs(Object ... args) {
		Scope.RenderArgs renderArgs = Scope.RenderArgs.current();
		for (Object o : args) {
			List<String> names = LocalVariablesNamesTracer.getAllLocalVariableNames(o);
			for (String name : names) {
				renderArgs.put(name, o);
			}
		}
		renderArgs.put("session", Scope.Session.current());
		renderArgs.put("request", Http.Request.current());
		renderArgs.put("flash", Scope.Flash.current());
		renderArgs.put("params", Scope.Params.current());
		renderArgs.put("errors", Validation.errors());
	}

	private VirtualFile getTemplateFile(Object ... args) {
		final Http.Request request = Http.Request.current();
		String templateName = null;
		List<String> renderNames = LocalVariablesNamesTracer.getAllLocalVariableNames(args[0]);
		if (args.length > 0 && args[0] instanceof String && renderNames.isEmpty()) {
			templateName = args[0].toString();
		} else {
			templateName = request.action.replace(".", "/") + ".fo";
		}
		if (templateName.startsWith("@")) {
			templateName = templateName.substring(1);
			if (!templateName.contains(".")) {
				templateName = request.controller + "." + templateName;
			}
			templateName = templateName.replace(".", "/") + ".fo";
		}
		VirtualFile file = VirtualFile.search(Play.templatesPath, templateName);
		return file;
	}

}
