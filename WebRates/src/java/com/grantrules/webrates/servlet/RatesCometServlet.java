/*
 * ChatServlet.java
 *
 * Created on August 21, 2007, 4:34 PM
 */

package com.grantrules.webrates.servlet;

import com.grantrules.webrates.data.Connection;
import com.grantrules.webrates.data.Price;
import com.grantrules.webrates.scheduler.PriceScheduler;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;

/**
 *
 * @author gharding
 * @version
 */
public class RatesCometServlet
    extends HttpServlet implements CometProcessor {

    /*
    protected List<HttpServletResponse> connections = 
        new ArrayList<HttpServletResponse>();
    protected MessageSender messageSender = null;
     */
    
    
    PriceScheduler ps = null;
    
    
    public void init() throws ServletException {

        ps = PriceScheduler.getInstance();

    }

    public void destroy() {

        /*
        connections.clear();

        messageSender.stop();
        messageSender = null;
         */

    }

    /**
     * Process the given Comet event.
     * 
     * @param event The Comet event that will be processed
     * @throws IOException
     * @throws ServletException
     */
    public void event(CometEvent event)
        throws IOException, ServletException {
        HttpServletRequest request = event.getHttpServletRequest();
        HttpServletResponse response = event.getHttpServletResponse();
        if (event.getEventType() == CometEvent.EventType.BEGIN) {
            log("Begin for session: " + request.getSession(true).getId());
            
            Connection con = new Connection(response, request.getSession());

            PrintWriter writer = response.getWriter();
            writer.println("<script type='text/javascript'>function Price() {}");
            //writer.println("window.parent.notifypricesloaded()");
            // send init prices
            writer.println("var prices = [];");
            for (Map.Entry entry : ps.getBuffer().getCurrentPrices().entrySet()) {
                Price p = (Price) entry.getValue();
                if (con.isEnabledPair(p.getSymbol())) {
                    writer.print(p);
                }
            }
            writer.println("window.parent.newprices(prices);</script>");
            writer.flush();

            ps.addConnection(con);

        } else if (event.getEventType() == CometEvent.EventType.ERROR) {
            log("Error for session: " + request.getSession(true).getId());
            
            ps.removeConnection(new Connection(response, null));

            event.close();
        } else if (event.getEventType() == CometEvent.EventType.END) {
            log("End for session: " + request.getSession(true).getId());
            
            ps.removeConnection(new Connection(response, null));
            //response.getWriter().close();
            
            /*
            synchronized(connections) {
                connections.remove(response);
            }
            PrintWriter writer = response.getWriter();
            writer.println("</body></html>");
            event.close();
             */
        } else if (event.getEventType() == CometEvent.EventType.READ) {
            /*
            InputStream is = request.getInputStream();
            byte[] buf = new byte[512];
            do {
                int n = is.read(buf); //can throw an IOException
                if (n > 0) {
                    log("Read " + n + " bytes: " + new String(buf, 0, n) 
                            + " for session: " + request.getSession(true).getId());
                } else if (n < 0) {
                    //error(event, request, response);
                    
                    return;
                }
            } while (is.available() > 0);
             */
        }
    }    
}