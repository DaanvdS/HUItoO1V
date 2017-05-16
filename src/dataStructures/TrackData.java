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
public class TrackData {
    public Track[] tracks = new Track[17];
    
    public TrackData(int amount){
        for(int i=0; i<amount; i++){
            tracks[i]=new Track();
        }
    }    
    
    public Track getTrack(int id){
        return tracks[id];
    }
}
