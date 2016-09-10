/*
 * InitServlet.java
 *
 * Created on August 28, 2007, 6:09 PM
 */

package com.grantrules.webrates.servlet;

import com.grantrules.webrates.receiver.PriceReceiver;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author gharding
 * @version
 */
public class InitServlet extends HttpServlet {
    
    public void init() {
        new Thread(
            new Runnable() {
                public void run() {
                    new PriceReceiver();
                }
            }).start();
    }
}
