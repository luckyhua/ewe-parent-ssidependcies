package test.ewe.indetity;

import org.junit.Test;

import com.ewe.common.utils.eweweb.EweWebUtils;
import com.ewe.common.utils.eweweb.ValidateResult;

public class IdentityTestCase {

	@Test
	public void test(){
//		ValidateResult ret = EweWebUtils.queryHanxinNameCertApi("李姚", "150203199002080176");
	//	Boolean result = ret.getResult();
	//	System.out.println(result);
		//System.out.println(ret.getRspContent());
	}
	
	@Test
	public void checkCard(){
		String phone = "15040453375";
		String name = "李姚";
		String card = "1111222233334444";
		String cert = "150203199002080176";
		
		ValidateResult ret = EweWebUtils.queryHanxinNameCardCertPhoneApi(name, card, cert, phone);
		System.out.println(ret.getRspContent());
		System.out.println(ret.getResult());
	}
}
