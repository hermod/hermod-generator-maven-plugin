package com.github.hermod.generator;

import com.github.hermod.generator.model.ClassContainerDescriptor;


/**
 * <p>Generator. </p>
 *
 * @author anavarro - Mar 10, 2013
 *
 */
public interface Generator {

    /**
     * generateClasses.
     * @param outputDir
     * @param templateFileName
     * @param aGeneratedClassType TODO
     * @param classDescriptors
     */
    public void generateSourceClasses(final ClassContainerDescriptor classContainerDescriptor, final String outputDir, final String templateFileName, final boolean generateOneFileForEachClass, final ClassType aGeneratedClassType);
    
}