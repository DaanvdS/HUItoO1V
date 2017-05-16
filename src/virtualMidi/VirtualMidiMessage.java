/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualMidi;

import java.time.Instant;

/**
 *
 * @author Daan van der Spek
 */
public class VirtualMidiMessage {
    byte[] message;
    long timestamp;
    boolean handled = false;
    
    public VirtualMidiMessage(byte[] temp){
        timestamp=Instant.now().toEpochMilli();
        message=temp;
    }
    
    public byte[] getMessage(){
        handled=true;
        return message;
    }
    
    public long getTimestamp(){
        return timestamp;
    }
    
    public void setHandled(){
        handled=true;
    }
}
