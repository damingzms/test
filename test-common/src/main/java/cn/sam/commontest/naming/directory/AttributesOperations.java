package cn.sam.commontest.naming.directory;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;

public class AttributesOperations {

	public static void main(String[] args) {
		// Identify service provider to use
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://192.168.204.128:389/dc=my-domain,dc=com");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=my-domain,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "secret");
//		env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");

		try {

			// Create the initial directory context
			DirContext ctx = new InitialDirContext(env);

			getAttributes(ctx);
//			modifyAttributes(ctx);

			// Close the context when we're done
			ctx.close();
		} catch (NamingException e) {
			System.err.println("Problem getting attribute: " + e);
		}
	}

	public static void getAttributes(DirContext ctx) throws NamingException {
		System.out.println("All Attributes:");
		Attributes answer = ctx.getAttributes("cn=Ted Geisel, ou=People");
		for (NamingEnumeration<? extends Attribute> ae = answer.getAll(); ae.hasMore();) {
			Attribute attr = ae.next();
			System.out.println("attribute: " + attr.getID());
			/* Print each value */
			for (NamingEnumeration<?> e = attr.getAll(); e.hasMore(); System.out
					.println("value: " + e.next()))
				;
			System.out.println();
		}

		System.out.println();
		System.out.println("Selected Attributes");
		// Specify the ids of the attributes to return
		String[] attrIDs = { "sn", "telephonenumber", "golfhandicap", "mail" };
		answer = ctx.getAttributes("cn=Ted Geisel, ou=People", attrIDs);
		for (NamingEnumeration<? extends Attribute> ae = answer.getAll(); ae.hasMore();) {
			Attribute attr = ae.next();
			System.out.println("attribute: " + attr.getID());
			/* Print each value */
			for (NamingEnumeration<?> e = attr.getAll(); e.hasMore(); System.out
					.println("value: " + e.next()))
				;
			System.out.println();
		}
	}

	public static void modifyAttributes(DirContext ctx) throws NamingException {
		
		ModificationItem[] mods = new ModificationItem[3];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("mail", "geisel@wizards.com"));
		mods[1] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
				new BasicAttribute("telephonenumber", "+1 555 555 5555"));
		mods[2] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
				new BasicAttribute("jpegphoto"));
		
		/*
		// Perform the requested modifications on the named object
		ctx.modifyAttributes("cn=Ted Geisel, ou=People", mods);
		
		ctx.modifyAttributes("cn=Ted Geisel, ou=People", DirContext.REPLACE_ATTRIBUTE,
				new BasicAttributes("mail", "Ted.Geisel@JNDITutorial.com"));
		ctx.modifyAttributes("cn=Ted Geisel, ou=People", DirContext.REMOVE_ATTRIBUTE,
				new BasicAttributes("telephonenumber", "+1 555 555 5555"));
		ctx.modifyAttributes("cn=Ted Geisel, ou=People", DirContext.ADD_ATTRIBUTE,
				new BasicAttributes("jpegphoto", "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"));
		*/
	}
	
	public static void printAttr(Attributes attrs) throws NamingException {
		for (NamingEnumeration<? extends Attribute> ae = attrs.getAll(); ae.hasMore();) {
			Attribute attr = ae.next();
			System.out.println("attribute: " + attr.getID());
			/* Print each value */
			for (NamingEnumeration<?> e = attr.getAll(); e.hasMore(); System.out
					.println("value: " + e.next()))
				;
			System.out.println();
		}
	}

}
