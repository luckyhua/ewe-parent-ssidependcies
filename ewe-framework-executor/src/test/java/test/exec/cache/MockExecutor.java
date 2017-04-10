package test.exec.cache;

import com.ewe.exec.executor.DataExecutor;

public class MockExecutor implements DataExecutor<String>{

	@Override
	public boolean execute(String data) {
		System.out.println("run in executor:"+data);
		return true;
	}

	@Override
	public String getName() {
		return "test";
	}

}
