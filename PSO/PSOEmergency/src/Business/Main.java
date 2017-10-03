package Business;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame {
	private RenderPanel sea;
	private JPanel container;

	public Main() {
		
		// Title of JFrame
		super("Particle Swarm Optimization");
		
		// Set size of JFrame
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);			
		
		// Create a JPanel in JFrame and set layout of panel as GridBagLayout
		container = new JPanel(new GridBagLayout());		
		add(container, BorderLayout.CENTER);	

		// Add main render panel(used for displaying simulation) inside the container panel
		sea = new RenderPanel();
		container.add(sea);
		
		// Add lister for component resized event
		container.addComponentListener(new ComponentListenerOne());
	}

	class ComponentListenerOne implements ComponentListener {

		@Override
		public void componentResized(ComponentEvent e) {

			int w = container.getWidth();
			int h = container.getHeight();
			int size = Math.min(w, h);
			sea.setPreferredSize(new Dimension(size, size));
			container.revalidate();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub

		}
	}

}
