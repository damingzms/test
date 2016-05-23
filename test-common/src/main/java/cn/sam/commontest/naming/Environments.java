package cn.sam.commontest.naming;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Environments {

	public static void main(String[] args) {
		
		// Initial environment with various properties
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, 
		    "com.sun.jndi.fscontext.FSContextFactory");
		env.put(Context.PROVIDER_URL, "file:D:/logs");
		env.put(Context.OBJECT_FACTORIES, "foo.bar.ObjFactory");
		env.put("foo", "bar");
		
		Context ctx;
		try {

			// Call the constructor
			ctx = new InitialContext(env);

			// Get the child context
			Context child = (Context)ctx.lookup("awt");

			// See what properties the initial context has
			System.out.println(ctx.getEnvironment());

			// Replace foo in the parent
			ctx.addToEnvironment("foo", "baz");
				  
			// Add a new property to the parent
			ctx.addToEnvironment("com.wiz.jndi.wizProp", "wizards");

			// Remove an attribute from the parent
			ctx.removeFromEnvironment(Context.OBJECT_FACTORIES);

			// Remote property from the child
			child.removeFromEnvironment(Context.PROVIDER_URL);

			// See what environment properties you have after updates
			System.out.println(">>>>> Parent context: ");
			System.out.println(ctx.getEnvironment());

			// See what environment properties the child has after updates
			System.out.println(">>>>> Child context: ");
			System.out.println(child.getEnvironment());
			
			ctx.close();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
