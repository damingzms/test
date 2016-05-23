package cn.sam.commontest.nio.sse.server;
import cn.sam.commontest.nio.sse.http.*;
/**
 * @author Taylor Cowan
 */
public class RequestHandlerThread extends Thread {
	private Queue myQueue;
	private ServletContext myServletContext;
	private DefaultServlet defaultServlet = new DefaultServlet();
	public RequestHandlerThread(ServletContext context, Queue q) {
		myServletContext = context;
		myQueue = q;
	}
	public void run() {
		while (true) {
			Client client = (Client) myQueue.remove();
			try {
				for (; ; ) {
					HttpRequest req = new HttpRequest(client.clientInputStream,
							myServletContext);
					HttpResponse res = new HttpResponse(client.key);
					defaultServlet.service(req, res);
					if (client.notifyRequestDone())
						break;
				}
			} catch (Exception e) {
				client.key.cancel();
				client.key.selector().wakeup();
			}
		}
	}
}