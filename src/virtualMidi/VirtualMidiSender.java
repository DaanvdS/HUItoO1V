package virtualMidi;

import de.tobiaserichsen.tevm.TeVirtualMIDI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;

public class VirtualMidiSender {
    TeVirtualMIDI midiPort;
    boolean initialized = false;
    
    public VirtualMidiSender(TeVirtualMIDI t_port){
        midiPort = t_port;
        initialized=true;
    }
    
   
    public void sendMessage(byte[] message){
        midiPort.sendCommand(message);
    }
}
