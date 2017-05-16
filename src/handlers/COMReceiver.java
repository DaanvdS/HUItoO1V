/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import huiTo01V.mainFrame;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;

public class COMReceiver implements Runnable {
    HUIHandler hui1Handler;
    YAMHandler yamHandler;
    mainFrame mainFrameInst;
    String ComPort;
    
    public COMReceiver(HUIHandler t_h, YAMHandler t_y, mainFrame t_m){
        hui1Handler=t_h;
        yamHandler=t_y;
        mainFrameInst=t_m;
    }
    
    public void run() {
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(ComPort);
            SerialPort serialPort = (SerialPort) portIdentifier.open(ComPort, 0);
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            InputStream inputStream = serialPort.getInputStream();
            while (true) {
                try {
                    byte[] buffer = new byte[10];
                    while ((buffer[0] = (byte) inputStream.read()) != 'D') {
                    }
                    int i = 1;
                    while (i < 9) {
                        buffer[i++] = (byte) inputStream.read();
                    }
                    
                    if(Byte.toUnsignedInt(buffer[1])==49){
                        if(mainFrameInst.getVPotButton().isSelected()){
                            int temp = mainFrameInst.getVPotMode();
                            if(!(temp==7)){
                                temp+=1;
                            }
                            mainFrameInst.getModeSlider().setValue(temp);
                        } else {
                            jog(false);
                        }
                    } else if(Byte.toUnsignedInt(buffer[2])==49){
                        if(mainFrameInst.getVPotButton().isSelected()){
                            int temp = mainFrameInst.getVPotMode();
                            if(!(temp==2)){
                                temp-=1;
                            }
                            mainFrameInst.getModeSlider().setValue(temp);
                        } else {
                            jog(true);
                        }
                    } 
                    
                    if(Byte.toUnsignedInt(buffer[3])==49){
                        stop();
                    }
                    
                    if(Byte.toUnsignedInt(buffer[4])==49){
                        play();                        
                    }
                    
                    if(Byte.toUnsignedInt(buffer[5])==49){
                        test();
                    }
                    
                    if(Byte.toUnsignedInt(buffer[6])==49){
                        moveBank(false);
                    }
                    
                    if(Byte.toUnsignedInt(buffer[7])==49){
                        mode();
                    }
                    
                    if(Byte.toUnsignedInt(buffer[8])==49){
                        moveBank(true);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(COMReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SerialReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(SerialReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortInUseException ex) {
            Logger.getLogger(SerialReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPortException ex) {
            Logger.getLogger(SerialReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setCOMPort(String t_comPort){
        ComPort = t_comPort;
    }
    
    public void moveBank(boolean moveRight){
        try {
            if(moveRight){
                hui1Handler.setSwitch((byte) 0x0a, 3, true);
            } else {
                hui1Handler.setSwitch((byte) 0x0a, 1, true);
            }
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void jog(boolean moveRight, int value){
        try {
            if(!moveRight){
                value=value*-1;
            }
            value=(int) (value*0.4);
            if(value!=0){
                System.out.println(value);
                hui1Handler.setJog(value);
            }
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void jog(boolean moveRight){
        try {
            if(moveRight){
                hui1Handler.setJog(4);
            } else {
                hui1Handler.setJog(-4);
            }
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop(){
        try {
            hui1Handler.setSwitch((byte) 0x0e, 3, true);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test() throws InvalidMidiDataException{
        hui1Handler.setSwitch((byte) 0x01, 0x06, true);
        hui1Handler.setSwitch((byte) 0x1c, 0x00, true);
        hui1Handler.setSwitch((byte) 0x1c, 0x01, true);
        hui1Handler.setSwitch((byte) 0x1c, 0x02, true);
    }
    
    public void play(){
        try {
            hui1Handler.setSwitch((byte) 0x0e, 4, true);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void rec(){
        try {
            hui1Handler.setSwitch((byte) 0x0e, 5, true);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mode() throws InvalidMidiDataException{
        yamHandler.modeSwitch();
    }
}
