import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

/**
 * Main application to display the water flow over a terrain.
 * @author James Gain, Rea Keebine
 * @version 1.0.0 Sep 6, 2020
 */
public class FlowApp {
	/**
	 * An object that activates and controls the flow of water.
	 */
	private static FlowCtrl flowC;

	/***
	 * Creates the terrain using a file and invokes the GUI setup.
	 * @param args command-line argument of the file name
	 */
	public static void main(String[] args) {
		Terrain landdata;
		int frameX, frameY;
		
		// check that number of command line arguments is correct
		if(args.length != 1) {
			System.out.println("Incorrect number of command line arguments. Should have form: java Flow intputfilename");
			System.exit(0);
		}
				
		// landscape information from file supplied as argument
		landdata = new Terrain(args[0]);
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));
	}

	/***
	 * Sets up the GUI frame using the given size and a Terrain object, and prepares the actions.
	 * @param frameX the width of the frame
	 * @param frameY the height of the frame
	 * @param landdata an object which contains heights across the terrain
	 */
	public static void setupGUI(int frameX, int frameY, Terrain landdata) {
		JFrame frame = new JFrame("Waterflow");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel g = new JPanel();
		g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		b.setMinimumSize(new Dimension(frameX, 50));
		b.setPreferredSize(new Dimension(frameX, 50));

		TimeText timeT = new TimeText();
		FlowPanel flowP = new FlowPanel(landdata, new Water(frameX,frameY));
		flowC = new FlowCtrl(flowP, timeT);

		flowP.setPreferredSize(new Dimension(frameX,frameY));
		timeT.setBorder(BorderFactory.createLineBorder(Color.black));
		timeT.setText("Time: 0");
		timeT.setMinimumSize(new Dimension(50, 25));
		timeT.setPreferredSize(new Dimension(90, 25));
		timeT.setMaximumSize(new Dimension(150, 25));
		timeT.setLineWrap(true);
		timeT.setFont(timeT.getFont().deriveFont(Font.BOLD));

		// to do: add a MouseListener, buttons and ActionListeners on those buttons
		flowP.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				flowC.makeSource(e.getX(), e.getY());
			}
		});

		// extension: allows the user to click and drag to create multiple water sources
		flowP.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				flowC.makeSource(e.getX(), e.getY());
			}
		});

		JButton resetB = new JButton("Reset");
		resetB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flowC.pause();
				flowC.reset();
			}
		});

		JButton pauseB = new JButton("Pause");
		pauseB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flowC.pause();
			}
		});

		JButton playB = new JButton("Play");
		playB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread flowCt = new Thread(flowC);
				flowCt.start();
			}
		});

		JButton endB = new JButton("End");
		endB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flowC.pause();
				frame.dispose();
			}
		});

		b.add(Box.createRigidArea(new Dimension(15,0)));
		b.add(resetB);
		b.add(Box.createHorizontalGlue());
		b.add(pauseB);
		b.add(Box.createHorizontalGlue());
		b.add(playB);
		b.add(Box.createHorizontalGlue());
		b.add(endB);
		b.add(Box.createHorizontalGlue());
		b.add(timeT);
		b.add(Box.createRigidArea(new Dimension(15,0)));
		g.add(flowP);
		g.add(b);

		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
		frame.setLocationRelativeTo(null);  // center window on screen
		frame.add(g); // add contents to window
		frame.setContentPane(g);
		frame.setVisible(true);
	}
}