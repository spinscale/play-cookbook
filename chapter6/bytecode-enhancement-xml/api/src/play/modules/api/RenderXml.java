package play.modules.api;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import play.Logger;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.Result;

public class RenderXml extends Result {

	private Marshaller m;
	private Object o;
	
	public static void renderXML(Object o) {
		throw new RenderXml(o);
	}
	
	public RenderXml(Object o) {
		this.o = o;
	}
	
	@Override
	public void apply(Request request, Response response) {
		try {
            setContentTypeIfNotSet(response, "text/xml");
			m = ApiPlugin.jc.createMarshaller();
			m.marshal(o, response.out);
		} catch (JAXBException e) {
			Logger.error(e, "Error renderXml");
		}
	}

}
