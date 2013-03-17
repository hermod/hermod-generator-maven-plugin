package com.github.hermod.generator.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * <p>MustacheGenerator. </p>
 * 
 * @author anavarro - Mar 10, 2013
 * 
 */
public final class MustacheGenerator implements Generator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MustacheGenerator.class);

    final private Function<String, String> capitalizeFunction = new Function<String, String>() {

        /**
         * apply.
         * 
         * @param aInput
         * @return
         */
        public String apply(String aInput) {
            return StringUtils.capitalize(aInput);
        }

    };

    /**
     * substring1Function
     */
    final private Function<String, String> upperFunction = new Function<String, String>() {

        /**
         * apply.
         * 
         * @param aInput
         * @return
         */
        public String apply(String aInput) {
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
     */
    public void generateSourceClasses(final ClassContainerDescriptor classContainerDescriptor, String aOutputDir, String aTemplateFileName,
            boolean generateOneFileForEachClass, final ClassType aGeneratedClassType) {
        LOGGER.info("MustacheGenerator.generateClasses started.");
        if (generateOneFileForEachClass) {
            for (final ClassDescriptor aClassDescription : classContainerDescriptor.getClasses()) {
                generateSourceClass(aClassDescription, aOutputDir, aTemplateFileName, aGeneratedClassType);
            }
        } else {
            generateSourceClass(classContainerDescriptor, aOutputDir, aTemplateFileName, aGeneratedClassType);
        }

        LOGGER.info("MustacheGenerator.generateClasses ended.");
    }

    /**
     * generateClass.
     * 
     * @param classDescriptor
     * @param outputDir
     * @param templateFileName
     */
    public void generateSourceClass(final ClassDescriptor classDescriptor, final String outputDir, final String templateFileName, final ClassType aGeneratedClassType) {

        final Map<String, Object> scopeMap = new HashMap<String, Object>();
        scopeMap.put("capitalize", this.capitalizeFunction);
        scopeMap.put("upper", this.upperFunction);
        scopeMap.put("class", classDescriptor);
        try {
            Preconditions.checkArgument(StringUtils.endsWith(templateFileName, ".mustache"), "The templateFileName must end with .mustache.");
            final List<String> splitTemplateFileName = Lists.newArrayList(Splitter.on(".").split(templateFileName));
            Preconditions
                    .checkArgument(splitTemplateFileName.size() == 3,
                            "The templateFileName must have 2 points (ended .mustache) to determine the extention of the file. Ex : interface-api.java.mustache.");
            final String extension = "." + splitTemplateFileName.get(1);
            final String fileName = outputDir + File.separator + classDescriptor.getPackageName(aGeneratedClassType).replace(".", File.separator) + File.separator
                    + classDescriptor.getName(aGeneratedClassType) + extension;
            Files.createParentDirs(new File(fileName));
            final Writer writer = new OutputStreamWriter(new FileOutputStream(fileName));
            final MustacheFactory mf = new DefaultMustacheFactory();

            final Mustache mustache = mf.compile(templateFileName);
            mustache.execute(writer, scopeMap);
            writer.flush();
            LOGGER.info(fileName + " generated.");
        } catch (IOException e) {
            //
            LOGGER.warn("Impossible to generate e=", e);
        }

    }

}
