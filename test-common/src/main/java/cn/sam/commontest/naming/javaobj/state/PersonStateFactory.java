package cn.sam.commontest.naming.javaobj.state;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SchemaViolationException;
import javax.naming.spi.DirStateFactory;

public class PersonStateFactory implements DirStateFactory {
	public PersonStateFactory() {
	}

	// DirStateFactory version
	public DirStateFactory.Result getStateToBind(Object obj, Name name,
			Context ctx, Hashtable<?, ?> env, Attributes inAttrs)
			throws NamingException {

		// Only interested in Person objects
		if (obj instanceof Person) {

			Attributes outAttrs;
			if (inAttrs == null) {
				outAttrs = new BasicAttributes(true);
			} else {
				outAttrs = (Attributes) inAttrs.clone();
			}

			// Set up object class
			if (outAttrs.get("objectclass") == null) {
				BasicAttribute oc = new BasicAttribute("objectclass", "person");
				oc.add("top");
				outAttrs.put(oc);
			}

			Person per = (Person) obj;
			// mandatory attributes
			if (per.surname != null) {
				outAttrs.put("sn", per.surname);
			} else {
				throw new SchemaViolationException("Person must have surname");
			}
			if (per.commonName != null) {
				outAttrs.put("cn", per.commonName);
			} else {
				throw new SchemaViolationException("Person must have common name");
			}

			// optional attributes
			if (per.passwd != null) {
				outAttrs.put("userPassword", per.passwd);
			}
			if (per.phone != null) {
				outAttrs.put("telephoneNumber", per.phone);
			}
			if (per.seeAlso != null) {
				outAttrs.put("seeAlso", per.seeAlso);
			}
			if (per.desc != null) {
				outAttrs.put("description", per.desc);
			}

			// System.out.println("state factory: " + outAttrs);
			return new DirStateFactory.Result(null, outAttrs);
		}
		return null;
	}

	// StateFactory version
	public Object getStateToBind(Object obj, Name name, Context ctx,
			Hashtable<?, ?> env) throws NamingException {

		// non-Attributes version not relevant here
		return null;
	}
}