package com.github.hermod.generator.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hermod.generator.Parser;
import com.github.hermod.generator.model.ClassContainerDescriptor;
import com.github.hermod.generator.model.ClassDescriptor;
import com.github.hermod.generator.model.EnumDescriptor;
import com.github.hermod.generator.model.FieldDescriptor;
import com.github.hermod.generator.model.MethodDescriptor;
import com.github.hermod.ser.descriptor.AEnum;
import com.github.hermod.ser.descriptor.AField;
import com.github.hermod.ser.descriptor.AInterface;
import com.github.hermod.ser.descriptor.AMessage;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * <p>
 * ClassParser.
 * </p>
 * 
 * @author anavarro - Mar 10, 2013
 * 
 */
public final class AnnotatedClassParser
    implements Parser
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedClassParser.class);
    
    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.generator.Parser#parse(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public ClassContainerDescriptor parse(final String modelName, final String packageToScan, final String aPrefixImplementationName,
                                          final String aPrefixInterfaceName, final String aPackageToAdd, final String aName,
                                          final String aSerializedImplementationClass)
    {
        LOGGER.info("AnnotatedClassParser.parse() started ...");
        final Class<? extends Annotation> annotatedClass;
        try
        {
            final Reflections reflections = new Reflections(Thread.currentThread().getContextClassLoader(), packageToScan );
            annotatedClass = (Class<? extends Annotation>) Class.forName(modelName);
            final List<Class<?>> classes = new ArrayList<Class<?>>(reflections.getTypesAnnotatedWith(annotatedClass));
            final List<ClassDescriptor> messageDescriptors = new ArrayList<>(classes.size());
            final List<Class<?>> enums = new ArrayList<Class<?>>(reflections.getTypesAnnotatedWith(AEnum.class));
            final List<EnumDescriptor> enumsDescriptors = new ArrayList<>(enums.size());
            for (int i = 0; i < classes.size(); i++)
            {
                final Class<?> clazz = classes.get(i);
                
                final ClassDescriptor messageDescriptor = createClassDescriptor(aPrefixImplementationName,
                                                                                aPrefixInterfaceName,
                                                                                aPackageToAdd,
                                                                                aSerializedImplementationClass,
                                                                                i,
                                                                                clazz);
                
                messageDescriptors.add(messageDescriptor);
            }
            
            for (Class<?> clazzEnum: enums)
            {
                if (clazzEnum.isEnum())
                    enumsDescriptors.add(new EnumDescriptor((Class<Enum<?>>) clazzEnum));
            }
            
            final ClassContainerDescriptor classContainerDescriptor = new ClassContainerDescriptor("NotUsed",
                                                                                                   aName,
                                                                                                   1,
                                                                                                   aPrefixImplementationName,
                                                                                                   aPrefixInterfaceName,
                                                                                                   aSerializedImplementationClass,
                                                                                                   messageDescriptors,
                                                                                                   enumsDescriptors);
            LOGGER.info("classContainerDescriptor=" + classContainerDescriptor);
            LOGGER.info("AnnotatedClassParser.parse() ended.");
            return classContainerDescriptor;
        }
        catch (final ClassNotFoundException e)
        {
            throw new IllegalArgumentException("The Annotation " + modelName + " is not Found (should be "
                                               + AInterface.class.getCanonicalName() + " or " + AMessage.class.getCanonicalName() + ").", e);
        }
        
    }
    
    /**
     * createClassDescriptor.
     * 
     * @param aPrefixImplementationName
     * @param aPrefixInterfaceName
     * @param aPackageToAdd
     * @param aSerializedImplementationClass
     * @param i
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    private ClassDescriptor createClassDescriptor(final String aPrefixImplementationName, final String aPrefixInterfaceName,
                                                  final String aPackageToAdd, final String aSerializedImplementationClass, int i,
                                                  final Class<?> clazz)
    {
        int id = 0;
        final String packageName = clazz.getPackage().getName();
        String name = clazz.getSimpleName();
        final AMessage message = clazz.getAnnotation(AMessage.class);
        String docName = "";
        String docComment = "";
        Class<?>[] responseMessageClasses = {};
        final Set<EnumDescriptor> enums = new HashSet<>();
        if (message != null)
        {
            id = message.id();
            name = ("".equals(message.name())) ? clazz.getSimpleName() : message.name();
            docName = message.docName();
            docComment = message.docComment();
            responseMessageClasses = message.responseMessages();
        }
        else
        {
            id = i;
        }
        
        final Method[] methods = clazz.getMethods();
        final List<FieldDescriptor> fieldDescriptors = new ArrayList<>(methods.length);
        final List<MethodDescriptor> methodDescriptors = new ArrayList<>(methods.length);
        
        for (final Method method : methods)
        {
            final AField field = method.getAnnotation(AField.class);
            if (field != null)
            {
                final Class<?> returnType = method.getReturnType();
                final FieldDescriptor fieldDescriptor = new FieldDescriptor(method,
                                                                            field.name(),
                                                                            field.id(),
                                                                            field.docName(),
                                                                            field.docComment(),
                                                                            getAdequateName(returnType, packageName),
                                                                            field.mandatory(),
                                                                            returnType);
                fieldDescriptors.add(fieldDescriptor);
                if (fieldDescriptor.isEnum())
                {
                    enums.add(new EnumDescriptor((Class<Enum<?>>) returnType));
                }
            }
            
            final List<String> parameterTypes = new ArrayList<>(method.getParameterTypes().length);
            for (final Class<?> parameterType : method.getParameterTypes())
            {
                parameterTypes.add(getAdequateName(parameterType, packageName));
            }
            final MethodDescriptor methodDescriptor = new MethodDescriptor(method.getName(), getAdequateName(method.getReturnType(),
                                                                                                             packageName), parameterTypes);
            methodDescriptors.add(methodDescriptor);
            
        }
        
        final Predicate<Class<?>> pAnnotedClass = new Predicate<Class<?>>() {
            public boolean apply(Class<?> aClazz) {
                return aClazz.getAnnotation(AMessage.class) != null;
            }
        };
        
        final Function<Class<?>, AMessage> fToString = new Function<Class<?>, AMessage>() {
            public AMessage apply(Class<?> aClazz) {
                return aClazz.getAnnotation(AMessage.class);
            }
        };
        
        
        final Collection<AMessage> responseMessages = Collections2.transform(Collections2.filter(Arrays.asList(responseMessageClasses), pAnnotedClass), fToString);
//        final List<String> responseMessages = new ArrayList<>(responseMessageClasses.length);
//        for (final Class<?> responseMessageClass : responseMessageClasses)
//        {
//            // TODO should be optimized with a cache
//            responseMessages.add(createClassDescriptor(aPrefixImplementationName,
//                                                       aPrefixInterfaceName,
//                                                       aPackageToAdd,
//                                                       aSerializedImplementationClass,
//                                                       i,
//                                                       responseMessageClass));
//        }
        
        final ClassDescriptor messageDescriptor = new ClassDescriptor(clazz.getSimpleName(),
                                                                      name,
                                                                      packageName,
                                                                      id,
                                                                      docName,
                                                                      docComment,
                                                                      aPrefixImplementationName,
                                                                      aPrefixInterfaceName,
                                                                      aPackageToAdd,
                                                                      aSerializedImplementationClass,
                                                                      fieldDescriptors,
                                                                      methodDescriptors,
                                                                      responseMessages,
                                                                      new ArrayList<EnumDescriptor>(enums),
                                                                      message.sender()); // TODO
        return messageDescriptor;
    }
    
    /**
     * getAdequateName.
     * 
     * @param clazz
     * @param currentPackage
     * @return
     */
    // TODO refactor it to add in FieldDescriptor
    public String getAdequateName(final Class<?> clazz, final String currentPackage)
    {
        return (clazz.getPackage() == null || (clazz.getPackage().getName().equals("java.lang")) || (clazz.getPackage().getName()
                .equals(currentPackage))) ? clazz.getSimpleName() : clazz.getCanonicalName();
    }
}
