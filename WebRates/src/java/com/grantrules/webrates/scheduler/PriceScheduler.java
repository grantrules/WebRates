/*
 * PriceScheduler.java
 *
 * Heart of a COMET app. Incoming connections are added to collection. Prices
 * are added to the buffer. On a timer, the thread pool is used to send the
 * buffer of messages to each connection. Stale connections are removed
 *
 */

package com.grantrules.webrates.scheduler;

import com.grantrules.webrates.buffer.PriceBuffer;
import com.grantrules.webrates.data.Connection;
import com.grantrules.webrates.data.Price;
import com.grantrules.webrates.util.Settings;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gharding
 */
public class PriceScheduler {
    
    private List<Connection> connections = new ArrayList<Connection>();
    
    private PriceBuffer buffer = new PriceBuffer();
    private ThreadPoolExecutor tpe;
    
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock writelock = rwl.writeLock();
    
    private static final PriceScheduler instance = new PriceScheduler();
    
    /** Creates a new instance of PriceScheduler */
    private PriceScheduler() {
        tpe = new ThreadPoolExecutor(Settings.getInt("scheduler.pool.min"), Settings.getInt("scheduler.pool.max"), Settings.getLong("scheduler.pool.timeout"), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10000));
        initTimer();
    }
    
    public static PriceScheduler getInstance() {
        return instance;
    }
    
    public void addConnection(Connection c) {
        //if (connections.size() > 10000) return;
        writelock.lock();
        try {
            connections.add(c);
        } finally {
            writelock.unlock();
        }
    }
    
    public void removeConnection(Connection c) {
        writelock.lock();
        try {
            for (int i = 0; i<connections.size(); i++) {
                if (connections.get(i).getResponse() == c.getResponse()) {
                    try {
                        c.getResponse().getWriter().close();
                    } catch (IOException ex) { System.out.println(ex.getMessage()); }
                    connections.remove(i);
                }
            }
        } finally {
            writelock.unlock();
        }
    }
    
    public PriceBuffer getBuffer() {
        return buffer;
    }
    
    private void sendMessages() {
        
        List msgs = buffer.fetch();
        
        if (!Settings.get("debug").trim().equalsIgnoreCase("off")) System.out.println("Connections: "+connections.size()+" / Messages: "+msgs.size() +" / Thread pool: "+tpe.getActiveCount() + "/"+tpe.getTaskCount()+"/"+tpe.getPoolSize() + " waiting: " + tpe.getQueue().size());
        
        if (msgs.size() > 0 && connections.size() > 0) {
            for (int i = 0; i < connections.size(); i++) {
                if (connections.get(i).isClosed() || connections.get(i).getResponse() == null) {
                    connections.remove(i--);
                } else {
                    try {
                        tpe.execute(new PriceSender(connections.get(i), msgs));
                    } catch (Exception e) {
                        try {
                            connections.get(i).getResponse().getWriter().close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } finally {
                            connections.get(i).close();
                        }
                        e.printStackTrace();
                    }
                }
            }
        }
        
    }
    
    
    /* timer to send the buffer */
    private void initTimer() {
        new Thread(
                new Runnable() {
            public void run() {
                for (;;) {
                    sendMessages();
                    try {
                        // the big guy.
                        Thread.sleep(Settings.getInt("scheduler.timer.delay"));
                    } catch(Exception e){}
                }
            }
        }).start();
    }
    
}

class PriceSender implements Runnable {
    
    private Connection con;
    private List<Price> msgs;
    
    public PriceSender(Connection con, List<Price> msgs) {
        this.con = con;
        this.msgs = msgs;
    }
    
    public void sendMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append("<script type='text/javascript'>var prices = [];");
        try {
            for (Price p : (List<Price>)msgs) {
                if (con.isEnabledPair(p.getSymbol())) {
                    sb.append(p);
                }
            }
            
        } catch (IllegalStateException e) {
            // session got screwed up
            
        }
        sb.append("window.parent.newprices(prices);</script>");
        
        
        if (!con.isClosed()) {
            try {
                final PrintWriter pw = ((HttpServletResponse)con.getResponse()).getWriter();
                synchronized (pw) {
                    
                    pw.println(sb);
                    pw.flush();
                    //pw.close();
                    
                    
                }
            } catch (Exception e) {
                System.out.println("IOException at the printwriter crap");
                con.close();
                //System.err.println(e.getMessage());
                //e.printStackTrace();
            }
            
        }
    }
    
    public void run() {
        try {
            // randomly delaying sending data, otherwise the connection bogs
            // down and connections get dropped
            Thread.sleep(Math.abs((new Random()).nextInt()) % 200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        sendMessage();
    }
    
}
