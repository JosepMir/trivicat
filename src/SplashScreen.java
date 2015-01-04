/* Copyright © 2006 Nokia. */


import java.io.IOException;

import javax.microedition.lcdui.*;


/**
 * SplashScreen: Implements the splash screen for a simple MIDlet game.
 */
class SplashScreen
    extends Canvas
    implements Runnable
{
    private final TrivicatMIDlet midlet;
    private Image splashImage;
    private volatile boolean dismissed = false;


    SplashScreen(TrivicatMIDlet midlet)
    {
        this.midlet = midlet;
        setFullScreenMode(true);
        
                
    	try {
			splashImage = Image.createImage("/splash.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        new Thread(this).start();
    }


    public void run()
    {
    	//aqui que miri el temps actual
    	long temps = System.currentTimeMillis();
    	midlet.splashScreenPainted();
    	synchronized(this)
        {
    		try
            {
    			//aqui torna a mirar el temps i si passa de 3 segs que no faci el wait i sino que el resti dels 3
    			temps = System.currentTimeMillis() - temps; 
    			if (temps < 3000) {
    				wait(3000L-temps);   // 3 seconds
    			}
            }
            catch (InterruptedException e)
            {
                // can't happen in MIDP: no Thread.interrupt method
            }
            dismiss();
        }
        
    }


    public void paint(Graphics g)
    {
        int width = getWidth();
        int height = getHeight();
        g.setColor(0x00FFFFFF);  // white
        g.fillRect(0, 0, width, height);

        
        if (splashImage != null)
        {
            g.drawImage(splashImage,
                        width/2,
                        height/2,
                        Graphics.VCENTER | Graphics.HCENTER);
            splashImage = null;
            
            
        }
        
    }


    public synchronized void keyPressed(int keyCode)
    {
        //dismiss();
    }


    private void dismiss()
    {
        if (!dismissed)
        {
            dismissed = true;
            midlet.splashScreenDone();
        }
    }
}
