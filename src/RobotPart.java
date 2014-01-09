

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static java.lang.Math.*;

/**
 *
 * @author Mark Hendriks & Gabriel Garcia
 */
public abstract class RobotPart
{
    // for 3 dimensional arrays
    protected static byte X = 0;
    protected static byte Y = 1;
    protected static byte Z = 2;

    protected static GL2 gl;
    protected static GLU glu;
    protected static GLUT glut;
    
    private double at;

    public static void initialize(GL2 gl, GLU glu, GLUT glut)
    {
        RobotPart.gl = gl;
        RobotPart.glu = glu;
        RobotPart.glut = glut;
    }

    /**
     * Draws the bodypart
     * @param isStick whether to draw a stick figure or a full robot
     */
    public abstract void draw(boolean isStick);
    
    /**
     * Recalculates the origin and dimensions for the bodypart
     */
    public abstract void recalculate();
    
    public void update(){}
    
    /**
     * Private method for the actual drawing. This implementation does not push
     * nor pop, to save space on the stack. (This is done by the public method
     * @param r radius of the sphere
     * @param scalex amount of slices
     * @param scaley amount of stacks
     */
    public static void _drawHalfSphere(float r, int scalex, int scaley)
    {
        int i, j;
        float[][] v = new float[scalex * scaley][3];
        for (i = 0; i < scalex; ++i)
        {
            for (j = 0; j < scaley; ++j)
            {
                v[i * scaley + j][0] = r * (float) cos(j * 2 * PI / scaley) * (float) cos(i * PI / (2 * scalex));
                v[i * scaley + j][1] = r * (float) sin(i * PI / (2 * scalex));
                v[i * scaley + j][2] = r * (float) sin(j * 2 * PI / scaley) * (float) cos(i * PI / (2 * scalex));
            }
        }
                
        gl.glBegin(GL2.GL_QUADS);
        for (i = 0; i < scalex - 1; ++i)
        {
            for (j = 0; j < scaley; ++j)
            {
                gl.glNormal3fv(v[i * scaley + j], 0);
                gl.glVertex3fv(v[i * scaley + j], 0);
                gl.glNormal3fv(v[i * scaley + (j + 1) % scaley], 0);
                gl.glVertex3fv(v[i * scaley + (j + 1) % scaley], 0);
                gl.glNormal3fv(v[(i + 1) * scaley + (j + 1) % scaley], 0);
                gl.glVertex3fv(v[(i + 1) * scaley + (j + 1) % scaley], 0);
                gl.glNormal3fv(v[(i + 1) * scaley + j], 0);
                gl.glVertex3fv(v[(i + 1) * scaley + j], 0);
            }
        }
        gl.glEnd();
    }
    
    /**
     * Draws a sphere at the current local origin
     * @param r radius of the sphere
     * @param scalex amount of slices
     * @param scaley amount of stacks
     */
    public void drawSphere(float r, int scalex, int scaley)
    {
        BufferData data = RobotRace.VBOUtil.bufferHalfSphere(1F, scalex, scaley);
        if(r != 1F)
            gl.glScalef(r, r, r);
        RobotRace.VBOUtil.drawBufferedObject(data);
        gl.glScaled(1, -1, 1);
        RobotRace.VBOUtil.drawBufferedObject(data);
        if(r != 1F)
            gl.glScaled(1/r, -1/r, 1/r);
        else
            gl.glScaled(1, -1, 1);
    }
    
    /**
     * Draws half a sphere at the current local origin
     * for creating body parts like feet and the head.
     * @param r radius of the sphere
     * @param scalex amount of slices
     * @param scaley amount of stacks
     */
    public void drawHalfSphere(float r, int scalex, int scaley)
    {
        
        BufferData data = RobotRace.VBOUtil.bufferHalfSphere(1F, scalex, scaley);
        if(r != 1F)
            gl.glScalef(r, r, r);
        RobotRace.VBOUtil.drawBufferedObject(data);
        if(r != 1F)
            gl.glScaled(1/r, 1/r, 1/r);
    }
}

