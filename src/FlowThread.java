/***
 * A single thread which performs the simulation work on one quarter of the area.
 * @author Rea Keebine
 * @version 1.0.0 Sep 16, 2020
 */
public class FlowThread extends Thread {

    /***
     * The number of which quarter of the area the thread should run on.
     */
    private int quarter;

    /***
     * An object which contains heights across the terrain.
     */
    private Terrain land;

    /***
     * An object which contains depths of the water across the terrain.
     */
    private Water rivers;

    /***
     * Creates the thread using the quarter number, and the Terrain and Water objects.
     * @param qrt the quarter the thread will run on
     * @param terrain an object which contains heights across the terrain
     * @param water an object which contains depths of the water across the terrain
     */
    public FlowThread (int qrt, Terrain terrain, Water water) {
        quarter = qrt;
        land = terrain;
        rivers = water;
    }

    /***
     * Gets one quarter of the permuted area and runs the water flow simulation over that area.
     */
    public void run() {
        int limit = rivers.dim()/4;
        int sectionStart = limit*quarter; //[..
        int sectionEnd = limit*(quarter+1); //..)
        waterFlow(sectionStart, sectionEnd);
    }

    /***
     * The water flow simulation. Compares the water surface at each point in the area to the surrounding points. If the surface is lower in the surrounds, transfers one block of water to that point. Also empties the water in the edge points.
     */
    public synchronized void waterFlow (int start, int end) {
        for (int idx = start; idx < end; idx++) {
            int[] loc = rivers.getPermute(idx);
            int x = loc[0];
            int y = loc[1];
            if (rivers.getDepth(x,y) >0) { // if there is water here
                if ((x > 0) && (y > 0) && (x < rivers.getDimX() - 1) && (y < rivers.getDimY() - 1)) { // and if we are not at the edge
                    float surfCurr = rivers.getDepth(x,y)*0.01f + land.getHeight(x,y); // calculate the current water surface
                    int[] lowest = new int[2]; // the co-ordinates of the lowest surface
                    int idxLowArnd = 0; // the index of the lowest surface in the surfAround array
                    float minValue = 1000000.0f;

                    // create an array with the water surfaces around the current position
                    float[] surfAround = {rivers.getDepth(x-1,y-1)*0.01f + land.getHeight(x-1,y-1),
                                          rivers.getDepth(x,y-1)*0.01f + land.getHeight(x,y-1),
                                          rivers.getDepth(x+1,y-1)*0.01f + land.getHeight(x+1,y-1),
                                          rivers.getDepth(x-1,y)*0.01f + land.getHeight(x-1,y),
                                          rivers.getDepth(x+1,y)*0.01f + land.getHeight(x+1,y),
                                          rivers.getDepth(x-1,y+1)*0.01f + land.getHeight(x-1,y+1),
                                          rivers.getDepth(x,y+1)*0.01f + land.getHeight(x,y+1),
                                          rivers.getDepth(x+1,y+1)*0.01f + land.getHeight(x+1,y+1) };

                    for (int i = 1; i < 8; i++) // loop through the surfAround array and find the lowest surface
                        if (surfAround[i] < minValue) {
                            minValue = surfAround[i];
                            idxLowArnd = i;
                        }

                    if (surfAround[idxLowArnd] < surfCurr) { // must be strictly lower
                        switch (idxLowArnd) { // get the actual co-ordinates of the lowest surface based on the surfAround array
                            case 0:
                                lowest = new int[]{x - 1, y - 1};
                                break;
                            case 1:
                                lowest = new int[]{x, y - 1};
                                break;
                            case 2:
                                lowest = new int[]{x + 1, y - 1};
                                break;
                            case 3:
                                lowest = new int[]{x - 1, y};
                                break;
                            case 4:
                                lowest = new int[]{x + 1, y};
                                break;
                            case 5:
                                lowest = new int[]{x - 1, y + 1};
                                break;
                            case 6:
                                lowest = new int[]{x, y + 1};
                                break;
                            case 7:
                                lowest = new int[]{x + 1, y + 1};
                                break;
                        }

                        // transfer the water
                        rivers.setDepth(x,y, rivers.getDepth(x,y) - 1);
                        rivers.setDepth(lowest[0],lowest[1], rivers.getDepth(lowest[0],lowest[1]) + 1);
                    }
                } else if ((x == 0) || (y == 0) || (x == rivers.getDimX() - 1) || (y == rivers.getDimY() - 1)) //if we are at the edge
                    rivers.setDepth(x,y,0);
            }
        }
    }
}