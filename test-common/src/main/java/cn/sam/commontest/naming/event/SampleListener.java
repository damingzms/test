package cn.sam.commontest.naming.event;

import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;
import javax.naming.ldap.UnsolicitedNotificationEvent;
import javax.naming.ldap.UnsolicitedNotificationListener;

public class SampleListener implements NamespaceChangeListener,
		ObjectChangeListener, UnsolicitedNotificationListener {
	private String id;

	public SampleListener(String id) {
		this.id = id;
	}

	public void objectAdded(NamingEvent evt) {
		System.out.println(id + ">>> added: " + evt.getNewBinding());
	}

	public void objectRemoved(NamingEvent evt) {
		System.out.println(id + ">>> removed: " + evt.getOldBinding());
	}

	public void objectRenamed(NamingEvent evt) {
		System.out.println(id + ">>> renamed: " + evt.getNewBinding()
				+ " from " + evt.getOldBinding());
	}

	public void objectChanged(NamingEvent evt) {
		System.out.println(id + ">>> object changed: " + evt.getNewBinding()
				+ " from " + evt.getOldBinding());
	}

	public void notificationReceived(UnsolicitedNotificationEvent evt) {
        System.out.println("received: " + evt);
	}

	public void namingExceptionThrown(NamingExceptionEvent evt) {
		System.out.println(id + ">>> SampleNCListener got an exception");
		evt.getException().printStackTrace();
	}
}
