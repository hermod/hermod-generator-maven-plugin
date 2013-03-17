package com.github.hermod.generator.test;

import com.github.hermod.generator.IRequestMsg;
import com.github.hermod.generator.ResponseMsg;
import com.github.hermod.ser.IMsg;

/**
 * <p>HermodMsgFactory. </p>
 *
 * @author anavarro - Mar 12, 2013
 *
 */
public class HermodMsgFactory implements MsgFactory {
     
     /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.generator.test.MsgFactory#createResponseMsg()
     */
    public ResponseMsg createResponseMsg() {
         return null;//new HermodResponseMsg();
     }
     
    
    public <T> T create(final Class<T> clazz) {
        
        return (T) null;//new HermodResponseMsg();
    }
    
    public <T> T create1(final Class<T> clazz) {
        final String canonicalName = clazz.getCanonicalName();
        switch (canonicalName) {
        case "HermodResponseMsg":
            return (T) null;//new HermodResponseMsg();

        default:
            throw new IllegalArgumentException("Impossible to create Implementation of this Interface Msg" + canonicalName);
        }
    }
    

    public static void main(String[] args) {
        HermodMsgFactory factory = new HermodMsgFactory();
        IRequestMsg msg = factory.create(IRequestMsg.class);
    }
}
