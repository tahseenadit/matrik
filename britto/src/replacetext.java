/* ------------------
This is the replaced text * Developed by:
 * Md. Tahseen Anam
--------------------- */

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.util.Scanner;

import javax.swing.Timer;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;



public class replacetext implements ClipboardOwner {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		replacetext foo=new replacetext();
		//Scanner sc=new Scanner(new BufferedInputStream(System.in));
		Timer timer = new Timer(0, null);
		try {
			 
			   /*timer.addActionListener(new ActionListener() {
				  @Override
				  public void actionPerformed(ActionEvent arg0) {
				    // Code to be executed
					  try {
						foo.getSelectedText(User32.INSTANCE, CustomUser32.INSTANCE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				  }
				});
			    timer.setDelay(500);
				timer.setRepeats(true); 
				timer.start();*/
			
				   foo.getSelectedText(User32.INSTANCE, CustomUser32.INSTANCE);
			   
			   
		   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void controlX(CustomUser32 customUser32) {
    	//System.out.println("capturing...!!");
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x58 /* 'C' */, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x58 /* 'C' */, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);// 'Left Control Up
    }
    
	void controlV(CustomUser32 customUser32) {
    	//System.out.println("capturing...!!");
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x56 /* 'C' */, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x56 /* 'C' */, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);// 'Left Control Up
    }
	
	  public interface CustomUser32 extends StdCallLibrary {
	        CustomUser32 INSTANCE = (CustomUser32) Native.loadLibrary("user32", CustomUser32.class);
	        HWND GetForegroundWindow();
	        void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
	    }
	
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
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
        controlX(customUser32); // emulate Ctrl C
        Thread.sleep(100); // give it some time
        String text = getClipboardText();	        
        System.out.println("Currently in clipboard: " + text);
        // restore what was previously in the clipboard
        setClipboardText("This is the replaced text");
        controlV(customUser32);
        return text;
    }

}
