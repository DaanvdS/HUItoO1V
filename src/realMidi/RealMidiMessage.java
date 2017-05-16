/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realMidi;

import java.time.Instant;
import javax.sound.midi.MidiMessage;

/**
 *
 * @author Daan van der Spek
 */
public class RealMidiMessage {
    byte[] message;
    long timestamp;
    boolean handled = false;
    
    public RealMidiMessage(byte[] temp){
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
