package test.syn;

import java.io.File;

import org.junit.Test;

import test.MockResult;

import com.ewe.rest.context.SenderFactory;
import com.ewe.rest.req.EweMultiPost;
import com.ewe.rest.req.EwePost;
import com.ewe.rest.req.EweRequest;
import com.ewe.rest.resp.EweResponse;
import com.ewe.rest.sender.HttpSender;

public class SimpleSenderTest {

	@Test
	public void simpleTest(){
		HttpSender<String> sender = SenderFactory.createSimple(String.class);
		//?userId=1&photoId=1&appKey=ewe&appSecret=auth&deviceToken=test+token&deviceType=1
		EweRequest<String> req = sender.createPost("http://192.168.2.101:8888/eweAuth/v1/sdkapi/auth/authPhoto")
		.addParam("userId", "1")
		.addParam("photoId", "1")
		.addParam("appKey", "ewe")
		.addParam("appSecret", "auth")
		.addParam("deviceToken", "test token")
		.addParam("deviceType", "1");
		System.out.println(req.send().getContentData());
	}
	
	@Test
	public void testMulti(){
		HttpSender<String> sender = SenderFactory.createSimple(String.class);
		EweMultiPost<String> post = sender.createMultilyPost("http://192.168.2.101:8080/photos/v1/base/photos/testupload");
		post.addParam("file", new File("D:\\temp\\2015\\11\\17\\test\\0c46888d5b1a449aaf85264500df7e38.jpg"));
		EweResponse<String> send = post.send();
		System.out.println(send.getContentData());
	}
	
	@Test
	public void testPopulateData(){
		HttpSender<MockResult> sender = SenderFactory.createSimple(MockResult.class);
		//?userId=1&photoId=1&appKey=ewe&appSecret=auth&deviceToken=test+token&deviceType=1
		EwePost<MockResult> req = sender.createPost("http://192.168.2.101:8888/eweAuth/v1/sdkapi/auth/authPhoto")
		.addParam("userId", "1")
		.addParam("photoId", "1")
		.addParam("appKey", "ewe")
		.addParam("appSecret", "auth")
		.addParam("deviceToken", "test token")
		.addParam("deviceType", "1");
		System.out.println(req.send().getContentData().getMsg());
		System.out.println(req.send().getContentData().getData().getResult());
	}
}
