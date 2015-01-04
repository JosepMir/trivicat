import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public class Foc extends Sprite  {
	    
	private int[] seq = {0,1,2,3,4,3,2,1};
	Foc(Image imatge) {
		super(imatge, 7, 8);
		this.setFrameSequence(seq);
		}
	 
	
}
