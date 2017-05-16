/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

/**
 *
 * @author Daan van der Spek
 */
public class VPot {
    public int value = 0; //Ranges from 0 to 127, dus eigenlijk byte.
    public int hui_value = 0;
    public int delta = 0x40;
    public int restDelta = 0;
    public boolean YamahaSentLast = false;
    
    // <editor-fold defaultstate="collapsed" desc="Setters ">
    public void setYamahaSentLast(boolean temp) {
        YamahaSentLast = temp;
    }
    
    public void setVal(int t_val) {
        value = t_val;
    }
    
    public void setDelta(int t_val){
        restDelta+=(value-t_val);
        value = t_val;
    }

// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters ">
    public boolean getYamahaSentLast() {
        return YamahaSentLast;
    }
    
    public int getVal() {
        return value;
    }
    
    public int getDelta() {
        int temp = restDelta;
        if(!((restDelta/12)==0)){
            restDelta=0;
        }
        return (temp/10);
    }
// </editor-fold>
}
