package com.github.hermod.generator;

import com.github.hermod.ser.descriptor.AField;
import com.github.hermod.ser.descriptor.AMessage;

/**
 * <p>MyMessage. </p>
 *
 * @author anavarro - Mar 10, 2013
 *
 */
@AMessage(id=2)
public interface ResponseMsg {
    
    /**
     * getName.
     *
     * @return
     */
    @AField(id=1, name="name")
    String getName();
    
    void setName(String aName);
    
    @AField(id=2, name="qty")
    int getQty();
    
    void setQty(int aQty);

}
