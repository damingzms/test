package cn.sam.commontest.naming.javaobj.state;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.spi.DirObjectFactory;

public class PersonObjectFactory implements DirObjectFactory {

	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment, Attributes attrs) throws Exception {

		// Only interested in Attributes with "person" objectclass
		// System.out.println("object factory: " + attrs);
		Attribute oc = (attrs != null ? attrs.get("objectclass") : null);
		if (oc != null && oc.contains("person")) {
			Attribute attr;
			String passwd = null;

			// Extract the password
			attr = attrs.get("userPassword");
			if (attr != null) {
				passwd = new String((byte[]) attr.get());
			}
			Person per = new Person(
					(String) attrs.get("sn").get(),
					(String) attrs.get("cn").get(),
					passwd,
					(attr = attrs.get("telephoneNumber")) != null ? (String) attr.get() : null,
					(attr = attrs.get("seealso")) != null ? (String) attr.get() : null,
					(attr = attrs.get("description")) != null ? (String) attr.get() : null);
			return per;
		}
		return null;
	}
	
}