package com.github.hermod.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.hermod.generator.impl.MustacheGenerator;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * <p>
 * HermodGeneratorMojo.
 * </p>
 * 
 * @author anavarro - Mar 20, 2013
 * 
 */
@Mojo(name = "annotation-generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
// (since Maven 3.0))
public final class AnnotatedGeneratorMojo
    extends AbstractHermodMojo
{
    @Parameter(property = "templateFileName", defaultValue = "${project.directory}/src/main/resources/template.java.mustache")
    private File            templateFileName; // templateName.extension.mustache don't generate is blank
                                              
    @Parameter(property = "outputDir", defaultValue = "${project.build.directory}/generated-sources")
    private File            outputDir;
    
    @Parameter(defaultValue = "${project.artifacts}", required = true, readonly = true)
    protected Set<Artifact> projectArtifacts;
    
    /**
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException
    {
        if (skip)
        {
            getLog().info("HermodBasicGeneratorMojo skipped (see conf).");
            return;
        }
        
        getLog().info("HermodBasicGeneratorMojo:annotation-generate started.");
        
        outputDir.mkdirs();
        if (!outputDir.exists())
            throw new MojoExecutionException("Impossible to create Output dir: {}" + outputDir);
        
        if (modelInput == null)
            throw new MojoExecutionException("modelInput must be set");
        
        for (Object value : project.getCompileDependencies())
        {
            System.err.println(value);
        }
        
        try
        {
            configurePluginClasspath();
            
            final File templateFile = templateFileName;
            if (!templateFile.exists())
                throw new MojoExecutionException("Template file doesn't exist: " + templateFile.getAbsolutePath());
            
            final Matcher match = scan(templateFile.getName());
            if (!match.matches())
                throw new MojoExecutionException("template file doesn't match the patter: '(.*).extension.mustache' : "
                                                 + templateFile.getName());
            
            outputDir = new File(outputDir, match.group(1) + "." + match.group(2));
            outputDir.createNewFile();
            getLog().info("start Generation based on " + templateFileName + " , " + outputDir);
            final Map<String, Object> scopeMap = new HashMap<String, Object>();
            scopeMap.put("capitalize", MustacheGenerator.capitalizeFunction);
            scopeMap.put("upper", MustacheGenerator.upperFunction);
            scopeMap.putAll(updateMustachScope());
            
            try (final Writer writer = new OutputStreamWriter(new FileOutputStream(outputDir, false)))
            {
                final MustacheFactory mf = new DefaultMustacheFactory();
                
                Mustache mustache = null;
                if (templateFileName.exists())
                {
                    try (FileReader reader = new FileReader(templateFileName))
                    {
                        mustache = mf.compile(reader, templateFileName.getName());
                    }
                }
                else
                {
                    getLog().error("File not found, we are looking up into classpath: " + templateFileName.getName());
                    mustache = mf.compile(templateFileName.getName());
                }
                mustache.execute(writer, scopeMap);
                writer.flush();
                getLog().info(outputDir + " generated.");
            }
            catch (IOException e)
            {
                getLog().warn("Impossible to generate e=", e);
            }
        }
        catch (Exception aException)
        {
            getLog().error(aException);
            throw new MojoExecutionException("Failed to instanciate " + modelInput);
        }
        getLog().info("HermodBasicGeneratorMojo ended.");
    }
    
    private static Matcher scan(String aText)
    {
        final Pattern p = Pattern.compile("(.*)\\.(.*)\\.(mustache)$");
        return p.matcher(aText);
    }
    
}
