package controllers;

import java.util.HashMap;
import java.util.Map;

import models.User;

import org.apache.commons.lang.StringUtils;

import play.libs.Codec;
import play.mvc.Http;
import play.mvc.Http.Request;

public class DigestRequest {
	private Map<String,String> params = new HashMap<String,String>();
	private Request request;

	public DigestRequest(Request request) {
		this.request = request;
	}

	public boolean isValid() {
		if (!request.headers.containsKey("authorization")) {
			return false;
		}
		String authString = request.headers.get("authorization").value();
		if (StringUtils.isEmpty(authString) || !authString.startsWith("Digest ")) {
			return false;
		}
		for (String keyValuePair : authString.replaceFirst("Digest ", "").split(",")) {
			String data[] = keyValuePair.trim().split("=", 2);
			String key = data[0];
			String value = data[1].replaceAll("\"", "");
			if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
				params.put(key, value);
			}
		}
		return params.containsKey("username") && params.containsKey("realm") &&
			params.containsKey("uri") && params.containsKey("nonce") &&
			params.containsKey("response");
	}

	public boolean isAuthorized() {
		User user = User.find("byName", params.get("username")).first();
		if (user == null) {
			throw new UnauthorizedDigest(params.get("realm"));
		}

		String digest = createDigest(user.apiPassword);
		return digest.equals(params.get("response"));		
	}
	
	private String createDigest(String pass) {
		String username = params.get("username");
		String realm = params.get("realm");
		String digest1 = Codec.hexMD5(username + ":" + realm + ":" + pass);
		String digest2 = Codec.hexMD5(request.method + ":" + params.get("uri"));
		String digest3 = Codec.hexMD5(digest1 + ":" + params.get("nonce") + ":" + digest2);

		return digest3;
	}

	public static boolean isAuthorized(Http.Request request) {
		DigestRequest req = new DigestRequest(request);
		return req.isValid() && req.isAuthorized();
	}
}
