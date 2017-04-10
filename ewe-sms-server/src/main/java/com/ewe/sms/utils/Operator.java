package com.ewe.sms.utils;

public enum Operator {

	CSHX(1, "cshx"),
	YUN_PIAN(2,"yunpian");
	
	// 成员变量
    private String name;
    private int index;
	
	// 构造方法
    private Operator(int index, String name) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (Operator o : Operator.values()) {
            if (o.getIndex() == index) {
                return o.name;
            }
        }
        return null;
    }
    
    public static Integer getIndex(String name){
    	for (Operator o : Operator.values()) {
    		if(o.name.equals(name))
    			return o.index;
        }
    	return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    //覆盖方法
    @Override
    public String toString() {
    	return this.name;
    }
    
    public static void main(String[] args) {
		System.out.println(Operator.CSHX);
	}
}
