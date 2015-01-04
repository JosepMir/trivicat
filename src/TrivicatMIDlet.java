

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class TrivicatMIDlet extends MIDlet
{
	Tauler tauler;
	SplashScreen jo;
int contador_final=0;
int cont = 0;
	public TrivicatMIDlet()
	{
		
	}

	public void startApp()
	{
		jo =  new SplashScreen(this);
		Display.getDisplay(this).setCurrent(jo);
	}
	
	
	public void pauseApp() 
	{
	}
	
	public void destroyApp(boolean b) 
	{
	}
	
	void splashScreenPainted() //s'aprofita que s'ensenya la splashscreen per anar fent coses
	{
		tauler =  new Tauler(Display.getDisplay(this));
		new Thread(tauler).start();
	}
	
	void splashScreenDone()
	{

		tauler.mostra(Display.getDisplay(this));
		Display.getDisplay(this).setCurrent(tauler);
		

		//for (int i = 0; i<5000; i++) {
		while (true) {
			synchronized(this) {
				try
				{
					wait(250L);
				}catch (InterruptedException e)
				{
					// can't happen in MIDP: no Thread.interrupt method
				}
				
				//controlar timer de imatge dau
				if (tauler.dau_img_mostrat_inici == true) {
					cont++;
					if (cont==5 ) {
					tauler.dau_img_mostrat=true;
					tauler.dau_img_mostrat_inici = false;
					cont = 0;
					}
				}
				
				//controlar les animacions per defecte
				tauler.repaint();
				tauler.mario.nextFrame();
				tauler.foc.nextFrame();
				
				//controlar efecte de quan el programa acaba
				if (tauler.acabant) {
					tauler.mostrar = !tauler.mostrar;
					contador_final++;
					if (contador_final>10) {
						notifyDestroyed();
					}
				}
			}
		}
	}
}

