package handlers;

import dataStructures.TrackData;
import dataStructures.Zone;
import dataStructures.ZoneData;
import huiTo01V.mainFrame;
import static java.lang.Math.abs;
import virtualMidi.VirtualMidiMessage;
import virtualMidi.VirtualMidiSender;
import virtualMidi.VirtualMidiReceiver;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;

public class HUIHandler {
    VirtualMidiReceiver mReceiver;
    VirtualMidiSender mSender;
    
    // <editor-fold defaultstate="collapsed" desc=" ENUMS ">
    static final byte SysEx = (byte) 0xf0;
    static final byte CC = (byte) 0xb0;
    static final byte noteOn = (byte) 0x90;
    static final byte noteOff = (byte) 0x80;
    static final byte afterTouch = (byte) 0xa0;
    static final byte zoneSel = (byte) 0x0c;
    static final byte portOnOff = (byte) 0x2c;
    static final byte trackName = (byte) 0x10;
    static final byte vPotAssign = (byte) 0x08;
    static final byte timeCode = (byte) 0x11;
    static final byte charDisp = (byte) 0x12;
    static final int faderLowMesgOffset = 0x20;
    static final int vPotOffset = 0x10;
    static final int vuROffset = 0x10;
    int faderSecondHUIOffset = 0x00;
    
    static final int HIGH = 0;
    static final int LOW = 1;

// </editor-fold>
    
    
    int reEntCounter = 0;
    byte tempHigh = 0x00;
    
    
    
    mainFrame mainFrameInst;
    ZoneData zones;
    TrackData tracks;
    Zone selZone;
    YAMHandler yamHandler;
    int zone = 0;
    boolean isSecond = false;
    boolean EnableTimeCode = true;
    boolean EnableAfterTouch = true;
    boolean EnableZonePort = true; 
    boolean EnableVPot = true;
    StringBuilder lineOne = new StringBuilder("1234567890123456789012345678901234567890");
    StringBuilder lineTwo = new StringBuilder("1234567890123456789012345678901234567890");
    
    public HUIHandler(ZoneData tzones, TrackData ttracks, boolean t_isSecond, VirtualMidiReceiver t_rec, VirtualMidiSender t_send, mainFrame t_main){
        zones=tzones;
        tracks=ttracks;
        mReceiver=t_rec;
        isSecond=t_isSecond;
        mSender=t_send;
        mainFrameInst=t_main;
        
        if(isSecond){
            faderSecondHUIOffset=0x08;
        }
    }
    
    
    public void setyamHandler(YAMHandler t){
        yamHandler=t;
    }
    
    
    //SENDER
    // <editor-fold defaultstate="collapsed" desc=" Sender ">
    public void selectZone(byte zone) throws InvalidMidiDataException {
        byte[] message = {(byte) 0xb0, 0x0f, zone};
        mSender.sendMessage(message);
    }
    
    public void turnPortOn(int port) throws InvalidMidiDataException {
        byte[] message = {(byte) 0xb0, 0x2f, (byte) (0x40 + port)};
        mSender.sendMessage(message);
    }
    
    public void turnPortOff(int port) throws InvalidMidiDataException {
        byte[] message = {(byte) 0xb0, 0x2f, (byte) port};
        mSender.sendMessage(message);
    }
    
    public void touchFader() throws InvalidMidiDataException {
        byte[] message = {(byte) 0xb0, 0x2f, 0x40};
        mSender.sendMessage(message);
    }
    
    public void releaseFader() throws InvalidMidiDataException {
        byte[] message = {(byte) 0xb0, 0x2f, 0x00};
        mSender.sendMessage(message);
    }
    
    public void moveFaderCommand(int fader, byte value, int HiLo) throws InvalidMidiDataException {
        byte[] message = {(byte) 0xb0, 0x00, value};
        //byte[] msg = {(byte) 0xf0, 0x00, 0x00, 0x66, 0x05, 0x00, (byte) 0xb0, 0x2f, 0x00};
        switch (HiLo) {
            case HIGH:                
                message[1] = (byte) fader;
                break;
            case LOW:                
                message[1] = (byte) (0x20 + fader);
                break;
        }
        mSender.sendMessage(message);
    }
    
    public void setSwitch(byte zone, int port, boolean bool_offOn) throws InvalidMidiDataException {
        selectZone(zone);
        switch ((bool_offOn) ? 1 : 0) {
            case 1:                
                turnPortOn(port);
                break;
            case 0:
                turnPortOff(port);
                break;
        }
    }
    
    public void setSwitch(byte zone, int port) throws InvalidMidiDataException {
        selectZone(zone);
        switch ((zones.getZone(zone).getPort(port).getOn()) ? 1 : 0) {
            case 1:                
                turnPortOn(port);
                break;
            case 0:
                turnPortOff(port);
                break;
        }
    }
    
    public void moveFader(int fader) throws InvalidMidiDataException, InterruptedException {
        int huiFader = fader;
        if (isSecond) {
            huiFader -= 8;
        }
        
        selectZone((byte) huiFader);
        touchFader();
        moveFaderCommand(huiFader, (byte) tracks.getTrack(fader).getFader().getHighVal(), HIGH);
        moveFaderCommand(huiFader, (byte) tracks.getTrack(fader).getFader().getLowVal(), LOW);
        selectZone((byte) huiFader);
        releaseFader();        
    }
    
    public void setVPot(int pot) throws InvalidMidiDataException{
        int huiPot = pot;
        if (isSecond) {
            huiPot -= 8;
        }
        int value = tracks.getTrack(pot).getVPot().getDelta();
        byte[] message = {(byte) 0xb0, (byte) (0x40+huiPot), 0x0b};
        if(value<0){
            message[2]=0x4b;
        } else if(value>0) {
            message[2]=0x0b;
        }
        for(int i=0;i<abs(value);i++){
            mSender.sendMessage(message);
        }
    }
    
    public void setVPot(byte pot, int value) throws InvalidMidiDataException{
        byte[] message = {(byte) 0xb0, (byte) (0x40+pot), (byte) (0x40+value)};
        mSender.sendMessage(message);
    }
    
    public void setJog(int value) throws InvalidMidiDataException{
        byte[] message = {(byte) 0xb0, 0x0d, (byte) (value)};
        mSender.sendMessage(message);
    }
    
    public void sendPing() throws InvalidMidiDataException{
        byte[] message = {(byte) 0x90, 0x00, (byte) 0x7f};
        mSender.sendMessage(message);
    }    
// </editor-fold>
    
    //RECEIVER
    // <editor-fold defaultstate="collapsed" desc=" Receiver ">
    public void faderReceived(int channel, int value) throws InvalidMidiDataException {
        if (!tracks.getTrack(channel).getFader().getYamahaSentLast()) {
            tracks.getTrack(channel).getFader().setVal(value);
            yamHandler.moveFader(channel);
        } else {
            tracks.getTrack(channel).getFader().setYamahaSentLast(false);
        }
    }
    
    public void vpotReceived(int channel, int value) throws InvalidMidiDataException {
        if(channel<=13){
            if(mainFrameInst.getVPotMode()==2){
                mainFrameInst.getMeterBridge().getTrackDisplay(channel).getPanBar().setValue(value);
            } else {
                mainFrameInst.getMeterBridge().getTrackDisplay(channel).getSendPanel().setValue(mainFrameInst.getVPotMode(), value);
            }
            if (!tracks.getTrack(channel).getVPot().getYamahaSentLast()) {
                tracks.getTrack(channel).getVPot().setVal(value);
                yamHandler.moveVPot(channel);
            } else {
                tracks.getTrack(channel).getVPot().setYamahaSentLast(false);
            }
        }
    }
    
    public void vuReceived(int channel, int value, boolean right) {
        if (right) {
            mainFrameInst.getMeterBridge().getTrackDisplay(channel).getVUMeters().setValueR(value);
        } else {
            mainFrameInst.getMeterBridge().getTrackDisplay(channel).getVUMeters().setValueL(value);
        }
    }    
    
    public void portReceived(int zone, int channel, boolean onOff) throws InvalidMidiDataException {
        int yamZone = zone;
        if (isSecond) {
            yamZone += 8;
        }

        //Mutes
        if (channel == 2) {
            selZone = zones.getZone(zone);
            if (selZone.getPort(2).getYamahaSentLast()) {                
                selZone.getPort(2).setYamahaSentLast(false);
            } else {
                selZone.getPort(2).setOn(onOff);
                yamHandler.setMute(yamZone, this);
            }
        }

        //Solos
        if (channel == 3) {
            //Niks
        }
    }

// </editor-fold>
    
    //HANDLER
    public void handleMessage() throws InvalidMidiDataException{
        int channel = 0;
        int value = 0;
        VirtualMidiMessage curr_midi_message = mReceiver.getNextMidiMessage();
        byte[] curr_message = curr_midi_message.getMessage();
        if(!(curr_message==null)){
            switch(curr_message[0]){
                // <editor-fold defaultstate="collapsed" desc=" SysEx ">
                case SysEx:
                    //Timecode,TrackName
                    switch (curr_message[6]) {
                        // <editor-fold defaultstate="collapsed" desc=" timeCode ">
                        case timeCode:
                            if (EnableTimeCode) {
                                if (mReceiver.howFarBehind() <= 30) {
                                    if (!isSecond) {
                                        mainFrameInst.setTimeCode(curr_message[14], curr_message[13], curr_message[12], curr_message[11], curr_message[10], curr_message[9], curr_message[8], curr_message[7]);
                                    }
                                }
                            }
                            break;

// </editor-fold>
                        // <editor-fold defaultstate="collapsed" desc=" trackName ">
                        case trackName:
                            if (curr_message[7] == vPotAssign) {
                                //System.out.println(tracks.getTrack(1).convertName(message[8], message[9], message[10], message[11]));
                            } else {
                                //channel display
                                channel = Byte.toUnsignedInt(curr_message[7]) + faderSecondHUIOffset;
                                if (channel <= 13) {
                                    mainFrameInst.getMeterBridge().getTrackDisplay(channel).setTrackName(curr_message[8], curr_message[9], curr_message[10], curr_message[11]);
                                }
                            }
                            break;

// </editor-fold>
                        // <editor-fold defaultstate="collapsed" desc=" charDisp ">
                        case charDisp:
                            char[] incomMesg = new char[40];
                            for(int i=0; i<10; i++){
                                incomMesg[i]=mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetterToChar(curr_message[8+i]);
                            }
                            
                            if(curr_message[7]<4){
                                for(int i=0; i<10; i++){
                                    int index = i+(curr_message[7]*10);
                                    System.out.println(index);
                                    lineOne.setCharAt(index,incomMesg[i]);
                                }
                            } else {
                                for(int i=0; i<10; i++){
                                    int index = i+((curr_message[7]-4)*10);
                                    System.out.println(index);
                                    lineTwo.setCharAt(index,incomMesg[i]);
                                }
                            }
                            
                            mainFrameInst.setAlphaText(lineOne.toString(), lineTwo.toString());
                            
                            String str1 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[8]);
                            String str2 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[9]);
                            String str3 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[10]);
                            String str4 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[11]);
                            String str5 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[12]);
                            String str6 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[13]);
                            String str7 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[14]);
                            String str8 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[15]);
                            String str9 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[16]);
                            String str10 = mainFrameInst.getMeterBridge().getTrackDisplay(1).convertLetter(curr_message[17]);
                            System.out.println(Byte.toUnsignedInt(curr_message[7]) + ": " + str1+str2+str3+str4+str5+str6+str7+str8+str9+str10);
                            break;

// </editor-fold>
                    }
                break;
// </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" AfterTouch ">
                case afterTouch:
                    if (EnableAfterTouch) {
                        if (mReceiver.howFarBehind() <= 30) {
                            boolean right = false;
                            channel = Byte.toUnsignedInt(curr_message[1]) + faderSecondHUIOffset;
                            value = Byte.toUnsignedInt(curr_message[2]);
                            
                            if (value > 0x0c) {
                                value -= vuROffset;
                                right = true;
                            }
                            if (channel <= 13) {
                                vuReceived(channel, value, right);
                            }
                        }
                    }
                    break;

// </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" CC ">
                case CC:
                    //Fader,Switch,Zone
                    // <editor-fold defaultstate="collapsed" desc=" Fader ">
                    if (curr_message[1] <= (byte) 0x07) {
                        //Fader high, is always directly followed by low.
                        tempHigh = curr_message[2];
                    } else if (curr_message[1] >= (byte) 0x20 && curr_message[1] <= (byte) 0x27) {
                        try {
                            //Fader low.
                            channel = Byte.toUnsignedInt(curr_message[1]) - faderLowMesgOffset + faderSecondHUIOffset;
                            value = ((Byte.toUnsignedInt(tempHigh) << 7) + Byte.toUnsignedInt(curr_message[2])) / 129;
                            faderReceived(channel, value);
                        } catch (InvalidMidiDataException ex) {
                            Logger.getLogger(HUIHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

// </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc=" Zone/Port ">
                    if (EnableZonePort) {
                        if (curr_message[1] == zoneSel) {
                            //Is always followed by port on/off
                            zone = Byte.toUnsignedInt(curr_message[2]);
                        }

                        if (curr_message[1] == portOnOff) {
                            if (zone <= 7) {
                                boolean onOff = false;
                                channel = Byte.toUnsignedInt(curr_message[2]);
                                //if(channel==2 || channel==66){
                                //System.out.println("zone: "+zone);
                                //System.out.println("port: "+channel);
                                if (channel > 7) {
                                    channel -= 0x40;
                                    onOff = true;
                                }
                                portReceived(zone, channel, onOff);
                                //}
                            }
                        }
                    }

// </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc=" VPot ">
                    if (EnableVPot) {
                        if (curr_message[1] <= (byte) 0x17 && curr_message[1] >= (byte) 0x10) {
                            channel = Byte.toUnsignedInt(curr_message[1]) - vPotOffset + faderSecondHUIOffset;
                            value = (Byte.toUnsignedInt(curr_message[2]));
                            if (value >= 0x10) {
                                value -= 0x10;
                            } else if (value >= 0x20) {
                                value -= 0x20;
                            } else if (value >= 0x30) {
                                //value-=0x20;
                            }
                            value = (int) ((12.7 * value) - 12.2);
                            if (value > 127) {
                                value = 127;
                            }
                            vpotReceived(channel, value);
                        }
                    }
                    break;

// </editor-fold>
// </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" noteOn ">
                case noteOn:
                    //Ping
                    sendPing();
                    break;

// </editor-fold>
            }
        }
    }
}
