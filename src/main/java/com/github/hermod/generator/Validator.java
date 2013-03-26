package com.github.hermod.generator;

import java.util.List;

/**
 * <p>Validator. </p>
 *
 * @author anavarro - Mar 24, 2013
 * @param <T>
 *
 */
public interface Validator<T> {
    
    /**
     * validate.
     *
     * @param aT
     * @return
     */
    List<String> validate(T aT);

}
