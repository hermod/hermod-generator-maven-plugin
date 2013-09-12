package com.github.hermod.generator.model;

import java.util.ArrayList;
import java.util.List;

import com.github.hermod.ser.descriptor.AEnum;
import com.github.hermod.ser.descriptor.IEnumIntConverter;

/**
 * <p>
 * Field.
 * </p>
 * 
 * @author anavarro - Mar 9, 2013
 * 
 */
public final class EnumDescriptor
{
    private final Class<? extends Enum> enumClass;
    protected final boolean             isImplConverter;
    final AEnum                         enumAnnotation;
    List<EnumFieldDescriptor>           enumValues;
    
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
    public EnumDescriptor(final Class<? extends Enum> aEnumClass)
    {
        super();
        this.enumClass = aEnumClass;
        isImplConverter = IEnumIntConverter.class.isAssignableFrom(aEnumClass);
        enumAnnotation =  aEnumClass.getAnnotation(AEnum.class);
        enumValues = new ArrayList<>();
        for (Object enumF : enumClass.getEnumConstants())
        {
            final EnumFieldDescriptor desc = new EnumFieldDescriptor((Enum<?>)enumF, isImplConverter);
            enumValues.add(desc);
        }
    }
    
    public String getName()
    {
        return (enumAnnotation == null || enumAnnotation.name().isEmpty()) ? enumClass.getSimpleName() : enumAnnotation.name();
    }
    
    public String getJavaName()
    {
        return enumClass.getSimpleName();
    }
    
    public String getJavaPackage()
    {
        return enumClass.getPackage().getName();
    }
    
    public String getDocName()
    {
        return (enumAnnotation == null) ? "" : enumAnnotation.docName();
    }
    
    public String getDocComment()
    {
        return enumAnnotation == null ? "" : enumAnnotation.docComment();
    }
    
    public List<EnumFieldDescriptor> getFields()
    {
        return enumValues;
    }
    
    /**
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return super.toString();
    }
    
}
