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
public class Port {
    public boolean on = false;
    public boolean YamahaSentLast = false;    
    
    public void setYamahaSentLast(boolean temp){
        YamahaSentLast=temp;
    }
    public void setOn(boolean t){ on=t; }
    public boolean getOn(){ return on; }
    public boolean getYamahaSentLast(){ return YamahaSentLast; }
}
