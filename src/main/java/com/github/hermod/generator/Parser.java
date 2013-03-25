package com.github.hermod.generator;

import com.github.hermod.generator.model.ClassContainerDescriptor;

/**
 * <p>Parser. </p>
 *
 * @author anavarro - Mar 10, 2013
 *
 */
public interface Parser {


    
    /**
     * parseMessage.
     * @param parserType
     * @param packageToScan
     * @param aClassPrefix TODO
     * @param aClassSufix TODO
     * @param aPackageToAdd TODO
     * @param aName TODO
     * @return
     */
    public ClassContainerDescriptor parse(final String modelName, final String packageToScan, final String aPrefixImplementationName, final String aPrefixInterfaceName, final String aPackageToAdd, final String aName, final String aImplementationClass);
}