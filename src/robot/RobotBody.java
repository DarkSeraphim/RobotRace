package robot;


/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public class RobotBody extends RobotPart
{
    
    private float[] o;
    private float[] dimensions;
    // I planned to use this if I was going to dynamically change body dimensions
    // But it was unused due a small lack of time
    private Robot robot;

    public RobotBody(Robot robot)
    {
        this.robot = robot;
        recalculate();
    }
    
    /*
     * Draw method for the body
     * using wire cubes for the stick figure
     */
    @Override
    public void draw(boolean isStick) 
    {
         gl.glPushMatrix();
            gl.glTranslatef(o[X], o[Y], o[Z]);
            // Scale
            gl.glScalef(dimensions[X], dimensions[Y], dimensions[Z]);
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
    
    @Override
    public final void recalculate()
    {
        float[] head = this.robot.getHeadDimensions();
        
        this.dimensions = new float[]
        {
            head[X]*2,
            head[Y]*0.8F,
            head[Z]*3.5F
        };
        
        this.o = this.robot.getOrigin().clone();
        this.o[Z] += head[Z]*(5.75F);
    }
    
    public final float[] getDimensions()
    {
        return this.dimensions;
    }

}
