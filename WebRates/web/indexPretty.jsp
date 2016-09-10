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
                    //e.innerHTML = '<strong>'+p.symbol+'</strong> bid: <span class="'+compareprices(old.bid, p.bid, [ 'green', '', 'red' ])+'">'+getfromright(tb,3)+'<strong>'+getfromright(tb,2)+'</strong><span>'+getfromright(tb,1)+'</span></span> / ask: <span class="'+compareprices(old.ask, p.ask, [ 'green', '', 'red' ])+'">'+getfromright(ta,3)+'<strong>'+getfromright(ta,2)+'</strong><span>'+getfromright(ta,1)+'</span> / high: <span class="'+compareprices(old.high, p.high, [ 'green', '', 'red' ])+'">'+fixp(p.high,p.precision)+'</span> / low: <span class="'+compareprices(old.low, p.low, [ 'green', '', 'red' ])+'">'+fixp(p.low,p.precision)+'</span>';
                    e.innerHTML = '<div class="pairContainer"><span class="pairName"><h1>'+p.symbol+'</h1></span><span class="pairTime"><h2>10:53:18</h2></span><div class="'+compareprices(old.bid, p.bid, [ 'green', '', 'red' ])+'"><h3>Low: '+fixp(p.low,p.precision)+'</h3><table align="center" border="0" cellpadding="2" cellspacing="0"><tr><td valign="baseline" class="numbers"><h1>'+getfromright(tb,3)+'</h1></td><td valign="baseline" class="numbers"><h2>'+getfromright(tb,2)+'</h2></td><td valign="top" class="numbers"><h3>'+getfromright(tb,1)+'</h3></td></tr></table></div><div class="'+compareprices(old.ask, p.ask, [ 'green', '', 'red' ])+'"><h3>High: '+fixp(p.high,p.precision)+'</h3><table align="center" border="0" cellpadding="2" cellspacing="0"><tr><td valign="baseline" class="numbers"><h1>'+getfromright(ta,3)+'</h1></td><td valign="baseline" class="numbers"><h2>'+getfromright(ta,2)+'</h2></td><td valign="top" class="numbers"><h3>'+getfromright(ta,1)+'</h3></td></tr></table></div></div>';
                    //setTimeout("clearBlinky('e')", 200);
                        
                    
                }
            }
            
            function clearBlinky(e) {
                var p = e.rate;
                e.innerHTML = '<div class="pairContainer"><span class="pairName"><h1>'+p.symbol+'</h1></span><span class="pairTime"><h2>10:53:18</h2></span><div><h3>Low: '+fixp(p.low,p.precision)+'</h3><table align="center" border="0" cellpadding="2" cellspacing="0"><tr><td valign="baseline" class="numbers"><h1>'+getfromright(tb,3)+'</h1></td><td valign="baseline" class="numbers"><h2>'+getfromright(tb,2)+'</h2></td><td valign="top" class="numbers"><h3>'+getfromright(tb,1)+'</h3></td></tr></table></div><div><h3>High: '+fixp(p.high,p.precision)+'</h3><table align="center" border="0" cellpadding="2" cellspacing="0"><tr><td valign="baseline" class="numbers"><h1>'+getfromright(ta,3)+'</h1></td><td valign="baseline" class="numbers"><h2>'+getfromright(ta,2)+'</h2></td><td valign="top" class="numbers"><h3>'+getfromright(ta,1)+'</h3></td></tr></table></div></div>';
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
                    //new Sortables(r, {});
                    clickable();
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
            
            
/* =========================
    blinky color stuff
========================= */
  function deselectAll() {
    var e = document.getElementById('rates').getElementsByTagName('li');
    for (var i = 0; i<e.length; i++) {
      e[i].isselected = false;
      e[i].className = '';
    }
    
  }
  
  function clicky(e) {
  
    if (!e) var e = window.event;
    
    var targ;
    if (e.target) targ = e.target;
	  else if (e.srcElement) targ = e.srcElement;
	  if (targ.nodeType == 3) targ = targ.parentNode;
		
    if (!e.ctrlKey) {
      deselectAll();
    }
    while (targ.tagName != "LI") { targ = targ.parentNode; }
    if (targ.isselected) {
      targ.isselected = false;
      targ.className = '';
    } else {
      targ.isselected = true;
      targ.className = 'selected';
    }
     
  }
  
  function clickable() {
    var e = document.getElementById('rates').getElementsByTagName('li');
    for (var i = 0; i<e.length; i++) {
      e[i].onclick = clicky;
      e[i].className='notselected';
    }
    
    document.getElementById('pick').onclick=setbgcolor;
    
    //setTimeout(blinky, 1000);
  }
  
  /*
  function blinky() {
    var e = document.getElementById('clickable').getElementsByTagName('div');
    for (var i = 0; i<e.length; i++) {
      if (e[i].highlighted) { e[i].style.backgroundColor = '#ffffff';}
      else {      e[i].style.backgroundColor = (e[i].mycolor != null ? e[i].mycolor : '#00ff00'); }
      e[i].highlighted = !e[i].highlighted;
    }
    setTimeout(blinky, 1000);
  }
  */
  
  function pad(str) {
    if (str.length == 1) { return "0" + str; }
    return str;
    
  }
  
  function setbgcolor(e) {
    var rgbhex = '#'+pad(parseInt(document.getElementById('r').value,10).toString(16))+pad(parseInt(document.getElementById('g').value,10).toString(16))+pad(parseInt(document.getElementById('b').value,10).toString(16));
    var e = document.getElementById('rates').getElementsByTagName('li');
    for (var i = 0; i<e.length; i++) {
      if (e[i].isselected) {
        if (e[i].highlighted) { e[i].style.backgroundColor = rgbhex; }
        e[i].mycolor=rgbhex;
      }
    }
  
    
  }
  /* end blinky */
            
            
            window.onload=function(){document.getElementById('iframeblock').innerHTML='<iframe onload="this.src=this.src;document.getElementById(\'update\').innerHTML=document.getElementById(\'update\').innerHTML+\'<br/>refresh tried:\'+Date();" src="/ratesComet"></iframe>';clickable();};
        </script>
        <script type='text/javascript' src="/js/mootools.v1.11.js"></script>
        <script type="text/javascript">
            
            
            
            
        </script>
        
        <style type="text/css">
            li {display: block;float: left;margin:5px;}
            xli { display: block; margin: 0; padding: 0; width: 1000px; height: 100px; float: left;}
            ul { margin: 0; padding:0; list-style-type: none; display: block; width: 650px; }
            .green { background-color: rgb(0,255,0); }
            .red { background-color: rgb(255,0,0); }
            #iframeblock {display:none;}
            
            .selected .pairContainer { border-color: rgb(255, 127, 0) !important; }
        </style>
        <style type="text/css">

.pairContainer {
        width:186px;
	height:81px;
	border-color:#000000;
	border-style:solid;
	border-width:3px;
	background-image:url(images/fade-lg.png);
	background-repeat:repeat-x;
	background-color:#9f9fb0;
}

.pairName {
	float:left;
	width:78px;
	height:20px;
	background-image:url(images/fade-sm.png);
	background-repeat:repeat-x;
	background-color:#5c5c70;
}

.pairTime {
	float:right;
	width:100px;
	height:20px;
	background-image:url(images/fade-sm.png);
	background-repeat:repeat-x;
	background-color:#b6b5c4;
}
/*
.pairOne {
	float:left;
	width:50%;
	height:61px;
}

.pairTwo {
	float:left;
	width:50%;
	height:61px;
}
*/
.pairContainer div {
    float:left;
    width:50%;
    height:61px;
}


h1 { 
font-family:Verdana, Helvetica, sans-serif;
font-weight:bold;
font-size:11px;
color:#ffffff;
padding:0;
margin-top:2%;
margin-left:8%;
margin-bottom:1%;
}

h2 { 
font-family:Verdana, Helvetica, sans-serif;
font-weight:normal;
font-size:11px;
color:#000000;
padding:0;
margin-top:2%;
margin-right:5%;
margin-bottom:1%;
text-align:right;
}

h3 { 
font-family:Verdana, Helvetica, sans-serif;
font-weight:normal;
font-size:10px;
color:#ffffff;
padding:0;
margin-top:2%;
margin-bottom:0px;
text-align:center;
}

.numbers h1 { 
font-family:Arial, Helvetica, sans-serif;
font-weight:normal;
font-size:12px;
color:#ffffff;
padding:0px;
margin:0px;
text-align:left;
}

.numbers h2 { 
font-family:Arial, Helvetica, sans-serif;
font-weight:bold;
font-size:39px;
color:#ffffff;
padding:0px;
margin:0px;
text-align:left;
}

.numbers h3 { 
font-family:Arial, Helvetica, sans-serif;
font-weight:normal;
font-size:12px;
color:#ffffff;
padding:0px;
margin:0px;
text-align:left;
}




</style>
    </head>
    <body>

    <h1>WebRates</h1>
    
    <ul id="rates">
        
    </ul>
    
    <div id="iframeblock"></div>
    <!-- iframe src="/ratesComet"></iframe -->
    <a href="javascript:var i = document.getElementsByTagName('iframe')[0];void(i.src=i.src);">reload iframe</a>
    <div style="display: none;"
         
         <a href="javascript:var i = document.getElementsByTagName('iframe')[0];void(i.src=i.src);">reload iframe</a>
        <div>
            Set bg color: r: <input id="r"> g: <input id="g"> b: <input id="b"> <input type="button" id="pick" value="Go!"/>
        </div>
        
        <div id="update"></div>
        
    </div>
    
    </body>
</html>
