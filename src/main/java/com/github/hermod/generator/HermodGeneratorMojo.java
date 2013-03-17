package com.github.hermod.generator;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.github.hermod.generator.impl.AnnotatedClassParser;
import com.github.hermod.generator.impl.MustacheGenerator;

//@Mojo( name = "HermodGeneratorMojo")
public final class HermodGeneratorMojo extends AbstractMojo
{

    //@Parameter(property = "generateFileForEachClass", defaultValue = "true" )
    private boolean generateFileForEachClass;
    
    //@Parameter(property = "packageToScan", defaultValue = "" )
    private String packageToScan;
    
    //@Parameter(property = "outputDir", defaultValue = "./target/generated-sources/hermod" )
    private String outputDir;
    
    //@Parameter(property = "modelType", defaultValue = "ANNOTATED_CLASSES" )
    private String modelType;
    
    //@Parameter(property = "modelName", defaultValue = "com.github.hermod.ser.descriptor.Message.class" )
    private String modelName;
    
    //@Parameter(property = "implementationTemplateFileName", defaultValue = "DefaultMessage.java.mustache" )
    private String implementationTemplateFileName; // templateName.extension.mustache don't generate is blank
    
    //@Parameter(property = "interfaceTemplateFileName", defaultValue = "Message.java.mustache" )
    private String interfaceTemplateFileName; // templateName.extension.mustache don't generate is blank
    
    //@Parameter(property = "prefixImplementationClass", defaultValue = "Hermod" )
    private String prefixImplementationClass;
    
    //@Parameter(property = "prefixInterfaceClass", defaultValue = "" )
    private String prefixInterfaceClass;
    
    //@Parameter(property = "suffixImplementationPackage", defaultValue = "hermod" )
    private String suffixImplementationPackage;
    
    //@Parameter(property = "serializedImplementationClass", defaultValue = "com.github.hermod.ser.impl.KeyObjectMsg" )
    private String serializedImplementationClass = "com.github.hermod.ser.impl.KeyObjectMsg";
    
    //@Parameter(property = "name", defaultValue = "" )
    private String name = "";// useful if generateFileForEachClass=false  // StringUtils.substringBefore(templateFileName, ".")
    
    
    /**
     * (non-Javadoc)
     *
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException
    {
        getLog().info( "HermodGenerator started." );
        
        final Parser classParser = new AnnotatedClassParser();
        final Generator generator = new MustacheGenerator();

        if (interfaceTemplateFileName != null && !"".equals(interfaceTemplateFileName)) {
            generator.generateSourceClasses(classParser.parse(modelName, packageToScan, prefixInterfaceClass, prefixInterfaceClass, suffixImplementationPackage, StringUtils.substringBefore(interfaceTemplateFileName, "."), serializedImplementationClass), outputDir, interfaceTemplateFileName, generateFileForEachClass, ClassType.INTERFACE);
        }

        if (implementationTemplateFileName != null && !"".equals(implementationTemplateFileName)) {
            generator.generateSourceClasses(classParser.parse(modelName, packageToScan, prefixImplementationClass, prefixInterfaceClass, "hermod", StringUtils.substringBefore(implementationTemplateFileName, "."), serializedImplementationClass), outputDir, implementationTemplateFileName, generateFileForEachClass, ClassType.CLASS);
        }
        
        getLog().info( "HermodGenerator ended." );
        
    }
}
