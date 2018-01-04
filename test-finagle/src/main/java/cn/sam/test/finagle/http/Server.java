package cn.sam.test.finagle.http;

import com.twitter.finagle.Http;
import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Await;
import com.twitter.util.Future;

public class Server {
	
	public static void main(String[] args) throws Exception {
		Service<Request, Response> service = new Service<Request, Response>() {

			@Override
			public Future<Response> apply(Request request) {
				Response res = Response.apply(request);
				return Future.value(res);
			}
		};
		ListeningServer server = Http.serve(":8081", service);
		Await.ready(server);
	}
}
