/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realMidi;

import javax.sound.midi.*;

/**
 *
 * @author Daan van der Spek
 */
public class RealMidiInPort {
    public Transmitter t;
    public Receiver r;
    public MidiDevice d;

    public boolean initialized;
    
    public RealMidiInPort(RealMidiReceiver tConverter){
        initialized = false;
        r=tConverter;
    }
    
    public void openConnection(String deviceName) throws MidiUnavailableException{
        if(!initialized){
            MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
            for (int i=0; i<info.length; i++){
                MidiDevice device = MidiSystem.getMidiDevice(info[i]);
                if(device.toString().charAt(24)=='I'){
                    if(info[i].getName().equals(deviceName)){
                        d = MidiSystem.getMidiDevice(info[i]);
                        d.open();
                    }
                }
            }
            d.getTransmitter().setReceiver(r);
            initialized = true;
        }
    }
    
    public void openConnection(int id) throws MidiUnavailableException{
        if(!initialized){
            MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
            d = MidiSystem.getMidiDevice(devices[id]);
            d.open();
            d.getTransmitter().setReceiver(r);
            initialized = true;
        }
    }
            
    public void closeConnection(){
        //Close the device
        if(initialized){
            d.close();
        }
    }
    
    public void showMIDIDevices() throws MidiUnavailableException{
        MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
        
        for (int i=0; i < info.length; i++) {
            System.out.println(i + ") " + info[i]);
            System.out.println("Name: " + info[i].getName());
            System.out.println("Description: " + info[i].getDescription());

            MidiDevice device = MidiSystem.getMidiDevice(info[i]);
            System.out.println("Device: " + device);
        }
    }
}
