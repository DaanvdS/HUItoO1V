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
public class ZoneData {
    public Zone[] zones = new Zone[40];
    
    public ZoneData(int amount){
        for(int i=0; i<40; i++){
            zones[i]=new Zone(i,amount);
        }
    }
    
    public Zone getZone(int id){
        return zones[id];
    }
}
