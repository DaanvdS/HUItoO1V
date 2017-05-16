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
public class Zone {
    public int id;
    public Port[] ports = new Port[30];
    
    public Zone(int t_id, int amount){
        id=t_id;
        for(int i=0; i<amount; i++){
            ports[i]=new Port();
        }
    }
        
    public Port getPort(int id){
        return ports[id];
    }
    
    public int getId(){
        return id;
    }
}
