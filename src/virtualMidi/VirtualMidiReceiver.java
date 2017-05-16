package virtualMidi;

import de.tobiaserichsen.tevm.TeVirtualMIDI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualMidiReceiver implements Runnable {
    VirtualMidiMessage[] bufferIn;
    TeVirtualMIDI midiPort;
    boolean initialized = false;
    final int bufferLength = 1024;
    int inputCounter = 0;
    int outputCounter = 0;
    byte[] empty_byte = {0x00,0x00,0x00};
    VirtualMidiMessage empty;
    
    public VirtualMidiReceiver(TeVirtualMIDI t_port) {
        empty = new VirtualMidiMessage(empty_byte);
        bufferIn = new VirtualMidiMessage[bufferLength];
        midiPort = t_port;
        initialized=true;
    }
    
    @Override
    public void run() {
        while(!initialized){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(VirtualMidiReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while(true){
            if(bufferIn[inputCounter]==null || bufferIn[inputCounter].handled==true){
                bufferIn[inputCounter]=new VirtualMidiMessage(midiPort.getCommand());
                inputCounter++;
                if(inputCounter==bufferLength){
                    inputCounter=0;
                }
            } else {
                System.out.println("Virtual: over buffer heen gelopen bij "+ inputCounter);
            }
        }
    }
    
    public VirtualMidiMessage getNextMidiMessage(){
        VirtualMidiMessage temp = bufferIn[outputCounter];
        
        if((temp==null) || bufferIn[outputCounter].handled==true){
            return empty;
        } else {
            outputCounter++;
            if(outputCounter==bufferLength){
                outputCounter=0;
            }
            return temp;
        }
    }
    
    public int howFarBehind(){
        return inputCounter-outputCounter;
    }
}
