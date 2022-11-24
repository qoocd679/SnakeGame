

import javax.swing.JFrame;
import javax.swing.JTextField;

public class gameFrame extends JFrame {
   gameFrame(){
//	   gamePanel panel = new gamePanel(); 把7的new panel
	   this.add(new gamePanelv2());
	   this.setTitle("snake");
	   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
   this.setResizable(false);
   this.pack();   //包裝後面的gamePanel
   this.setVisible(true);
   this.setLocationRelativeTo(null);
   
 
   }
  
   
}
