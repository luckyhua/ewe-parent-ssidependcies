package test.asyn;

import org.junit.Test;

import com.ewe.rest.context.SenderFactory;
import com.ewe.rest.req.EweRequest;
import com.ewe.rest.sender.HttpSender;

public class AsynSenderTest {

	@Test
	public void sendTest(){

	}
	
	public static void main(String[] args) throws InterruptedException {
		HttpSender<String> sender = SenderFactory.createAsynSender(String.class, new MockCaller());

		long begin = System.currentTimeMillis();
		for(int i=0;i<10;i++){
			EweRequest<String> req = sender.createPost("http://192.168.2.101:8888/eweAuth/v1/sdkapi/auth/authPhoto")
					.addParam("userId", "1")
					.addParam("photoId", "1")
					.addParam("appKey", "ewe")
					.addParam("appSecret", "auth")
					.addParam("deviceToken", "test token")
					.addParam("deviceType", "1");
			req.send();
		}
		long end = System.currentTimeMillis();
		
		
		SenderFactory.destory();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("run!----------------------------------");
			}
		}));
		System.out.println("sys run in:"+(end-begin));
	}
}
