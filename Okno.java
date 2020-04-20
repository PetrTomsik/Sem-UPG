
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;

/*
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
*/

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.org.apache.bcel.internal.classfile.PMGClass;

import waterflowsim.Cell;
import waterflowsim.Simulator;
import waterflowsim.Vector2D;
import waterflowsim.WaterSourceUpdater;


public class Okno extends JPanel implements Printable {
	
	
	double pox=0;
	double poy=0;
	/**
	 * Nastaveni pro citlivost hranic kliknuti mysi.
	 */
	public static final int HIT_BOX = 8;
	/**
	 * Pole s prvky x, kde zacina tect voda (zacina string)
	 */
	private ArrayList<Integer> X = new ArrayList<Integer>();
	/**
	 * Pole s prvky y, kde zacina tect voda (zacina string)
	 */
	private ArrayList<Integer> Y = new ArrayList<Integer>();
	/**
	 * Maximalni vyska.
	 */
	private static double maxY;
	/**
	 * Maximalni sirka.
	 */
	private static double maxX;

	private long startTime = System.currentTimeMillis();
	/**
	 * Celkova sirka rastru.
	 */
	//W =  Math.abs(Simulator.getDimension().x);		//sirka
	//H = Math.abs(Simulator.getDimension().y);		//vyska
	private static double meritko=1;
	
	private static double W;
	/**
	 * Celkova vyska rastru
	 */
	private static double H;
	/**
	 * Sirka jednoho pixelu()rastrového obrazku.
	 */
	private static double deltaX;
	/**
	 * Vzska jednoho pixelu.
	 */
	private static double deltaY;
	/**
	 * Pocateckní souradnice (x)
	 */
	private static double Sx;
	/**
	 * Pocatecni souradnice (y)
	 */
	private static double Sy;
	/**
	 * Minimalni sirka.
	 */
	private static double minX;
	/**
	 * Minimalni vyska.
	 */
	private static double minY;
	/**
	 * Celkovy pomer stran, kvůli změně okna
	 */
	double pomer=0;
	/**
	 * Pomer stran na ose x
	 */
	double pomerX;
	/**
	 * Pomer stran na ose y
	 */
	double pomerY;
	/**
	 * Sirka prevedena na rozmery okna.
	 */
	double sirka;
	/**
	 * Vyska prevedena na rozmery okna.
	 */
	double vyska;
	/**
	 * Stred okna na ose x.
	 */
	double centerX;
	/**
	 * Stred okna na ose y.
	 */
	double centerY;
	/**
	 * Stredove souradnice okna.
	 */
	double center;

	Cell cell;
	
	
	/**
	 * Promenna pro pan na ose X a na ose Y.
	 */
	public static double PomocnaPanX = 0, PomocnaPanY = 0, panX = 0, panY = 0;


	
	Graphics2D g2;
	double v=0;
	static double stredX;
	static double stredY;
	
	int okraje =100;
	@Override
	public void paint(Graphics g){
	//	System.out.println("ej");
		spocti();
		super.paint(g);
		g2 = (Graphics2D)g;
		//computeModelDimensions();
	//	g2.translate((this.getWidth()),0);
	//	computeModel2WindowTransformation((this.getWidth()-okraje), (this.getHeight()-okraje));
	//	g2.translate((this.getWidth()/2),this.getHeight()/2);
		g2.translate(-PomocnaPanX, -PomocnaPanY);
		g2.translate(stredX,stredY);
		
		computeModel2WindowTransformation((this.getWidth()), (this.getHeight()));
	//	computeModel2WindowTransformation((this.getWidth()), (this.getHeight()));
		
	//	System.out.println((this.getWidth()/2)/v+" , "+(this.getHeight()/2)/v);
		vytvorMatici(g2);
		
	}
	private void spocti() {
		
		
	}
	public void vytvorMatici(Graphics2D g2) {
		double XX = Sx;
		double YY = Sy;
	//	System.out.println("vyska"+Vyskapixelu.size());
	//	System.out.println("sirka"+Sirkapixelu.size());
		int pocitadlo=0;
		for(int v=0;v<H;v++) {
			for(int s=0;s<W;s++) {
				XX = XX+deltaX;
			//	System.out.println("bod X"+XX);
			//	System.out.println(YY);
				Point2D point2  = new Point2D.Double(XX,YY); 
				Point2D point= model2window(point2);
				
				
				 cell = Simulator.getData()[pocitadlo];
				//cell.isDry();
				//	System.out.println(cell);
				if(cell.isDry()==true) {
				//	System.out.println("Ano prosim"+startx);
				g2.setColor(Color.lightGray);
				g2.fill(new Rectangle2D.Double((point.getX()),(point.getY()),(deltaX*pomer),(deltaY*pomer)));
				}else {
					g2.setColor(Color.blue);
					g2.fill(new Rectangle2D.Double((point.getX()),(point.getY()),(deltaX*pomer),(deltaY*pomer)));
				}
				//	System.out.println(""+(kk*pomer)+","+(o*pomer)+","+(deltaX*pomer)+","+(deltaY*pomer));
				//	System.out.println("Dul"+startx);
				pocitadlo++;
				}
			YY = YY+deltaY;
			XX=Sx;
		}
		
		drawWaterSources(g2); 
	//	g2.draw(new Rectangle.Double(0,0,579,503));		//zobrazeni posledniho pixelu x obrazku
	//	System.out.println("pomocna"+ pocitadlo);
	//	System.out.println("vyska"+Vyskapixelu.size());
	//	System.out.println("sirka"+Sirkapixelu.size());
		}
		//drawWaterSources(g2);
	
	public void drawWaterFlowState(Graphics2D g) {
	//	 System.out.println("jsem zde");
		 drawWaterLayer(g);
	}
	
	
	void drawTerrain(Graphics2D g) {
		//prazdna metoda
	}
	
	public void drawWaterLayer(Graphics2D g) {
		
	}
	
	//sourdanice pro jmeno
	void drawWaterSources(Graphics2D g2) {
		double XX = Sx;
		double YY = Sy;
		
		for(int p=0;p<Hlavni.ZacVoda.size();p++) {
		//	System.out.println("vodicka"+ Hlavni.ZacVoda.get(p));
			int x= (int) ((int)Hlavni.ZacVoda.get(p)/W);
			int y= (int) ((int)Hlavni.ZacVoda.get(p)%W);
			X.add((x));
			Y.add((y));
		//	System.out.println("vypocet"+(x)+" , "+(y));
		}
		int upg1=0;
		int upg=0;
		int kk=0;
		int o=0;
		
	for(int u=0;u<Hlavni.ZacVoda.size();u++) {
		for( kk=0;kk<H;kk++ ) {
			for( o=0;o<W;o++) {
				Point2D point2  = new Point2D.Double(XX,YY); 
				Point2D point= model2window(point2);
				if(upg<Hlavni.ZacVoda.size()) {
					if((int)kk==X.get(upg) && o==Y.get(upg)) {
						g2.setColor(Color.red);
						g2.fill(new Rectangle2D.Double((point.getX()),(point.getY()),(deltaX*pomer),(deltaY*pomer)));
						if(!Simulator.getGradient(upg).x.equals(Double.NaN) )
						cell = Simulator.getData()[upg1];		//tady potřebuji ten presnou pozici gradientu
						Cell[] k =  new Cell[upg];
					    drawWaterFlowLabel(point,cell.getGradient(),Simulator.getWaterSources()[upg].getName(),g2);
						upg++;
					
					}
				}	XX = XX + deltaX;
				upg1++;
				}
			YY = YY + deltaY;
			XX = Sx;
		}
			XX=Sx;
			YY=Sy;
			kk=0;
			o=0;
			upg1=0;
		}
	g2.translate(W/2, H/2);
	g2.drawLine(0, 0, 500, -500);
	g2.drawLine(0, 0, 0, -500);
	g2.translate(-W/2, -H/2);
	
	
	}
	
	static double dirx;
	static double diry;
	void drawWaterFlowLabel(Point2D position, Vector2D dirFlow, String name,  Graphics2D g2) {
		//position.getX();
		double uhel2 = 0;
		double uhel1 = 0;
		//	System.out.println(position.getX());
		//	System.out.println(position.getY());
		//	System.out.println(name);
		//	System.out.println("xxx"+dirFlow.x);
		//	System.out.println("yyy"+dirFlow.y);
		dirx = (double)(dirFlow.x);
		diry = (double)(dirFlow.y);
		/*
		dirx = (double)(0);
		diry = (double)(3);
	*/
	//	g2.draw(new Line2D.Double((posx),(posy),(posx+dirx),(posy+diry)));
	//	g2.draw(new Line2D.Double(dxx,dyy,(posx+dirx),(posy+diry)));
	//	g2.draw(new Line2D.Double(dxxx,dyyy,(posx+dirx),(posy+diry)));
		//	System.out.println(dirx+" ,"+diry);
		//	System.out.println("dulezite" + posx + " ," + posy + " ," + (posx+dirx)+" ,"+(posy+diry));
		//	System.out.println("dulezite2" + dxx + " ," + dyy + " ," + (posx+dirx)+" ,"+(posy+diry));
		if(dirx==0) {
			uhel1=0;
		}else {
			double vypocet2= (dirx)/(diry);//tangenc
		//	double vypocet2= diry/len1;//tangenc
			uhel2 = (Math.atan(vypocet2));
			double len1 = Math.hypot(diry, dirx);
			double vypocet1= (diry)/(dirx);//tangenc
			 vypocet2= diry/len1;//tangenc
			uhel1 = (Math.atan(vypocet1));
			uhel2 =	 Math.asin(vypocet2) ;
			}
	//	}
	//		System.out.println("uhel"+uhel1);
		napis( position,dirFlow, name, uhel2 ,g2);
	
	}
	
	int ll=0;
	private void napis(Point2D position,Vector2D dirFlow, String name, double uhel, Graphics2D g2) {
		int posunOdPole = -10;
		int konstanta = 30;
		//g2.translate(position.getX(),position.getY());
		AffineTransform oldTR = g2.getTransform();	
		g2.translate(position.getX(),position.getY());
	//	g2.rotate((Math.toRadians(90)));
		g2.scale(1, -1);
	/*	g2.setColor(Color.black);
		g2.drawLine(0, 0, 0, 500);			//zobrazeni osy 
			g2.drawLine(0, 0, 500, 0);			//zobrazeni osy
	*/	//	System.out.println(name);			//jmeno vodniho toku
		g2.setColor(Color.red);
		
		g2.rotate((uhel));
		
		barvaVelikostFontu(g2);
		//zbavite se foru
		
		double ux = (pomer*konstanta) - 0;
		double uy = 0 - 0;
		
		//System.out.println("Ux" + ux);
		//System.out.println("Uy" + uy);
		// vypocita za me velikost vektoru 
		double len = Math.hypot(ux, uy);
		
		ux/=len;
		uy/=len;
		//System.out.println("len" + len);
		//System.out.println("Ux"+ux);
		//System.out.println("Uy"+uy);

		double k=((pomer)*(konstanta/6));
		double m=((pomer)*(konstanta/6));
		double nx = uy;
		double ny = -ux; 
		double cx = (pomer*konstanta)-k*ux;
		double cy = 0-k*uy;
		
	
		double dx = cx+ m* nx;
		double dy = cy+ m* ny;

		double dx1 = cx- m* nx;
		double dy1 = cy- m* ny;
		
		
		
		g2.setColor(Color.red);
		g2.draw(new Line2D.Double(0,0,pomer*konstanta,0));
		g2.draw(new Line2D.Double(dx,dy,(pomer*konstanta),0));
		g2.draw(new Line2D.Double(dx1,dy1,(pomer*konstanta),0));
		g2.scale(-1, 1);
		g2.rotate(Math.toRadians(180));
	
		g2.translate(0, posunOdPole);	
		g2.setColor(Color.black);
		g2.drawString(name, (0),(0));
		
		
		g2.rotate((-uhel));
		
		g2.translate(-position.getX(),-position.getY());
		g2.setTransform(oldTR);

		
	}
	
	
	
	
	void barvaVelikostFontu( Graphics2D g2) {
		int pomocneCislo=18;
		int promena =(int) (pomer*pomocneCislo);
		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, promena));

		/*
		if(pomer<=0.4) {
			g2.setColor(Color.YELLOW);
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		}else if(pomer>0.4&&pomer<=0.6){
			g2.setColor(Color.orange);
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
		}else if(pomer>0.6&&pomer<=0.8){
			g2.setColor(Color.PINK);
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		}else if(pomer>0.8&&pomer<=1){
			g2.setColor(Color.MAGENTA);
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		}else if(pomer>1&&pomer<=1.2){
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		}else if(pomer>1.2&&pomer<=1.4){
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		}else if(pomer>1.4&&pomer<=1.6){
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
		}else if(pomer>1.6&&pomer<=1.8){
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
		}else if(pomer>1.8&&pomer<=2.0){
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 26));
		}else if(pomer>2.0&&pomer<=2.2){
			g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
		}else {
		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		}*/
	}
	
	
	
	
	public static void computeModelDimensions() {

		W =  Math.abs(Simulator.getDimension().x);		//sirka
		H = Math.abs(Simulator.getDimension().y);		//vyska
		//System.out.println("kkkkp"+W);
		
		Sx = Simulator.getStart().x;		//zacinajici pozice
		Sy = Simulator.getStart().y;		//zacinajici pozice Y
	
		
		minX = Sx;						//minimální rozměr
		minY = Sy;		
		
		deltaX =  Math.abs(Simulator.getDelta().x);			//nevim jestli ma byt v absolutní hodnote, ale ziska kus po kterem budu delit sirku na jednotlivy pixely
		deltaY =  Math.abs(Simulator.getDelta().y);
		
		maxX = (W * deltaX) + Sx;					//maximalni rozmer
		maxY = (H * deltaY) + Sy;
		
		stredX= maxX/2;
		stredY= maxY/2;
		//System.out.println(W);
		//System.out.println("Zacina x"+Sx);
		//System.out.println("ZacinaY"+Sy);
		//System.out.println("vyska"+Vyskapixelu.size());
		//System.out.println("sirka"+Sirkapixelu.size());

	}
	
	
	
	void computeModel2WindowTransformation(int width, int height) {
		//System.out.println("W"+W);
		sirka = ((W * deltaX));			// vypocita celkouvou sirku
	
    
		vyska = ((H * deltaY) );
       // g2.translate(-sirka/4,-vyska/4);
		pomerX =( width / sirka)*meritko;			// pomer mezi 
        pomerY = (height/ vyska)*meritko;
        pomer = Math.min(pomerX, pomerY);	
        
        // pomer podle jaka cast okna se zvetsi
        centerX = Math.abs(((width-pomer)*sirka)/2.0);		// upresneni na center
        centerY = Math.abs(((height-pomer)*vyska)/2.0);		//pak vyzkoset na stred
        center = Math.min(centerX, centerY);		//pomer na stred
        //System.out.println("Center"+centerX);
        //System.out.println((width));
        //System.out.println((sirka));
        //System.out.println((vyska));
        //System.out.println("pomer je:"+pomer);
        //System.out.println("Centr"+centerX);
        //System.out.println("CentrY "+centerY);
        //System.out.println("konecka "+center);
	}
	
	
	/*
	 * prevede 
	 */
	public Point2D model2window(Point2D bod) {
		return new Point2D.Double(((bod.getX()) - (stredX )- minX) * pomer, (( bod.getY()) -( stredY )- minY ) * pomer);
		}
	
	
	
	public void zoomIn() {
		meritko = meritko*2;
		this.repaint();
	}
	
	public void zoomOut() {
		meritko=meritko/2;
		this.repaint();
		}
	public void vetsiNadpis() {
		deltaY=deltaY/2;//na zvetseni nadpisu
		deltaX=deltaX/2;//na zvetseni nadpisu
		this.repaint();
	}

public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
	if(pageIndex > 0) {
		return NO_SUCH_PAGE;
	}
	Graphics2D g2 = (Graphics2D)graphics;
	g2.translate(pageFormat.getWidth()/2, pageFormat.getHeight()/2);
	vytvorMatici(g2);//musim tam dodat pro pixeli (50*(72/25.4), g2);
	//ne matici ale ten obraz
	return 0;
}


/**
 * nastavi hodnoty x a y pro pan.
 * @param x - hodnotax
 * @param y - hodnota y
 */

public void setPan1(int x, int y){
	panY = y+poy;
	panX = x+pox;
	System.out.println(panX+"Panik");
	System.out.println(panY);

}

/**
 * nastavi hodnoty x a y pro pan ktery se bude pohybovat.
 * @param x - hodnota x
 * @param y - hodnota y
 */
public void setPan2(int x, int y){
	PomocnaPanX = panX-x;
	PomocnaPanY = panY-y;
	pox=PomocnaPanX;
	poy=PomocnaPanY;
	System.out.println(PomocnaPanX+ "pom");
	System.out.println(PomocnaPanY);
	System.out.println(x+"x");
	System.out.println(y);
	System.out.println(panX+"panX");
	System.out.println(panY);
	//Gui gui = new Gui();
	//gui.panel.repaint();
	}

/**
 * Medota s pomoci instance  tridy DataForGraphs vytvori okna s grafy zavislosti rychlosti/poctu aut na case.
 * Nasledne kontroluje na jakou silnici se kliklo.
 * @param x souradnice mysi na ose X.
 * @param y souradnice mysi na nase Y.
 */
/*
public void VytvorGraf(int x, int y){
	int boxX2 = x - (HIT_BOX);
	int boxY2 = y - (HIT_BOX);
	int HitBox = HIT_BOX;

	for(int i = 0; i<vsechnySilnice.length; i++){

		if(vsechnySilnice[i] != null && vsechnySilnice[i].intersects(boxX2, boxY2, HitBox, HitBox)){
			int WIDTH = 600;
			int HEIGHT = 400;
			JFrame frame;
			ChartPanel ChartPan;
	
			XYSeries dataCelk = new XYSeries("Auta celkem");
			for (int k = 0; k<SpousteciTrida.Listik.get(i).size();k++){
			dataCelk.add(SpousteciTrida.Listik.get(i).get(k).getCas(), SpousteciTrida.Listik.get(i).get(k).getCelkemPocetaAut());
			}
			
			XYSeries dataAkt = new XYSeries("Auta aktualne");
			for(int j = 0; j<SpousteciTrida.Listik.get(i).size();j++){
			dataAkt.add(SpousteciTrida.Listik.get(i).get(j).getCas(), SpousteciTrida.Listik.get(i).get(j).getAktualniPocetAut());
			}
			
			// data vlozime do stejneho kontejneru 
			XYSeriesCollection dataCollection = new XYSeriesCollection();
			dataCollection.addSeries(dataAkt);
			dataCollection.addSeries(dataCelk);

			JFreeChart chart1 = ChartFactory.createXYLineChart("Pocet aut: " + vsechnyPruhy[i].toString(),
					"Cas", "Pocet aut", dataCollection);

			frame = new JFrame();
			ChartPan = new ChartPanel(chart1);
			ChartPan.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			frame.add(ChartPan);
			frame.pack();
			frame.setTitle("Grafy pro silnici:");
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		}
	}
}*/
}
