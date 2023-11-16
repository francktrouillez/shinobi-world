package View.Thread;


import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class SoundThread implements Runnable{
	private Clip c;
	private String path;
	
	private boolean loop = false;
	private boolean quit = false;
	
	private static boolean pause = false;
	private static boolean resume = false;
	
	private static int globalVolume = 100;
	private int volume;
	
	public SoundThread(String path) {
		try {
			c = AudioSystem.getClip();
			this.path = path;
			
		} catch(Exception e) {}
		
		
	}
	
	public SoundThread(String path, boolean loop) {
		try {
			
			this.path = path;
			this.loop = loop;
			this.volume = 100;
			
		} catch(Exception e) {}
		
		
	}

	
	public void run() {
		boolean done = false;
		while(!quit && (loop || !done )) {
			try {
				done = true;
				c = AudioSystem.getClip();
				
				c.open(AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(path)));
				c.start();
				int time = 0;
				long maxTime = c.getMicrosecondLength()/1000;
				while(time < maxTime && !quit) {
					if (pause) {
						c.stop();
						int frame = c.getFramePosition();
						
						if (resume) {
							pause = false;
							resume = false;
							c.setFramePosition(frame);
							c.start();
						}
						
					}
					else {
						
						time++;
						Thread.sleep(1);
					}
				}
				
			}
		
			catch(Exception e) {}
		}
		c.stop();
	}
	
	public static void pause() {
		pause = true;
		
	}
	
	public static void resume() {
		resume = true;
	}
	
	public void setVolume(int value) {
		if (value > 100) {
			value = 100;
		}
		else if (value < 0) {
			value = 0;
		}
		pause();
		this.volume = value;
		float v2 = (float)value;
		float v3 = v2*globalVolume/100;
		FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
		
		float min = gainControl.getMinimum();
		float max = gainControl.getMaximum();
		float res;
		if (v3 == 0) {
			res = min;
		}
		else {
			res = max - (max-min)*((100-v3)/100)/4;
		}
		
		gainControl.setValue(res); // Reduce volume by 10 decibels.
		resume();
	}
	
	
	public void refreshVolume() {
		setVolume(volume);
	}
	
	public static void setGlobalVolume(int value) {
		if (value > 100) {
			value = 100;
		}
		else if (value < 0) {
			value = 0;
		}
		globalVolume = value;
	}
	
	public void quit() {
		quit = true;
	}
	
	public static int getGlobalVolume() {
		return globalVolume;
	}
	
}
