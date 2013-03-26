package com.github.hermod.generator;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.hermod.generator.impl.AnnotatedClassParser;
import com.github.hermod.generator.impl.ClassContainerDescriptorValidator;
import com.github.hermod.generator.impl.MustacheGenerator;
import com.github.hermod.generator.model.ClassContainerDescriptor;
import com.github.hermod.ser.descriptor.AInterface;
import com.github.hermod.ser.descriptor.AMessage;



/**
 * <p>Generator. </p>
 * 
 * @author anavarro - Mar 9, 2013
 * 
 */
public final class HermodGeneratorDemo {

    
    private static String packageToScan = ""; // default = ""
    
    private static String modelName = AMessage.class.getCanonicalName();// "com.github.hermod.ser.descriptor.Message.class";
    private static String modelType = "ANNOTATED_CLASSES"; // ANNOTATED_CLASSES
    
    private static String prefixInterfaceClass = ""; // default = ""
    private static String prefixImplementationClass = "Hermod"; // default = "Hermod"
    private static String suffixImplementationPackage = "hermod"; // default = "hermod"
    private static String serializedImplementationClass = "com.github.hermod.ser.impl.KeyObjectMsg";
    
    private static boolean generateFileForEachClass = true;
    private static String implementationTemplateFileName = "DefaultMessage.java.mustache"; // templateName.extension.mustache don't generate is blank
    private static String interfaceTemplateFileName = "Message.java.mustache"; // templateName.extension.mustache don't generate is blank
    
    private static String name = "";// useful if generateFileForEachClass=false  // StringUtils.substringBefore(templateFileName, ".")
    
    private static String outputDir = "./target/generated-sources/hermod"; // 
    
    
    /**
     * main.
     * 
     * @param args
     */
    public static void main(String[] args) {
        final Parser classParser = new AnnotatedClassParser();
        final Validator<ClassContainerDescriptor> validator = new ClassContainerDescriptorValidator();
        final Generator generator = new MustacheGenerator();
   
        

        
//        final String interfaceTemplateFileName = "Interface.java.mustache";
//        final ClassContainerDescriptor classContainerDescriptor = classParser.parse(AMessage.class.getCanonicalName(), packageToScan, "", prefixInterfaceClass, suffixImplementationPackage, StringUtils.substringBefore(interfaceTemplateFileName, "."), serializedImplementationClass);
//        final List<String> errors = validator.validate(classContainerDescriptor);
//   
        
        final ClassContainerDescriptor classContainerDescriptor = classParser.parse(modelName, packageToScan, "",
                "", "", StringUtils.substringAfterLast(StringUtils.substringBefore("/home/anavarro/workspace/hermod-generator-maven-plugin-sampleb/messages-doc.xdoc.mustache", "."), File.separator),
                "");
        
        generator.generateSourceClasses(classContainerDescriptor, outputDir, false,
                "/home/anavarro/workspace/hermod-generator-maven-plugin-sampleb/messages-doc.xdoc.mustache", true, ClassType.BASIC);
        
        
        /*
        
        generator.generateSourceClasses(classContainerDescriptor, outputDir, true, interfaceTemplateFileName, generateFileForEachClass, ClassType.INTERFACE);
        
        
        
        
        final String messageTemplateFileName = "Message.java.mustache";
        generator.generateSourceClasses(classParser.parse(AMessage.class.getCanonicalName(), packageToScan, prefixInterfaceClass, "", suffixImplementationPackage, StringUtils.substringBefore(messageTemplateFileName, "."), serializedImplementationClass), outputDir, true, messageTemplateFileName, generateFileForEachClass, ClassType.INTERFACE);
        
        final String defaultMessageTemplateFileName = "DefaultMessage.java.mustache";
        generator.generateSourceClasses(classParser.parse(AMessage.class.getCanonicalName(), packageToScan, prefixImplementationClass, "", "hermod", StringUtils.substringBefore(defaultMessageTemplateFileName, "."), serializedImplementationClass), outputDir, true, defaultMessageTemplateFileName, generateFileForEachClass, ClassType.CLASS);
   
        
        final String msgFactoryTemplateFileName = "MsgFactory.java.mustache";
        generator.generateSourceClasses(classParser.parse(AMessage.class.getCanonicalName(), packageToScan, "", "", suffixImplementationPackage, StringUtils.substringBefore(msgFactoryTemplateFileName, "."), serializedImplementationClass), outputDir, true, msgFactoryTemplateFileName, false, ClassType.INTERFACE);

        final String defaultMsgFactoryTemplateFileName = "DefaultMsgFactory.java.mustache";
        generator.generateSourceClasses(classParser.parse(AMessage.class.getCanonicalName(), packageToScan, "Hermod", "", "hermod", "MsgFactory", serializedImplementationClass), outputDir, true, defaultMsgFactoryTemplateFileName, false, ClassType.CLASS);
*/
        
    }

}