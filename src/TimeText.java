import javax.swing.JTextArea;

/***
 * A text area that shows the number of timesteps since the start of the simulation.
 * @author Rea Keebine
 * @version 1.0.0 Sep 17, 2020
 */
public class TimeText extends JTextArea {

    /***
     * Timesteps since the start of the simulation.
     */
    private int time;

    /***
     * Sets up a new TimeText text area with a time of zero.
     */
    public TimeText() {
        time = 0;
    }

    /***
     * Sets the text of the text area to the current time.
     */
    public void showTime() {
        super.setText("Time: " + time);
    }

    /***
     * Resets the time to zero and shows the new time.
     */
    public void resetTime() {
        time = 0;
        showTime();
    }

    /***
     * Updates the time by one timestep and shows the new time.
     */
    public void updateTime() {
        time = time + 1;
        showTime();
    }
}