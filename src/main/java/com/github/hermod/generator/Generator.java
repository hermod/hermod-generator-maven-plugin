package com.github.hermod.generator;

import java.io.File;
import java.io.IOException;

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
     * @param generatePackageInOutputDir TODO
     * @param templateFileName
     * @param aGeneratedClassType TODO
     * @param classDescriptors
     * @throws IOException 
     */
    public void generateSourceClasses(final ClassContainerDescriptor classContainerDescriptor, final File outputDir, boolean generatePackageInOutputDir, final File templateFileName, final boolean generateOneFileForEachClass, final ClassType aGeneratedClassType) throws IOException;
    
}
