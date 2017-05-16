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
public class Fader {
    public int value = 0; //Ranges from 0 to 127, dus eigenlijk byte.
    public boolean YamahaSentLast = false;
    
    // <editor-fold defaultstate="collapsed" desc="Setters ">
    public void setYamahaSentLast(boolean temp) {
        YamahaSentLast = temp;
    }
    
    public void setVal(int t_val) {
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
    
    public int getHighVal(){
        return (value*129) >> 7;
    }

    public int getLowVal(){
        return ((value*129) - getHighVal()) >> 7;
    }
// </editor-fold>
}
