/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.javolution.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Indicates that an element has strict timing constraints and has a deterministic time behavior.</p>
 *  
 * <p> The {@link #limit limit} parameter shows the evolution of the 
 *     <a href="http://en.wikipedia.org/wiki/Worst-case_execution_time"> worst-case execution time</a> of a method with 
 *     the cumulative size of the instance and its inputs (or only the size of the inputs for static methods).    
 *     
 * <pre>{@code
 * public class FastCollection<E> {
 *     {@literal@}Realtime(limit = LINEAR)
 *     public boolean contains(Object obj) { ... }
 *     
 *     {@literal@}Realtime(limit = N_SQUARE)
 *     public boolean containsAll(Collection<?> that) { ... }
 *     
 *     {@literal@}Realtime(limit = LINEAR, comment = "Could count the elements (e.g. filtered view).")
 *     public abstract int size();
 * }}</pre></p>
 * 
 * <p> The {@link #concurrency} parameter documents the temporal behavior in case of concurrent access, the default is 
 *     {@code NOT_THREAD_SAFE} which means that external synchronization or coordination is required. 
 *     
 * <pre>{@code
 * public class MyCache<K,V> { 
 *      final FastMap<K,V> map = FastMap.<K,V>newMap().atomic();
 *      
 *      {@literal@}Realtime(concurrency = LOCK_FREE)
 *      public V get(K key) {
 *          return map.get(key); // Reads over atomic maps are lock-free.
 *      }
 *      
 *      {@literal@}Realtime(concurrency = SYNCHRONIZED)
 *      public V put(K key, V value) {
 *          return map.put(key, value); // Writes over atomic maps are synchronized.
 *      }
 * }</pre></p>
 *          
 * <p> If a class is annotated {@link Realtime}, all its methods and constructors are assumed the same temporal behavior 
 *     unless explicitly stated. Although Java methods annotations are not inherited, overridden methods 
 *     are expected to have to same or more strenuous timing behavior as their parents (otherwise timing 
 *     assumptions could be broken by an implementation).</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 7.0, November 15, 2016
 * @see <a href="http://en.wikipedia.org/wiki/Real-time_computing">Real-Time Computing</a>
 */
@Documented
@Inherited
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface Realtime {

    /**
     * Indicates if this element has a bounded worst-case execution time (default {@code true}).
     */
    boolean value() default true;

    /**
     * Returns the limit behavior for the worst-case execution time (default {@link Limit#CONSTANT}).
     */
    Limit limit() default Limit.CONSTANT;

    /**
     * Returns the temporal behavior in case of concurrent access (default {@link Concurrency#NOT_THREAD_SAFE}).
     */
    Concurrency concurrency() default Concurrency.NOT_THREAD_SAFE;

    /**
     * Provides additional information (default {@code ""}).
     */
    String comment() default "";

    /**
     * Identifies the limit behavior for the worst case execution time.
     */
    public enum Limit {

        /**
         * The worst case execution time is constant.
         */
        CONSTANT,

        /**
         * The worst case execution time is bounded in <i>O(log(n))</i> with <i>n</i> characteristic 
         * of the current size of the inputs.
         */
        LOG_N,

        /**
         * The worst case execution time is bounded in <i>O(n)</i> with <i>n</i> characteristic 
         * of the current size of the inputs.
         */
        LINEAR,

        /**
         * The worst case execution time is bounded in <i>O(n log(n))</i> with <i>n</i> characteristic 
         * of the current size of the inputs.
         */
        N_LOG_N,

        /**
         * The worst case execution time is bounded in <i>O(n²)</i> with <i>n</i> characteristic 
         * of the current size of the inputs.
         */
        N_SQUARE,

        /**
         * The limit behavior of the worst case execution time is unknown or unspecified.
         */
        UNKNOWN,
    }

    /**
     * Identifies the temporal behavior in case of concurrent access (e.g. blocking or not).
     */
    public enum Concurrency {

        /**
         * Additional synchronization or coordination on the part of the caller is required.
         */
        NOT_THREAD_SAFE,

        /**
         * No sequences of accesses (reads and writes to public fields, calls to public methods) 
         * may put the object into an invalid state and access is never blocking.
         */
        LOCK_FREE,

        /**
         * Concurrent reads are supported without blocking as long as there is no concurrent write.
         */
        READ_WRITE_LOCK,

        /**
         * Concurrent threads have exclusive access to variables/resources.
         */
        SYNCHRONIZED,

        /**
         * Limited number of concurrent read/write may be supported without blocking.
         */
        SEMAPHORE,
        
        /**
         * A lock continuously testing a variable until some value appears.
         */
        SPINLOCK,
        
        /**
         * Custom locking behavior (should be documented in the comment field). 
         */
        CUSTOM,
    }
}