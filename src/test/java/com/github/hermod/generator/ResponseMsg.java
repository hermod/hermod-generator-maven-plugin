package com.github.hermod.generator;

import com.github.hermod.ser.descriptor.Field;
import com.github.hermod.ser.descriptor.Message;

/**
 * <p>MyMessage. </p>
 *
 * @author anavarro - Mar 10, 2013
 *
 */
@Message(id=2)
public interface ResponseMsg {
    
    /**
     * getName.
     *
     * @return
     */
    @Field(id=1, name="name")
    String getName();
    
    void setName(String aName);
    
    @Field(id=2, name="qty")
    int getQty();
    
    void setQty(int aQty);

}
