package cn.sam.commontest.naming.directory;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class TestDirectory {

	public static void main(String[] args) {
		// jndi.properties
		// Identify service provider to use
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://192.168.204.128:389/dc=my-domain,dc=com");

		// env.put(Context.INITIAL_CONTEXT_FACTORY,
		// "com.sun.jndi.fscontext.RefFSContextFactory");
		// env.put(Context.PROVIDER_URL, "file:D:/logs");

		try {

			// Create the initial directory context
			DirContext ctx = new InitialDirContext(env);
			
			// Compose a name within the LDAP namespace
//			Context ldapCtx = (Context)ctx.lookup("cn=Londo Mollari, ou=people");
//			String ldapName = ldapCtx.composeName("cn=homedir", "cn=Londo Mollari, ou=people");
//			System.out.println(ldapName);
//
//			Context homedirCtx = (Context)ctx.lookup(ldapName);
//			String fullname = homedirCtx.getNameInNamespace();
//			System.out.println(fullname);
			
//			// Compose a name when it crosses into the next naming system
//			Context homedirCtx = (Context)ctx.lookup(ldapName);
//			String compositeName = homedirCtx.composeName("tutorial", ldapName);
//			System.out.println(compositeName);
//
//			// Compose a name within the File namespace
//			Context fileCtx = (Context)ctx.lookup(compositeName);
//			String fileName = fileCtx.composeName("report.txt", compositeName);
//			System.out.println(fileName);

//			String name = "cn=Ted Geisel, ou=People/'D:/logs/report.txt'";
//			try {
//				CompositeName cn = new CompositeName(name);
//				System.out.println(cn + " has " + cn.size() + " components: ");
//				for (Enumeration<String> all = cn.getAll(); all
//						.hasMoreElements();) {
//					System.out.println(all.nextElement());
//				}
//			} catch (InvalidNameException e) {
//				System.out.println("Cannot parse name: " + name);
//			}
////			
//			System.out.println();
//			System.out.println("**********************************");
//
			// Get the parser for the namespace
			NameParser parser = ctx.getNameParser("");

			// Parse the string name into a compound name
			Name compound = parser.parse("cn=homedir, cn=Londo Mollari, ou=people");
//			
//			// Specify the changes to make
//			ModificationItem[] mods = new ModificationItem[3];
//
//			// Replace the "mail" attribute with a new value
//			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
//			    new BasicAttribute("javaReferenceAddress", "#0#URL#file:D:/logs/awt/im"));
//			
//			ctx.modifyAttributes(compound, mods);
			
//			System.out.println(compound.getClass().getName());
//			for (Enumeration<String> all = compound.getAll(); all
//					.hasMoreElements();) {
//				System.out.println(all.nextElement());
//			}
//
//			System.out.println();
//			System.out.println("**********************************");
//			
			Context jon = (Context) ctx.lookup(compound);

			String fullname = jon.getNameInNamespace();
			System.out.println(fullname);
			
			// Close the context when we're done
			ctx.close();
		} catch (NamingException e) {
			System.err.println("Problem getting attribute: " + e);
		}
	}

	/*
	 * public static void main(String[] args) { // Identify service provider to
	 * use Hashtable<String, String> env = new Hashtable<String, String>(11);
	 * env.put(Context.INITIAL_CONTEXT_FACTORY,
	 * "com.sun.jndi.ldap.LdapCtxFactory"); env.put(Context.PROVIDER_URL,
	 * "ldap://192.168.204.128:389/dc=my-domain,dc=com");
	 * env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=my-domain,dc=com");
	 * env.put(Context.SECURITY_CREDENTIALS, "secret");
	 * 
	 * try {
	 * 
	 * // Create the initial directory context DirContext ctx = new
	 * InitialDirContext(env);
	 * 
	 * // Create the object to be bound Fruit fruit = new Fruit("orange");
	 * 
	 * // Create attributes to be associated with the object Attributes attrs =
	 * new BasicAttributes(true); // case-ignore Attribute objclass = new
	 * BasicAttribute("objectclass"); objclass.add("top");
	 * objclass.add("organizationalUnit"); attrs.put(objclass);
	 * 
	 * // Perform bind ctx.rebind("ou=favorite, ou=Fruits", fruit, attrs);
	 * 
	 * // 感觉没有用到FruitFactory
	 * 
	 * NamingEnumeration<NameClassPair> list = ctx.list("ou=Fruits"); while
	 * (list.hasMore()) { NameClassPair nc = list.next();
	 * System.out.println(nc); }
	 * 
	 * Attributes answer = ctx.getAttributes("ou=favorite, ou=Fruits"); for
	 * (NamingEnumeration<? extends Attribute> ae = answer.getAll();
	 * ae.hasMore();) { Attribute attr = ae.next();
	 * System.out.println("attribute: " + attr.getID()); Print each value for
	 * (NamingEnumeration<?> e = attr.getAll(); e.hasMore(); System.out
	 * .println("value: " + e.next())) ; System.out.println(); }
	 * 
	 * // Close the context when we're done ctx.close(); } catch
	 * (NamingException e) { System.err.println("Problem getting attribute: " +
	 * e); } }
	 */
}
