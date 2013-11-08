package com.github.hermod.generator.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hermod.generator.ClassType;
import com.github.hermod.generator.Generator;
import com.github.hermod.generator.Util;
import com.github.hermod.generator.model.ClassContainerDescriptor;
import com.github.hermod.generator.model.ClassDescriptor;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Function;

/**
 * <p>
 * MustacheGenerator.
 * </p>
 * 
 * @author anavarro - Mar 10, 2013
 * 
 */
public final class MustacheGenerator
    implements Generator
{
    
    private static final Logger                  LOGGER             = LoggerFactory.getLogger(MustacheGenerator.class);
    
    final public static Function<String, String> capitalizeFunction = new Function<String, String>()
                                                                    {
                                                                        
                                                                        /**
                                                                         * apply.
                                                                         * 
                                                                         * @param aInput
                                                                         * @return
                                                                         */
                                                                        public String apply(String aInput)
                                                                        {
                                                                            return StringUtils.capitalize(aInput);
                                                                        }
                                                                        
                                                                    };
    
    /**
     * substring1Function
     */
    final public static Function<String, String> upperFunction      = new Function<String, String>()
                                                                    {
                                                                        
                                                                        /**
                                                                         * apply.
                                                                         * 
                                                                         * @param aInput
                                                                         * @return
                                                                         */
                                                                        public String apply(String aInput)
                                                                        {
                                                                            return aInput.toUpperCase();
                                                                        }
                                                                        
                                                                    };
    
    /**
     * generateClasses.
     * 
     * @param classContainerDescriptor
     * @param aOutputDir
     * @param aTemplateFileName
     * @param generateOneFileForEachClasses
     * @throws IOException
     */
    public void generateSourceClasses(final ClassContainerDescriptor classContainerDescriptor, File aOutputDir,
                                      boolean generatePackageLayoutInOutputDir, File aTemplateFileName,
                                      boolean generateOneFileForEachClass, final ClassType aGeneratedClassType,
                                      Map<String, Object> enrichedScope) throws IOException
    {
        LOGGER.info("MustacheGenerator.generateClasses started.");
        // if (!aTemplateFileName.exists())
        // throw new IllegalArgumentException("Template file doesn't exist: " + aTemplateFileName.getAbsolutePath());
        
        final Matcher match = scan(aTemplateFileName.getName());
        if (!match.matches())
            throw new IllegalArgumentException("template file doesn't match the patter: '(.*).extension.mustache' : "
                                               + aTemplateFileName.getName());
        
        final String normalizedName = match.group(1) + "." + match.group(2);
        final String packageDir = ((generatePackageLayoutInOutputDir) ? classContainerDescriptor.getPackageName(aGeneratedClassType)
                                                                                                .replace(".", File.separator)
                                                                        + File.separator : "");
        final File outputPackage = new File(aOutputDir, packageDir);
        outputPackage.mkdirs();
        Util.populateExistingFiles(enrichedScope, outputPackage, aOutputDir);
        
        if (generateOneFileForEachClass)
        {
            for (final ClassDescriptor aClassDescription : classContainerDescriptor.getClasses())
            {
                final File output = new File(outputPackage, aClassDescription.getName(aGeneratedClassType) + "." + match.group(2));
                output.createNewFile();
                generateSourceClass(aClassDescription,
                                    output,
                                    generatePackageLayoutInOutputDir,
                                    aTemplateFileName,
                                    aGeneratedClassType,
                                    enrichedScope);
            }
        }
        else
        {
            final File output = new File(outputPackage, normalizedName);
            output.createNewFile();
            generateSourceClass(classContainerDescriptor,
                                output,
                                generatePackageLayoutInOutputDir,
                                aTemplateFileName,
                                aGeneratedClassType,
                                enrichedScope);
        }
        

        
        LOGGER.info("MustacheGenerator.generateClasses ended.");
    }

//    protected final static void populateExistingFiles(Map<String, Object> enrichedScope, final File outputPackage, String packageDir)
//    {
//        File[] files = outputPackage.listFiles();
//        Function<File, String> fileToString = new Function<File, String>()
//        {
//            @Override
//            public String apply(File input)
//            {
//                return input.getAbsolutePath();
//            }
//        };
//        Collection<String> filesStr =  Collections2.transform(Arrays.asList(files), fileToString);
//        enrichedScope.put("files", filesStr);
//    }
//    
    /**
     * generateClass.
     * 
     * @param classDescriptor
     * @param outputDir
     * @param generatePackageLayoutInOutputDir
     *            TODO
     * @param templateFileName
     */
    public void generateSourceClass(final ClassDescriptor classDescriptor, final File outputDir, boolean generatePackageLayoutInOutputDir,
                                    final File templateFileName, final ClassType aGeneratedClassType,
                                    final Map<String, Object> enrichedScope)
    {
        LOGGER.info("start Generation based on {}, {}", templateFileName, outputDir);
        final Map<String, Object> scopeMap = new HashMap<String, Object>();
        scopeMap.put("capitalize", capitalizeFunction);
        scopeMap.put("upper", upperFunction);
        scopeMap.put("class", classDescriptor);
        scopeMap.putAll(enrichedScope);
        try (final Writer writer = new OutputStreamWriter(new FileOutputStream(outputDir)))
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
                LOGGER.info("File not found, we are looking up into classpath: ", templateFileName.getName());
                mustache = mf.compile(templateFileName.getName());
            }
            mustache.execute(writer, scopeMap);
            writer.flush();
            LOGGER.info(outputDir + " generated.");
        }
        catch (IOException e)
        {
            LOGGER.warn("Impossible to generate e=", e);
        }
        
    }
    
    private static Matcher scan(String aText)
    {
        final Pattern p = Pattern.compile("(.*)\\.(.*)\\.(mustache)$");
        return p.matcher(aText);
    }
    
}
