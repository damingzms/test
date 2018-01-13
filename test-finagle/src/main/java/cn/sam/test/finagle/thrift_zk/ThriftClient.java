package cn.sam.test.finagle.thrift_zk;

import com.twitter.finagle.Service;
import com.twitter.finagle.SimpleFilter;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.Thrift.Client;
import com.twitter.finagle.example.thriftjava.LoggerService;
import com.twitter.finagle.example.thriftjava.TLogObjRequest;
import com.twitter.finagle.example.thriftjava.TLogObjResponse;
import com.twitter.finagle.service.RetryFilter;
import com.twitter.finagle.service.RetryPolicy;
import com.twitter.finagle.service.SimpleRetryPolicy;
import com.twitter.finagle.stats.NullStatsReceiver;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.finagle.util.DefaultTimer;
import com.twitter.util.Await;
import com.twitter.util.Duration;
import com.twitter.util.Function;
import com.twitter.util.Future;
import com.twitter.util.Timer;
import com.twitter.util.Try;

import scala.Tuple2;
import scala.runtime.BoxedUnit;

public final class ThriftClient {

	private ThriftClient() {
	}

	public static void main(String[] args) throws Exception {
		
		// 1.初始化Client
		Client client = Thrift.client()
				.withLabel("finagle client")
				.withSessionPool().minSize(10)
				.withSessionPool().maxSize(10)
				.withSessionPool().maxWaiters(100)
				.withSession().acquisitionTimeout(Duration.fromMilliseconds(5000L))
				.withSession().maxIdleTime(Duration.fromMilliseconds(60000L))
//				.withSession().maxLifeTime(20.seconds)
				.withRequestTimeout(Duration.fromMilliseconds(8000L)) // 从测试效果看来，with方式和filter方式配置的timeout，效果一样，with方式输出的异常信息更详细
//				.withMonitor(monitor)
//				.withTracer(tracer)
				;
		
		// 2.Service
		Service<ThriftClientRequest, byte[]> service = client.newService(ThriftServer.CONSUMER_PATH);
		
		// 3.filter, 注意filter的顺序，本例中，越后定义的filter越早运行，注意andThen方法
		// a.SimpleFilter
		SimpleFilter<ThriftClientRequest, byte[]> simpleFilter = new SimpleFilter<ThriftClientRequest, byte[]>() {

			@Override
			public Future<byte[]> apply(ThriftClientRequest request, Service<ThriftClientRequest, byte[]> service) {
				System.out.println("simple filter");
				return service.apply(request);
			}
		};
		service = simpleFilter.andThen(service);

		// b.timeout, filter的方式相比with的方式，可以做更多精细化配置
		Timer defaultTimer = DefaultTimer.getInstance();
//		Duration timeout = Duration.fromMilliseconds(8000L);
//		TimeoutFilter<ThriftClientRequest, byte[]> timeoutFilter = new TimeoutFilter<>(timeout, new IndividualRequestTimeoutException(timeout), defaultTimer);
//		service = timeoutFilter.andThen(service);
		
		// c.retry, 简化客户端重试策略的措施：远程方法必须是幂等，或者规范远程方法返回的异常类型规范化
		// TODO 怎样确定是否需要重试？怎样在服务器返回异常的时候重试？
		int maxRetry = 3;
		RetryPolicy<Tuple2<ThriftClientRequest, Try<byte[]>>> retryPolicy = new SimpleRetryPolicy<Tuple2<ThriftClientRequest, Try<byte[]>>>(1 /* 重试的起始序号，backoffAt的入参 */) {
			@Override
			public Duration backoffAt(int retry) {
				if (retry > maxRetry) {
					return this.never();
				} else {
					System.out.println(retry + "nd retry");
					return Duration.fromSeconds(1);
				}
			}

			@Override
			public boolean shouldRetry(Tuple2<ThriftClientRequest, Try<byte[]>> tuple) {
				Try<byte[]> _try = tuple._2;
				if (_try.isThrow() && !_try.toString().contains("com.twitter.finagle.ChannelClosedException")) {
					return true;
				}
				return false;
			}
		};
		RetryFilter<ThriftClientRequest, byte[]> retryFilter = new RetryFilter<ThriftClientRequest, byte[]>(retryPolicy, defaultTimer, new NullStatsReceiver());
		service = retryFilter.andThen(service);
		
		// 4.ServiceToClient
		LoggerService.ServiceIface serviceIface = new LoggerService.ServiceToClient(service);
		
		// 5.远程调用方法
		// a.方法1
		String logResp = Await.result(serviceIface.log("hello", 2));
		System.out.println("Received response: " + logResp);
		
		// b.方法2
		TLogObjRequest request = new TLogObjRequest();
		request.setLogLevel(1);
		request.setMessage("hi");
		Future<TLogObjResponse> logObjResp = serviceIface.logObj(request);
		logObjResp.onSuccess(new Function<TLogObjResponse, BoxedUnit>() {
			
			@Override
			public BoxedUnit apply(TLogObjResponse response) {
				System.out.println(String.format("Received response code: %s, desc: %s", response.getRespCode(), response.getRespDesc()));
				return BoxedUnit.UNIT;
			}
		});
	      
		// 防止进程在响应返回之前结束
		Await.result(logObjResp);
	}
}
