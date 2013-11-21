
import robotrace.Vector;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
public class RaceTrack
{

    /**
     * Array with control points for the O-track.
     */
    private Vector[] controlPointsOTrack;
    /**
     * Array with control points for the L-track.
     */
    private Vector[] controlPointsLTrack;
    /**
     * Array with control points for the C-track.
     */
    private Vector[] controlPointsCTrack;
    /**
     * Array with control points for the custom track.
     */
    private Vector[] controlPointsCustomTrack;

    /**
     * Constructs the race track, sets up display lists.
     */
    public RaceTrack()
    {
        // code goes here ...
    }

    /**
     * Draws this track, based on the selected track number.
     */
    public void draw(int trackNr)
    {

        // The test track is selected
        if (0 == trackNr)
        {
            // code goes here ...
            // The O-track is selected
        }
        else if (1 == trackNr)
        {
            // code goes here ...
            // The L-track is selected
        }
        else if (2 == trackNr)
        {
            // code goes here ...
            // The C-track is selected
        }
        else if (3 == trackNr)
        {
            // code goes here ...
            // The custom track is selected
        }
        else if (4 == trackNr)
        {
            // code goes here ...
        }
    }

    /**
     * Returns the position of the curve at 0 <= {@code t} <= 1.
     */
    public Vector getPoint(double t)
    {
        return Vector.O; // <- code goes here
    }

    /**
     * Returns the tangent of the curve at 0 <= {@code t} <= 1.
     */
    public Vector getTangent(double t)
    {
        return Vector.O; // <- code goes here
    }
}
