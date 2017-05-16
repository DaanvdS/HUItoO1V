/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

public class Track {
    public Fader fader;
    public VPot vpot;
    public String name = "";
    
    public Track(){
        fader = new Fader();
        vpot = new VPot();
    }
    
    public Fader getFader(){
        return fader;
    }
    
    public VPot getVPot(){
        return vpot;
    }
    
    public String getName(){
        return name;
    }
}
