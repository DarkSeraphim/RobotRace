package robot;

/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public class RobotHead extends RobotPart
{

    private final Robot robot;
    
    public float[] origin;
    
    public RobotHead(Robot robot, float[] o)
    {
        this.robot = robot;
        this.origin = o;
    }
    
    /*
     * Draw method for the head
     * including some different behaviour if it is a stick figures
     */
    public void draw(boolean isStick)
    {   
        gl.glColor3fv(this.robot.getColour(), 0);
        gl.glPushMatrix();
            gl.glTranslatef(origin[0], origin[1], origin[2]);
            gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
            if(!isStick)
            {
                gl.glScalef(1.0f, 2.0f, 1.0f);
                drawHalfSphere(32, 32, 0.2f);
                gl.glScalef(1.0f, 1/2f, 1.0f);
                glut.glutSolidSphere(0.2, 16, 16);
            }
            else
            {
                gl.glTranslatef(0.0f, 0.2f, 0.0f);
                glut.glutSolidSphere(0.2, 16, 16);
            }
        gl.glPopMatrix();
    }

}
