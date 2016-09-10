/*
 * Connection.java
 *
 * Created on August 28, 2007, 11:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.grantrules.webrates.data;

import java.util.List;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gharding
 */
public class Connection {
    
    private ServletResponse response;
    private HttpSession session;
    private boolean closed = false;
    
    /** Creates a new instance of Connection */
    /*
    public Connection() {
    }
    */
    public Connection(ServletResponse resp, HttpSession sess) {
        response = resp;
        session = sess;
    }
    
    public boolean isEnabledPair(String ccy) {
        if (session != null) {
            if (session.getAttribute("disabledPairs") != null) {
                List<String> disabledPairs = (List<String>) session.getAttribute("disabledPairs");
                for (String pair : disabledPairs) {
                    pair.equalsIgnoreCase(ccy);
                    return false;
                }
            }
        }
        return true;
    }
    
    public void close() {
        session = null;
        response = null;
        closed = true;
    }
    
    public boolean isClosed() {
        return closed;
    }
    
    public ServletResponse getResponse() {
        return response;
    }
    
    public HttpSession getSession() {
        return session;
    }
    
}
