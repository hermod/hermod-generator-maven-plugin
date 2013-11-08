package com.github.hermod.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reflections.Reflections;

import com.github.hermod.generator.model.EnumDescriptor;
import com.github.hermod.ser.descriptor.AEnum;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class Util
{

    @SuppressWarnings("unchecked")
    final static List<EnumDescriptor> getEnums(final String aPackageName)
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

    static Matcher scan(String aText)
    {
        final Pattern p = Pattern.compile("(.*)\\.(.*)\\.(mustache)$");
        return p.matcher(aText);
    }

    public final static void populateExistingFiles(Map<String, Object> enrichedScope, final File outputPackage, final File baseDir2)
    {
        File[] files = outputPackage.listFiles();
        Function<File, String> fileToString = new Function<File, String>()
        {
            @Override
            public String apply(File input)
            {
                return baseDir2.toPath().relativize(input.toPath()).toString();
                //return input.getPath().replace(baseDir2.getPath(), "").replaceFirst("^..", ""); // CRA-CRA
            }
        };
        Collection<String> filesStr =  Collections2.transform(Arrays.asList(files), fileToString);
        enrichedScope.put("files", filesStr);
        enrichedScope.put("filesDir", baseDir2.toPath().relativize(outputPackage.toPath()).toString());
    }
    
}
