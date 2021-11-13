import java.awt.image.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/***
 * A class containing a 2D array representing the water depths.
 * @author James Gain, Rea Keebine
 * @version 1.0.0 Sep 15, 2020
 */
public class Water {

    /***
     * A regular grid of depth values.
     */
    private int [][] depth;

    /***
     * Dimensions for the size of the area.
     */
    private int dimx, dimy;

    /***
     * A transparent image for displaying the area top-down with shades of blue representing the water.
     */
    private BufferedImage img;

    /***
     * A permuted list of integers in range [0, dimx*dimy).
     */
    private ArrayList<Integer> permute;

    /***
     * Returns the total size of the area.
     * @return total number of elements in the grid
     */
    public int dim(){
        return dimx*dimy;
    }

    /***
     * Gets the x-dimension (i.e. the number of columns in the water grid / the width of the area).
     * @return the x-dimension of the water grid
     */
    public int getDimX(){
        return dimx;
    }

    /***
     * Gets the y-dimension (i.e. the number of rows in the water grid / the height of the area).
     * @return the y-dimension of the water grid
     */
    public int getDimY(){
        return dimy;
    }

    /***
     * Returns the depth at a particular point in the area.
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @return depth at coordinate (x,y)
     */
    public int getDepth(int x, int y) {
        return depth[x][y];
    }

    /***
     * Sets the depth at a particular point in the area
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @param d new depth at coordinate (x,y) to set
     */
    public void setDepth(int x, int y, int d) {
        depth[x][y] = d;
    }

    /***
     * Gets the image of the area with the water points as shades of blue.
     * @return image of area with water points
     */
    public BufferedImage getImage() {
        return img;
    }

    /***
     * Creates the Water object using given dimensions. Runs the reset method.
     * @param dX x-dimension for new Water object
     * @param dY y-dimension for new Water object
     */
    public Water(int dX, int dY) {
        dimx = dX;
        dimy = dY;
        depth = new int[dimx][dimy];
        reset();
    }

    /***
     * Resets the Water object by setting all the depths in the grid to zero.
     */
    public void reset(){
        for (int x = 0; x < dimx; x++)
            for (int y = 0; y < dimy; y++)
                depth[x][y] = 0;
        deriveImage();
        genPermute();
    }

    /***
     * Generates a permuted list of linear index positions to allow a random traversal over the area.
     */
    private void genPermute() {
        permute = new ArrayList<Integer>();
        for(int idx = 0; idx < dim(); idx++)
            permute.add(idx);
        java.util.Collections.shuffle(permute);
    }

    /***
     * Converts a given linear position into a 2D location
     * @param pos linear position
     * @return 2D location
     */
    private int[] locate(int pos) {
        int [] ind = new int[2];
        ind[0] = (int) pos / dimy; // x
        ind[1] = pos % dimy; // y
        return ind;
    }

    /***
     * Finds a permuted 2D location from a linear index in the range [0, dimx*dimy)
     * @param i linear index
     * @return 2D location in the area
     */
    public int[] getPermute(int i) {
        return locate(permute.get(i));
    }

    /***
     * Converts depth values in the water grid to shades of blue [extension to assignment], or transparent if the depth is zero, and populates an image.
     */
    public void deriveImage() {
        img = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
        float aveD = 0.0f;
        int maxD = -1000, minD = 1000, count = 0, totD = 0;

        // determine range of depths (where the min>0 - so that the initial dot is a normal blue)
        for(int x=0; x < dimx; x++)
            for(int y=0; y < dimy; y++) {
                int d = depth[x][y];
                if(d > maxD)
                    maxD = d;
                if(d > 0 && d < minD) // min>0
                    minD = d;
                if(d>0) {
                    count++;
                    totD = totD + d;
                }
            }

        if (count>0)
            aveD = totD/count; // find the average depth, depths > aveD will be made darker, depths < aveD will be made lighter

        for(int x=0; x < dimx; x++)
            for(int y=0; y < dimy; y++)
                if (depth[x][y] > 0) {
                    int d = depth[x][y];
                    float colSat, colBri;
                    if ((maxD - minD) == 0) { // if the range is 0 make the dot a regular blue
                        colSat = 1.0f;
                        colBri = 1.0f;
                    } else if (aveD > 0 && d < aveD) {
                        colSat = 1.0f - ((aveD-d) / (aveD-0) * 0.75f); // make lighter, a value of 0 is white so min is 0.25
                        colBri = 1.0f;
                    } else if (aveD > 0 && d > aveD) {
                        colSat = 1.0f;
                        colBri = 1.0f - ((d-aveD) / (maxD-aveD) * 0.75f); // make darker, a value of 0 is black so min is 0.25
                    } else { // if d == aveD make the dot a regular blue
                        colSat = 1.0f;
                        colBri = 1.0f;
                    }
                    Color col = new Color(Color.HSBtoRGB(2/3f,colSat,colBri)); // convert the HSB (easier to manipulate shades) to RGB
                    img.setRGB(x, y, col.getRGB());
                } else {
                    Color col = new Color(0.0f, 0.0f, 0.0f, 0.0f); // make the dot transparent
                    img.setRGB(x, y, col.getRGB());
                }
    }

    /***
     * Creates a cube of water at a given position of size 7×7×3 (x×y×depth).
     * @param x x-coordinate of centre of cube face for new water
     * @param y y-coordinate of centre of cube face for new water
     */
    public void makeSource(int x, int y) {
        for (int i = x-3; i < x+4; i++)
            for (int j = y-3; j < y+4; j++)
                if (i>=0 && j>=0 && i<=dimx-1 && j<=dimy-1)
                    depth[i][j] = depth[i][j] + 3;
        deriveImage();
    }
}