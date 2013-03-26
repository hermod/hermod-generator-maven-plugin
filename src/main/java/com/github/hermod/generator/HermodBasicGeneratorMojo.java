package com.github.hermod.generator;

import java.io.File;
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
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES ,
requiresDependencyResolution = ResolutionScope.COMPILE,
requiresDependencyCollection = ResolutionScope.COMPILE) // (since Maven 3.0))
public final class HermodBasicGeneratorMojo extends AbstractMojo {

    // Parser options
    @Parameter(property = "modelType", defaultValue = "ANNOTATED_CLASSES")
    private String modelType;
    
    @Parameter(property = "modelName", defaultValue = "com.github.hermod.ser.descriptor.AMessage")
    private String modelName;

    @Parameter(property = "packageToScan", defaultValue = "")
    private String packageToScan = "";
    
    // Generator options
    @Parameter(property = "generateFileForEachClass", defaultValue = "false")
    private boolean generateFileForEachClass;
    
    @Parameter(property = "templateFileName", defaultValue = "messages-doc.xdoc.mustache")
    private String templateFileName; // templateName.extension.mustache don't generate is blank
    
    @Parameter(property = "generatePackageLayoutInOutputDir", defaultValue = "false")
    private boolean generatePackageLayoutInOutputDir;
    
    @Parameter(property = "outputDir", defaultValue = "${project.build.directory}/generated-site")
    private String outputDir;
    
    /**
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("HermodBasicGeneratorMojo started.");
        final Parser classParser = new AnnotatedClassParser();
        final Validator<ClassContainerDescriptor> validator = new ClassContainerDescriptorValidator();
        final Generator generator = new MustacheGenerator();

        final String normalizedName = this.templateFileName.contains(File.separator) ? StringUtils.substringAfterLast(StringUtils.substringBefore(this.templateFileName, "."), File.separator) : StringUtils.substringBefore(this.templateFileName, ".");
        final ClassContainerDescriptor classContainerDescriptor = classParser.parse(this.modelName, this.packageToScan, "",
                "", "", normalizedName,
                "");
        final List<String> errors = validator.validate(classContainerDescriptor);

        if (errors.size() > 0) {
            for (String error : errors) {
                getLog().error(error);
            }
            getLog().error("Model is invalid (see errors above).");
            throw new IllegalArgumentException("Model is invalid");
        } else {
                generator.generateSourceClasses(classContainerDescriptor, this.outputDir, this.generatePackageLayoutInOutputDir,
                        this.templateFileName, this.generateFileForEachClass, ClassType.BASIC);
        }
        getLog().info("HermodBasicGeneratorMojo ended.");
    }
}
