
import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


import waterflowsim.Simulator;




public class Gui {
	/**
	 * Delka kroku casu.
	 */
	public static double timeStep = 0.1;
	int zastav=1;
	double rychlost =0;
	double poc=0;	
	double celk;
	Okno panel;
	JButton pauza = new JButton("Pauza / Pokraèovat");
	
	
 public void vytvorGui(JFrame win) {
		System.out.println("LLL");
		panel = new Okno();
		System.out.println("KKKk");
		//panel1.computeModelDimensions();
		panel.setPreferredSize(new Dimension(579, 503));
	
		win.setLayout(new BorderLayout());
		win.add(panel, BorderLayout.NORTH );
		
		
		JButton bttExit = new JButton("Konec");
		JButton vetsirych = new JButton("UpRych");
		JButton mensirych = new JButton("DownRych");
		JButton vetsi = new JButton("Približ");
		JButton mensi = new JButton("Oddal");
		JButton bttTisk = new JButton("Tisk");
		JPanel tlacitka = new JPanel();
		JPanel tlacitka2 = new JPanel();
		//System.out.println(tlacitka.getColorModel());
		//tlacitka
		tlacitka.add(vetsi);
		tlacitka.add(mensi);
		tlacitka.add(bttExit);
		tlacitka.add(bttTisk);
		tlacitka.add(pauza);
		tlacitka2.add(vetsirych);
		tlacitka2.add(mensirych);
		//tlacitka.add(znovu);
		
		win.add(tlacitka,BorderLayout.CENTER);
		win.add(tlacitka2,BorderLayout.SOUTH);
		
		
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				//sim.VytvorGraf(x,y);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				panel.setPan1(e.getX(), e.getY());
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
			});

			panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				panel.setPan2(e.getX(), e.getY());
		//		System.out.println();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});
	
	
		
		
		pauza.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(zastav==0) { 
				zastav=1;
				 pauza.setName("Znovu");
				}
				else {
					zastav=0;
					 pauza.setName("Znovu");
					// vytvorGui(win);
				}
			}
		});
		
		
			
		vetsirych.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					poc = timeStep;
					poc=poc/5;
					System.out.println(timeStep);
					System.out.println(poc);
					timeStep=timeStep+poc;
					System.out.println(timeStep);
					if(timeStep>0.999) {
						timeStep=0.98;
					}
				}
			});
			
		mensirych.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			poc = timeStep;
			poc=poc/5;
			System.out.println(timeStep);
			System.out.println(poc);
			timeStep=timeStep-poc;

			System.out.println(timeStep);
			if(timeStep>0.001) {
				
				timeStep=0.001;
			}
		}
	});

	/*	znovu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				zastav=1;
			}
		});
		*/
		
		bttTisk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PrinterJob job = PrinterJob.getPrinterJob();// vrátí tiskarnu defoltnì nastavená
				if(job.printDialog()) {
					job.setPrintable(panel);
				}try{
					job.print();
					
				}catch(PrinterException e1){
					e1.printStackTrace();
				}
			}
		});
		
		
		mensi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.zoomOut();
			}
		});
		
		vetsi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
			panel.zoomIn();
				
			}
			});
		
		//win.add(tlacitka,BorderLayout.SOUTH);
		bttExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				win.dispose();
				//kdyby neslo
				//win.dispatchEvent(e);
			}
		});
		
		casovac(panel);
		
 }


private void casovac(Okno panel) {
	Timer myTimer = new Timer();
	myTimer.scheduleAtFixedRate(new TimerTask() {
		
		@Override
		public void run() {
			if(zastav==1) {
		
			Simulator.nextStep(timeStep);		//+0.89,- 0.089
			panel.repaint();
			}
		}
	}, 0, 100);
	
}
 
}
