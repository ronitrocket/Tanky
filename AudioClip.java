package Tanky;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

//AudioClip
//Final Programming Assignment - Tanky
/**
 * Class that provides useful abstractions of Java's built in Clip class used to
 * play audio.
 * 
 * @author ronittaleti
 */
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public class AudioClip {

	// Instance Variables
	private Clip clip;
	
	public boolean isPlaying;

	/**
	 * Default constructor.
	 * 
	 * Attempts to get a clip based on a file path.
	 * 
	 * @param filepath		The path to the audio file to be loaded.
	 */
	public AudioClip(String filepath) {
		isPlaying = false;
		try {
			File soundFile = new File(filepath);
			AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

			// load the sound from the filepath
			clip = AudioSystem.getClip();
			clip.open(sound);
		} catch (Exception e) {
			clip = null;
			System.out.println("failed to load");
		}
	}

	/**
	 * Sets the volume of the AudioClip
	 * 
	 * @param volume		The volume of the clip (0.0-1.0)
	 */
	public void setVolume(float volume) {
		if (clip != null) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			if (gainControl != null) {
				gainControl.setValue(20f * (float) Math.log10(volume));
			}
		}
	}

	/**
	 * Plays the AudioClip, with an option to loop the clip.
	 * 
	 * @param shouldLoop		Whether the clip should loop
	 */
	public void play(boolean shouldLoop) {
		if (clip != null) {
			isPlaying = true;
			if (shouldLoop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.setFramePosition(0);
				clip.start();
			}
		}
	}

	/**
	 * Stops playback of the AudioClip
	 */
	public void stop() {
		if (clip != null && isPlaying) {
			isPlaying = false;
			clip.stop();
		}
	}
}// end of class
