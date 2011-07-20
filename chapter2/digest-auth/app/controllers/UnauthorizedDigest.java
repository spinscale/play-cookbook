package controllers;

import play.libs.Codec;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.Result;

public class UnauthorizedDigest extends Result {

    String realm;

    public UnauthorizedDigest(String realm) {
        this.realm = realm;
    }

    @Override
    public void apply(Request request, Response response) {
        response.status = Http.StatusCode.UNAUTHORIZED;
        String auth = "Digest realm=" + realm + ", nonce=" + Codec.UUID();
        response.setHeader("WWW-Authenticate", auth);
    }

}