package robot;

/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public class RobotLeg extends RobotPart
{

    private float length;
    private float diametre;
    private Robot robot;
    private boolean isLeft;

    public RobotLeg(Robot robot, boolean isLeft)
    {
        this.robot = robot;
        this.isLeft = isLeft;
    }

    /*
     * Draw method for legs
     * including different behaviour for stick figures, and if it is a left or right leg
     */
    @Override
    public void draw(boolean isStick)
    {
        float[] origin = new float[]
        {
            (isLeft ? 1 : -1) * robot.headDimensions[0] / 2 + (isLeft ? -1 : 1) * 0.1f, 0f, -robot.headDimensions[2] / 2
        };
        gl.glPushMatrix();
        gl.glColor3fv(this.robot.getColour(), 0);
        gl.glTranslatef(origin[0], origin[1], origin[2]);
        if (isStick)
        {
            glut.glutSolidCylinder(0.01, -length / 1.75, 16, 16);
        }
        else
        {
            glut.glutSolidCylinder(diametre, -length / 1.75, 16, 16);
        }
        gl.glTranslatef(0, 0, -length / 1.75f);
        gl.glColor3fv(this.robot.getColour(), 0);
        glut.glutSolidSphere(diametre * 1.26, 16, 16);
        gl.glTranslatef(0, 0, -diametre / 2);
        gl.glColor3fv(this.robot.getColour(), 0);
        if (isStick)
        {
            glut.glutSolidCylinder(0.01, -length / 2, 16, 16);
        }
        else
        {
            glut.glutSolidCylinder(diametre, -length / 2, 16, 16);
        }
        gl.glTranslatef(0, diametre * 5f * 0.25f, -length / 2 - diametre / 2);
        gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
        // Foot
        gl.glTranslatef(-diametre, 0.0f, 0.0f);
        gl.glScalef(diametre * 2.5f, diametre * 1f, diametre * 1.25f);
        gl.glColor3fv(this.robot.getColour(), 0);
        if (!isStick)
        {
            drawHalfSphere(1f, 32, 32);
            gl.glScalef(1.0f, 0.0f, 1.0f);
            drawHalfSphere(1.0f, 32, 32);
            gl.glScalef(1 / 2f, 1.0f, 1.0f);
        }
        gl.glPopMatrix();
    }
    
    public final void recalculate()
    {
        
    }
}