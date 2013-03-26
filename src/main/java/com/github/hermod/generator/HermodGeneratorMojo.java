package com.github.hermod.generator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.hermod.generator.impl.AnnotatedClassParser;
import com.github.hermod.generator.impl.ClassContainerDescriptorValidator;
import com.github.hermod.generator.impl.MustacheGenerator;
import com.github.hermod.generator.model.ClassContainerDescriptor;

/**
 * <p>HermodGeneratorMojo. </p>
 * 
 * @author anavarro - Mar 20, 2013
 * 
 */
@Mojo(name = "generateInterfaceImplementation", defaultPhase = LifecyclePhase.GENERATE_SOURCES ,
requiresDependencyResolution = ResolutionScope.COMPILE,
requiresDependencyCollection = ResolutionScope.COMPILE) // (since Maven 3.0))
public final class HermodGeneratorMojo extends AbstractMojo {

    // Parser options
    @Parameter(property = "modelType", defaultValue = "ANNOTATED_CLASSES")
    private String modelType;
    
    @Parameter(property = "modelName", defaultValue = "com.github.hermod.ser.descriptor.AMessage")
    private String modelName;

    @Parameter(property = "packageToScan", defaultValue = "")
    private String packageToScan;
    
    // Generator options
    @Parameter(property = "prefixInterfaceClass", defaultValue = "")
    private String prefixInterfaceClass;
    
    @Parameter(property = "prefixImplementationClass", defaultValue = "Default")
    private String prefixImplementationClass;

    @Parameter(property = "suffixImplementationPackage", defaultValue = "impl")
    private String suffixImplementationPackage;

    @Parameter(property = "serializedImplementationClass", defaultValue = "com.github.hermod.ser.impl.KeyObjectMsg")
    private String serializedImplementationClass;

    @Parameter(property = "generateFileForEachClass", defaultValue = "true")
    private boolean generateFileForEachClass;
    
    @Parameter(property = "generateImplementation", defaultValue = "true")
    private boolean generateImplementation;

    @Parameter(property = "implementationTemplateFileName", defaultValue = "DefaultMessage.java.mustache")
    private String implementationTemplateFileName; // templateName.extension.mustache don't generate is blank
    
    @Parameter(property = "generateInterface", defaultValue = "false")
    private boolean generateInterface;
    
    @Parameter(property = "interfaceTemplateFileName", defaultValue = "Message.java.mustache")
    private String interfaceTemplateFileName; // templateName.extension.mustache don't generate is blank
    
    @Parameter(property = "generatePackageLayoutInOutputDir", defaultValue = "true")
    private boolean generatePackageLayoutInOutputDir;
    
    
    @Parameter(property = "outputDir", defaultValue = "${project.build.directory}/generated-sources/")
    private String outputDir;
    
    /**
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("HermodGenerator started.");
        if (this.packageToScan == null) {
            this.packageToScan = "";
        }
        if (this.prefixInterfaceClass == null) {
            this.prefixInterfaceClass = "";
        }
        final Parser classParser = new AnnotatedClassParser();
        final Validator<ClassContainerDescriptor> validator = new ClassContainerDescriptorValidator();
        final Generator generator = new MustacheGenerator();

        final ClassContainerDescriptor classContainerDescriptor = classParser.parse(this.modelName, this.packageToScan, this.prefixImplementationClass,
                this.prefixInterfaceClass, this.suffixImplementationPackage, StringUtils.substringBefore(this.interfaceTemplateFileName, "."),
                this.serializedImplementationClass);
        final List<String> errors = validator.validate(classContainerDescriptor);

        if (errors.size() > 0) {
            for (String error : errors) {
                getLog().error(error);
            }
            getLog().error("Model is invalid (see errors above).");
            throw new IllegalArgumentException("Model is invalid");
        } else {
            if (this.generateInterface) {
                generator.generateSourceClasses(classContainerDescriptor, this.outputDir, this.generatePackageLayoutInOutputDir,
                        this.interfaceTemplateFileName, this.generateFileForEachClass, ClassType.INTERFACE);
            }
            if (this.generateImplementation) {
                generator.generateSourceClasses(classContainerDescriptor, this.outputDir, this.generatePackageLayoutInOutputDir,
                        this.implementationTemplateFileName, this.generateFileForEachClass, ClassType.CLASS);
            }
        }
        getLog().info("HermodGenerator ended.");
    }
}
