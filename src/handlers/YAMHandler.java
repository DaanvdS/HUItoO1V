package handlers;

import dataStructures.TrackData;
import huiTo01V.mainFrame;
import realMidi.RealMidiMessage;
import realMidi.RealMidiSender;
import realMidi.RealMidiReceiver;
import javax.sound.midi.*;

public class YAMHandler {
    public static final byte SysEx = (byte) 0xf0;
    public static final byte CC = (byte) 0xb0;
    
    
    public RealMidiSender mSender;    
    public RealMidiReceiver mReceiver; 
    public TrackData tracks;     
    public mainFrame mainFrameInst;
    public HUIHandler hui1Handler, hui2Handler;
    
    boolean VPotOn = false;
    public boolean modeSwitchDone = false;
    
    public YAMHandler(TrackData ttracks, RealMidiSender tSender, RealMidiReceiver tReceiver, HUIHandler h1, HUIHandler h2, mainFrame t_main){
        tracks=ttracks;
        mSender = tSender;
        mReceiver = tReceiver;
        hui1Handler=h1;
        hui2Handler=h2;
        mainFrameInst=t_main;
    }
    
    public void faderReceived(int channel, int value) throws InvalidMidiDataException, InterruptedException{
        tracks.getTrack(channel).getFader().setVal(value);
        tracks.getTrack(channel).getFader().setYamahaSentLast(true);
        if(channel>7){
            hui2Handler.moveFader(channel);
        } else {
            hui1Handler.moveFader(channel);
        }
    }
    
    public void modeSwitch() throws InvalidMidiDataException{
        if(VPotOn){
            //Back to fader mode
            VPotOn=false;
            for(int i=0;i<14;i++){
                moveFader(i);
            }
            mainFrameInst.getVPotButton().setSelected(false);
        } else {
            VPotOn=true;
            for(int i=0;i<14;i++){
                moveVPot(i);
            }
            mainFrameInst.getVPotButton().setSelected(true);
        }
    }
    
    public void modeSwitch(boolean t_VPot) throws InvalidMidiDataException{
        if(!(t_VPot==VPotOn)){
            if(!(t_VPot)){
                //Back to fader mode
                VPotOn=false;
                for(int i=0;i<14;i++){
                    moveFader(i);
                }
            } else {
                VPotOn=true;
                for(int i=0;i<14;i++){
                    moveVPot(i);
                }
            }
        }
    }
    
    public void vpotReceived(int channel, int value) throws InvalidMidiDataException, InterruptedException{
        tracks.getTrack(channel).getVPot().setDelta(value);
        tracks.getTrack(channel).getVPot().setYamahaSentLast(true);
        if(channel>7){
            hui2Handler.setVPot(channel);
        } else {
            hui1Handler.setVPot(channel);
        }
    }
    
    public void onReceived(int channel, boolean onOff, HUIHandler h) throws InvalidMidiDataException, InterruptedException{
        h.zones.getZone(channel).getPort(2).setOn(onOff);
        h.zones.getZone(channel).getPort(2).setYamahaSentLast(true);
        h.setSwitch((byte) channel, 2);
    }
    
    
    //SENDER
    public void moveFader(int fader) throws InvalidMidiDataException{     
        if(!VPotOn){
            mSender.sendCC(fader+1,tracks.getTrack(fader).getFader().getVal());
        }
    }
    
    public void moveVPot(int fader) throws InvalidMidiDataException{     
        if(VPotOn){
            //System.out.println(mainFrameInst.getVPotMode());
            mSender.sendCC(fader+1,tracks.getTrack(fader).getVPot().getVal());
        }
    }
    
    public void setMute(int zone, HUIHandler h) throws InvalidMidiDataException{
        int value=127;
        int add=0;
        if(!h.isSecond){
            if(zone<4){
                add=0x1c;
            } else {
                add=0x1d;
            }
        } else {
            add=0x1d+8;
        }
        
        switch ((h.zones.getZone(zone).getPort(2).getOn()) ? 1 : 0) {
            case 1: 
                value=0;
                break;
            case 0:
                value=127;
                break;
        }
        
        mSender.sendCC(zone+add,value);
    }
    
    //HANDLER
    public void handleMessage() throws InvalidMidiDataException, InterruptedException{
        int channel = 0;
        int value = 0;
        RealMidiMessage curr_midi_message = mReceiver.getNextMidiMessage();
        byte[] curr_message = curr_midi_message.getMessage();
        if(!(curr_message==null)){
            switch(curr_message[0]){
                case SysEx:
                    //Solo
                    break;
                case CC:
                    //Fader,On
                    if(curr_message[1]<=(byte)0x0e){
                        channel = Byte.toUnsignedInt(curr_message[1])-1;
                        value = Byte.toUnsignedInt(curr_message[2]);
                        if(VPotOn){
                            vpotReceived(channel,value);
                        } else {
                            faderReceived(channel,value);
                        }
                    }
                    if(curr_message[1]>=0x1c && curr_message[1]<=0x2c){
                        //ON
                        channel = 0;
                        boolean on = false;

                        switch (curr_message[2]) {
                            case 0: 
                                on = true;
                                break;
                            case 1:
                                on = false;
                                break;
                        }

                        if(curr_message[1]<=0x24){
                            if(curr_message[1]<=0x1f){
                                channel = curr_message[1]-0x1c;
                            } else {
                                channel = curr_message[1]-0x1d;
                            }
                            onReceived(channel, on, hui1Handler);
                        } else if(curr_message[1]<=0x2a){
                            channel = curr_message[1]-0x25;
                            onReceived(channel, on, hui2Handler);
                        }
                    }

                    break;
                    
                    
            }
        }
    }
}
