package huiTo01V;

import uiObjects.meterBridge;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

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
import handlers.OverallHandler;
import handlers.COMReceiver;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public class mainFrame extends javax.swing.JFrame {
    TeVirtualMIDI hui1, hui2;
    HUIHandler hui1Handler, hui2Handler;
    YAMHandler yamHandler;
    VirtualMidiReceiver hui1Rec, hui2Rec;
    VirtualMidiSender hui1Send, hui2Send;
    RealMidiSender yamSend;
    RealMidiReceiver yamRec;
    RealMidiInPort yamInPort;
    ZoneData hui1Zones, hui2Zones;
    TrackData tracks;
    COMReceiver comRec;
    
    int VPotMode = 2;
    
    public mainFrame() throws MidiUnavailableException, UnsupportedCommOperationException, PortInUseException, PortInUseException, NoSuchPortException, NoSuchPortException, IOException, IOException, InvalidMidiDataException, InterruptedException {
        initComponents();       
        
        Hashtable labelTable = new Hashtable();
        labelTable.put(2,new JLabel("Pan"));
        labelTable.put(3,new JLabel("Send E"));
        labelTable.put(4,new JLabel("Send D"));
        labelTable.put(5,new JLabel("Send C"));
        labelTable.put(6,new JLabel("Send B"));
        labelTable.put(7,new JLabel("Send A"));
        modeSlider.setLabelTable(labelTable);
        
        hui1Zones = new ZoneData(8);
        hui2Zones = new ZoneData(8);
        tracks = new TrackData(17);
        
        hui1 = new TeVirtualMIDI("HUI 1");
        hui1Rec = new VirtualMidiReceiver(hui1);
        hui1Send = new VirtualMidiSender(hui1);
        new Thread(hui1Rec).start();
        
        hui2 = new TeVirtualMIDI("HUI 2");
        hui2Rec = new VirtualMidiReceiver(hui2);
        hui2Send = new VirtualMidiSender(hui2);
        new Thread(hui2Rec).start();
        
        yamSend = new RealMidiSender();
        yamRec = new RealMidiReceiver();
        yamInPort = new RealMidiInPort(yamRec);
        
        hui1Handler = new HUIHandler(hui1Zones, tracks, false, hui1Rec, hui1Send, this);
        hui2Handler = new HUIHandler(hui2Zones, tracks, true, hui2Rec, hui2Send, this);
        yamHandler = new YAMHandler(tracks, yamSend, yamRec, hui1Handler, hui2Handler, this);
        hui1Handler.setyamHandler(yamHandler);
        hui2Handler.setyamHandler(yamHandler);
        
        comRec = new COMReceiver(hui1Handler,yamHandler,this);
        connectMIDI();
    }
        
    
    // <editor-fold defaultstate="collapsed" desc=" MIDI ">
    public void connectMIDI() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        Preferences prefs = Preferences.userNodeForPackage(mainFrame.class);
        //yamInPort.openConnection(prefs.get("YamIn", ""));
        //yamSend.openConnection(prefs.get("YamOuts", ""));
        //comRec.setCOMPort(prefs.get("COMS", ""));
        //new Thread(comRec).start();
        
        OverallHandler oHandler = new OverallHandler(hui1Handler, hui2Handler, yamHandler, this);
        Thread t = new Thread(oHandler);
        t.start();
        
        Thread t2 = new Thread();
        t2.start();
    }
    
    public void disconnectMIDI() throws MidiUnavailableException {
        yamSend.closeConnection();
        yamInPort.closeConnection();
    }

// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" SETTERS ">
    public void setAlphaText(String lineOne, String lineTwo){
        alphaLineOne.setText(lineOne);
        alphaLineTwo.setText(lineTwo);
    }
    
    public void setVPotMode(int mode) throws InvalidMidiDataException {
        VPotMode = mode;
        hui1Handler.setSwitch((byte) 0x0b, mode, true);
        hui2Handler.setSwitch((byte) 0x0b, mode, true);
    }
    
    public void setTimeCode(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {        
        String out = convertTimeCode(b1);
        out = out.concat(convertTimeCode(b2));
        out = out.concat(convertTimeCode(b3));
        out = out.concat(convertTimeCode(b4));
        out = out.concat(convertTimeCode(b5));
        out = out.concat(convertTimeCode(b6));
        out = out.concat(convertTimeCode(b7));
        out = out.concat(convertTimeCode(b8));
        
        timeCode.setText(out);
    }

// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" ADDIT METHODS ">
    public String convertTimeCode(byte in) {
        String out1 = "";
        String out2 = "";
        if (in >= 0x10) {
            out2 = ".";
            in -= 0x10;
        }
        if (in <= 0x09) {
            out1 = String.valueOf(in);
        } else {
            switch (in) {
                case 0x0a:
                    out1 = "A";
                    break;
                case 0x0b:
                    out1 = "b";
                    break;
                case 0x0c:
                    out1 = "C";
                    break;
                case 0x0d:
                    out1 = "d";
                    break;
                case 0x0e:
                    out1 = "E";
                    break;
                case 0x0f:
                    out1 = "F";
                    break;
            }
        }
        
        return out1.concat(out2);
    }

// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" GETTERS ">
    public meterBridge getMeterBridge() {
        return meterBridge;
    }
    
    public JToggleButton getVPotButton() {
        return VPotButton;
    }
    
    public int getVPotMode() {
        if (modeSlider.getValue() == 1) {
            return 2;
        } else {
            return modeSlider.getValue();
        }
    }
    
    public JSlider getModeSlider() {
        return modeSlider;
    }

// </editor-fold>
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        timeCode = new javax.swing.JLabel();
        meterBridge = new uiObjects.meterBridge();
        modeSlider = new javax.swing.JSlider();
        VPotButton = new javax.swing.JToggleButton();
        alphaLineOne = new javax.swing.JLabel();
        alphaLineTwo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton1.setText("Config");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        timeCode.setFont(new java.awt.Font("DSEG7 Classic", 0, 30)); // NOI18N
        timeCode.setText("01.02.34.08");

        modeSlider.setMajorTickSpacing(1);
        modeSlider.setMaximum(7);
        modeSlider.setMinimum(2);
        modeSlider.setMinorTickSpacing(1);
        modeSlider.setPaintLabels(true);
        modeSlider.setPaintTicks(true);
        modeSlider.setSnapToTicks(true);
        modeSlider.setValue(2);
        modeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                modeSliderStateChanged(evt);
            }
        });

        VPotButton.setText("VPot");
        VPotButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                VPotButtonStateChanged(evt);
            }
        });

        alphaLineOne.setFont(new java.awt.Font("DSEG14 Classic", 0, 14)); // NOI18N
        alphaLineOne.setText("1234567890123456789012345678901234567890");

        alphaLineTwo.setFont(new java.awt.Font("DSEG14 Classic", 0, 14)); // NOI18N
        alphaLineTwo.setText("1234567890123456789012345678901234567890");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(meterBridge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VPotButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeCode))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(alphaLineTwo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alphaLineOne, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(VPotButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(timeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(modeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alphaLineOne, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alphaLineTwo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 285, Short.MAX_VALUE)
                .addComponent(meterBridge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            disconnectMIDI();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            new configFrame().setVisible(true);
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void modeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_modeSliderStateChanged
        try {
            setVPotMode(modeSlider.getValue());
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_modeSliderStateChanged

    private void VPotButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_VPotButtonStateChanged
        try {
            yamHandler.modeSwitch(VPotButton.isSelected());
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_VPotButtonStateChanged
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new mainFrame().setVisible(true);
                } catch (MidiUnavailableException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedCommOperationException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PortInUseException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchPortException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton VPotButton;
    private javax.swing.JLabel alphaLineOne;
    private javax.swing.JLabel alphaLineTwo;
    private javax.swing.JButton jButton1;
    private javax.swing.JSpinner jSpinner1;
    private uiObjects.meterBridge meterBridge;
    private javax.swing.JSlider modeSlider;
    private javax.swing.JLabel timeCode;
    // End of variables declaration//GEN-END:variables
}

