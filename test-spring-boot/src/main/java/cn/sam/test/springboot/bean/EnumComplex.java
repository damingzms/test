package cn.sam.test.springboot.bean;

public enum EnumComplex {
	
	Const1("Const1", 1),
	
	Const2("Const2", 2)
	
	;
	
	private String str;
	
	private int i;

	private EnumComplex(String str, int i) {
		this.str = str;
		this.i = i;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}
	
}
