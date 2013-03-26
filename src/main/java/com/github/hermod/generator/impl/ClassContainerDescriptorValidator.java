package com.github.hermod.generator.impl;

import java.util.List;

import com.github.hermod.generator.Validator;
import com.github.hermod.generator.model.ClassContainerDescriptor;

/**
 * <p>ClassContainerDescriptorValidator. </p>
 *
 * @author anavarro - Mar 24, 2013
 *
 */
public final class ClassContainerDescriptorValidator implements Validator<ClassContainerDescriptor> {

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.generator.Validator#validate(java.lang.Object)
     */
    @Override
    public List<String> validate(final ClassContainerDescriptor aClassContainerDescriptor) {
        return aClassContainerDescriptor.validate();
    }

}
