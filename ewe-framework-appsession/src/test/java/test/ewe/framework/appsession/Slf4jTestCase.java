package test.ewe.framework.appsession;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTestCase {

	@Test
	public void testSlf4jParamsLog(){
		Logger log = LoggerFactory.getLogger(Slf4jTestCase.class);
		
		String test = null;
		
		log.debug("test msg:{}","test");
		log.debug("test null:{}",test);
		
	}
}
