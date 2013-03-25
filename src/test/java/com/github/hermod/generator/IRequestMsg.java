package com.github.hermod.generator;

import com.github.hermod.ser.descriptor.AField;
import com.github.hermod.ser.descriptor.AMessage;

/**
 * <p>MyMessage. </p>
 *
 * @author anavarro - Mar 10, 2013
 *
 */
@AMessage(id=1, name = "RequestMsg", responseMessages = {ResponseMsg.class})
public interface IRequestMsg {
    
    
    /**
     * getName.
     *
     * @return
     */
    @AField(id=1, name="name")
    String getName();
    
    void setName(String aName);
    
    @AField(id=2, name="requestId")
    int getRequestId();
    
    
    void setRequestId(int requestId);
    
    @AField(id=3, name="status")
    Status getStatus();
    
    void setStatus(Status status);
    
    @AField(id=4, name="parameter")
    Parameter getParameter();
    
    void setParameter(Parameter parameter);
    

}
