package cn.sam.commontest.naming.objfactory;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;

public class Fruit implements Referenceable {
	
	private String name;

	public Fruit() {
	}

	public Fruit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Reference getReference() throws NamingException {
		return new Reference(Fruit.class.getName(), new StringRefAddr("name", name),
				FruitFactory.class.getName(), null);
	}

}
