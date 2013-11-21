package robot;

/**
 *
 * @author Mark Hendriks & Frank Kuipers
 */
public class RobotBody extends RobotPart
{
    
    private float[] dimensions;
    // I planned to use this if I was going to dynamically change body dimensions
    // But it was unused due a small lack of time
    private Robot robot;

    public RobotBody( Robot robot, float[] dimensions)
    {
        this.robot = robot;
        this.dimensions = dimensions;
    }
    
    /*
     * Draw method for the body
     * using wire cubes for the stick figure
     */
    @Override
    public void draw(boolean isStick) 
    {
         gl.glPushMatrix();
            // Scale
            gl.glScalef(dimensions[0], dimensions[1], dimensions[2]);
            // Draw cube
            gl.glColor3fv(this.robot.getColour(), 0);
            if(isStick)
            {
                glut.glutWireCube(1);
            }
            else
            {
                glut.glutSolidCube(1);
            }
            // And scale back
        gl.glPopMatrix();
    }

}
