<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type='text/javascript'>
            /*var IframeState = {
                
                iframe: ,
                pricesLoadedTime: ,
                delay: 2,
                
                notifyPricesLoaded : function () {
                    pricesLoadedTime = this.getSeconds();
                }
                getSeconds : function () {
                    return Math.round(new Date.getTime() /  1000);
                }
                reload : function () {
                    if (iframe != null) iframe.src = iframe.src;
                }
                checkLoadedTime : function () {
                    if (pricesLoadedTime == null) return false;
                    if (this.pricesLoadedTime > this.getSeconds() - 2) {
                        return true; // time to disable the timer
                    }
                    return false; // keep going
                }
                timerRun : function () {
                    if (!this.checkLoadedTime()) {
                        this.reload();
                        setTimeout('IframeState.timerRun()', 2000);
                    }
                }
                    
            }
            */
            var IframeState = {
                iframe: null,
                loaded: false,
                notifyloadedstate: function() { this.loaded = true; },
                onload: function() { this.loaded = false; this.timerRun(); },
                timerRun: function() { if (!this.loaded) { this.reload(); setTimeout('IframeState.timerRun()', 2000); } },
                reload: function() { this.iframe.src = this.iframe.src; }
            }
                
            function compareprices(old, notold, values) {
                if (old < notold) return values[0];
                if (old == notold) return values[1];
                if (old > notold) return values[2];
                return "";
            }
            
            function newprices(arr) {
                var r = document.getElementById('rates');
                for (var i = 0; i< arr.length; i++) {
                    var p = arr[i];
                    var e = findElement(r, p);
                    var old = e.rate;
                    e.rate = p;
                    var tb = fixp(p.bid, p.precision);
                    var ta = fixp(p.ask, p.precision);
                    e.innerHTML = '<strong>'+p.symbol+'</strong> bid: <span class="'+compareprices(old.bid, p.bid, [ 'green', '', 'red' ])+'">'+getfromright(tb,3)+'<strong>'+getfromright(tb,2)+'</strong><span>'+getfromright(tb,1)+'</span></span> / ask: <span class="'+compareprices(old.ask, p.ask, [ 'green', '', 'red' ])+'">'+getfromright(ta,3)+'<strong>'+getfromright(ta,2)+'</strong><span>'+getfromright(ta,1)+'</span> / high: <span class="'+compareprices(old.high, p.high, [ 'green', '', 'red' ])+'">'+fixp(p.high,p.precision)+'</span> / low: <span class="'+compareprices(old.low, p.low, [ 'green', '', 'red' ])+'">'+fixp(p.low,p.precision)+'</span>';
                        
                    
                }
            }
            
            function fixp(v, p) {
                v = "" + v;
                if (v.indexOf('.') + p + 1 > v.length) {
                    v += "0000000000000".substr(0, p - (v.length - v.indexOf('.') - 1));
                }
                return v;
            }
            
            function getfromright(v, what) {
                if (what == 1) {
                    return v.substr(v.length - 1, 1);
                }
                if (what == 2) {
                    return v.substr(v.length - 3, 2);
                }
                if (what == 3) {
                    var t = v.substr(0, v.length - 3);
                    if (t.indexOf('.') == t.length - 1) {
                        return t.substr(0, t.length - 1);
                    }
                    return t;
                }
            }
            function findElement(r, p) {
                var rs = r.getElementsByTagName('li');
                var e = null;
                for (var i = 0; i<rs.length;i++) {
                    rate = rs[i];
                    if (rate.rate != null && rate.rate.symbol == p.symbol) {
                        e = rate;
                        //alert('found');
                        break;
                    }
                }
                if (e == null) {
                    e = document.createElement('li');
                    r.appendChild(e);
                    e.rate = p;
                    new Sortables(r, {});
                }
                
                return e;
            
            }
            
            /*
            
            
            var Rate = {
                lastPrice: ,
                newPrice: ,
                
                
                getHTML: function() {
                
                }
                
            };
            */
            
            function zeropad(v, l) {
                if (v.length < l) {
                    v = v + "0000000".substr(0,l-v.length);
                }
                return v;
            }
            
            
            window.onload=function(){document.getElementById('iframeblock').innerHTML='<iframe onload="this.src=this.src;document.getElementById(\'update\').innerHTML=document.getElementById(\'update\').innerHTML+\'<br/>refresh tried:\'+Date();" src="/ratesComet"></iframe>';};
        </script>
        <script type='text/javascript' src="/js/mootools.v1.11.js"></script>
        <script type="text/javascript">
            
            
            
            
        </script>
        
        <style type="text/css">
            xli { display: block; margin: 0; padding: 0; width: 1000px; height: 100px; float: left;}
            ul { margin: 0; padding:0; list-style-type: none; display: block; width: 450px; }
            .green { background-color: rgb(0,255,0); }
            .red { background-color: rgb(255,0,0); }
            #iframeblock {display:none;}
        </style>
    </head>
    <body>

    <h1>WebRates</h1>
    
    <ul id="rates">
        
    </ul>
    
    <div id="iframeblock"></div>
    <!-- iframe src="/ratesComet"></iframe -->
    
    <a href="javascript:var i = document.getElementsByTagName('iframe')[0];void(i.src=i.src);">reload iframe</a>
    
    <div id="update"></div>
    
    </body>
</html>
