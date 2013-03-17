package com.github.hermod.generator.model;

import java.util.Collection;

/**
 * <p>Method. </p>
 *
 * @author anavarro - Mar 9, 2013
 *
 */
public final class MethodDescriptor {

    private final String name;
    private final String returnType;
    private final Collection<Element<String>> parameterTypes;
    
    /**
     * Constructor.
     *
     * @param name
     * @param returnType
     * @param parameterTypes
     */
    public MethodDescriptor(String name, String returnType, Collection<String> parameterTypes) {
        super();
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = new DecoratedCollection<String>(parameterTypes);
    }

    /**
     * getName.
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * getReturnType.
     *
     * @return
     */
    public String getReturnType() {
        return this.returnType;
    }

    /**
     * getParameterTypes.
     *
     * @return
     */
    public Collection<Element<String>> getParameterTypes() {
        return this.parameterTypes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.parameterTypes == null) ? 0 : this.parameterTypes.hashCode());
        result = prime * result + ((this.returnType == null) ? 0 : this.returnType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MethodDescriptor other = (MethodDescriptor) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        if (this.parameterTypes == null) {
            if (other.parameterTypes != null)
                return false;
        } else if (!this.parameterTypes.equals(other.parameterTypes))
            return false;
        if (this.returnType == null) {
            if (other.returnType != null)
                return false;
        } else if (!this.returnType.equals(other.returnType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MethodDescriptor [name=" + this.name + ", returnType=" + this.returnType + ", parameterTypes=" + this.parameterTypes + "]";
    }
    
    
    
    
    
    
    
    
}
