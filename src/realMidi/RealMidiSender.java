package realMidi;

import javax.sound.midi.*;

public class RealMidiSender {
    public Receiver r;
    public MidiDevice d;
    public boolean initialized;
    
    public RealMidiSender(){
        initialized=false;
    }
    
    public void openConnection(String deviceName) throws MidiUnavailableException{
        if(!initialized){
            MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
            for (int i=0; i<info.length; i++){
                MidiDevice device = MidiSystem.getMidiDevice(info[i]);
                //System.out.println(device.toString());
                if(device.toString().charAt(24)=='O'){
                    if(info[i].getName().equals(deviceName)){
                        //System.out.println(deviceName);
                        d = MidiSystem.getMidiDevice(info[i]);
                        d.open();
                    }
                }
            }
            r = d.getReceiver();
            initialized = true;
        }
    }
    
    public void openConnection(int id) throws MidiUnavailableException{
        if(!initialized){            
            MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
            d = MidiSystem.getMidiDevice(devices[id]);
            d.open();
            r = d.getReceiver();
            initialized = true;
        }
    }
            
    public void closeConnection(){
        //Close the device
        if(initialized){
            d.close();
            initialized = false;
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
    
    public void sendSysExMessage(byte [] messageContent) throws InvalidMidiDataException{
        if(initialized){
            for(int i=0; i<messageContent.length; i++){
                System.out.print(Byte.toUnsignedInt(messageContent[i])+" ");
            }
                System.out.print("\n");
            SysexMessage sysexMsg = new SysexMessage();    
            sysexMsg.setMessage(messageContent, messageContent.length); 
            r.send(sysexMsg, -1);
        }
    }
    
    
    
    public void sendNoteOn(int int_note, int int_value, boolean bool_noteOffOn) throws InvalidMidiDataException{
        if(initialized){
            ShortMessage shortMsg = new ShortMessage();
            int shortMsg_noteOffOn=ShortMessage.NOTE_ON;

            switch ((bool_noteOffOn) ? 1 : 0) {
                case 1: 
                    shortMsg_noteOffOn=ShortMessage.NOTE_ON;
                    break;
                case 0: 
                    shortMsg_noteOffOn=ShortMessage.NOTE_OFF;
                    break;
            }

            shortMsg.setMessage(shortMsg_noteOffOn, int_note, int_value);
            r.send(shortMsg, -1);
        }
    } 
    
    public void sendCC(int int_note, int int_value) throws InvalidMidiDataException{        
        if(initialized){
            ShortMessage shortMsg = new ShortMessage();
            shortMsg.setMessage(0xb0, int_note, int_value);
            r.send(shortMsg, -1);
        }
    } 
}
