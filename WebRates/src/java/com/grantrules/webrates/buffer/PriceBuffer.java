/*
 * PriceBuffer.java
 *
 * Created on August 28, 2007, 10:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.grantrules.webrates.buffer;

import com.grantrules.webrates.data.Price;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author gharding
 */
public class PriceBuffer {
    
    private List<Price> collection = new ArrayList<Price>();
    private Map<String,Price> currentPrices = new HashMap<String,Price>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock writelock = rwl.writeLock();
    
    /** Creates a new instance of PriceBuffer */
    public PriceBuffer() {
    }
    
    public void addMessage(Price o) {
        writelock.lock();
        try {
            if (!collection.contains(o) && !currentPrices.containsValue(o)) {
                collection.add(o);
                currentPrices.put(o.getSymbol(), o);
            }
        } finally {
            writelock.unlock();
        }
    }
    
    public void clear() {
        writelock.lock();
        try {
            collection.clear();
        } finally {
            writelock.unlock();
        }
    }
    
    public List fetch() {
        List<Price> temp = new ArrayList<Price>();
        writelock.lock();
        try {
            temp.addAll(collection);
            collection.clear();
        } finally {
            writelock.unlock();
        }
        return temp;
    }
    
    public Map<String,Price> getCurrentPrices() {
        return currentPrices;
    }
    
}
