/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import huiTo01V.mainFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;

    
public class OverallHandler implements Runnable {

    HUIHandler hui1Handler, hui2Handler;
    YAMHandler yamHandler;
    mainFrame mainFrameInst;
    int temp=0;
            
    public OverallHandler(HUIHandler h1, HUIHandler h2, YAMHandler y, mainFrame m) {
        hui1Handler=h1;
        hui2Handler=h2;
        yamHandler=y;
        mainFrameInst=m;
    }

    public void run() {
        while(true){
            try {
                hui1Handler.handleMessage();
                yamHandler.handleMessage();
                hui2Handler.handleMessage();
                
                //int count = hui1Handler.mReceiver.howFarBehind();
                //if(!(temp==count)){
//                    System.out.println(count);
//                }
//                temp=count;
            } catch (InvalidMidiDataException ex) {
                Logger.getLogger(OverallHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(OverallHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
