
import robotrace.Vector;

/**
 * Implementation of a camera with a position and orientation.
 */
public class Camera
{
    
    /**
     * 
     */
    public final RobotRace main;
    /**
     * The position of the camera.
     */
    public Vector eye = new Vector(3f, 6f, 5f);
    /**
     * The point to which the camera is looking.
     */
    public Vector center = Vector.O;
    /**
     * The up vector.
     */
    public Vector up = Vector.Z;

    protected Camera(RobotRace main)
    {
        this.main = main;
    }
    
    /**
     * Updates the camera viewpoint and direction based on the selected camera
     * mode.
     */
    public void update(int mode)
    {
        Robot[] robots = this.main.getRobots();
        robots[0].toString();

        // Helicopter mode
        if (1 == mode)
        {
            setHelicopterMode();

            // Motor cycle mode
        }
        else if (2 == mode)
        {
            setMotorCycleMode();

            // First person mode
        }
        else if (3 == mode)
        {
            setFirstPersonMode();

            // Auto mode
        }
        else if (4 == mode)
        {
            // code goes here...
            // Default mode
        }
        else
        {
            setDefaultMode();
        }
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the
     * camera's default mode.
     */
    private void setDefaultMode()
    {
        // code goes here ...
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the
     * helicopter mode.
     */
    private void setHelicopterMode()
    {
        // code goes here ...
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the
     * motorcycle mode.
     */
    private void setMotorCycleMode()
    {
        // code goes here ...
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the first
     * person mode.
     */
    private void setFirstPersonMode()
    {
        // code goes here ...
    }
}
