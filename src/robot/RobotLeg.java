package robot;

import static robot.RobotPart.gl;
import static robot.RobotPart.glut;

/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public class RobotLeg extends RobotPart
{

    float[] origin;
    float[] uDimensions;
    float kneeRadius = 0.1F;
    float[] lDimensions;
    
    private Robot robot;
    private boolean isLeft;

    public RobotLeg(Robot robot, boolean isLeft)
    {
        this.robot = robot;
        this.isLeft = isLeft;
        this.recalculate();
    }

    /*
     * Draw method for legs
     * including different behaviour for stick figures, and if it is a left or right leg
     */
    @Override
    public void draw(boolean isStick)
    {
        gl.glPushMatrix();
        gl.glTranslatef(origin[X], origin[Y], origin[Z]-this.uDimensions[Z]);
        gl.glScalef(this.uDimensions[X], this.uDimensions[Y], this.uDimensions[Z]);
        if (isStick)
        {
            glut.glutSolidCylinder(0.01*(1/this.lDimensions[X]), 1D, 16, 16);
        }
        else
        {
            glut.glutSolidCylinder(.5D, 1D, 16, 16);
        }
        gl.glScalef(1/this.uDimensions[X], 1/this.uDimensions[Y], 1/this.uDimensions[Z]);
        gl.glTranslatef(0, 0, -(this.kneeRadius));
        //glut.glutSolidSphere(this.kneeRadius, 16, 16);
        drawSphere(this.kneeRadius, 16, 16);
        gl.glTranslatef(0, 0, -(this.kneeRadius)-this.lDimensions[Z]);
        gl.glScalef(this.lDimensions[X], this.lDimensions[Y], this.lDimensions[Z]);
        if (isStick)
        {
            glut.glutSolidCylinder(0.01*(1/this.lDimensions[X]), 1D, 16, 16);
        }
        else
        {
            glut.glutSolidCylinder(.5D, 1D, 16, 16);
        }
        if (!isStick)
        {
            gl.glScalef(1/this.lDimensions[X], 1/this.lDimensions[Y], 1/this.lDimensions[Z]);
            //gl.glTranslatef(0, 0, -this.lDimensions[Z]/2);
            gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
            // Foot
            gl.glTranslatef(this.lDimensions[X]/2, 0.0f, 0.005F);
            gl.glScalef(this.lDimensions[X] * 1.5f, this.lDimensions[X] * 1f, this.lDimensions[X] * 1.25f);
            drawHalfSphere(1f, 32, 32);
            gl.glScalef(1.0f, 0.0f, 1.0f);
            drawHalfSphere(1f, 32, 32);
        }
        gl.glPopMatrix();
    }
    
    @Override
    public final void recalculate()
    {
        this.origin = this.robot.getOrigin().clone();
        float[] head = this.robot.getHeadDimensions();
        float[] body = this.robot.getBodyDimensions();
        
        this.uDimensions = new float[]
        {
            (head[Z]*2 - this.kneeRadius)/3, // According to our resources, a third of the length
            (head[Z]*2 - this.kneeRadius)/3, // According to our resources, a third of the length
            head[Z]*2 - this.kneeRadius
        };
        
        this.origin[Z] += head[Z]*4;
        this.origin[X] += (this.isLeft ? 1 : -1) * (body[X]/2F - this.uDimensions[X]/2F);
        
        
        this.lDimensions = new float[]
        {
            (head[Z]*1.5F)/3, // According to our resources
            (head[Z]*1.5F)/3, // According to our resources
            head[Z]*1.5F
        };
    }
}
