package cn.sam.commontest.naming.directory;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class Search {

	public static void main(String[] args) {
		// Identify service provider to use
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://192.168.204.128:389/dc=my-domain,dc=com");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=my-domain,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "secret");

		try {

			// Create the initial directory context
			DirContext ctx = new InitialDirContext(env);

//			baseSearch1(ctx);
//			baseSearch2(ctx);
//			filterSearch1(ctx);
			filterSearch2(ctx);
			
			// Close the context when we're done
			ctx.close();
		} catch (NamingException e) {
			System.err.println("Problem getting attribute: " + e);
		}
	}
	
	public static void baseSearch1(DirContext ctx) throws NamingException {
		// Specify the attributes to match
		// Ask for objects that has a surname ("sn") attribute with
		// the value "Geisel" and the "mail" attribute
		Attributes matchAttrs = new BasicAttributes(true); // ignore attribute name case
		matchAttrs.put(new BasicAttribute("sn", "Geisel"));
		matchAttrs.put(new BasicAttribute("mail"));

		// Search for objects that have those matching attributes
		NamingEnumeration<SearchResult> searchResults = ctx.search("ou=People", matchAttrs);
		
		print(searchResults);
	}
	
	public static void baseSearch2(DirContext ctx) throws NamingException {
		// Specify the attributes to match
		// Ask for objects that has a surname ("sn") attribute with
		// the value "Geisel" and the "mail" attribute
		Attributes matchAttrs = new BasicAttributes(true); // ignore attribute name case
		matchAttrs.put(new BasicAttribute("sn", "Geisel"));
		matchAttrs.put(new BasicAttribute("mail"));
		
		// Specify the ids of the attributes to return
		String[] attrIDs = {"sn", "telephonenumber", "golfhandicap", "mail"};

		// Search for objects that have those matching attributes
		NamingEnumeration<SearchResult> searchResults = ctx.search("ou=People", matchAttrs, attrIDs);

		print(searchResults);
	}
	
	public static void filterSearch1(DirContext ctx) throws NamingException {
		// Create the default search controls
		SearchControls ctls = new SearchControls();

		// Specify the search filter to match
		// Ask for objects that have the attribute "sn" == "Geisel"
		// and the "mail" attribute
		String filter = "(&(sn=Geisel)(mail=*))";

		// Search for objects using the filter
		NamingEnumeration<SearchResult> searchResults = ctx.search("ou=People", filter, ctls);

		print(searchResults);
	}
	
	public static void filterSearch2(DirContext ctx) throws NamingException {
		// Create the default search controls
		SearchControls ctls = new SearchControls();
		
		// Specify the ids of the attributes to return
		String[] attrIDs = {"sn", "telephonenumber", "golfhandicap", "mail"};
		
		// SearchControls的更多用法，请查看官方文档
		ctls.setReturningAttributes(attrIDs);

		// Specify the search filter to match
		// Ask for objects that have the attribute "sn" == "Geisel"
		// and the "mail" attribute
		String filter = "(&(sn=Geisel)(mail=*))";

		// Search for objects using the filter
		NamingEnumeration<SearchResult> searchResults = ctx.search("ou=People", filter, ctls);

		print(searchResults);
	}
	
	public static void print(NamingEnumeration<SearchResult> searchResults) throws NamingException {
		if (searchResults == null) {
			return;
		}
		while (searchResults.hasMore()) {
			SearchResult sr = (SearchResult) searchResults.next();
			System.out.println(">>>" + sr.getName());
			Attributes answer = sr.getAttributes();
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
		}
	}

}
