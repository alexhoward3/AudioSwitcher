import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;

public class AudioDeviceSelect {
	public static void main(String[] args) {
		String device = "";
		if(args.length == 1) {
			device = args[0];
			AudioDeviceSelect obj = new AudioDeviceSelect();
			//Uses the nirsoft nircmdc.exe to change the audio settings
			String cmd = "nircmdc.exe setdefaultsounddevice \"" + device + "\"";
			
			obj.executeCommand(cmd);
			obj.triggerTrayIcon(device);
			
			try {
				Thread.sleep(3000L); //Timer to destroy app
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No argument specified");
		}
		
		System.exit(0);
	}
	
	private void triggerTrayIcon(String device) {
		ClassLoader cl = this.getClass().getClassLoader();
		ImageIcon image = new ImageIcon(cl.getResource("vol16.png"));
		if(SystemTray.isSupported()) {
			TrayIcon trayIcon = null;
			try {
				SystemTray tray = SystemTray.getSystemTray();
				trayIcon = new TrayIcon(image.getImage(), "Audio Switcher");
				trayIcon.setImageAutoSize(true);
				tray.add(trayIcon);
				trayIcon.displayMessage("Audio Device Changed", device, MessageType.INFO);
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Tray icons not supported.\n");
		}
	}

	private void executeCommand(String command) {

		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
