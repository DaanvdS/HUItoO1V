package realMidi;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;

public class RealMidiReceiver implements Receiver  {
    RealMidiMessage[] bufferIn;
    boolean initialized = false;
    boolean reEntBlock = false;
    int reEntCounter = 0;
    final int bufferLength = 1024;
    int inputCounter = 0;
    int outputCounter = 0;
    byte[] empty_byte = {0x00,0x00,0x00};
    RealMidiMessage empty;
    
    public RealMidiReceiver() {
        empty = new RealMidiMessage(empty_byte);
        bufferIn = new RealMidiMessage[bufferLength];
        initialized=true;
    }
       
    @Override
    public void send(MidiMessage midi_Message, long timeStamp) {
        if(!reEntBlock){
            reEntBlock=true;
            if(bufferIn[inputCounter]==null || bufferIn[inputCounter].handled==false){
                bufferIn[inputCounter]=new RealMidiMessage(midi_Message.getMessage());
                //System.out.println("In");
                inputCounter++;
                if(inputCounter==bufferLength){
                    inputCounter=0;
                }
            } else {
                System.out.println("Virtual: over buffer heen gelopen bij "+inputCounter);
            }
            reEntBlock=false;
        } else {
            reEntCounter++;
        }
    }
    
    public RealMidiMessage getNextMidiMessage(){
        RealMidiMessage temp = bufferIn[outputCounter];
        
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
    
    @Override
    public void close() {
        initialized=false;
    }
}