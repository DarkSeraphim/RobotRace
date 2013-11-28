package robot;

/**
 *
 * @author Mark Hendriks & Frank Kuipers
 */
public class RobotArm extends RobotPart
{

    private float[] upper;
    private float[] lower;
    private float shoulder;
    private float elbow;
    private float length;
    private float diametre;
    private Robot robot;
    private boolean isLeft;

    /*
     * Creates an arm that belongs to Robot robot
     */
    public RobotArm(Robot robot, boolean isLeft)
    {
        this.robot = robot;
        this.isLeft = isLeft;
    }

    /*
     * Draw method. whether it is stick or not changes some behaviour of cylinders
     * behaviour differs if it is a stick figure or not, and if it is the left or right leg
     */
    @Override
    public void draw(boolean isStick)
    {
        float[] origin = new float[]
        {
            (isLeft ? 1 : -1) * robot.headDimensions[0] / 2 + (isLeft ? 1 : -1) * 0.05f * 1.5f, 0f, +robot.headDimensions[2] / 2 - 0.05f
        };
        gl.glPushMatrix();
        gl.glTranslatef(origin[0], origin[1], origin[2]);
        gl.glColor3fv(this.robot.getColour(), 0);
        glut.glutSolidSphere(0.1, 16, 16); // shoulder
        gl.glColor3fv(this.robot.getColour(), 0);
        if (isStick)
        {
            glut.glutSolidCylinder(0.01, -length / 2, 16, 16); // upper arm
        }
        else
        {
            glut.glutSolidCylinder(diametre, -length / 2, 16, 16); // upper arm
        }
        gl.glTranslatef(0, 0, -length / 1.75f + diametre / 2);
        gl.glColor3fv(this.robot.getColour(), 0);
        glut.glutSolidSphere(diametre * 1.26, 16, 16);
        gl.glTranslatef(0, 0, -diametre / 2);
        gl.glColor3fv(this.robot.getColour(), 0);
        if (isStick)
        {
            glut.glutSolidCylinder(0.01, -length / 2, 16, 16); // upper arm
        }
        else
        {
            glut.glutSolidCylinder(diametre, -length / 2, 16, 16);
        }
        gl.glTranslatef(0, /*armDiametre*5f*0.25f*/ 0f, -length / 2 - diametre / 2 - 0.08f);
        if (!isStick)
        {
            drawHand();
        }
        gl.glPopMatrix();
    }

    /*
     * Drawing a hand
     * If it is not a stick figure, it has a hand with 4 fingers and 1 thumb
     */
    private void drawHand()
    {
        // length, z-offset of the next finger
        float[][] fingers = new float[][]
        {
            {
                0.22f, 0f
            }, // pinky :D
            
            {
                0.26f, 0f
            }, // ring finger
            
            {
                0.27f, 0f
            }, // middle finger
            
            {
                0.25f, 0f
            }
        }; // index finger
        // translate to the middle of the palm
        gl.glPushMatrix();
        gl.glColor3fv(this.robot.getColour(), 0);
        gl.glRotatef(90, 0f, 1f, 0f);
        gl.glRotatef(-90, 1f, 0f, 0f);
        gl.glScalef(0.6f, 0.2f, 0.5f);
        glut.glutSolidSphere(0.2f, 16, 16);
        gl.glScalef(1 / 0.6f, 1 / 0.2f, 1 / 0.5f);
        gl.glRotatef(-90, 0f, 1f, 0f);
        gl.glColor3fv(this.robot.getColour(), 0);
        gl.glPushMatrix();
        // Thumb code
        {
            gl.glTranslatef(0.09f, 0f, 0f);
            gl.glRotatef(340, 0f, 1f, 0f);
            glut.glutSolidCylinder(0.02f, -0.06, 16, 16);
            gl.glTranslatef(0f, 0f, -0.06f);
            gl.glRotatef(15, 0f, 1f, 0f);
            glut.glutSolidSphere(0.022, 16, 16);
            glut.glutSolidCylinder(0.02f, -0.05, 16, 16);
            gl.glTranslatef(0f, 0f, -0.05f);
            glut.glutSolidSphere(0.022, 16, 16);
            glut.glutSolidCylinder(0.02f, -0.05, 16, 16);
        }
        gl.glPopMatrix();
        // Back to where we were and draw 4 fingers
        gl.glTranslatef(-0.075f, 0.0f, 0.00f);// translate to pink
        for (float[] finger : fingers)
        {
            gl.glPushMatrix();
            for (int i = 0; i < 3; i++)
            {
                glut.glutSolidCylinder(0.02f, -finger[0] / 3, 16, 16);
                glut.glutSolidSphere(0.025, 16, 16);
                gl.glTranslatef(0f, 0f, -finger[0] / 3);
            }
            gl.glPopMatrix();
            gl.glTranslatef(0.05f, 0f, finger[1]);
        }
        gl.glColor3f(0f, 0f, 0f);
        gl.glPopMatrix();
    }
    
    public final void recalculate()
    {
        
    }
}
