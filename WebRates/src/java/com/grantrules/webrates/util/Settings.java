/*
 * Settings.java
 *
 * Created on August 31, 2007, 11:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.grantrules.webrates.util;

import java.util.ResourceBundle;

/**
 *
 * @author gharding
 */
public class Settings {
    
    private static ResourceBundle settings = ResourceBundle.getBundle("rates");
    
    public static String get(String s) {
        return settings.getString(s);
    }
    
    public static int getInt(String s) {
        try {
            return Integer.parseInt(get(s));
        } catch (Exception e){}
        return 0;
    }
    
    public static long getLong(String s) {
        try {
            return Long.parseLong(get(s));
        } catch (Exception e){}
        return 0;
    }
    
    public static void reload() {
        settings = ResourceBundle.getBundle("rates");
    }
    

    
}
