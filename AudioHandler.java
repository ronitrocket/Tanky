package Tanky;

import java.util.ArrayList;

//AudioHandler
//Final Programming Assignment - Tanky
/**
 * Class that is responsible for handling the playing of audio within the game.
 * It uses a separate thread so as to not cause noticable lag during gameplay
 * 
 * @author ronittaleti
 */
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public class AudioHandler implements Runnable {

	// Instance Variables
	private Thread thread;
	
	// Queues for handling playback
	private ArrayList<AudioClip> clipsToPlay; 
	private ArrayList<AudioClip> clipsToLoop;
	private ArrayList<AudioClip> clipsLooping;
	private ArrayList<AudioClip> clipsToStop;

	/**
	 * Default constructor.
	 * 
	 * Initializes audio handler and the queues for each action for audios.
	 */
	public AudioHandler() {
		clipsToPlay = new ArrayList<AudioClip>();
		clipsToLoop = new ArrayList<AudioClip>();
		clipsLooping = new ArrayList<AudioClip>();
		clipsToStop = new ArrayList<AudioClip>();
		
		// Start the thread when the AudioHandler is initialized
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Runnable's built in run() method that is called when the thread with this
	 * object as a parameter is called.
	 */
	@Override
	public void run() {
		// Variables for the game loop
		double drawInterval = 1000 / 200; // Draw interval is 1 frame every 5 ms (1000/200)
		double deltaTime = 0;
		long lastTime = System.currentTimeMillis();
		long currentTime;

		while (true) {

			currentTime = System.currentTimeMillis();

			deltaTime += (currentTime - lastTime) / drawInterval; // Change in time

			lastTime = currentTime;

			// One frame has passed when deltaTime = 1 assuming there is absolutely no lag
			// If deltaTime is >= 1, update the model and view
			if (deltaTime >= 1) {
				// Play all queued clips and remove them from the play queue
				for (int i = 0; i < clipsToPlay.size(); i++) {
					AudioClip clip = clipsToPlay.get(i);
					if (clip != null) {
						clip.play(false);
					}
				}
				for (int i = 0; i < clipsToPlay.size(); i++) {
					clipsToPlay.remove(i);
				}

				// Loop all queued clips and remove them from the loop queue
				for (int i = 0; i < clipsToLoop.size(); i++) {
					AudioClip clip = clipsToLoop.get(i);
					if (clip != null) {
						clip.play(true);
						clipsLooping.add(clip);
					}
				}
				for (int i = 0; i < clipsToLoop.size(); i++) {
					clipsToLoop.remove(i);
				}

				// Stop playback of all queued clips and remove them from the stop queue
				for (int i = 0; i < clipsToStop.size(); i++) {
					AudioClip clip = clipsToStop.get(i);
					if (clip != null) {
						if (clipsLooping.contains(clip)) {
							clipsLooping.remove(clipsLooping.indexOf(clip)).stop();
						}
					}
				}
				for (int i = 0; i < clipsToStop.size(); i++) {
					clipsToStop.remove(i);
				}
				deltaTime--;
			}
		}
	}

	/**
	 * Queues the specified clip to be played.
	 * 
	 * @param clip		The AudioClip to be played.
	 */
	public void playAudio(AudioClip clip) {
		clipsToPlay.add(clip);
	}

	/**
	 * Queues the specified clip to be looped.
	 * 
	 * @param clip		The AudioClip to be looped.
	 */
	public void loopAudio(AudioClip clip) {
		clipsToLoop.add(clip);
	}

	/**
	 * Queues the specified clip to be stopped.
	 * 
	 * @param clip		The AudioClip to be stopped.
	 */
	public void stopAudio(AudioClip clip) {
		clipsToStop.add(clip);
	}
}
