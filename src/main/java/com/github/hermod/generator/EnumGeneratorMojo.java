package com.github.hermod.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.reflections.Reflections;

import com.github.hermod.generator.impl.MustacheGenerator;
import com.github.hermod.generator.model.EnumDescriptor;
import com.github.hermod.ser.descriptor.AEnum;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Function;

/**
 * <p>
 * HermodGeneratorMojo.
 * </p>
 * 
 * @author anavarro - Mar 20, 2013
 * 
 */
@Mojo(name = "enum-annotate-generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
// (since Maven 3.0))
public final class EnumGeneratorMojo
    extends AbstractHermodMojo
{
    @Parameter(property = "templateFileName", defaultValue = "src/main/resources/Template-enum.java.mustache")
    private File            templateFileName;   // templateName.extension.mustache don't generate is blank
                                                 
    @Parameter(property = "outputDir", defaultValue = "${project.build.directory}/generated-sources")
    private File            outputDir;
    
    @Parameter(defaultValue = "${project.artifacts}", required = true, readonly = true)
    protected Set<Artifact> projectArtifacts;
    
    @Parameter(property = "annotation", defaultValue = "com.github.hermod.ser.descriptor.AEnum")
    private String          annotation;
    
    @Parameter(property = "callTemplatePerEnum", defaultValue = "true")
    private boolean         callTemplatePerEnum;
    
    @Parameter(property = "packageToScan", required = true)
    private String          packageToScan;
    
    /**
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @SuppressWarnings("unchecked")
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
            
            final Map<String, Object> scopeMap = new HashMap<String, Object>();
            scopeMap.put("capitalize", MustacheGenerator.capitalizeFunction);
            scopeMap.put("upper", MustacheGenerator.upperFunction);
            scopeMap.putAll(updateMustachScope());
            
            if (callTemplatePerEnum)
            {
                Function<String, String> filenameMapper = null;
                final String functName = "filenameMapper";
                if (scopeMap.containsKey(functName))
                {
                    filenameMapper = (Function<String, String>) scopeMap.get(functName);
                    getLog().debug("Find filenameMapper for " + functName + ", " + functName);
                }
                for (EnumDescriptor desc : getEnums(packageToScan))
                {
                    scopeMap.put("enumDescriptor", desc);
                    String fileNamePrefix = filenameMapper == null ? desc.getJavaName() : filenameMapper.apply(desc.getJavaName());
                    writeFile(scopeMap, fileNamePrefix + "." + match.group(2));
                }
            }
            else
            {
                scopeMap.put("enumDescriptors", getEnums(packageToScan));
                writeFile(scopeMap, match.group(1) + "." + match.group(2));
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
    
    private void writeFile(Map<String, Object> scopeMap, String aFileName)
    {
        final File fileDir = new File(outputDir, aFileName);
        try
        {
            fileDir.createNewFile();
            getLog().info("start Generation based on " + templateFileName + " , " + fileDir);
            
            try (final Writer writer = new OutputStreamWriter(new FileOutputStream(fileDir, false)))
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
                getLog().info(fileDir + " generated.");
            }
        }
        catch (IOException e)
        {
            getLog().warn("Impossible to generate " + fileDir, e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private final static List<EnumDescriptor> getEnums(final String aPackageName)
    {
        final Reflections reflections = new Reflections(Thread.currentThread()
                                                              .getContextClassLoader(), aPackageName);
        final List<Class<?>> enums = new ArrayList<Class<?>>(reflections.getTypesAnnotatedWith(AEnum.class));
        final List<EnumDescriptor> enumsDescriptors = new ArrayList<>(enums.size());
        
        for (Class<?> clazzEnum : enums)
        {
            if (clazzEnum.isEnum())
                enumsDescriptors.add(new EnumDescriptor((Class<Enum<?>>) clazzEnum));
        }
        
        return enumsDescriptors;
    }
}
