/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uiObjects;

/**
 *
 * @author Daan van der Spek
 */
public class trackDisplay extends javax.swing.JPanel {

    /**
     * Creates new form trackDisplay
     */
    public trackDisplay() {
        initComponents();
    }

    public vuMeter getVUMeters(){
        return vuMeters;
    }
    
    public panBar getPanBar(){
        return panBar;
    }
    
    public sendPanel getSendPanel(){
        return sendPanel;
    }
    
    public void setTrackName(int byte1, int byte2, int byte3, int byte4){
        trackName.setText(convertName(byte1,byte2,byte3,byte4));
    }
    
    public String convertName(int byte1, int byte2, int byte3, int byte4){
        return convertLetter(byte1)+convertLetter(byte2)+convertLetter(byte3)+convertLetter(byte4);
    }
    
    public char convertLetterToChar(int in){
        return convertLetter(in).toCharArray()[0];
    }
    
    public String convertLetter(int in){
        String out="";
        if(in>=0x30 && in<=0x39)out=Integer.toString(in-0x30);
        boolean cap=false;
        if(in>=0x41 && in<=0x5a){in=in+0x20;cap=true;}
            
        switch (in){
            case 0x00: out="ì"; break;
            case 0x01: out="↑"; break;
            case 0x02: out="→"; break;
            case 0x03: out="↓"; break;
            case 0x04: out="←"; break;
            case 0x05: out="¿"; break;
            case 0x06: out="à"; break;
            case 0x07: out="Ø"; break;
            case 0x08: out="ø"; break;
            case 0x09: out="ò"; break;
            case 0x0a: out="ù"; break;
            case 0x0b: out="Ň"; break;
            case 0x0c: out="Ç"; break;
            case 0x0d: out="ê"; break;
            case 0x0e: out="É"; break;
            case 0x0f: out="é"; break;
            case 0x10: out="è"; break;
            case 0x11: out="Æ"; break;
            case 0x12: out="æ"; break;
            case 0x13: out="Å"; break;
            case 0x14: out="å"; break;
            case 0x15: out="Ä"; break;
            case 0x16: out="ä"; break;
            case 0x17: out="Ö"; break;
            case 0x18: out="ö"; break;
            case 0x19: out="Ü"; break;
            case 0x1a: out="ü"; break;
            case 0x1b: out="°C"; break;
            case 0x1c: out="°F"; break;
            case 0x1d: out="ß"; break;
            case 0x1e: out="£"; break;
            case 0x1f: out="¥"; break;
            case 0x20: out=" "; break;
            case 0x21: out="!"; break;
            case 0x22: out="\""; break;
            case 0x23: out="#"; break;
            case 0x24: out="$"; break;
            case 0x25: out="%"; break;
            case 0x26: out=""; break;
            case 0x27: out="'"; break;
            case 0x28: out="("; break;
            case 0x29: out=")"; break;
            case 0x2a: out="*"; break;
            case 0x2b: out="+"; break;
            case 0x2c: out=","; break;
            case 0x2d: out="-"; break;
            case 0x2e: out="."; break;
            case 0x2f: out="/"; break;
            case 0x3a: out=":"; break;
            case 0x3b: out=";"; break;
            case 0x3c: out="<"; break;
            case 0x3d: out="="; break;
            case 0x3e: out=">"; break;
            case 0x3f: out="?"; break;
            case 0x40: out="@"; break;
            case 0x5b: out="["; break;
            case 0x5c: out="\\"; break;
            case 0x5d: out="]"; break;
            case 0x5e: out="^"; break;
            case 0x5f: out="_"; break;
            case 0x60: out="`"; break;
            case 0x61: out="a"; break;
            case 0x62: out="b"; break;
            case 0x63: out="c"; break;
            case 0x64: out="d"; break;
            case 0x65: out="e"; break;
            case 0x66: out="f"; break;
            case 0x67: out="g"; break;
            case 0x68: out="h"; break;
            case 0x69: out="i"; break;
            case 0x6a: out="j"; break;
            case 0x6b: out="k"; break;
            case 0x6c: out="l"; break;
            case 0x6d: out="m"; break;
            case 0x6e: out="n"; break;
            case 0x6f: out="o"; break;
            case 0x70: out="p"; break;
            case 0x71: out="q"; break;
            case 0x72: out="r"; break;
            case 0x73: out="s"; break;
            case 0x74: out="t"; break;
            case 0x75: out="u"; break;
            case 0x76: out="v"; break;
            case 0x77: out="w"; break;
            case 0x78: out="x"; break;
            case 0x79: out="y"; break;
            case 0x7a: out="z"; break;
            case 0x7b: out="{"; break;
            case 0x7c: out="|"; break;
            case 0x7d: out="}"; break;
            case 0x7e: out="~"; break;
            case 0x7f: out=""; break;
        }
        
        if(cap==true)out=String.valueOf(Character.toUpperCase(out.toCharArray()[0]));
        
        return out;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        trackName = new javax.swing.JTextField();
        vuMeters = new uiObjects.vuMeter();
        panBar = new uiObjects.panBar();
        sendPanel = new uiObjects.sendPanel();

        trackName.setEditable(false);
        trackName.setFont(new java.awt.Font("DSEG14 Classic", 0, 16)); // NOI18N
        trackName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trackName.setText("TRK1");
        trackName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trackNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(trackName)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vuMeters, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(sendPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(vuMeters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trackName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void trackNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trackNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trackNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private uiObjects.panBar panBar;
    private uiObjects.sendPanel sendPanel;
    private javax.swing.JTextField trackName;
    private uiObjects.vuMeter vuMeters;
    // End of variables declaration//GEN-END:variables
}
