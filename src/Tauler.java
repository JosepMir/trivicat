/* Copyright © 2006 Nokia. */


import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Canvas;

import java.util.Date;
/**
 * Tauler: La pantalla de de joc principal
 */

class Tauler
extends Canvas
implements Runnable
{
	public Display display;
	Question questio;
	private Image tauler_fons;
	private Image[] f;
	private Image[] dau_img;
	boolean[] formatget;
	public Mario mario;
	public int mario_pos=1;
	private Image Mariosprite;
	public Foc foc;
	private Image Focsprite;
	public int foc_pos=1;
	private Vector possibles_pos_foc;
	private int possibles_pos_foc_cont=0;
	int[] posicio_temp;
	
	public boolean acabant = false;
	public boolean mostrar = false;
	boolean dau_tirat = false;
	boolean mogut = false;
	boolean dau_img_mostrat=false;
	boolean dau_img_mostrat_inici=false;
	int dau=1;
	int desplacament_visca=-30;
	static public Random rng;
	private int[][] camins = 	
	{{2,22,43,23,73,58},//casella 1
			{1,3},
			{2,4}, //casella 3
			{3,5}, //casella 4
			{4,6},
			{5,7},
			{44,6,8}, //node 7
			{7,9}, //casella 8
			{8,10},
			{9,11},
			{10,12},
			{11,13},
			{12,14},
			{13,18,15}, //node 14
			{14,16},
			{15,17},
			{16,38},
			{14,19},
			{18,20},
			{19,21},
			{20,22},
			{21,1},
			{1,24},
			{23,25},
			{24,26},
			{25,27},
			{26,28},
			{29,27,59}, //node 28
			{28,30},
			{29,31},
			{30,32},
			{31,33},
			{32,34},
			{33,35},
			{36,39,34},
			{35,37},
			{36,38},
			{37,17},
			{35,40},
			{39,41},
			{40,42},
			{41,43},
			{42,1},
			{7,45},
			{44,46},
			{45,47},
			{46,48},
			{47,49},
			{48,50},
			{51,54,49},
			{50,52},
			{51,53},
			{52,68},
			{50,55},
			{54,56},
			{55,57},
			{56,58},
			{57,1},
			{28,60},
			{59,61},
			{60,62},
			{61,63},
			{62,64},
			{63,65},
			{64,69,66},
			{65,67},
			{66,68},
			{67,53},
			{65,70},
			{69,71},
			{70,72},
			{71,73},
			{72,1}, 
	};
	//0: centre
	//1: torna a tirar
	//2: geografia /blau
	//3: art i llengua /marro
	//4: historia i religio /groc
	//5: ciencia /verd
	//6: entreteniment i espectacles /rosa
	//7: esports /taronja
	
	
	private int[] tipus_casella = {	0,	7,	6,	5,	2,	3,	4,
			3,1,2,6,1,5,7,5,1,3,5,3,6,4,2,
			5,2,7,6,4,3,4,1,7,5,1,6,2,6,1,4,
			6,5,4,7,3,
			3,1,5,7,1,2,6,2,1,3,2,7,3,5,4,
			4,1,6,2,1,7,5,7,1,4,
			7,4,2,3,6
	};
	
	private String getTipusNom (int tipus) {
		switch (tipus) {
		case 1:
			return "Torna a tirar";
		case 2:
			return "Geografia";
		case 3:
			return "Art i llengua";
		case 4:
			return "Història i religió";
		case 5:
			return "Ciència";
		case 6:
			return "Entreteniment";
		case 7:
			return "Esports";
		default:
			return "";
		}
	}
	
	
	/**
	 * @param casella
	 * @param dau
	 * @param origen sempre ha de ser 0 (només s'usa dins la funció per la recursió)
	 * @return les següents posicions possibles donat un dau i una casella
	 */
	private Vector segsPosicio(int casella, int dau, int origen){ //si és el començament origen=0. S'usa quan es crida recursivament per no tornar enrere
		int num_camins = camins[casella-1].length;
		Vector resultat = new Vector();
		
		
		if (dau==0) {
			resultat.addElement(new Integer(casella));
			return resultat;
		}
		
		for (int i=0; i<num_camins; i++) {
			if (origen == 0 || camins[casella-1][i]!=origen) {
				
				Vector resultat2 = segsPosicio(camins[casella-1][i], dau-1, casella);
				for (int j=0; j<resultat2.size(); j++) //copiem els valors retornats al Vector de resultats
					resultat.addElement(resultat2.elementAt(j));
			}
			
		}
		return resultat;
	}
	
	Tauler(Display display)
	{
		//this.display = display;
		
		
		
		
	}
	//bola de foc width 11   height 9
	/**
	 * @return Donat casella (1-73) retorna les coordenades (x,y) de la pantalla
	 */
	private int[] posicio(int casella) {
		int[][] posicions = //aprofitem la simetria per nomes introduir un quart de les posicions. aqui definim les 22 posicions del quadrant de baix a la dreta	
		{{64,64}, //casella1
				{64,78},
				{64,86},
				{64,94},
				{64,103},
				{64,111},
				{64,120},
				{76,120},
				{84,118}, //casella 9
				{91,114},
				{99,110},
				{104,105},
				{109,100},
				{115,90},
				{119,82},
				{121,74},
				{122,68},
				{106,85},
				{99,82},
				{91,78},
				{84,75},
				{77,72}};
		int[] sortida = {0,0};
		if (casella < 23) {
			sortida[0] = posicions[casella-1][0]-64; //ara ho retorna en base 64. Més endavant es podria fer pq aquí s'adaptés a la resolucio de pantalla
			sortida[1] = posicions[casella-1][1]-64;
		} else {
			if (casella < 44) {
				sortida[0] = posicions[casella-22][0]-64; //seria 21 si no fos pq al ser base 0 se n'hi resta un altre
				sortida[1] = - (posicions[casella-22][1]-64);
			} else {
				if (casella < 59) {
					sortida[0] = - (posicions[casella-37][0]-64);
					sortida[1] = posicions[casella-37][1]-64;
				} else {
					sortida = posicio(casella-30); //amb les de dalt tb es pot fer recursiu, pero he preferit ferho directe (aqui la cosa ja es complicava mes)
					sortida[0] = - sortida[0]; 
				}
			}
		}
		return sortida;
	}
	
	private Image ferTransparent(Image imatge) {
		int[]rawInt;
		
		rawInt = new int[imatge.getWidth() * imatge.getHeight()];
		imatge.getRGB(rawInt, 0, imatge.getWidth(), 0, 0, imatge.getWidth(), imatge.getHeight());
		imatge = Image.createRGBImage(rawInt, imatge.getWidth(), imatge.getHeight(), true);
		return imatge;
	}
	public void run()
	{
		questio = new Question(this);
		rng = new Random(new Date().getTime());
		
		//començem amb 0 formatgets
		formatget = new boolean[6];
		for (int i=0; i<6; i++) {
			formatget[i] = false;
		}
		
		
		
		try {
			//          
			//	      	crear imatge del fons
			//
			tauler_fons = Image.createImage("/tauler.png");
			
			//          
			//	      	crear imatges de tots els formatgets
			//
			f = new Image[6];
			for (int i=0; i<6; i++) {
				String i_temp = new Integer(i+1).toString();
				f[i] = Image.createImage("/f" + i_temp + ".png"); 
				f[i] = ferTransparent(f[i]);
			}
			
			//          
			//	      	crear imatge del personatge
			//
			
			switch (rng.nextInt(3)) {
			case 0:
				Mariosprite = Image.createImage("/ermita.png");
				break;
			case 1:
				Mariosprite = Image.createImage("/guai.png");
				break;
			case 2:
				Mariosprite = Image.createImage("/sang.png");
				break;
			}
			//Mariosprite = Image.createImage("/ermita.png"); //mariosprite.png");
			Mariosprite = ferTransparent(Mariosprite);
			mario = new Mario(Mariosprite);
			mario.defineReferencePixel(12,30);//9,24);
			
			//          
			//	      	crear imatge del cursor
			//
			Focsprite = Image.createImage("/aaspritepng.png");
			Focsprite = ferTransparent(Focsprite);
			foc = new Foc(Focsprite);
			foc.defineReferencePixel(3,4);
			
			
			
			//           
			//	      	crear imatges de tots els daus
			//
			dau_img = new Image[6];
			for (int i=0; i<6; i++) { 
				String i_temp = new Integer(i+1).toString();
				dau_img[i] = Image.createImage("/" + i_temp + ".png"); 
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NullPointerException ex){
			ex.printStackTrace();
		}
	}
	
	public void mostra (Display display) {
		synchronized(this)
		{
			this.display = display;
			setFullScreenMode(true);
		}
	}
	public void paint(Graphics g)
	{
		int width = getWidth();
		int height = getHeight();
		g.setColor(255,255,255);  // white
		g.fillRect(0, 0, width, height);
		
		g.setColor(0,0,0);  // black
		g.drawRect(1, 1, width-3, height-3);  // red border one pixel from edge
		g.setColor(255,0,0);  // red
		if (tauler_fons != null) {
			g.drawImage(tauler_fons, //si ho faig amb copyarea podre triar la resolucio de desti. i sempre treballar amb la maxima independentment del desti
					width/2,
					height/2,
					Graphics.VCENTER | Graphics.HCENTER);
		}
		
		if (!acabant || (acabant && mostrar)) {
			for (int i=0; i<6; i++){
				if (formatget[i]) {
					g.drawImage(f[i],
							width/2,
							height/2,
							Graphics.VCENTER | Graphics.HCENTER);
				}
			}
		}
		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,
				Font.STYLE_BOLD,
				Font.SIZE_MEDIUM));
		if (acabant) {
			g.setFont(Font.getFont(Font.FACE_SYSTEM,
					Font.STYLE_ITALIC,
					Font.SIZE_LARGE));
			g.drawString("ENHORABONA",
					width/2-30,
					height/2+desplacament_visca,
					0);
			desplacament_visca+=7;
		} else {
			posicio_temp = posicio(foc_pos);
			mario.setRefPixelPosition(width/2 + posicio_temp[0], height/2 + posicio_temp[1]);
			mario.paint(g);
			if (dau_tirat) {
				if (dau_img_mostrat == false) {
					
					g.drawImage(dau_img[dau-1],
							width/2,
							height/2,
							Graphics.VCENTER | Graphics.HCENTER);
					
					dau_img_mostrat_inici = true;
				} else {
					
//					dibuixa el foc a tots els llocs on és possible
					for (int i=0; i<possibles_pos_foc.size();i++) {
						posicio_temp = posicio(((Integer) possibles_pos_foc.elementAt(i)).intValue());
						foc.setRefPixelPosition(width/2 + posicio_temp[0], height/2 + posicio_temp[1]);
						foc.paint(g);
					}
					mario.paint(g); //es dibuixa dos cops pq en cas que shagi dibuixat foc aquest no quedi per sobre el mario
					
					g.drawString(new Integer(dau).toString(),
							width/2+55,
							height/2-62,
							0);
					
					
					if (mogut) {
						g.drawString(getTipusNom(tipus_casella[foc_pos-1]),
								width/2-62,
								height/2-62,
								0);
						
					} else {
						g.drawString("  Casella destí?",
								width/2-62,
								height/2-62,
								0);
						
					}
				}
			} else {
				g.drawString("  Tira el dau",
						width/2-62,
						height/2-62,
						0);
			}
		}
		
		
		
		
	}
	
	
	public synchronized void keyPressed(int keyCode)
	{
		//passar la tecla de tirar el dau a qualsevol de les de direccio i eliminar el 5
		
		if (dau_tirat) {
			if (keyCode == -4 || keyCode == -1 ) {
				
				if (possibles_pos_foc_cont == possibles_pos_foc.size()-1) {
					possibles_pos_foc_cont = -1;
				}
				foc_pos = ((Integer) possibles_pos_foc.elementAt(++possibles_pos_foc_cont)).intValue();
				
				mogut = true;
				this.repaint();
			}
			
			if (keyCode == -3 || keyCode == -2 ) {
				if (possibles_pos_foc_cont == 0) {
					possibles_pos_foc_cont = possibles_pos_foc.size();
				}
				foc_pos = ((Integer) possibles_pos_foc.elementAt(--possibles_pos_foc_cont)).intValue();
				
				
				mogut = true;
				this.repaint();
				
			}
			
			if (keyCode == - 5 && mogut) { //mig
				mario_pos = foc_pos;
				//dau = rng.nextInt(6) + 1;
				//nomss = new Integer(dau).toString(); //mostrar per pantalla 
				
				
				this.repaint();
				switch (tipus_casella[mario_pos-1]) {
				
				case 0:
					questio.mostraQuestio(rng.nextInt(6)+1);
					break;
				case 1: //tornar a tirar
					dau = rng.nextInt(6) + 1;
					possibles_pos_foc = segsPosicio(mario_pos, dau, 0);
					possibles_pos_foc_cont = 0;
					
					dau_tirat = true;
					dau_img_mostrat=false;
					dau_img_mostrat_inici=false;
					mogut = false;
					break;
				default:
					questio.mostraQuestio(tipus_casella[mario_pos-1]-1);
				break;
				}
				
			}
		} else {
			//si no s'ha tirat el dau
			if (keyCode == - 5 ||keyCode == - 1 ||keyCode == - 2 ||keyCode == - 3 ||keyCode == - 4) {
				dau = rng.nextInt(6) + 1;
				possibles_pos_foc = segsPosicio(mario_pos, dau, 0);
				possibles_pos_foc_cont = 0;
				
				dau_tirat = true;
				dau_img_mostrat=false;
				dau_img_mostrat_inici=false;
				mogut = false;
				
				
				//display.setCurrent(splashdau);
				//splashdau.run();
				
			}
		}
	}
	
	public void redefineixFormatget() {
		if ((questio.encertat == true) && ((mario_pos == 28) || (mario_pos == 7)|| (mario_pos == 14)|| (mario_pos == 35)|| (mario_pos == 65)|| (mario_pos == 50))) { //he de saber si es formatget
			formatget[tipus_casella[mario_pos-1]-2] = true;
			
		}
		
		//si ja tens tots els formatgets...
		if (formatget[0] && formatget[1] && formatget[2] && formatget[3] && formatget[4] && formatget[5] ) {
			acabant = true;
		}
		
		dau_tirat = false;
	}
	
	
}
