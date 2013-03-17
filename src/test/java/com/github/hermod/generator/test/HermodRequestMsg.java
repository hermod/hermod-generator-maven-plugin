package com.github.hermod.generator.test;

import org.reflections.serializers.Serializer;

import com.github.hermod.generator.IRequestMsg;
import com.github.hermod.generator.Parameter;
import com.github.hermod.generator.Status;
import com.github.hermod.ser.IMsg;
import com.github.hermod.ser.impl.KeyObjectMsg;

/**
 * <p>RequestParam. </p>
 * 
 * @author anavarro - Mar 12, 2013
 * 
 */
public class HermodRequestMsg extends KeyObjectMsg implements IRequestMsg {

    
    private final static int NAME_ID = 1;
    private final static int REQUEST_ID = 2;
    private final static int STATUS_ID = 3;
    

    @Override
    public String getName() {
        return this.getAsString(NAME_ID);
    }

    @Override
    public void setName(String aName) {
        this.set(NAME_ID, aName);
    }

    @Override
    public int getRequestId() {
        return this.getAsInt(REQUEST_ID);
    }

    @Override
    public void setRequestId(int aRequestId) {
        this.set(REQUEST_ID, aRequestId);
    }

    @Override
    public Status getStatus() {
        return Status.values()[this.getAsInt(STATUS_ID)];
    }

    @Override
    public void setStatus(Status aStatus) {
        this.set(STATUS_ID, aStatus.ordinal());
        
    }

    @Override
    public Parameter getParameter() {
        Object parameter = new HermodParameter();
        this.getAsMsg(4, (IMsg) parameter);
        return (Parameter) parameter;
    }

    @Override
    public void setParameter(Parameter aParameter) {
        if (aParameter instanceof IMsg) {
            this.set(4, (IMsg) aParameter);
        }
    }
    
    
    
    

}
