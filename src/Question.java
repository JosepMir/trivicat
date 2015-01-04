import java.io.InputStream;
import javax.microedition.lcdui.*;
public class Question implements ItemStateListener {

	//posar com a titol el tipus de pregunta?
	private String pregunta;
	private String resposta1;
	private String resposta2;
	private String resposta3;
	private String resposta4;
	private Tauler tauler;
	private Form fmMain;
	private int[] num_preguntes;
	int fitxer_entrades = 38;
	boolean[] sortit;
	public boolean encertat;
	//Command OK;
	private ChoiceGroup cgQuestion;
	private StringItem sI;
	private int correcta;
	int numero_aleatori = 0;
	
	//funcio getFile
	StringBuffer sb = new StringBuffer();
	byte[] data = new byte[4600];
	
	Question (Tauler tauler) {
		int i;
		this.tauler = tauler;
		 
		sortit = new boolean[fitxer_entrades*6];
		for (i=0; i< fitxer_entrades*6 ;i++) {
			sortit[i] = false;
		}
		num_preguntes = new int[6];
		for (i=0; i<6; i++) {
			num_preguntes[i] =0; 
		}
		
		sI = new StringItem("", ""); 
		//'FORMULARI' http://www.java2s.com/Code/Java/J2ME/multipleChoiceGroup.htm
		 cgQuestion = new ChoiceGroup("", Choice.MULTIPLE);
		 cgQuestion.setFitPolicy(Choice.TEXT_WRAP_ON);
		 
//			Create Form, add components, listen for events
			fmMain = new Form("");
			fmMain.setItemStateListener(this);
			fmMain.setTitle("Trivicat");
			sI.setFont(Font.getFont(Font.FACE_PROPORTIONAL,
					 Font.STYLE_BOLD,
		                         Font.SIZE_MEDIUM));
	}
	 
   public void itemStateChanged(Item item)              
    {
    	if (item  == cgQuestion) {
   				  Alert alert1;
	    		if (cgQuestion.isSelected(correcta)) {
	    			encertat = true;
	    			alert1 = new Alert("Correcte");
	    			alert1.setString("Correcte");
	    		} else {
	    			encertat = false;
	    			alert1 = new Alert("Erroni");
	    			alert1.setString("Erroni");// .Resposta:" + resposta1);
	    			
	    		
	    		}
				alert1.setTimeout(500);
				tauler.redefineixFormatget();
	    		tauler.display.setCurrent(alert1,  tauler);
    	}	
     	
    }

	 /**
		 * @param type del 1 al 6, indica 1.txt, 2.txt..
		 */
	 public void mostraQuestio(int type) {
	nextQuestion(type);
	
	sI.setText(this.pregunta);
	cgQuestion.deleteAll();
	
	boolean un=true,dos=true,tres=true,quatre=true;
	 while (un || dos || tres || quatre) {
		 int num_aleatori2 = Tauler.rng.nextInt(4);
	 if (num_aleatori2 == 0 && un) {
		 correcta = cgQuestion.append(this.resposta1, null);
		 un = false;
	 }
	 if (num_aleatori2 == 1 && dos) {
		 cgQuestion.append(this.resposta2, null);
		 dos = false;
	 }
	 if (num_aleatori2 == 2 && tres) {
		 cgQuestion.append(this.resposta3, null);
	 	tres = false;
	 }
	 if (num_aleatori2 == 3 && quatre) {
		 cgQuestion.append(this.resposta4, null);
		 quatre = false;
	 }
	 }
	
	fmMain.deleteAll();
	fmMain.append(sI);
	fmMain.append(cgQuestion);
	
	
	tauler.display.setCurrent(fmMain);

}
	
	
	/**
	 * @param type del 1 al 6, indica 1.txt, 2.txt..
	 */
	private void nextQuestion(int type) {
		String linia;
		
		//	LECTURA FITXER
	try {
	 linia = getFile(type + ".txt"); //desar en format ANSI
	} catch (Exception ex) {
		//shaura de dir 
		linia = "";
		ex.printStackTrace();
	}
	

	if (num_preguntes[type-1] == fitxer_entrades) { //si ja han sortit totes les preguntes reiniciem les d'aquell tipus
		num_preguntes[type-1] = 0;
		
		for (int i=(type-1)*fitxer_entrades; i<type*fitxer_entrades ; i++) {
			sortit[i] = false;	
		}
		
	}
	
	do {
	numero_aleatori = Tauler.rng.nextInt(fitxer_entrades)+1;
	
	} while (sortit[((type-1)*fitxer_entrades)+ (numero_aleatori-1)]); //va fent el while mentre els numeros que surtin ja hagin sortit abans
	//abans estava malament, abans deia [(type*numero_aleatori)-1]
	sortit[((type-1)*fitxer_entrades)+ (numero_aleatori-1)] = true; //aquest es marca com a sortit
	
	num_preguntes[type-1]++; 
		
//shaura de fer que sigui 02, pq sino pot ser que busquis el "2#" i t'accepti el "42#"		
	String numero_aleatoriA = Integer.toString(numero_aleatori) + "#";
	String numero_aleatoriB = Integer.toString(numero_aleatori+1) + "#";
	int indexA = linia.indexOf(numero_aleatoriA) + numero_aleatoriA.length(); //sumar la llargada del "2-" pq no surti el numero per pantalla 
	int indexB = linia.indexOf(numero_aleatoriB);
	String frase = linia.substring(indexA, indexB);
	
	int index4 = frase.lastIndexOf('º');
	resposta4 = frase.substring(index4+1);
	
	frase = frase.substring(0, index4); //tallar resposta4
	int index3 = frase.lastIndexOf('º');
	resposta3 = frase.substring(index3+1);
	
	frase = frase.substring(0, index3); //tallar resposta3
	int index2 = frase.lastIndexOf('º');
	resposta2 = frase.substring(index2+1);
	
	frase = frase.substring(0, index2); //tallar resposta2
	int index1 = frase.lastIndexOf('º');
	resposta1 = frase.substring(index1+1);
	
	frase = frase.substring(0, index1); //tallar resposta1
	pregunta = frase;
	}
	
    //LECTURA FITXER    
    private String getFile(String file) throws Exception {
    	InputStream is = this.getClass().getResourceAsStream(file);
    	int len=0;
    	   	
    	sb.delete(0, sb.length()); //reiniciem StringBuffer
    	while((len=is.read(data))!=-1) { //(byte[] b, int off, int len) 
    	sb.append(new String(data,0,len)); 
    	}
    	is.close();
    	return sb.toString();
    }
}
