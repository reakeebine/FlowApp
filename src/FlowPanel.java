import java.awt.Graphics;
import javax.swing.JPanel;

/***
 * A panel that shows the water and terrain images overlaid on each other.
 * @author James Gain, Rea Keebine
 * @version 1.0.0 Sep 6, 2020
 */
public class FlowPanel extends JPanel {

	/***
	 * An object which contains heights across the terrain.
	 */
	private Terrain land;

	/***
	 * An object which contains depths of the water across the terrain.
	 */
	private Water rivers;

	/***
	 * Creates a FlowPanel object with given Terrain and Water objects.
	 * @param terrain an object which contains heights across the terrain
	 * @param water an object which contains depths of the water across the terrain
	 */
	public FlowPanel(Terrain terrain, Water water) {
		land = terrain;
		rivers = water;
	}

	/***
	 * Gets the Terrain object in the panel.
	 * @return an object which contains heights across the terrain
	 */
	public Terrain getTerrain() {
		return land;
	}

	/***
	 * Gets the Water object in the panel.
	 * @return an object which contains depths of the water across the terrain
	 */
	public Water getWater() {
		return rivers;
	}

	/***
	 * Responsible for painting the terrain and water as images.
	 * @param g the graphic object used to draw the images
	 */
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		land.deriveImage();
		rivers.deriveImage();
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
		}

		// draw the water as an image
		if (rivers.getImage() != null){
			g.drawImage(rivers.getImage(), 0, 0, null);
		}
	}

	/***
	 * Creates the water source on the Water object and repaints the panel.
	 * @param x x-coordinate of the new water point
	 * @param y y-coordinate of the new water point
	 */
	public void makeSource(int x, int y) {
		rivers.makeSource(x, y);
		repaint();
	}

	/***
	 * Resets the Water object and repaints the panel.
	 */
	public void resetRivers() {
		rivers.reset();
		repaint();
	}
}