package com.github.hermod.generator.model;

import java.lang.reflect.Field;

import com.github.hermod.ser.descriptor.AEnumValue;
import com.github.hermod.ser.descriptor.IEnumIntConverter;

/**
 * <p>
 * Field.
 * </p>
 * 
 * @author anavarro - Mar 9, 2013
 * 
 */
public final class EnumFieldDescriptor
{
    final Enum<?> value;
    final AEnumValue enumValueAnnotation;
    final boolean isImplConverter;
    
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
    public EnumFieldDescriptor(final Enum<?> aEnum, boolean aIsImplConverter)
    {
        super();
        this.value = aEnum;
        final String field = aEnum.toString();  
        Field rField = null;
        try
        {
            rField = aEnum.getClass().getField(field);
        }
        catch (Exception aException)
        {
            System.err.println("unknow field: "+field);
        }
        enumValueAnnotation = rField == null ? null : rField.getAnnotation(AEnumValue.class);
        isImplConverter = aIsImplConverter;
    }
    
    public String getName()
    {
        return (enumValueAnnotation == null || enumValueAnnotation.docName().isEmpty()) ? value.name() : enumValueAnnotation.docName();
    }

    public String getJavaName()
    {
        return value.name();
    }

    public String getDocName()
    {
        return enumValueAnnotation == null ? "" : enumValueAnnotation.docName();
    }

    public String getDocComment()
    {
        return enumValueAnnotation == null ? "" : enumValueAnnotation.docComment();
    }
    
    public int getId()
    {
        return isImplConverter ? ((IEnumIntConverter) value).convert() : value.ordinal(); 
    }
    
    /**
     * Returns the value.
     *
     * @return The value to return.
     */
    public Enum<?> getValue()
    {
        return value;
    }
    
    /**
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "name: "+getName()+" , JavaName: "+getJavaName()+" , docName: "+getDocName()+" , docComment: "+getDocComment()+" , id:"+getId();
    }
}
