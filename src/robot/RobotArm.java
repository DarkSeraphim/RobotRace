package robot;

import static robot.RobotPart.gl;

/**
 *
 * @author Mark Hendriks & Frank Kuipers
 */
public class RobotArm extends RobotPart
{

    private float[] origin;
    private float[] uDimensions;
    private float[] lDimensions;
    private float[] hand;
    private float shoulderRadius = 0.1F;
    private float elbowRadius = 0.1F;
    private Robot robot;
    private boolean isLeft;

    /*
     * Creates an arm that belongs to Robot robot
     */
    public RobotArm(Robot robot, boolean isLeft)
    {
        this.robot = robot;
        this.isLeft = isLeft;
        recalculate();
    }

    /*
     * Draw method. whether it is stick or not changes some behaviour of cylinders
     * behaviour differs if it is a stick figure or not, and if it is the left or right leg
     */
    @Override
    public void draw(boolean isStick)
    {
        gl.glPushMatrix();
        gl.glTranslatef(this.origin[X], this.origin[Y], this.origin[Z]);
        glut.glutSolidSphere(this.shoulderRadius, 16, 16); // shoulder
        gl.glTranslatef(0, 0, -this.uDimensions[Z] - this.shoulderRadius);
        gl.glScalef(this.uDimensions[X], this.uDimensions[Y], this.uDimensions[Z]);
        if (isStick)
        {
            glut.glutSolidCylinder(0.1, 1D, 16, 16); // upper arm
        }
        else
        {
            glut.glutSolidCylinder(0.5, 1D, 16, 16); // upper arm
        }
        gl.glScalef(1/this.uDimensions[X], 1/this.uDimensions[Y], 1/this.uDimensions[Z]);
        gl.glTranslatef(0, 0, -this.elbowRadius);
        glut.glutSolidSphere(this.elbowRadius, 16, 16);
        gl.glTranslatef(0, 0, -this.elbowRadius - this.lDimensions[Z]);
        gl.glScalef(this.lDimensions[X], this.lDimensions[Y], this.lDimensions[Z]);
        if (isStick)
        {
            glut.glutSolidCylinder(0.01, 1D, 16, 16); // upper arm
        }
        else
        {
            glut.glutSolidCylinder(0.5D, 1D, 16, 16);
        }
        
        if (!isStick)
        {
            gl.glScalef(1/this.lDimensions[X], 1/this.lDimensions[Y], 1/this.lDimensions[Z]);
            gl.glTranslatef(0, 0f, -this.hand[Z]/2);
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
        //gl.glRotatef(90, 0f, 1f, 0f);
        //gl.glRotatef(-90, 1f, 0f, 0f);
        gl.glScalef(this.hand[X], this.hand[Y], this.hand[Z]);
        glut.glutSolidSphere(1f, 16, 16);
        gl.glScalef(1/this.hand[X], 1/this.hand[Y], 1/this.hand[Z]);
        //gl.glRotatef(-90, 0f, 1f, 0f);
        gl.glPushMatrix();
        // Thumb code
        {
            gl.glTranslatef(0F, this.hand[Y]/1F, this.hand[Z]/3F);
            gl.glRotatef(90, 0f, 0f, 1f);
            gl.glRotatef(-45, 0f, 1f, 0f);
            thumbpart();
            gl.glTranslatef(0f, 0f, -this.hand[Z]/3);
            gl.glRotatef(15, 0f, 1f, 0f);
            glut.glutSolidSphere(0.01, 16, 16);
            thumbpart();
            gl.glTranslatef(0f, 0f, -this.hand[Z]/3);
            glut.glutSolidSphere(0.01, 16, 16);
            thumbpart();
        }
        gl.glPopMatrix();
        // Back to where we were and draw 4 fingers
        /*gl.glTranslatef(-0.075f, 0.0f, 0.00f);// translate to pink
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
        */
        gl.glPopMatrix();
    }
    
    private void thumbpart()
    {
        gl.glScalef(this.hand[X]/1.5F, this.hand[X]/1.5F, -this.hand[Z]/3);
        glut.glutSolidCylinder(0.5f, 1F, 16, 16);
        gl.glScalef(1.5F/this.hand[X], 1.5F/this.hand[X], -3F/this.hand[Z]);
    }
    
    @Override
    public final void recalculate()
    {
        this.origin = this.robot.getOrigin().clone();
        float[] head = this.robot.getHeadDimensions();
        float[] body = this.robot.getBodyDimensions();
        
        this.uDimensions = new float[]
        {
            (head[Z]*1.75F - this.shoulderRadius - this.elbowRadius/2)/3F, // According to our resources, a third of the length
            (head[Z]*1.75F - this.shoulderRadius - this.elbowRadius/2)/3F, // According to our resources, a third of the length
            head[Z]*1.75F - this.shoulderRadius - this.elbowRadius/2
        };
        
        this.origin[Z] += head[Z]*7.5F - this.shoulderRadius;
        this.origin[X] += (this.isLeft ? 1 : -1) * (body[X]/2F + this.uDimensions[X]/2F);
        
        
        this.lDimensions = new float[]
        {
            (head[Z] - this.elbowRadius/2)/3, // According to our resources
            (head[Z] - this.elbowRadius/2)/3, // According to our resources
            head[Z] - this.elbowRadius/2
        };
        
        this.hand = new float[]
        {
            this.lDimensions[X]/4F,
            this.lDimensions[Z]/4F,
            this.lDimensions[Z]/4F
        };
    }
}
