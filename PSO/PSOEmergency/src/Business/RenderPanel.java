package Business;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;


public class RenderPanel extends JPanel
{
	private PSOLogic logic;
	private double minX, maxX, minY, maxY;
	private Font font;
	private Timer IterateTimer;
	int iterator = 0;

	public RenderPanel()
	{
		super();
		setBackground(Color.white);
		
		logic = new PSOLogic();
		executeProcess(false);
		
		
		IterateTimer = new Timer(100, new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						iterator++;						
						if(iterator <= 100) {

							executeProcess(true);
						}
					}
				}
		);
		IterateTimer.start();
		
		minX = PSOLogic.X_LOC_LOW;
		maxX = PSOLogic.X_LOC_HIGH;
		minY = PSOLogic.Y_LOC_LOW;
		maxY = PSOLogic.Y_LOC_HIGH;
		
		font = new Font("Serif", Font.PLAIN, 10);
	}

	public void setIterateTimerDelay(int delay)
	{
		IterateTimer.setDelay(delay);
	}
	
	private Point formattedPoint(double x, double y)
	{
		
		double normX = (x - (minX + maxX)/2)/((maxX - minX)/2);
		double normY = (y - (minY + maxY)/2)/((maxY - minY)/2);
		
		return new Point(getWidth()/2 + (int)(getWidth()/2*normX), getHeight()/2 - ((int)(getHeight()/2*normY)));
	}
	
	public void executeProcess(boolean value)
	{
		logic.execute(value);
		repaint();
	}

	
	private void drawGrid(Graphics g)
	{
		Point p1, p2;
		
		for(int i = (int) minX; i <= maxX; i++)
		{
			p1 = formattedPoint(i, minY);
			p2 = formattedPoint(i, maxY);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
			g.drawString(String.valueOf(i), p1.x, (p1.y + p2.y)/2);
		}
		
		for(int i = (int) minY; i <= maxY; i++)
		{
			p1 = formattedPoint(minX, i);
			p2 = formattedPoint(maxX, i);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
			g.drawString(String.valueOf(i), (p1.x + p2.x)/2, p1.y);
		}
	}
	
	public void drawCircle(Graphics g, int x, int y, int size)
	{
		g.drawOval(x - size/2, y - size/2, size, size);
	}
	
	public void fillCircle(Graphics g, int x, int y, int size)
	{
		g.fillOval(x - size/2, y - size/2, size, size);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		 
		Point point;
		
		g.setColor(Color.GRAY);  
		g.setFont(font);
		
		drawGrid(g);
		
		g.setColor(Color.GRAY);
		
		for(Boat boat: logic.getSwarm())
		{
			point = formattedPoint(boat.getLocation().getLoc()[0], boat.getLocation().getLoc()[1]);
			fillCircle(g, point.x, point.y, 15);
		}
		
		g.setColor(Color.GREEN);
		
		point = formattedPoint(logic.getgBLoc().getLoc()[0], logic.getgBLoc().getLoc()[1]);
		drawCircle(g, point.x, point.y, 50);
		

	}
}
