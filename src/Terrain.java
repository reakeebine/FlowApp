import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

/***
 * A class containing a 2D array representing the terrain heights.
 * @author James Gain, Rea Keebine
 * @version 1.0.0 Sep 6, 2020
 */
public class Terrain {

	/***
	 * A regular grid of height values.
	 */
	private float [][] height;

	/***
	 * Dimensions for the size of the terrain.
	 */
	private int dimx, dimy;

	/***
	 * A greyscale image for displaying the terrain top-down.
	 */
	private BufferedImage img;

	/***
	 * Returns the total size of the terrain.
	 * @return total number of elements in the grid
	 */
	public int dim(){
		return dimx*dimy;
	}

	/***
	 * Gets the x-dimensions (i.e. the number of columns in the terrain grid / the width of the terrain).
	 * @return the x-dimensions of the terrain
	 */
	public int getDimX(){
		return dimx;
	}
	
	/***
	 * Gets the y-dimensions (i.e. the number of rows in the terrain grid / the height of the terrain).
	 * @return the y-dimensions of the terrain
	 */
	public int getDimY(){
		return dimy;
	}

	/***
	 * Returns the height at a particular point in the terrain.
	 * @param x x-coordinate of point
	 * @param y y-coordinate of point
	 * @return height at coordinate (x,y)
	 */
	public float getHeight(int x, int y) {
		return height[x][y];
	}

	/***
	 * Gets the greyscale image of the terrain.
	 * @return greyscale image of terrain
	 */
	public BufferedImage getImage() {
		  return img;
	}

	/***
	 * Creates the terrain object by reading in a given file
	 * @param fileName the name of the file which stores the heights in the terrain
	 */
	public Terrain(String fileName) {

		try {
			Scanner sc = new Scanner(new File("data/" + fileName.trim()));

			// read grid dimensions
			// x and y correpond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimx = sc.nextInt();
			dimy = sc.nextInt();

			// populate height grid
			height = new float[dimx][dimy];

			for(int x = 0; x < dimx; x++)
				for(int y = 0; y < dimy; y++)
					height[x][y] = sc.nextFloat();

			sc.close();

			// generate greyscale heightfield image
			deriveImage();
		} catch (IOException e) {
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		} catch (java.util.InputMismatchException e) {
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
	
	/***
	 * Converts height values to greyscale colour and populates an image.
	 */
	public void deriveImage() {
		img = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);
		float maxh = -10000.0f, minh = 10000.0f;
		
		// determine range of heights
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				float h = height[x][y];
				if(h > maxh)
					maxh = h;
				if(h < minh)
					minh = h;
			}
		
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 float val = (height[x][y] - minh) / (maxh - minh);
				 Color col = new Color(val, val, val, 1.0f);
				 img.setRGB(x, y, col.getRGB());
			}
	}
}