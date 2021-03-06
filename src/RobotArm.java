
import robotrace.Vector;
import static java.lang.Math.*;



/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public class RobotArm extends RobotPart
{

    private float[] origin;
    private float[] uDimensions;
    private float[] lDimensions;
    private float[] hand;
    private float shoulderRadius = 0.075F;
    private Vector shoulderRotVec = Vector.X;
    private double shoulderRot = 0;
    private float elbowRadius = 0.050F;
    private Vector elbowRotVec = Vector.X;
    private double elbowRot = 0;
    private Robot robot;
    private boolean isLeft;

    /*
     * Creates an arm that belongs to Robot robot
     */
    public RobotArm(Robot robot, boolean isLeft)
    {
        this.robot = robot;
        this.isLeft = isLeft; //  if left or right arm
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
        this.drawSphere(this.shoulderRadius, 128, 128);
        gl.glRotated(shoulderRot * (this.isLeft ? 1 : -1), shoulderRotVec.x(), shoulderRotVec.y(), shoulderRotVec.z());
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
        drawSphere(this.elbowRadius, 128, 128);
        gl.glRotated(elbowRot, elbowRotVec.x(), elbowRotVec.y(), elbowRotVec.z());
        gl.glTranslatef(0, 0, -this.elbowRadius - this.lDimensions[Z]);
        gl.glScalef(this.lDimensions[X], this.lDimensions[Y], this.lDimensions[Z]);
        if (isStick) // if stick figures mode is on
        {
            glut.glutSolidCylinder(0.1, 1D, 16, 16); // upper arm
        }
        else
        {
            glut.glutSolidCylinder(0.5D, 1D, 16, 16);
        }
        
        if (!isStick) // if solid robots mode is on
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
     * If it is a stick figure, this method will be ignore in the calling method
     */
    private void drawHand()
    {
        // the x and z offset from the center-top point of the palm of the hand
        float[][] fingers = new float[][]
        {
            {
                -2*(this.hand[Y]/3), this.hand[Z]/1.5f
            }, // pinky :D
            
            {
                -this.hand[Y]/4.5F, this.hand[Z]/8f
            }, // ring finger
            
            {
                this.hand[Y]/4.5F, this.hand[Z]/8f
            }, // middle finger
            
            {
                2*(this.hand[Y]/3), this.hand[Z]/2f
            }
        }; // index finger
        // translate to the middle of the palm
        gl.glPushMatrix();
        //gl.glRotatef(90, 0f, 1f, 0f);
        //gl.glRotatef(-90, 1f, 0f, 0f);
        gl.glScalef(this.hand[X], this.hand[Y], this.hand[Z]);
        //glut.glutSolidSphere(1f, 16, 16);
        this.drawSphere(1F, 128, 128);
        gl.glScalef(1/this.hand[X], 1/this.hand[Y], 1/this.hand[Z]);
        //gl.glRotatef(-90, 0f, 1f, 0f);
        gl.glPushMatrix();
        // Thumb code
        {
            gl.glTranslatef(0F, this.hand[Y]/1.5F, this.hand[Z]/4F);
            gl.glRotatef(90, 0f, 0f, 1f);
            gl.glRotatef(-40, 0f, 1f, 0f);
            fingerpart();
            gl.glTranslatef(0f, 0f, -this.hand[Z]/3F);
            gl.glRotatef(15, 0f, 1f, 0f);
            fingerpart();
            gl.glTranslatef(0f, 0f, -this.hand[Z]/3F);
            gl.glRotatef(5, 0f, 1f, 0f);
            fingerpart();
        }
        gl.glPopMatrix();
        // Back to where we were and draw 4 fingers
        gl.glRotatef(90, 0F, 0F, 1F);
        gl.glTranslatef(0f, 0f, -this.hand[Z]);
        for (float[] finger : fingers)
        {
            gl.glPushMatrix();
            {
                gl.glTranslatef(finger[0], 0f, finger[1]);
                for (int i = 0; i < 3; i++)
                {
                    fingerpart();
                    gl.glTranslatef(0f, 0f, -this.hand[Z]/3F);
                }
            }
            gl.glPopMatrix();
            //gl.glTranslatef(0.05f, 0f, finger[1]);
        }
        gl.glPopMatrix();
    }
    /**
     * Draws a part of a finger
     */
    private void fingerpart()
    {
        gl.glScalef(this.hand[X]/1.5F, this.hand[X]/1.5F, -this.hand[Z]/3);
        gl.glTranslatef(0f, 0f, 1f);
        this.drawSphere(0.6F, 16, 16);
        gl.glTranslatef(0f, 0f, -1f);
        glut.glutSolidCylinder(0.5f, 1F, 16, 16);
        gl.glScalef(1.5F/this.hand[X], 1.5F/this.hand[X], -3F/this.hand[Z]);
    }
    
    @Override
    public final void recalculate() // recalculated he origin proportional to the head size
    {
        this.origin = this.robot.getOrigin().clone();
        float[] head = this.robot.getHeadDimensions();
        float[] body = this.robot.getBodyDimensions();
        
        this.uDimensions = new float[]
        {
            (head[Z]*1.75F - this.shoulderRadius - this.elbowRadius/2)/3F,
            (head[Z]*1.75F - this.shoulderRadius - this.elbowRadius/2)/3F,
            head[Z]*1.75F - this.shoulderRadius - this.elbowRadius/2
        };
        
        this.origin[Z] += head[Z]*7.5F - this.shoulderRadius;
        this.origin[X] += (this.isLeft ? 1 : -1) * (body[X]/2F + this.uDimensions[X]/2F);
        
        
        this.lDimensions = new float[]
        {
            (head[Z] - this.elbowRadius/2)/3,
            (head[Z] - this.elbowRadius/2)/3,
            head[Z] - this.elbowRadius/2
        };
        
        this.hand = new float[]
        {
            this.lDimensions[X]/4F,
            this.lDimensions[Z]/5F,
            this.lDimensions[Z]/4F
        };
    }
    
    @Override
    public void update()
    {
        double rrot = ((this.robot.getTime()*(0.01/this.robot.getSpeed())) % 1)*2*PI;
        this.shoulderRot = sin(rrot)*50;
        this.elbowRot = (this.shoulderRot / 2) + 90;//(sin(rrot) * 5) + 90;
    }
}
