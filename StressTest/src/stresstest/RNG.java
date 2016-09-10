/*
 * RNG.java
 *
 * Created on September 13, 2007, 12:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package stresstest;

import java.util.Random;

/**
 *
 * @author gharding
 */
public class RNG {
    
    /** Creates a new instance of RNG */
    public RNG() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        while (true) { System.out.println(Math.abs((new Random()).nextInt()) % 200); }
    }
    
}
