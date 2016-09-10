/*
 * Price.java
 *
 * Created on August 28, 2007, 10:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.grantrules.webrates.data;

import java.io.Serializable;

/**
 *
 * @author gharding
 */
public class Price implements Serializable {
    
    private String symbol;
    private double bidLow;
    private double askHigh;
    private double bid;
    private double ask;
    private int precision;
    
    /** Creates a new instance of Price */
    public Price() {
    }
    
    public String toString() {
        // generates javascript. i wonder if this is bad
        return "var p = new Price();p.symbol='"+getSymbol()+"';p.low="+getBidLow()+";p.high="+getAskHigh()+";p.bid="+getBid()+";p.ask="+getAsk()+";p.precision="+getPrecision()+";prices.push(p);";
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getBidLow() {
        return bidLow;
    }

    public void setBidLow(double bidLow) {
        this.bidLow = bidLow;
    }

    public double getAskHigh() {
        return askHigh;
    }

    public void setAskHigh(double askHigh) {
        this.askHigh = askHigh;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }
    
    public boolean equals(Object o) {
        if (o instanceof Price) {
            Price p = (Price)o;
            if (p.getSymbol().equalsIgnoreCase(this.symbol)
                && p.getAsk() == this.getAsk()
                && p.getAskHigh() == this.getAskHigh()
                && p.getBid() == this.getBid()
                && p.getBidLow() == this.getBidLow()) {
                    return true;
            }
        }
        
        return false;
        
    }

    public void setPrecision(int i) {
        this.precision = i;
    }

    // returns number of digits following dec. point
    public int getPrecision() {
        return precision;
    }
    
}
