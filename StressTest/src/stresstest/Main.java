package stresstest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

/**
 *
 * @author gharding
 */
public class Main {
    
    private static final int THREADS = 1000;
    private static final int DELAY = 2000;
    private static final int BATCH_SIZE = 100;
    
    //private static final boolean REPEAT = false; // true won't really work as planned
    
    
    
    public static void main(String[] args) {
        URL turl = null;
        if (args.length > 0) {
            try {
                turl = new URL(args[0]);
                System.out.println("Using input URL");
            } catch (Exception e) {}
        }
        
        if (turl == null) {
            try {
                //turl = new URL("http://localhost:8080/ratesComet");
                turl = new URL("http://webrates.dev.fxcorp.prv:80/ratesComet");
                System.out.println("Using default URL");
            } catch (Exception e) {}
        }
        
        if (turl == null) {
            System.err.println("Default URL didn't even work???");
            System.exit(0);
        }
        
        final URL url = turl;
        
        System.out.println("-----------");
        
        for (int i = 0; i < THREADS; i++) {
            new Thread(new Runnable() {
                public void run() {
                    Socket socket = null;
                    PrintWriter out = null;
                    BufferedReader in = null;
                    //do {
                        try {
                            socket = new Socket(url.getHost(), url.getPort());
                            out = new PrintWriter(socket.getOutputStream(), true);
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));



                        } catch (Exception e){System.out.println(e.getMessage());}
                        out.print("GET "+url.getPath()+" HTTP/1.1\nHost: localhost\n\n");
                        out.flush();

                        try {
                            String strIn;
                            while ((strIn = in.readLine()) != null) {
                                //System.out.println(strIn);
                            } 
                        } catch (IOException ioe) { System.out.println(ioe.getMessage()); }
                    //} while (REPEAT);
                }
            }).start();
            
            if (i % BATCH_SIZE == 0) {
                System.out.println("Threads started: "+i);
                try {
                    Thread.sleep(DELAY);
                } catch (Exception e) {}
            }
            
        }
        while (true) {
            if (Thread.activeCount() == 1) break;
            System.out.println("Active threads: "+Thread.activeCount());
            try {
                Thread.sleep(5000);
            } catch (Exception e) {}
        }
    }
}
