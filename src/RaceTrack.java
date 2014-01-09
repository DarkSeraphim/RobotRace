
import robotrace.Vector;
import static java.lang.Math.*;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.media.opengl.GL2;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
public class RaceTrack
{
    
    private AbstractTrack otrack;
    
    /**
     * Array with control points for the L-track.
     */
    private Vector[] controlPointsLTrack;
    /**
     * Array with control points for the C-track.
     */
    private Vector[] controlPointsCTrack;
    /**
     * Array with control points for the custom track.
     */
    private Vector[] controlPointsCustomTrack;
    
    private GL2 gl;
    private int trackNr;
    
    /**
     * Constructs the race track, sets up display lists.
     */
    public RaceTrack(GL2 gl)
    {
        this.gl = gl;
        this.otrack = new OTrack();
    }

    /**
     * Draws this track, based on the selected track number.
     */
    public void draw(int trackNr)
    {
        AbstractTrack track = getTrack(trackNr);
        // The test track is selected
        if (0 == trackNr)
        {
            // The O-track is selected
            //this.gl.glScaled(14, 10, 1);
            RobotRace.VBOUtil.drawBufferedObject(track.buf);
            //this.gl.glScaled(1/14D, 1/10D, 1D);
        }
        else if (1 == trackNr)
        {
            // code goes here ...
            // The L-track is selected
        }
        else if (2 == trackNr)
        {
            // code goes here ...
            // The C-track is selected
        }
        else if (3 == trackNr)
        {
            // code goes here ...
            // The custom track is selected
        }
        else if (4 == trackNr)
        {
            // code goes here ...
        }
    }

    /**
     * Returns the position of the curve at 0 <= {@code t} <= 1.
     */
    public final Vector getPoint(double t)
    {
        return getTrack(-1).getPoint(t);
    }

    /**
     * Returns the tangent of the curve at 0 <= {@code t} <= 1.
     */
    public final Vector getTangent(double t)
    {
        return getTrack(-1).getTangent(t);
    }
    
    private final AbstractTrack getTrack(int trackNr)
    {
        if(trackNr >= 0)
            this.trackNr = trackNr;
        switch(this.trackNr)
        {
            default:
            case 0:
                return this.otrack;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        // return null;
        return this.otrack;
    }
}

abstract class AbstractTrack
{
    /**
     * Array with control points for the O-track.
     */
    protected Vector[] controlPointsOTrack;
    
    protected BufferData buf;
    
    public abstract Vector getPoint(double t);
    
    public abstract Vector getTangent(double t);
}

class OTrack extends AbstractTrack
{
    
    protected OTrack()
    {
        int slices = 256;
        double d = 1D/slices;
        double cur = 0D;
        Vector height = new Vector(0, 0, 4);
        FloatBuffer vertices = FloatBuffer.allocate(slices * 4 * 4 * 3);
        FloatBuffer normals = FloatBuffer.allocate(slices * 4 * 4 * 3);
        ShortBuffer indices = ShortBuffer.allocate(slices * 16);
        double maxdist = Double.MIN_VALUE; // Set this for the scaling later
        int i;
        Vector p = this.getPoint(cur % 1);
        Vector width = p.normalized().scale(2);
        Vector p1 = p.subtract(width),
               p2 = p.add(width),
               p3 = p.add(width).subtract(height),
               p4 = p.subtract(width).subtract(height);
        Vector q1, q2, q3, q4;
        int index = 4;
        short[] si = new short[16];
        for(i = 0; i < slices; i++)
        {
            cur += d;
            Vector q = this.getPoint(cur % 1);
            width = q.normalized().scale(2);
            q1 = q.subtract(width);
            q2 = q.add(width);
            q3 = q.add(width).subtract(height);
            q4 = q.subtract(width).subtract(height);
            
            double len = q.length();
            if(len > maxdist)
                maxdist = len;
            
            // Vertices
            vec2buf(p1, vertices); si[0] = (short)(index - 4);
            vec2buf(p2, vertices); si[1] = (short)(index - 3);
            vec2buf(q2, vertices); si[2] = (short)(index + 1);
            vec2buf(q1, vertices); si[3] = (short)(index + 0);
            // Normals
            vec2buf(Vector.Z, normals);
            vec2buf(Vector.Z, normals);
            vec2buf(Vector.Z, normals);
            vec2buf(Vector.Z, normals);
            
            vec2buf(p2, vertices); si[4] = (short)(index - 3);
            vec2buf(p3, vertices); si[5] = (short)(index - 2);
            vec2buf(q3, vertices); si[6] = (short)(index + 2);
            vec2buf(q2, vertices); si[7] = (short)(index + 1);
            vec2buf(this.getTangent(cur-d).cross(Vector.Z), normals);
            vec2buf(this.getTangent(cur-d).cross(Vector.Z), normals);
            vec2buf(this.getTangent(cur).cross(Vector.Z), normals);
            vec2buf(this.getTangent(cur).cross(Vector.Z), normals);
            
            vec2buf(p3, vertices); si[8] = (short)(index - 2);
            vec2buf(p4, vertices); si[9] = (short)(index - 1);
            vec2buf(q4, vertices); si[10] = (short)(index + 3);
            vec2buf(q3, vertices); si[11] = (short)(index + 2);
            vec2buf(Vector.Z.scale(-1), normals);
            vec2buf(Vector.Z.scale(-1), normals);
            vec2buf(Vector.Z.scale(-1), normals);
            vec2buf(Vector.Z.scale(-1), normals);
            
            vec2buf(p4, vertices); si[12] = (short)(index - 1);
            vec2buf(p1, vertices); si[13] = (short)(index - 4);
            vec2buf(q1, vertices); si[14] = (short)(index + 0);
            vec2buf(q4, vertices); si[15] = (short)(index + 3);
            vec2buf(Vector.Z.cross(this.getTangent(cur-d)), normals);
            vec2buf(Vector.Z.cross(this.getTangent(cur-d)), normals);
            vec2buf(Vector.Z.cross(this.getTangent(cur)), normals);
            vec2buf(Vector.Z.cross(this.getTangent(cur)), normals);
           
            
            indices.put(si);
            
            p1 = q1;
            p2 = q2;
            p3 = q3;
            p4 = q4;
            index += 16;
        }
        
        // Scale the vertices please!
        //float[] v = vertices.array();
        //for(int j = 0; j < v.length; j++)
          //  v[j] = v[j]/(float)maxdist;
        
        vertices.flip();
        normals.flip();
        indices.flip();
        /*
        short k;
        for(k = 0; k < slices*4; k++)
        {
            indices.put((short)((k) % (vertices.capacity())));
            indices.put((short)((k+1) % (vertices.capacity())));
            indices.put((short)((k+2) % (vertices.capacity())));
            indices.put((short)((k+3) % (vertices.capacity())));
            
            for(int j = 0; j < 4; j++)
            {
                indices.put((short)(k % (vertices.capacity())));
                indices.put((short)((k+1) % (vertices.capacity())));
                indices.put((short)((k+1+4) % (vertices.capacity())));
                indices.put((short)((k+4) % (vertices.capacity())));
                k++;
            }
            k--;
        }
        indices.flip();*/
        
        this.buf = RobotRace.VBOUtil.bufferData(vertices, normals, indices);
    }
    
    private void vec2buf(Vector vec, FloatBuffer buf)
    {
        buf.put((float)vec.x());
        buf.put((float)vec.y());
        buf.put((float)vec.z());
    }

    @Override
    public final Vector getPoint(double t)
    {
        return new Vector(10*cos(2*PI*t), 14*sin(2*PI*t), 0D);
    }

    @Override
    public final Vector getTangent(double t)
    {
        return new Vector(-sin(2*PI*t), cos(2*PI*t), 0D);
    }
    
}