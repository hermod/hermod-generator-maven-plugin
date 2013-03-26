package com.github.hermod.generator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * <p>ClassesDescriptor. </p>
 * 
 * @author anavarro - Mar 10, 2013
 * 
 */
public final class ClassContainerDescriptor extends ClassDescriptor {

    private final List<ClassDescriptor> classes;

    /**
     * Constructor.
     * 
     * @param aName
     * @param aId TODO
     * @param aPrefixImplementationName TODO
     * @param aPrefixInterfaceName TODO
     * @param aSuffixImplementationPackageName TODO
     * @param aSerializableImplementationClass TODO
     * @param aClasses
     * @param aPackageName
     */
    public ClassContainerDescriptor(final String aName, final int aId, final String aPrefixImplementationName, final String aPrefixInterfaceName, String aSuffixImplementationPackageName, String aSerializableImplementationClass, final List<ClassDescriptor> aClasses, final String aPackageName) {
        super(aName, aPackageName, aId, "", "", aPrefixImplementationName, aPrefixInterfaceName, aSuffixImplementationPackageName, aSerializableImplementationClass, Collections.<FieldDescriptor> emptyList(), Collections.<MethodDescriptor> emptyList(), Collections.<ClassDescriptor> emptyList());
        this.classes = aClasses;
        //this.validate();
    }

    /**
     * Constructor.
     * @param aName TODO
     * @param aClasses
     */
    public ClassContainerDescriptor(final String aName, final int id, final String aPrefixImplementationName, final String aPrefixInterfaceName, String aSerializableImplementationClass, final List<ClassDescriptor> aClasses) {
        this(aName, id, aPrefixImplementationName, aPrefixInterfaceName, (aClasses != null && aClasses.size() >= 1) ? aClasses.get(0).getSuffixImplementationPackageName() : "", null, aClasses, (aClasses != null && aClasses.size() >= 1) ? aClasses.get(0).getPackageName() : "");
    }

    /**
     * validate.
     * 
     */
    public List<String> validate() {
        final List<String> errors = new ArrayList<>();
        if (this.classes != null && this.classes.size() >= 1) {
            final Map<Integer, ClassDescriptor> idMap = Maps.newHashMap();
            for (final ClassDescriptor classDescriptor : this.classes) {                
//                Preconditions.checkArgument(!idMap.containsKey(classDescriptor.getId()),
//                            "The Message Id is %s for message %s is already used for %s, it must be unique.", classDescriptor.getId(),
//                            classDescriptor.getImplementationName(), (idMap.get(classDescriptor.getId()) != null) ?idMap.get(classDescriptor.getId()).getImplementationName() : "");
                if (idMap.containsKey(classDescriptor.getId())) {
                    errors.add(String.format("The Message Id is %s for message %s is already used for %s, it must be unique.", classDescriptor.getId(),
                            classDescriptor.getImplementationName(), (idMap.get(classDescriptor.getId()) != null) ?idMap.get(classDescriptor.getId()).getImplementationName() : ""));
                }
                
                idMap.put(classDescriptor.getId(), classDescriptor);
            }
            for (final ClassDescriptor classDescriptor : this.classes) {
                //Preconditions.checkArgument(this.getPackageName().equals(classDescriptor.getPackageName()), "The class %s don't have the same package of this class %s, you must set a package.", classDescriptor.getImplementationName(), this.classes.get(0).getImplementationName());
                if (!this.getPackageName().equals(classDescriptor.getPackageName())) {
                    errors.add(String.format("The class %s don't have the same package of this class %s, you must set a package.", classDescriptor.getImplementationName(), this.classes.get(0).getImplementationName()));
                }
            }
        }
        for (final ClassDescriptor classDescriptor : this.getClasses()) {
            errors.addAll(classDescriptor.validate());
        }
        return errors;
    }


    /**
     * getClasses.
     * 
     * @return
     */
    public List<ClassDescriptor> getClasses() {
        return this.classes;
    }

    
}
