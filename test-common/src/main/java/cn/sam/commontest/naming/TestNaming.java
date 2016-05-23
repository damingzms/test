package cn.sam.commontest.naming;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

public class TestNaming {

	public static void main(String[] args) {
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
		try {
		    // Create the initial context
		    Context ctx = new InitialContext(env);

//			env.put(Context.PROVIDER_URL, "file:D:/logs");
//		    Context ctx = new InitialContext(env);
//		    String name = "fruits/orange";
//			ctx.bind(name, new Fruit("orange"));
//		    Fruit f = (Fruit) ctx.lookup(name);
//		    System.out.println(f.getName());
//		    ctx.unbind(name);
			
//			Context jon = (Context) ctx.lookup("/root/java/");
//
//			String fullname = jon.getNameInNamespace();
//			System.out.println(fullname);
//
//			System.out.println();
//			System.out.println("**********************************");
//
			// Get the parser for the namespace
			NameParser parser = ctx.getNameParser("");

			// Parse the string name into a compound name
			Name compound = parser.parse("D:/logs/awt/im/");
			
			System.out.println(compound.getClass().getName());
			for (Enumeration<String> all = compound.getAll(); all
					.hasMoreElements();) {
				System.out.println(all.nextElement());
			}
//
//			System.out.println();
//			System.out.println("**********************************");
//			
//			Context jon = (Context) ctx.lookup(compound);
//
//			String fullname = jon.getNameInNamespace();
//			System.out.println(fullname);
			
//			File f = (File) ctx.lookup(compound);
//			
//			System.out.println(f);
			
			ctx.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
