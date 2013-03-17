package com.github.hermod.generator;

import org.apache.commons.lang3.StringUtils;

import com.github.hermod.generator.impl.AnnotatedClassParser;
import com.github.hermod.generator.impl.MustacheGenerator;
import com.github.hermod.ser.descriptor.InterfaceApi;
import com.github.hermod.ser.descriptor.Message;



/**
 * <p>Generator. </p>
 * 
 * @author anavarro - Mar 9, 2013
 * 
 */
public final class Main {

    private static boolean generateFileForEachClass = true;
    
    private static String packageToScan = ""; // default = ""
    private static String outputDir = "./target/generated-sources/hermod"; // 
    
    private static String modelType = "ANNOTATED_CLASSES"; // ANNOTATED_CLASSES
    private static String modelName = Message.class.getCanonicalName();// "com.github.hermod.ser.descriptor.Message.class";
    
    private static String implementationTemplateFileName = "DefaultMessage.java.mustache"; // templateName.extension.mustache don't generate is blank
    private static String interfaceTemplateFileName = "Message.java.mustache"; // templateName.extension.mustache don't generate is blank
    
    private static String prefixImplementationClass = "Hermod"; // default = "Hermod"
    private static String prefixInterfaceClass = ""; // default = ""
    private static String suffixImplementationPackage = "hermod"; // default = "hermod"
    private static String serializedImplementationClass = "com.github.hermod.ser.impl.KeyObjectMsg";
    
    private static String name = "";// useful if generateFileForEachClass=false  // StringUtils.substringBefore(templateFileName, ".")
    
    
    
    /**
     * main.
     * 
     * @param args
     */
    public static void main(String[] args) {
        final Parser classParser = new AnnotatedClassParser();
        final Generator generator = new MustacheGenerator();
   
        final String interfaceTemplateFileName = "InterfaceApi.java.mustache";
        generator.generateSourceClasses(classParser.parse(InterfaceApi.class.getCanonicalName(), packageToScan, "", prefixInterfaceClass, suffixImplementationPackage, StringUtils.substringBefore(interfaceTemplateFileName, "."), serializedImplementationClass), outputDir, interfaceTemplateFileName, generateFileForEachClass, ClassType.INTERFACE);
   
        
        final String messageTemplateFileName = "Message.java.mustache";
        generator.generateSourceClasses(classParser.parse(Message.class.getCanonicalName(), packageToScan, "", prefixInterfaceClass, suffixImplementationPackage, StringUtils.substringBefore(messageTemplateFileName, "."), serializedImplementationClass), outputDir, messageTemplateFileName, generateFileForEachClass, ClassType.INTERFACE);
        
        final String defaultMessageTemplateFileName = "DefaultMessage.java.mustache";
        generator.generateSourceClasses(classParser.parse(Message.class.getCanonicalName(), packageToScan, "Hermod", prefixInterfaceClass, "hermod", StringUtils.substringBefore(defaultMessageTemplateFileName, "."), serializedImplementationClass), outputDir, defaultMessageTemplateFileName, generateFileForEachClass, ClassType.CLASS);
   
        
        final String msgFactoryTemplateFileName = "MsgFactory.java.mustache";
        generator.generateSourceClasses(classParser.parse(Message.class.getCanonicalName(), packageToScan, "", prefixInterfaceClass, suffixImplementationPackage, StringUtils.substringBefore(msgFactoryTemplateFileName, "."), serializedImplementationClass), outputDir, msgFactoryTemplateFileName, false, ClassType.INTERFACE);

        final String defaultMsgFactoryTemplateFileName = "DefaultMsgFactory.java.mustache";
        generator.generateSourceClasses(classParser.parse(Message.class.getCanonicalName(), packageToScan, "Hermod", prefixInterfaceClass, "hermod", "MsgFactory", serializedImplementationClass), outputDir, defaultMsgFactoryTemplateFileName, false, ClassType.CLASS);

        
    }

}