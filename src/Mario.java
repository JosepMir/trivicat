import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public class Mario extends Sprite  {
	private int[] seq = {0,1,2,1};
	Mario(Image imatge) {
		//super(imatge, 18, 26);
		super(imatge, 24, 32);
		this.setFrameSequence(seq);
		}
	 
	
}
