import java.util.concurrent.atomic.AtomicBoolean;

/***
 * A runnable class which activates and controls the flow of water, and updates the time.
 * @author James Gain, Rea Keebine
 * @version 1.0.0 Sep 17, 2020
 */
public class FlowCtrl implements Runnable {

    /***
     * An array of four threads that will run the flow simulation.
     */
    private FlowThread[] flowThreads = new FlowThread[4];
    
    /**
     * Used to determine whether the threads should stop or not.
     */
    private AtomicBoolean stopWork = new AtomicBoolean(false);

    /**
     * Used to determine whether the threads are complete or not.
     */
    private AtomicBoolean done = new AtomicBoolean(true);

    /**
     * A text area that shows the simulation time.
     */
    private TimeText timeT;

    /**
     * A panel that shows the water and terrain images.
     */
    private FlowPanel flowP;

    /***
     * Creates a FlowCtrl object with the panel and text area to control.
     * @param fp the panel containing the terrain and water
     * @param tt the text area with the simulation time
     */
    public FlowCtrl(FlowPanel fp, TimeText tt) {
        flowP = fp;
        timeT = tt;
    }

    /***
     * Creates the water source on the FlowPanel.
     * @param x x-coordinate of the new water point
     * @param y y-coordinate of the new water point
     */
    public void makeSource(int x, int y) {
        flowP.makeSource(x, y);
    }

    /***
     * Resets the Water object and the time text.
     */
    public void reset() {
        flowP.resetRivers();
        timeT.resetTime();
    }

    /***
     * Sets the stopWork boolean to true to pause the simulation.
     */
    public void pause() {
        stopWork.set(true);
    }

    /***
     * Creates and starts the four threads which run the simulation, and sets the done boolean to false.
     */
    public void doWork() {
        for (int i = 0; i<4; i++) {
            flowThreads[i] = new FlowThread(i, flowP.getTerrain(), flowP.getWater());
            flowThreads[i].setName("fThread" + i);
        }
        for (int i = 0; i<4; i++) // separate for loop so that threads start as close to simultaneously as possible
            flowThreads[i].start();

        done.set(false);
    }

    /***
     * Returns true if all the threads are complete.
     * @return the done value
     */
    public AtomicBoolean allDone() {
        if (flowThreads[0]!=null && !flowThreads[0].isAlive() && !flowThreads[1].isAlive() && !flowThreads[2].isAlive() && !flowThreads[3].isAlive())
            done.set(true);
        return done;
    }

    /***
     * Starts the simulation and continues while stopWork is false and only if the previous threads are complete.
     */
    public void run() {
        stopWork.set(false);
        while (!stopWork.get())
            if (allDone().get()) {
                doWork();
                flowP.repaint();
                timeT.updateTime();
            }
    }
}