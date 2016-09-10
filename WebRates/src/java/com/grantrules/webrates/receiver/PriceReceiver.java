/*
 * Copyright (c) 2004 FXCM, LLC. All Rights Reserved.
 * 32 Old Slip, 10th Floor, New York, NY 10005 USA
 *
 * THIS SOFTWARE IS THE CONFIDENTIAL AND PROPRIETARY INFORMATION OF
 * FXCM, LLC. ("CONFIDENTIAL INFORMATION"). YOU SHALL NOT DISCLOSE
 * SUCH CONFIDENTIAL INFORMATION AND SHALL USE IT ONLY IN ACCORDANCE
 * WITH THE TERMS OF THE LICENSE AGREEMENT YOU ENTERED INTO WITH
 * FXCM.
 * 01/10/2005   Andre Mermegas  updated to demo setting/updating stop/limit on entry/market orders
 * 02/11/2005   Andre Mermegas  changes for new msg format.
 * 6/03/2005    Andre Mermegas  sendMessage(), follow up to interface changes
 * 6/13/2005   Miron follow up to FXCMLoginProperties changes
 */

package com.grantrules.webrates.receiver;

import com.fxcm.external.api.transport.FXCMLoginProperties;
import com.fxcm.external.api.transport.GatewayFactory;
import com.fxcm.external.api.transport.IGateway;
import com.fxcm.external.api.transport.listeners.IGenericMessageListener;
import com.fxcm.external.api.transport.listeners.IStatusMessageListener;
import com.fxcm.external.api.util.MessageGenerator;
import com.fxcm.fix.other.BusinessMessageReject;
import com.fxcm.fix.posttrade.CollateralReport;
import com.fxcm.fix.posttrade.PositionReport;
import com.fxcm.fix.pretrade.MarketDataSnapshot;
import com.fxcm.fix.pretrade.TradingSessionStatus;
import com.fxcm.fix.trade.ExecutionReport;
import com.fxcm.fix.trade.OrderSingle;
import com.fxcm.fix.SideFactory;
import com.fxcm.messaging.ISessionStatus;
import com.fxcm.messaging.ITransportable;
import com.grantrules.webrates.buffer.PriceBuffer;
import com.grantrules.webrates.data.Price;
import com.grantrules.webrates.scheduler.PriceScheduler;
import com.grantrules.webrates.util.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Example of how to use the FXCM API
 *
 * @author: Andre Mermegas
 * Date: Dec 15, 2004
 * Time: 4:19:00 PM
 */
public class PriceReceiver {
    private static boolean order = true;
    private static List accounts = new ArrayList();
    private ExecutionReport[] orders;
    private PositionReport[] positions;
    private static String accountMassID;
    private static String openOrderMassID;
    private static String openPositionMassID;
    private static String closedPositionMassID;
    private static String tradingSessionStatusID;
    private static TradingSessionStatus tradingSessionStatus;
    
    //private PriceBuffer buffer = new PriceBuffer();
    private PriceScheduler scheduler;


    public PriceReceiver() {
        
        scheduler = PriceScheduler.getInstance();
        
        // step 1: get an instance of IGateway from the GatewayFactory
        final IGateway fxcmGateway = GatewayFactory.createGateway();
        /*
            step 2: register a generic message listener with the gateway, this
            listener in particular gets all messages that are related to the trading
            platform MarketDataSnapshot,OrderSingle,ExecutionReport, etc...
        */
        fxcmGateway.registerGenericMessageListener(new IGenericMessageListener() {
            public void messageArrived(ITransportable message) {
                if (message instanceof MarketDataSnapshot) {
                    MarketDataSnapshot incomingQuote = (MarketDataSnapshot) message;
                    /*
                        here is an example of how to send a market order to the FXCM
                        platform, in this test client it executes a BUY Market Order on
                        the first MarketDataSnapshot recieved.
                    */
                    try {
                        Price price = new Price();
                        price.setSymbol(incomingQuote.getInstrument().getSymbol());
                        price.setAskHigh(incomingQuote.getAskHigh());
                        price.setBidLow(incomingQuote.getBidLow());
                        
                        // not sure about these
                        price.setAsk(incomingQuote.getAskOpen());
                        price.setBid(incomingQuote.getBidOpen());
                        //price.setPointSize(incomingQuote.getInstrument().getFXCMSymPointSize());
                        price.setPrecision(incomingQuote.getInstrument().getFXCMSymPrecision());
                        
                        //System.out.println(price);
                        scheduler.getBuffer().addMessage(price);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /*
            step 3: register a status message listener, this listener recieves messages
            pertaining to the status of your current session.
        */
        fxcmGateway.registerStatusMessageListener(new IStatusMessageListener() {
            public void messageArrived(ISessionStatus status) {
                String statusMessage = status.getStatusMessage();
                if (statusMessage != null && statusMessage.trim().length() > 0) {
                    System.out.println("client: inc status msg = " + statusMessage);
                }
            }
        });

        try {
            /*
                step 4: call login on the gateway, this method takes an instance of FXCMLoginProperties
                which takes 4 parameters: username,password,terminal and server or path to a Hosts.xml
                file which it uses for resolving servers.
                As soon as the login  method executes your listeners begin receiving asynch messages from the FXCM servers.
            */
            //FXCMLoginProperties properties = new FXCMLoginProperties("FX553938001", "4962", "Demo", "http://www.fxcorporate.com/Hosts.jsp", null);
            System.out.println("client: login attempt");
            FXCMLoginProperties properties = new FXCMLoginProperties(Settings.get("login.user"), Settings.get("login.pass"), Settings.get("login.terminal"), Settings.get("login.server"), null);
            fxcmGateway.login(properties);
            fxcmGateway.requestTradingSessionStatus();
            //accountMassID = fxcmGateway.requestAccounts();
            //openOrderMassID = fxcmGateway.requestOpenOrders();
            //openPositionMassID = fxcmGateway.requestOpenPositions();
            //closedPositionMassID = fxcmGateway.requestClosedPositions();
            //step 5: remember to call fxcmGateway.logout(); when you are done with your connection and wish to logout
            System.out.println("client: done logging in\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}