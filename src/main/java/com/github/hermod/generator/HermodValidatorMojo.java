package com.github.hermod.generator;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.hermod.generator.impl.AnnotatedClassParser;
import com.github.hermod.generator.impl.ClassContainerDescriptorValidator;
import com.github.hermod.generator.model.ClassContainerDescriptor;

/**
 * <p>HermodValidatorMojo. </p>
 *
 * @author anavarro - Mar 24, 2013
 *
 */
@Mojo(name = "validate", defaultPhase = LifecyclePhase.GENERATE_SOURCES ,
requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME) // (since Maven 3.0))
public class HermodValidatorMojo extends AbstractMojo {

    @Parameter(property = "modelType", defaultValue = "ANNOTATED_CLASSES")
    private String modelType;

    @Parameter(property = "modelName", defaultValue = "com.github.hermod.ser.descriptor.AMessage")
    private String modelName;
    
    @Parameter(property = "packageToScan", defaultValue = "")
    private String packageToScan = "";
    /**
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("HermodValidator started.");
        final Parser classParser = new AnnotatedClassParser();
        final Validator<ClassContainerDescriptor> validator = new ClassContainerDescriptorValidator();

        final ClassContainerDescriptor classContainerDescriptor = classParser.parse(this.modelName, this.packageToScan, "",
                "", "", "genericName",
                "");
        final List<String> errors = validator.validate(classContainerDescriptor);

        if (errors.size() > 0) {
             for (String error : errors) {
                getLog().error(error);
            }
            getLog().error("Model is invalid (see errors above).");
        } else {
            getLog().info("Model is valid.");
        }
        getLog().info("HermodValidator ended.");
    }
}
