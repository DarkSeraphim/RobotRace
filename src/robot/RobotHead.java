package robot;

import static robot.RobotPart.gl;

/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public class RobotHead extends RobotPart
{

    private final Robot robot;
    
    public float[] origin;
    
    public RobotHead(Robot robot)
    {
        this.robot = robot;
        recalculate();
    }
    
    /*
     * Draw method for the head
     * including some different behaviour if it is a stick figures
     */
    @Override
    public void draw(boolean isStick)
    {   
        gl.glColor3fv(this.robot.getColour(), 0);
        gl.glPushMatrix();
            gl.glTranslatef(origin[X], origin[Y], origin[Z]);
            if(!isStick)
            {
                gl.glScaled(1, 1, -1);
                drawHalfSphere(0.2f, 32, 32);
                gl.glScaled(1, 1, -2);
                drawHalfSphere(0.2f, 32, 32);
            }
            else
            {
                gl.glTranslatef(0.0f, 0.2f, 0.0f);
                glut.glutSolidSphere(0.2, 16, 16);
            }
        gl.glPopMatrix();
    }
    
    @Override
    public final void recalculate()
    {
        this.origin = this.robot.getOrigin();
        float[] head = this.robot.getHeadDimensions();
        this.origin[Z] += head[Z]*(6+2/3);
    }

}
