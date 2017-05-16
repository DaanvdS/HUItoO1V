/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huiTo01V;

import handlers.YAMHandler;
import handlers.HUIHandler;
import dataStructures.TrackData;
import dataStructures.ZoneData;
import realMidi.RealMidiSender;
import realMidi.RealMidiReceiver;
import realMidi.RealMidiInPort;
import virtualMidi.VirtualMidiSender;
import virtualMidi.VirtualMidiReceiver;
import de.tobiaserichsen.tevm.TeVirtualMIDI;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 *
 * @author Daan van der Spek
 */
public class keep {
    
    TeVirtualMIDI hui1, hui2;
    
    int temp = 1000;
    
    public keep() throws InvalidMidiDataException, MidiUnavailableException, InterruptedException{
        hui1 = new TeVirtualMIDI("HUI 1");
        hui2 = new TeVirtualMIDI("HUI 2");
        
        ZoneData hui1Zones = new ZoneData(8);
        ZoneData hui2Zones = new ZoneData(8);
        TrackData tracks = new TrackData(17);
        VirtualMidiReceiver hui1Rec = new VirtualMidiReceiver(hui1);
        VirtualMidiSender hui1Send = new VirtualMidiSender(hui1);
        new Thread(hui1Rec).start();
        
        VirtualMidiReceiver hui2Rec = new VirtualMidiReceiver(hui2);
        VirtualMidiSender hui2Send = new VirtualMidiSender(hui2);
        new Thread(hui2Rec).start();
        
        RealMidiSender yamSender = new RealMidiSender();
        RealMidiReceiver yamReceiver = new RealMidiReceiver();
        RealMidiInPort yamInPort = new RealMidiInPort(yamReceiver);
        
        
        HUIHandler hui1Handler = new HUIHandler(hui1Zones, tracks, false, hui1Rec, hui1Send, null);
        
        
        HUIHandler hui2Handler = new HUIHandler(hui2Zones, tracks, true, hui2Rec, hui2Send, null);
        
        
        YAMHandler yamHandler = new YAMHandler(tracks, yamSender, yamReceiver, hui1Handler, hui2Handler,null);
        yamSender.openConnection("USB Midi Cable"); 
        yamInPort.openConnection("USB Midi Cable"); 
        
        hui1Handler.setyamHandler(yamHandler);
        hui2Handler.setyamHandler(yamHandler);
        while(true){
            hui1Handler.handleMessage();
            yamHandler.handleMessage();
            /*int count = huiRec.howFarBehind();
            if(!(temp==count)){
                System.out.println(count);
            }
            temp=count;*/
            //Thread.sleep(1);
        }
    }
    
    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
       new keep();
    }   
}
