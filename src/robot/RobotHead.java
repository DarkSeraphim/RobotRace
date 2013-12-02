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
    
    public float[] dimensions;
    
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
                gl.glRotated(90, 1, 0, 0);
                gl.glScalef(this.dimensions[X]/2, this.dimensions[Z]/3, this.dimensions[X]/2);
                drawHalfSphere(1F, 32, 32);
                gl.glRotated(180, 1, 0, 0);
                gl.glScaled(1, 2, 1);
                drawHalfSphere(1F, 32, 32);
            }
            else
            {
                gl.glRotated(90, 1, 0, 0);
                gl.glScalef(this.dimensions[X]/2, this.dimensions[Z]/2, this.dimensions[X]/2);
                drawHalfSphere(1F, 32, 32);
                gl.glRotated(180, 1, 0, 0);
                drawHalfSphere(1F, 32, 32);
            }
        gl.glPopMatrix();
    }
    
    @Override
    public final void recalculate()
    {
        this.origin = this.robot.getOrigin().clone();
        float[] head = this.robot.getHeadDimensions();
        this.origin[Z] += head[Z]*(7F+2F/3F);
        this.dimensions = new float[]
        {
            head[X],
            head[Y],
            head[Z]
        };
    }

}
