package com.github.hermod.generator;

import com.github.hermod.ser.descriptor.Field;
import com.github.hermod.ser.descriptor.Message;

/**
 * <p>MyMessage. </p>
 *
 * @author anavarro - Mar 10, 2013
 *
 */
@Message(id=1, name = "RequestMsg", responseMessages = {ResponseMsg.class})
public interface IRequestMsg {
    
    
    /**
     * getName.
     *
     * @return
     */
    @Field(id=1, name="name")
    String getName();
    
    void setName(String aName);
    
    @Field(id=2, name="requestId")
    int getRequestId();
    
    
    void setRequestId(int requestId);
    
    @Field(id=3, name="status")
    Status getStatus();
    
    void setStatus(Status status);
    
    @Field(id=4, name="parameter")
    Parameter getParameter();
    
    void setParameter(Parameter parameter);
    

}
