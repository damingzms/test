package cn.sam.test.finagle.http;

import com.twitter.finagle.Http;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Await;
import com.twitter.util.Future;

public class Client {

	public static void main(String[] args) throws Exception {

		Service<Request, Response> client = Http.client().newService(":8081");

		Request request = Request.apply(Method.Get(), "/");
//		request.host("localhost");
		Future<Response> future = client.apply(request);
//		Await.ready(future);
//		future.onSuccess(rep -> {System.out.println("GET success: " + rep); return BoxedUnit.UNIT;});
		Response response = Await.result(future);
		System.out.println("GET success: " + response);
	}

}
