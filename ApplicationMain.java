package lifesim;

import asciiPanel.AsciiPanel;
import lifesim.screens.Screen;
import lifesim.screens.StartScreen;

import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ApplicationMain extends JFrame implements KeyListener, ActionListener {
	//------------------
	//Instance Variables
	//------------------
	private static final long serialVersionUID = 1060623638149583738L;
	
	private AsciiPanel terminal;
	private Screen screen;
	
	private Timer timer;
	private int delay;
	private int timePassed;
	
	//------------------
	//Constructor
	//------------------
	public ApplicationMain() {
		super();
		
		terminal = new AsciiPanel(80, 27);
		this.add(terminal);
		pack();
		
		screen = new StartScreen(20, 20, 0.5f, this);
		
		delay = 1000;
		timePassed = 0;
		
		timer = new Timer(delay, this);
		addKeyListener(this);
		repaint();
	}
	
	//------------------
	//Class Methods
	//------------------
	public void repaint() {
		terminal.clear();
		screen.displayOutput(terminal);
		super.repaint();
	}
	
	public void startTimer() {
		timer.start();
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	public boolean isTimerRunning() {
		return timer.isRunning();
	}
	
	public void speedUpTimer() {
		delay -= 10;
		timer.setDelay(delay);
	}
	
	public void slowDownTimer() {
		delay += 10;
		timer.setDelay(delay);
	}
	
	public void resetDelay() {
		delay = 1000;
		timer.setDelay(delay);
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getTimePassed() {
		return timePassed;
	}
	
	public void resetTimePassed() {
		timePassed = 0;
	}
	
	//Inherited KeyListener methods
	@Override
	public void keyTyped(KeyEvent e) {
		//Nothing
	}

	@Override
	public void keyPressed(KeyEvent e) {
		screen = screen.respondToUserInput(e);
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Nothing
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (screen.getWorld().entitiesDead()) {
			//Stop timer if all entities have died
			stopTimer();
		}
		else {
			screen.getWorld().update();
			timePassed += 1;
			repaint();
		}
	}
	
	//------------------
	//Main
	//------------------
	public static void main(String[] args) {
		ApplicationMain app = new ApplicationMain();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}
}
