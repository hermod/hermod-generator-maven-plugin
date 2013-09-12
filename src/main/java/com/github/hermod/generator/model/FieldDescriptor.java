package com.github.hermod.generator.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.descriptor.AMessage;

/**
 * <p>
 * Field.
 * </p>
 * 
 * @author anavarro - Mar 9, 2013
 * 
 */
public final class FieldDescriptor
{
    private final Method         method;
    private final String         name;
    private final int            id;
    private final String         docName;
    private String               docComment;
    private final String         type;
    private final String         packageName;
    private final boolean        mandatory;
    private final boolean        isEnum;
    private final boolean        isArray;
    private final boolean        isBasicType;
    private final boolean        isMessage;
    private final boolean        isUuid;
    
    // TODO maybe move in hermod-ser
    private final List<Class<?>> BASIC_MANAGED_TYPES = Arrays.<Class<?>> asList(boolean.class,
                                                                                byte.class,
                                                                                short.class,
                                                                                int.class,
                                                                                long.class,
                                                                                double.class,
                                                                                float.class,
                                                                                String.class);
    
    /**
     * Constructor.
     * 
     * @param aMethod
     * 
     * @param aName
     * @param aId
     * @param aDocName
     * @param aDocComment
     *            TODO
     * @param aType
     * @param aMandatory
     */
    public FieldDescriptor(Method aMethod, String aName, int aId, String aDocName, String aDocComment, String aType, boolean aMandatory,
                           Class<?> aClazz)
    {
        super();
        this.method = aMethod;
        this.name = aName;
        this.id = aId;
        this.docName = aDocName;
        this.docComment = aDocComment;
        this.mandatory = aMandatory;
        this.packageName = aClazz.getPackage() != null ? aClazz.getPackage().getName() : "";
        this.isArray = aClazz.isArray();
        this.isUuid = aClazz.equals(UUID.class);
        if (aClazz.isArray())
        {
            final Class<?> classInsideArray = aClazz.getComponentType();
            this.type = classInsideArray.getSimpleName();
            this.isEnum = classInsideArray.isEnum();
            this.isBasicType = BASIC_MANAGED_TYPES.contains(classInsideArray) ? true : false;
            this.isMessage = (classInsideArray.getAnnotation(AMessage.class) != null || classInsideArray.equals(Msg.class) || Arrays
                    .<Class<?>> asList(classInsideArray.getInterfaces()).contains(Msg.class)) ? true : false;
        }
        else
        {
            this.type = aClazz.getSimpleName();
            this.isEnum = aClazz.isEnum();
            this.isBasicType = BASIC_MANAGED_TYPES.contains(aClazz) ? true : false;
            this.isMessage = (aClazz.getAnnotation(AMessage.class) != null || aClazz.equals(Msg.class) || Arrays
                    .<Class<?>> asList(aClazz.getInterfaces()).contains(Msg.class)) ? true : false;
        }
        // update doc.
        final Size size = method.getAnnotation(Size.class);
        if (size != null)
        {
            docComment = aDocComment + "<br/>Lentgh is <b>[" + size.min() + " - " + size.max() + "]</b>";
        }
        
        // this.validate();
    }
    
    /**
     * validate.
     * 
     */
    public List<String> validate()
    {
        final List<String> errors = new ArrayList<>();
        // Preconditions.checkArgument(this.isEnum || this.isBasicType || this.isMessage,
        // "The type=%s for field name=%s must be a BasicType (%s) or an Enum or a Message", this.type, this.name,
        // this.BASIC_MANAGED_TYPES.toString());
        if (!(this.isEnum || this.isBasicType || this.isMessage || this.isUuid))
        {
            errors.add(String.format("The type=%s for field name=%s must be a BasicType (%s) or an Enum or a Message",
                                     this.type,
                                     this.name,
                                     this.BASIC_MANAGED_TYPES.toString()));
        }
        return errors;
    }
    
    /**
     * getName.
     * 
     * @return
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * getCapitalizeName.
     * 
     * @return
     */
    public String getCapitalizeName()
    {
        return StringUtils.capitalize(this.name);
    }
    
    /**
     * getUpperName.
     * 
     * @return
     */
    public String getUpperName()
    {
        return this.getName().toUpperCase();
    }
    
    /**
     * getId.
     * 
     * @return
     */
    public int getId()
    {
        return this.id;
    }
    
    /**
     * getDocName.
     * 
     * @return
     */
    public String getDocName()
    {
        return this.docName;
    }
    
    /**
     * getType.
     * 
     * @return
     */
    public String getType()
    {
        return this.type;
    }
    
    public String getTypeAndArray()
    {
        return this.type + (isArray ? "[]" : "");
    }
    
    /**
     * getCapitalizeType.
     * 
     * @return
     */
    public String getCapitalizeType()
    {
        return StringUtils.capitalize(this.getType());
    }
    
    /**
     * isMandatory.
     * 
     * @return
     */
    public boolean isMandatory()
    {
        return this.mandatory;
    }
    
    /**
     * isArray.
     * 
     * @return
     */
    public boolean isArray()
    {
        return this.isArray;
    }
    
    /**
     * isEnum.
     * 
     * @return
     */
    public boolean isEnum()
    {
        return this.isEnum;
    }
    
    /**
     * Returns the isUuid.
     *
     * @return The isUuid to return.
     */
    public boolean isUuid()
    {
        return isUuid;
    }
    
    /**
     * isBasicType.
     * 
     * @return
     */
    public boolean isBasicType()
    {
        return this.isBasicType;
    }
    
    /**
     * isMessage.
     * 
     * @return
     */
    public boolean isMessage()
    {
        return this.isMessage;
    }
    
    /**
     * Returns the docComment.
     * 
     * @return The docComment to return.
     */
    public String getDocComment()
    {
        return docComment;
    }
    
    /**
     * getPackageName.
     * 
     * @return
     */
    public String getPackageName()
    {
        return this.packageName;
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FieldDescriptor other = (FieldDescriptor) obj;
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
    public String toString()
    {
        return "FieldDescriptor [name=" + this.name + ", id=" + this.id + ", docName=" + this.docName + ", type=" + this.type
               + ", mandatory=" + this.mandatory + "]";
    }
    
}
