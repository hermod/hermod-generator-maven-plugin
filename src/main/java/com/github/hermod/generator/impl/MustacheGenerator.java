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
    
    private static final Logger            LOGGER             = LoggerFactory.getLogger(MustacheGenerator.class);
    
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
                                      boolean generateOneFileForEachClass, final ClassType aGeneratedClassType) throws IOException
    {
        LOGGER.info("MustacheGenerator.generateClasses started.");
//        if (!aTemplateFileName.exists())
//            throw new IllegalArgumentException("Template file doesn't exist: " + aTemplateFileName.getAbsolutePath());
        
        final Matcher match = scan(aTemplateFileName.getName());
        if (!match.matches())
            throw new IllegalArgumentException("template file doesn't match the patter: '(.*).extension.mustache' : "
                                               + aTemplateFileName.getName());
        
        final String normalizedName = match.group(1) + "." + match.group(2);
        final String packageDir = ((generatePackageLayoutInOutputDir) ? classContainerDescriptor.getPackageName(aGeneratedClassType)
                .replace(".", File.separator) + File.separator : "");
        final File outputPackage = new File(aOutputDir, packageDir);
        outputPackage.mkdirs();
        
        if (generateOneFileForEachClass)
        {
            for (final ClassDescriptor aClassDescription : classContainerDescriptor.getClasses())
            {
                final File output = new File(outputPackage, aClassDescription.getName(aGeneratedClassType)+"."+match.group(2));
                output.createNewFile();
                generateSourceClass(aClassDescription, output, generatePackageLayoutInOutputDir, aTemplateFileName, aGeneratedClassType);
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
                                aGeneratedClassType);
        }
        
        LOGGER.info("MustacheGenerator.generateClasses ended.");
    }
    
    /**
     * generateClass.
     * 
     * @param classDescriptor
     * @param outputDir
     * @param generatePackageLayoutInOutputDir
     *            TODO
     * @param templateFileName
     */
    public void generateSourceClass(final ClassDescriptor classDescriptor, final File outputDir,
                                    boolean generatePackageLayoutInOutputDir, final File templateFileName,
                                    final ClassType aGeneratedClassType)
    {
        LOGGER.info("start Generation based on {}, {}", templateFileName, outputDir);
        final Map<String, Object> scopeMap = new HashMap<String, Object>();
        scopeMap.put("capitalize", this.capitalizeFunction);
        scopeMap.put("upper", this.upperFunction);
        scopeMap.put("class", classDescriptor);
        try
        {
//            Preconditions.checkArgument(StringUtils.endsWith(templateFileName, ".mustache"),
//                                        "The templateFileName must end with .mustache.");
//            final List<String> splitTemplateFileName = Lists.newArrayList(Splitter.on(".").split(templateFileName));
//            // Preconditions
//            // .checkArgument(splitTemplateFileName.size() == 3,
//            // "The templateFileName must have 2 points (ended .mustache) to determine the extention of the file. Ex : interface-api.java.mustache.");
//            final String extension = "." + splitTemplateFileName.get(splitTemplateFileName.size() - 2);
//            
///********************** */
//            final File templateFile = new File(templateFileName);
//            if (!templateFile.exists())
//                throw new IllegalArgumentException("Template file doesn't exist: "+templateFile.getAbsolutePath());
//            
//            final Matcher match = scan(templateFile.getName());
//            if (!match.matches())
//                throw new IllegalArgumentException("template file doesn't match the patter: '(.*).extension.mustache' : "+templateFile.getName());
//
//            final String normalizedName = match.group(1)+"."+match.group(2);
//
///************************/
//            
//            File fOutputDir = new File(outputDir, ((generatePackageLayoutInOutputDir) ? classDescriptor.getPackageName(aGeneratedClassType)
//                    .replace(".", File.separator) + File.separator : "")+"/"+
//                    classDescriptor.getName()+"."+match.group(2));
//            fOutputDir.mkdirs();
            
//            final String fileName = 
//                    outputDir
//                                    + File.separator
//                                    + ((generatePackageLayoutInOutputDir) ? classDescriptor.getPackageName(aGeneratedClassType)
//                                            .replace(".", File.separator) + File.separator : "")
//                                    + normalizedName;
//            Files.createParentDirs(new File(match.group(1)+""));
            final Writer writer = new OutputStreamWriter(new FileOutputStream(outputDir));
            final MustacheFactory mf = new DefaultMustacheFactory();
            
            Mustache mustache = null;
//            if (Thread.currentThread().getContextClassLoader().getResource(templateFileName) == null)
//            {
                //final File file = new File(templateFileName);
                if (templateFileName.exists())
                {
                    mustache = mf.compile(new FileReader(templateFileName), templateFileName.getName());
                }
                else
                {
                    LOGGER.info("File not found, we are looking up into classpath: ", templateFileName.getName());
                    mustache = mf.compile(templateFileName.getName());
//                    throw new IllegalArgumentException("The file " + templateFileName + " does not exist. You must specify a real file.");
                }
//            }
//            else
//            {
//                mustache = mf.compile(templateFileName);
//            }
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
