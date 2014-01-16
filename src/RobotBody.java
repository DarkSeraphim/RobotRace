
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;




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
    private final BufferData bodyData;

    public RobotBody(Robot robot)
    {
        this.robot = robot;
        recalculate();
        
        float[] v = new float[]
        {
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f, -0.5f, -0.5f
        };
        float[] n = v;
        float[] t = new float[]
        {
            
        };
        short[] i = new short[]
        {
            0, 1, 2, 3,
            
            0, 1, 5, 4,
            1, 2, 6, 5,
            2, 3, 7, 6,
            3, 0, 4, 7,
            
            4, 5, 6, 7
        };
        
        FloatBuffer vertices = FloatBuffer.wrap(v);
        FloatBuffer normals = FloatBuffer.wrap(n);
        FloatBuffer texture = null;//FloatBuffer.wrap(t);
        ShortBuffer indices = ShortBuffer.wrap(i);
        this.bodyData = RobotRace.VBOUtil.bufferData(vertices, normals, indices);
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
            if(isStick) // if stick figures mode is on
            {
                glut.glutWireCube(1);
            }
            else // if solid robots mode is on
            {
                RobotRace.VBOUtil.drawBufferedObject(this.bodyData);
                //glut.glutSolidCube(1);
            }
            // And scale back
        gl.glPopMatrix();
    }
    
    @Override
    public final void recalculate() // recalculates the dimensions proportional to the head size
    {
        float[] head = this.robot.getHeadDimensions();
        
        this.dimensions = new float[]
        {
            head[X]*2,
            head[Y]*0.8F,
            head[Z]*3.5F
        };
        
        System.out.println(java.util.Arrays.toString(this.dimensions));
        
        this.o = this.robot.getOrigin().clone();
        this.o[Z] += head[Z]*(5.75F);
    }
    
    /**
     * 
     * @return the body dimensions, based on the head dimensions
     */
    public final float[] getDimensions()
    {
        return this.dimensions;
    }

}
