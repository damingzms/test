package cn.sam.commontest.naming;

import java.io.File;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import cn.sam.commontest.naming.objfactory.Fruit;

public class NamingMethods {
	/*
	public static void main(String[] args) {

		// Set up the environment for creating the initial context
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "cn.sam.commontest.naming.myprovider.FruitFactory");

		try {
			// Create the initial context
			Context ctx = new InitialContext(env);

			bind(ctx);
//			rebind(ctx);
//			unbind(ctx);
			
			// Close the context when we're done
			ctx.close();
		} catch (NamingException e) {
			System.out.println("Perform failed: " + e);
		}
	}
	*/
	
	public static void main(String[] args) {

		// Set up the environment for creating the initial context
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.fscontext.RefFSContextFactory");
		env.put(Context.PROVIDER_URL, "file:D:/logs");

		try {
			// Create the initial context
			Context ctx = new InitialContext(env);

//			lookup(ctx);
//			list(ctx);
//			listBindings(ctx);
//			rename(ctx);
//			createSubcontext(ctx);
//			destroySubcontext(ctx);
			File f = (File)ctx.lookup(
				    "cn=homedir,cn=Jon Ruiz,ou=People/report.txt");

			// Close the context when we're done
			ctx.close();
		} catch (NamingException e) {
			System.out.println("Perform failed: " + e);
		}
	}
	

	public static void lookup(Context ctx) throws NamingException {
		File f = (File) ctx.lookup("report.txt");
		System.out.println(f);
	}

	public static void list(Context ctx) throws NamingException {
		NamingEnumeration<NameClassPair> list = ctx.list("awt");
		while (list.hasMore()) {
			NameClassPair nc = list.next();
			System.out.println(nc);
		}
	}

	public static void listBindings(Context ctx) throws NamingException {
		NamingEnumeration<Binding> bindings = ctx.listBindings("awt");
		while (bindings.hasMore()) {
			Binding bd = bindings.next();
			System.out.println(bd.getName() + ": " + bd.getObject());
		}
	}

	public static void rename(Context ctx) throws NamingException {
		ctx.rename("old_report.txt", "report.txt");
	}

	public static void createSubcontext(Context ctx) throws NamingException {
		Context result = ctx.createSubcontext("new");
	}

	public static void destroySubcontext(Context ctx) throws NamingException {
		ctx.destroySubcontext("new");
	}


	public static void bind(Context ctx) throws NamingException {
		Fruit fruit = new Fruit("orange");
		ctx.bind("favorite", fruit);
	}

	public static void rebind(Context ctx) throws NamingException {
		Fruit fruit = new Fruit("lemon");
		ctx.rebind("favorite", fruit);
	}

	public static void unbind(Context ctx) throws NamingException {
		ctx.unbind("favorite");
	}
	
}
