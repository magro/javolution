/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;

/**
 *  <p> This class represents a light OSGi framework which can be
 *      used to run bundles as "standard" java applications. This can 
 *      be particularly useful for testing/debugging purpose.
 *      This requires that the bundle activator classes are accessible (public)
 *      [code]
 *      public void main(String[] args) throws Exception {
 *           OSGi osgi = OSGi.newInstance();
 *           osgi.start("Javolution", new javolution.internal.osgi.JavolutionActivator());
 *           osgi.start("Foo", new org.foo.internal.osgi.FooActivator());
 *           Thread.sleep(10000);
 *           osgi.stop("Javolution");
 *           osgi.stop("Foo");
 *       }
 *       [/code]
 * </p>
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 6.0, December 12, 2012
 */
public abstract class OSGi {

    /**
     * Default constructor.
     */
    protected OSGi() {
    }

    /**
     * Returns a new OSGi instance.
     */
    public static OSGi newInstance() {
        return new OSGiImpl();
    }

    /**
     * Starts a bundle having the specified identifier and activator.
     *
     * @param bundleId the identifier of the bundle.
     * @param activator the activator for the bundle.
     */
    public abstract void start(String bundleId, BundleActivator activator);

    /**
     * Stops a bundle of this OSGi framework.
     *
     * @param bundleId the identifier of the bundle.
     */
    public abstract void stop(String bundleId);

    /**
     * Returns a bundle from this OSGi framework.
     *
     * @param bundleId the identifier of the bundle.
     */
    public abstract Bundle getBundle(String bundleId);

}