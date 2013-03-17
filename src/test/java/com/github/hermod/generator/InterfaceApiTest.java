package com.github.hermod.generator;

import com.github.hermod.ser.descriptor.InterfaceApi;

/**
 * <p>InterfaceApiTest. </p>
 *
 * @author anavarro - Mar 9, 2013
 *
 */
@InterfaceApi
public interface InterfaceApiTest {

    /**
     * test.
     *
     * @param test1
     * @param test2
     * @return
     */
    String test(final String test1, final int test2);
}
