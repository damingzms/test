package cn.sam.commontest.naming.javaobj;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import cn.sam.commontest.naming.javaobj.state.Person;

public class JavaObjsOperations {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Identify service provider to use
		Hashtable<String, Object> env = new Hashtable<String, Object>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://192.168.204.128:389/dc=my-domain,dc=com");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=my-domain,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "secret");

		try {

			// Create the initial directory context
			DirContext ctx = new InitialDirContext(env);
			
			
			// 1.Serializable Object
//			String name = "cn=Button";
//			Button b = new Button("Push me");
//
//			bind(ctx, name, b);
//			lookup(ctx, name);
//			unbind(ctx, name);
			
			
			// 2.Referenceable Object
//			String name = "cn=Fruit";
//			Fruit fruit = new Fruit("apple");
//
//			bind(ctx, name, fruit);
//			Fruit f = lookup(ctx, name);
//			System.out.println(f.getName());
//			unbind(ctx, name);
			
			
			// 3.DirContext Object
			
			// 4.Remote Objects
			
			
			// 5.CORBA Objects
//			// Create and initialize the ORB
//			ORB orb = ORB.init(args, null);
//
//			// Create servant and register it with the ORB
//			HelloServant helloRef = new HelloServant();
//			orb.connect(helloRef);
//
//			// Let service provider use the ORB
//			env.put("java.naming.corba.orb", orb);
//
//			// Create the initial context
//			DirContext ctx = new InitialDirContext(env);
//
//			// Bind to directory
//			ctx.rebind("cn=CorbaHello", helloRef);
//
//			// Look up and narrow
//			Hello h2 = HelloHelper.narrow((org.omg.CORBA.Object) ctx.lookup("cn=CorbaHello"));
//
//			// Invoke method on object
//			System.out.println(h2.sayHello());
//			
//			// If asked to keep alive for other clients
//			if (args.length > 0) {
//				// wait for invocations from clients
//				while (true) {
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//					}
//				}
//			}
			
			
			// 6.Using State Factories
//			env.put("java.naming.factory.state",
//					"cn.sam.commontest.naming.javaobj.state.PersonStateFactory");
//			env.put("java.naming.factory.object",
//					"cn.sam.commontest.naming.javaobj.state.PersonObjectFactory");
//			
//			// Create the initial context
//		    DirContext ctx = new InitialDirContext(env);
//
//		    // Create object to be bound
//		    Person john = new Person("Smith", "John Smith");
//
//		    // Perform bind
//		    ctx.rebind("cn=John Smith", john);
//
//		    // Read object back
//		    Person john2 = (Person) ctx.lookup("cn=John Smith");
//		    System.out.println(john2);
			
			// Close the context when we're done
			ctx.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public static <T> T lookup(DirContext ctx, String name) throws NamingException {
		
		// Check that it is bound
		@SuppressWarnings("unchecked")
		T t = (T) ctx.lookup(name);
		
		System.out.println(t);
		return t;
	}
	
	public static void bind(DirContext ctx, String name, Object obj) throws NamingException {
		ctx.bind(name, obj);
	}
	
	public static void unbind(DirContext ctx, String name) throws NamingException {
		ctx.unbind(name);
	}

}
