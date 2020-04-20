
import waterflowsim.Simulator;

import waterflowsim.Scenarios;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Hlavni {
	/**
	 * Pomocna promenna pro zadani parametru prikazove radkz
	 */
	public static int scenar;
	
	/**
	 * Pole pro pocinani toku vody.
	 */
	public static ArrayList ZacVoda= new ArrayList<Integer>();

	public static void main(String[] args){
		scenar = 0;
		if (args.length != 0) {
			scenar = Integer.parseInt(args[0]);
		}
		System.out.println("Halo");
		
			Scenarios[] scenarios = Simulator.getScenarios();
			
			// Nahrani a spusteni prvniho scenare
			Simulator.runScenario(scenar);
			spocti();
			System.out.println("nene");
			Okno.computeModelDimensions();
			JFrame frame = new JFrame();
			Gui gui = new Gui();
			gui.vytvorGui(frame);
		//	JFrame frame = new JFrame();
			
			//frame.setSize(579, 503);		// proprvni
			frame.setTitle("WaterFlowSim Petr Tomsik");
			
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
	}
		
	
	
public static void spocti() {
	
		waterflowsim.WaterSourceUpdater[] zdroje = Simulator.getWaterSources();
		for(waterflowsim.WaterSourceUpdater up: zdroje){
			//System.out.println(up.getIndex());
			ZacVoda.add(up.getIndex());
			//System.out.println("Prosim yyy" +up.getIndex());
		}
		ZacVoda.toArray();
	//	Collections.sort(ZacVoda);
	}
}


