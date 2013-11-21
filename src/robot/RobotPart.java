package robot;

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

    protected static GL2 gl;
    protected static GLU glu;
    protected static GLUT glut;

    public static void initialize(GL2 gl, GLU glu, GLUT glut)
    {
        RobotPart.gl = gl;
        RobotPart.glu = glu;
        RobotPart.glut = glut;
    }

    public abstract void draw(boolean isStick);

    public static void drawHalfSphere(int scaley, int scalex, float r)
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
                gl.glVertex3fv(v[i * scaley + j], 0);
                gl.glVertex3fv(v[i * scaley + (j + 1) % scaley], 0);
                gl.glVertex3fv(v[(i + 1) * scaley + (j + 1) % scaley], 0);
                gl.glVertex3fv(v[(i + 1) * scaley + j], 0);
            }
        }
        gl.glEnd();
    }
}
