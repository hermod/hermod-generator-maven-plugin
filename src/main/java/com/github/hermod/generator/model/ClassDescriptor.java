package com.github.hermod.generator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.hermod.generator.ClassType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * <p>MessageDescriptor. </p>
 * 
 * @author anavarro - Mar 10, 2013
 * 
 */
public class ClassDescriptor {

    private final String name;
    private final String packageName;
    private final int id;
    private final String docName;
    private final String docComment;
    private final String prefixImplementationName;
    private final String prefixInterfaceName;
    private final String serializableImplementationClass;
    private final String suffixImplementationPackageName;

    private final Collection<FieldDescriptor> fields;
    private final Collection<MethodDescriptor> methods;
    private final Collection<ClassDescriptor> responseMessages;

    /**
     * Constructor.
     * 
     * @param aName
     * @param aPackageName
     * @param aId
     * @param aDocName
     * @param aDocComment TODO
     * @param aSuffixImplementationPackageName TODO
     * @param aFields
     */
    public ClassDescriptor(final String aName, final String aPackageName, final int aId, final String aDocName,
            final String aDocComment, final String aPrefixImplementationName, final String aPrefixInterfaceName,
            final String aSuffixImplementationPackageName, final String aserializableImplementationClass, final Collection<FieldDescriptor> aFields, final Collection<MethodDescriptor> aMethods, final Collection<ClassDescriptor> aResponseMessages) {
        super();
        this.name = aName;
        this.packageName = aPackageName;
        this.id = aId;
        this.docName = aDocName;
        this.docComment = aDocComment;
        this.prefixImplementationName = aPrefixImplementationName;
        this.prefixInterfaceName = aPrefixInterfaceName;
        this.serializableImplementationClass = aserializableImplementationClass;
        this.suffixImplementationPackageName = aSuffixImplementationPackageName;
        this.fields = aFields;
        this.methods = aMethods;
        this.responseMessages = aResponseMessages;
        //validate();

    }

    /**
     * validateFields.
     * 
     * @param aFields
     * @throws IllegalArgumentException
     */
    public List<String> validate() /*throws IllegalArgumentException*/ {
        final List<String> errors = new ArrayList<>();
        for (final FieldDescriptor fieldDescriptor : this.fields) {
//            Preconditions.checkArgument(fieldDescriptor.getId() >= 0, "The field Id is %s but must >= 0 for message %s", fieldDescriptor.getId(),
//                    this.getImplementationName());
            if (fieldDescriptor.getId() < 0) {
                errors.add(String.format("The field Id is %s but must >= 0 for message %s", fieldDescriptor.getId(),
                        this.getImplementationName()));
            }
//            Preconditions.checkArgument(fieldDescriptor.getName().matches("^[a-zA-Z][a-zA-Z0-9]*?$"),
//                    "The field Name is %s but must match ^[a-zA-Z][a-zA-Z0-9]*?$ for message %s", fieldDescriptor.getName(),
//                    this.getImplementationName());
            if (!fieldDescriptor.getName().matches("^[a-zA-Z][a-zA-Z0-9]*?$")) {
                errors.add(String.format("The field Name is %s but must match ^[a-zA-Z][a-zA-Z0-9]*?$ for message %s", fieldDescriptor.getName(),
                        this.getImplementationName()));
            }
        }

        final Map<Integer, FieldDescriptor> idMap = Maps.newHashMap();
        for (final FieldDescriptor fieldDescriptor : this.fields) {
//            Preconditions.checkArgument(!idMap.containsKey(fieldDescriptor.getId()),
//                    "The field Id is %s for field %s is already used for field %s, it must be unique for message %s", fieldDescriptor.getId(),
//                    fieldDescriptor.getName(), idMap.get(fieldDescriptor.getId()) != null ? idMap.get(fieldDescriptor.getId()).getName() : "",
//                    this.name);
            if (idMap.containsKey(fieldDescriptor.getId())) {
                errors.add(String.format("The field Id is %s for field %s is already used for field %s, it must be unique for message %s", fieldDescriptor.getId(),
                        fieldDescriptor.getName(), idMap.get(fieldDescriptor.getId()) != null ? idMap.get(fieldDescriptor.getId()).getName() : "",
                                this.name));
            }
            
            idMap.put(fieldDescriptor.getId(), fieldDescriptor);
            
            errors.addAll(fieldDescriptor.validate());
        }
        
        return errors;
    }

    /**
     * getName.
     * 
     * @return
     */
    public String getImplementationName() {
        return this.getPrefixImplementationName() + this.getName();
    }

    /**
     * getName.
     * 
     * @param classType
     * @return
     */
    public String getName(final ClassType classType) {
        switch (classType) {
        case INTERFACE:
            return getInterfaceName();
        case CLASS:
            return getImplementationName();
        case BASIC:
            return getName();
        default:
            return getName();
        }
    }

    /**
     * getInterfaceName.
     * 
     * @return
     */
    public String getInterfaceName() {
        return this.getPrefixInterfaceName() + this.getName();
    }

    public String getCanonicalInterfaceName() {
        return this.getPackageName() + "." + this.getInterfaceName();
    }

    /**
     * getCapitalizeName.
     * 
     * @return
     */
    public String getCapitalizeName() {
        return StringUtils.capitalize(this.getImplementationName());
    }

    /**
     * getUpperInterfaceName.
     * 
     * @return
     */
    public String getUpperInterfaceName() {
        return this.getInterfaceName().toUpperCase();
    }

    /**
     * getOriginalName.
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * getUpperOriginalName.
     * 
     * @return
     */
    public String getUpperName() {
        return this.getName().toUpperCase();
    }


    /**
     * getPackageName.
     * 
     * @return
     */
    public String getImplementationPackageName() {
        return this.getPackageName() + ("".equals(this.getSuffixImplementationPackageName()) ? "" : "." + this.getSuffixImplementationPackageName());
    }
    
    public String getInterfacePackageName() {
        return this.getPackageName();
    }

    /**
     * getPrefixName.
     * 
     * @return
     */
    public String getPrefixName() {
        return this.prefixImplementationName;
    }

    /**
     * getSuffixPackageName.
     * 
     * @return
     */
    public String getSuffixImplementationPackageName() {
        return this.suffixImplementationPackageName;
    }

    /**
     * getOriginalPackageName.
     * 
     * @return
     */
    public String getPackageName() {
        return this.packageName;
    }

    /**
     * getPackageName.
     * 
     * @param classType
     * @return
     */
    public String getPackageName(final ClassType classType) {
        switch (classType) {
        case INTERFACE:
            return getInterfacePackageName();
        case CLASS:
            return getImplementationPackageName();
        default:
            return getImplementationPackageName();
        }
    }

    /**
     * getId.
     * 
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * getDocName.
     * 
     * @return
     */
    public String getDocName() {
        return this.docName;
    }
    
    

    public String getDocComment() {
        return this.docComment;
    }

    /**
     * getFields.
     * 
     * @return
     */
    public Collection<FieldDescriptor> getFields() {
        return this.fields;
    }

    /**
     * getPrefix.
     * 
     * @return
     */
    public String getPrefixImplementationName() {
        return this.prefixImplementationName;
    }

    /**
     * getSuffix.
     * 
     * @return
     */
    public String getPrefixInterfaceName() {
        return this.prefixInterfaceName;
    }

    /**
     * getImplementationClass.
     * 
     * @return
     */
    public String getImplementationClass() {
        return this.serializableImplementationClass;
    }

    /**
     * getMethods.
     * 
     * @return
     */
    public Collection<MethodDescriptor> getMethods() {
        return this.methods;
    }
    
    

    /**
     * getResponseMessages.
     *
     * @return
     */
    public Collection<ClassDescriptor> getResponseMessages() {
        return this.responseMessages;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClassDescriptor other = (ClassDescriptor) obj;
        if (this.id != other.id)
            return false;
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ClassDescriptor [name=" + this.name + ", packageName=" + this.packageName + ", id=" + this.id + ", docName=" + this.docName
                + ", prefixImplementationName=" + this.prefixImplementationName + ", prefixInterfaceName=" + this.prefixInterfaceName
                + ", implementationClass=" + this.serializableImplementationClass + ", suffixImplementationPackageName="
                + this.suffixImplementationPackageName + ", fields=" + this.fields + ", methods=" + this.methods + "]";
    }

}
