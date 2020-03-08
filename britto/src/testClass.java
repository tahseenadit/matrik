/* ------------------
 * Developed by:
 * Md. Tahseen Anam
--------------------- */

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
//import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

//import com.gtranslate.Language;
//import com.gtranslate.Translator;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import org.openqa.selenium.WebDriver;




	public class testClass extends Application implements ClipboardOwner{

		
		   TextField txtnum1, txtnum2;
		   Button btnselect, btnstart, btnstop, btnexit; 
		   Label lblanswer;
		    
		   Timer timer = new Timer(0, null);
		   databaseSqlite databs=new databaseSqlite();
		   
		   private double x;
		   private double y;

		   private void addDragListeners(final javafx.scene.Node n, Stage primaryStage){

			    n.setOnMousePressed((MouseEvent mouseEvent) -> {
			        this.x = n.getScene().getWindow().getX() - mouseEvent.getScreenX();
			        this.y = n.getScene().getWindow().getY() - mouseEvent.getScreenY();
			    });

			    n.setOnMouseDragged((MouseEvent mouseEvent) -> {
			        primaryStage.setX(mouseEvent.getScreenX() + this.x);
			        primaryStage.setY(mouseEvent.getScreenY() + this.y);
			    });
			    
			    n.setOnMouseEntered((MouseEvent mouseEvent) -> {
			        
			    	primaryStage.setOpacity(1.0);
			    	
			    }); 
			    n.setOnMouseExited((MouseEvent mouseEvent) -> {
			    	  if(primaryStage.isFocused())
			        	  primaryStage.setOpacity(1.0);
			          else
			              primaryStage.setOpacity(0.35);
			    });
		   }

		 
		    @Override
		    public void start(Stage primaryStage) {
		        //make the controls
		    	 File f = new File("src/dictionaryenbn.db");
			        if(f.exists() && !f.isDirectory()) { 
			        	databs.connection();
			        }else{
			        	
			        	databs.createDatabase();	        	
			        }
			        /*File icon=new File("Icon.png");
			        if(icon.exists()){
			        Image image = new ImageIcon("Icon.png").getImage();
			        }*/
			        
   
			        
			        
		        txtnum1=new TextField();
		        txtnum2=new TextField();
		        txtnum1.setEditable(false);
		        txtnum2.setEditable(false);
		        btnselect=new Button("T"); 
		        btnstart=new Button();
		        btnstop=new Button();
		        //btnexit=new Button("X");
		        btnselect.getStyleClass().add("btnselect");
		        btnstart.getStyleClass().add("btnstart");
		        btnstop.getStyleClass().add("btnexit");
		        //btnstop.getStyleClass().remove("btnstop");
				//btnstop.setText("X");			
		        //btnselect.setStyle("-fx-background-color: #36544b; -fx-color: green;");
                //btnstart.setStyle("-fx-background-color: #36944b; -fx-color: green;");
                //btnstop.setStyle("-fx-background-color: #cc4c4c; -fx-color: green;");
		        //lblanswer.setStyle("-fx-border-color: #000; -fx-padding: 5px;");
		       
		        //make container for app
		        GridPane root = new GridPane();
		        //root.setStyle("-fx-background-color: #242729;");
		        root.getStyleClass().add("root");
		        try {
		            addDragListeners(root, primaryStage);

		        } catch (Exception e) {
		            e.printStackTrace(System.out);
		        }
		        //put container in middle of scene
		        root.setAlignment(Pos.CENTER);
		        //setspacing between controls in grid
		        root.setHgap(2);
		        root.setVgap(4);
		        //add to grid, cell by cell
		        root.add(txtnum1, 3,0);
		        root.add(txtnum2,4,0);
		        root.add(btnselect,0,0);
		        root.add(btnstart,1,0);
		        root.add(btnstop,2,0);
		        //root.add(btnexit,5,0);
		        //last 2 rows span across 2 columns
		        /*col, rol, colspan, rowspan
		        root.add(lblanswer,0,3,2,1);
		        root.add(btnclear,0,4,2,1);*/
		        //set widths of all controls in separate method
		        setWidths();
		        //attach buttons to code in separate method
		        attachCode();
		        //usual stuff
		        
		        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
		        fadeIn.setFromValue(0.6);
		        fadeIn.setToValue(1.0);
		        
		        //PauseTransition stayOn = new PauseTransition(Duration.millis(1000));
		        FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), root);
		        fadeOut.setFromValue(1.0);
		        fadeOut.setToValue(0.6); 
		        
		        
		        Scene scene = new Scene(root, 305, 33);
		        primaryStage.setTitle("matrik");
		        primaryStage.setAlwaysOnTop(true);
		        primaryStage.initStyle(StageStyle.UNDECORATED);
		        primaryStage.setY(0);
		        primaryStage.setX(300);
		        primaryStage.setScene(scene);
		        //InputStream in =testClass.class.getResourceAsStream("resources/Style.css");
		        
		        try {
		        	//File f1=new File("resources/Style.css");
					URL url = testClass.class.getResource("resources/Style.css");//f1.toURI().toURL();
					scene.getStylesheets().clear();
			        scene.getStylesheets().add(url.toExternalForm());
			        
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        //String css = this.getClass().getResource("/resources/Style.css").toExternalForm();
		        //scene.getStylesheets().add("Style.css");
              
		        
		        try {
		            //create the font to use. Specify the size!
		            javafx.scene.text.Font customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/SolaimanLipi_22-02-2012.ttf"), 12);//new FileInputStream(new File("SolaimanLipi_22-02-2012.ttf")), 12);
		            //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		            //register the font
		            //ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("SolaimanLipi_22-02-2012.ttf")));
		            //use the font
		            txtnum2.setFont(customFont);
	            
		        } catch (Exception e) {
		            e.printStackTrace();
		        } /*catch(FontFormatException e) {
		            e.printStackTrace();
		        }*/
		        
		        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("icon.png")));
		        
		        //primaryStage.setOpacity(0.2);
		        
		        
		       /* primaryStage.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
		            @SuppressWarnings("deprecation")
					@Override
		            public void handle(MouseEvent mouseEvent) {
		                
		            	primaryStage.setFocused(true);
		            	System.out.println("mouse entered! " + mouseEvent.getSource());
		                
		            }
		        });*/
		        primaryStage.show();
		        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>()
		        {
		          @Override
		          public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1)
		          {
		        	  if(primaryStage.isFocused()){
		        	      
		        		  fadeIn.play();
		        		  primaryStage.setOpacity(1.0);
		        	  }else{
		        		 
		        		 fadeOut.play();
		        		 fadeOut.setOnFinished((ae)->primaryStage.setOpacity(0.35));
		        		 //primaryStage.setOpacity(0.35);
		        	  }
		          }
		        });
		    }
		    public void setWidths(){
		        //set widths of all controls
		        txtnum1.setPrefWidth(110);
		        txtnum2.setPrefWidth(110);
		        btnselect.setPrefWidth(15);
		        btnstart.setPrefWidth(24);
		        btnstop.setPrefWidth(24);
		    }
		   
		    public void attachCode()
		    {
		        //have each button run BTNCODE when clicked
		        btnselect.setOnAction(e -> btncode(e));
		        btnstart.setOnAction(e -> btncode1(e));
		        btnstop.setOnAction(e -> btncode2(e));
		    }
		   
		    private void btncode(javafx.event.ActionEvent e) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
		    testClass foo=new testClass(); 
		    if(capture){	
				try {
					WebDriver driver=null; 
			    	//driver.close();
					txtnum1.setText(foo.getSelectedText(User32.INSTANCE, CustomUser32.INSTANCE));
					txtnum2.setText(foo.translate(driver,txtnum1.getText(),databs));
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			    }else{
				  
				  JOptionPane.showMessageDialog(null, "Oops! You forgot to start capturing!!!");
		        }
			}
		    
		    private void btncode1(javafx.event.ActionEvent e) {
		    	   
		    	   btnstart.setStyle(" -fx-border-color:#50b3ea; -fx-color: #50b3ea;");
		 
		    	   testClass foo=new testClass();
		    	   
				   timer.addActionListener(new ActionListener() {
					  @Override
					  public void actionPerformed(ActionEvent arg0) {
					    // Code to be executed
						  foo.controlC(CustomUser32.INSTANCE);
					  }
					});
				    timer.setDelay(500);
					timer.setRepeats(true); 
					timer.start();
				    capture=true;
				    //top(frame,trnsarea,args0);
				    //btnstop.getStyleClass().clear();
				    //btnstop.setText(" ");
				    btnstop.getStyleClass().remove("btnexit");
				    btnstop.getStyleClass().add("btnstop");
		    }
            private void btncode2(javafx.event.ActionEvent e) {
            	
            	if(capture==false){
            	  System.exit(0);	
            	}else{
                btnstart.setStyle("-fx-background-color: #36944b; -fx-color: green;");
                btnstop.setStyle("-fx-background-color: #cc4c4c; -fx-color: green;");
            	
            	try {
    				timer.stop();
    				capture=false;
    				//btnstop.getStyleClass().clear();
    				btnstop.getStyleClass().remove("btnstop");
    				btnstop.getStyleClass().add("btnexit");
    				//btnstop.setText("X");
    				
    			} catch (Exception ex) {
    				// TODO Auto-generated catch block
    				ex.printStackTrace();
    			  }
            	}
		    }
            
            
		Boolean capture=false;
		
		
		  /*private int duration;

		  // A simple little method to show a title screen in the center
		  // of the screen for the amount of time given in the constructor
		  public void showSplash() {
			JWindow window = new JWindow();  
		    JPanel content = new JPanel();
		    content.setBackground(Color.white);

		    // Set the window's bounds, centering the window
		    int width = 450;
		    int height =115;
		    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		    int x = (screen.width-width)/2;
		    int y = (screen.height-height)/2;
		    content.setBounds(x,y,width,height);

		    // Build the splash screen
		    JLabel label = new JLabel(new ImageIcon("splash2.jpg"));
		    JLabel copyrt = new JLabel
		      ("Copyright 2002, O'Reilly & Associates", JLabel.CENTER);
		    copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
		    content.add(label, BorderLayout.CENTER);
		    content.add(copyrt, BorderLayout.SOUTH);
		    Color oraRed = new Color(156, 20, 20,  255);
		    content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

		    window.getContentPane().add(content); 
		    // Display it
		    window.setVisible(true);

		    // Wait a little while, maybe while loading resources
		    try { Thread.sleep(1000); } catch (Exception e) {}

		    window.setVisible(false);
		  }

		  public void showSplashAndExit() {
		    showSplash();
		    //System.exit(0);
		  }*/
		
		private Image zoomimage(ImageIcon splashimage){
			
			int w = (int) (splashimage.getIconWidth() * 0.5);
			int h = (int) (splashimage.getIconHeight() * 0.55);
		    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2 = resizedImg.createGraphics();

		    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2.drawImage(splashimage.getImage(), 0, 0, w, h, null);
		    g2.dispose();

		    return resizedImg;
		}
		
		
	    public static void main(String[] args){

	    	testClass testclass=new testClass();
	    	ImageIcon splashimage=new ImageIcon();
	        InputStream is = testclass.getClass().getResourceAsStream("splash2.jpg");
	        try {
				Image image = ImageIO.read(is);
				splashimage=new ImageIcon(image);
				splashimage=new ImageIcon(testclass.zoomimage(splashimage));
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	    	
	    	JWindow window = new JWindow();
	    	try {
				window.getContentPane().add(new JLabel("",splashimage,SwingConstants.CENTER));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	window.setBounds(500, 150, 450, 320);
	    	window.setVisible(true);
	    	try {
	    	    Thread.sleep(4000);
	    	} catch (InterruptedException e) {
	    	    e.printStackTrace();
	    	}
	    	window.setVisible(false);
	    	window.dispose();
	        /*File fontbn=new File("SolaimanLipi_22-02-2012.ttf");
	        if(!fontbn.exists()){
	        	JOptionPane.showMessageDialog(null, "It seems you do not have the Bangla font required.For this,the application will not function properly. Please make sure that 'SolaimanLipi_22-02-2012.ttf' is in the current directory.");
	        }*/

	        launch(args);
	        
        /*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	
            		//driver.close();
            
            }
        }));*/
	        
	    }
	    
        /*public void top(JFrame frame,JTextArea trnsarea, String[] args){
        	
        	Application.launch(args);
        	frame.setAlwaysOnTop(true);
        	//frame.setUndecorated(false);
        	frame.setSize(450,250);
        	trnsarea.setLineWrap(true);
        	//trnsarea.setPreferredSize(new Dimension(10, 10));
        	frame.pack();
        }*/
	    
	    /*public void go(testClass foo,databaseSqlite databs,String[] args0){
	    	
	    	    	
	    	Timer timer = new Timer(0, null);
	        try {
	            //create the font to use. Specify the size!
	            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("SolaimanLipi_22-02-2012.ttf")).deriveFont(12f);
	            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	            //register the font
	            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("SolaimanLipi_22-02-2012.ttf")));
	            //use the font
	            trnsarea.setFont(customFont);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch(FontFormatException e) {
	            e.printStackTrace();
	        }

	        File icon=new File("Icon.png");
	        if(icon.exists()){
	        Image image = new ImageIcon("Icon.png").getImage();
	        }
	        
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // take some time for you to select something anywhere
	      	       	    	
	    }*/

/*public static class FrameDragListener extends MouseAdapter {

    private final JFrame frame;
    private Point mouseDownCompCoords = null;

    public FrameDragListener(JFrame frame) {
        this.frame = frame;
    }

    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        Point currCoords = e.getLocationOnScreen();
        frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
}*/
	    
	    public String translate(WebDriver driver,String text,databaseSqlite databs){

	    	String bangla=databs.gettranslatedData(text);
	    	if(bangla.isEmpty() || bangla.matches(".*[a-zA-Z]+.*")){
	    	/*System.setProperty("webdriver.chrome.driver", "chromedriver.exe");	 
	        driver=new ChromeDriver();
	    	driver.get("http://translate.google.com/#auto/bn");
	    	WebElement query = driver.findElement(By.id("source"));
	    	query.sendKeys(text);
	        WebElement query1 = driver.findElement(By.id("gt-submit")); 
	        query1.click();
	        WebElement result;*/
	        //String bangla="";	 
	        
	        try {
				/*Thread.sleep(1000);
				//result=driver.findElement(By.id("result_box")).findElement(By.tagName("span")); 
				result = driver.findElement(By.xpath(".//span[@id='result_box']/span"));
				bangla=new String(result.getAttribute("innerHTML"));		
				
		        databs.insertData(text, bangla);*/		        	
		        bangla="অর্থ সংযুক্ত হয়নি!"; 
	        }catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        //driver.close();
	        
	    	}
	    	
	        return bangla;
	        
	    }
		
	    public interface CustomUser32 extends StdCallLibrary {
	        CustomUser32 INSTANCE = (CustomUser32) Native.loadLibrary("user32", CustomUser32.class);
	        HWND GetForegroundWindow();
	        void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
	    }	    	  

	    public void lostOwnership(Clipboard clipboard, Transferable contents) {
	        // dummy: needed for `ClipboardOwner`
	    }

	    void controlC(CustomUser32 customUser32) {
	    	//System.out.println("capturing...!!");
	        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 0, 0);
	        customUser32.keybd_event((byte) 0x43 /* 'C' */, (byte) 0, 0, 0);
	        customUser32.keybd_event((byte) 0x43 /* 'C' */, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);
	        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);// 'Left Control Up
	    }

	    String getClipboardText() throws Exception {
	        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
	        
	    }

	    void setClipboardText(String data) throws Exception {
	        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(data), this);
	    }

	    String getSelectedText(User32 user32, CustomUser32 customUser32) throws Exception {

	        HWND hwnd = customUser32.GetForegroundWindow();
	        char[] windowText = new char[512];
	        user32.GetWindowText(hwnd, windowText, 512);
	        String windowTitle = Native.toString(windowText);
	        System.out.println("Will take selected text from the following window: [" + windowTitle + "]");   
	        //String before = getClipboardText();
	        controlC(customUser32); // emulate Ctrl C
	        Thread.sleep(100); // give it some time
	        String text = getClipboardText();	        
	        System.out.println("Currently in clipboard: " + text);
	        // restore what was previously in the clipboard
	        //setClipboardText(before);
	        text=text.trim();
	        text=text.replaceAll("\\s+","");
	        return text.toLowerCase();
	    }

		

	}
	
	
	

