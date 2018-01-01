package cn.sam.test.finagle.http;

import com.twitter.finagle.Http;
import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Await;

public class Proxy {

	public static void main(String[] args) throws Exception {
		Service<Request, Response> client = Http.newService(":8081");
		ListeningServer server = Http.serve(":8080", client);
		Await.ready(server);
	}

}
