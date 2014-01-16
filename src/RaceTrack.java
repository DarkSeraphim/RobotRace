
import com.jogamp.opengl.util.texture.Texture;
import robotrace.Vector;
import static java.lang.Math.*;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
public class RaceTrack
{
    
    private AbstractTrack testtrack;
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
    private Texture track;
    private int trackNr;
    
    /**
     * Constructs the race track, sets up display lists.
     */
    public RaceTrack(GL2 gl, Texture brick)
    {
        this.gl = gl;
        this.track = brick;
        this.testtrack = new TestTrack();
        this.otrack = new OTrack();
    }

    /**
     * Draws this track, based on the selected track number.
     */
    public void draw(int trackNr)
    {
        AbstractTrack track = getTrack(trackNr);
        this.track.bind(this.gl);
        RobotRace.VBOUtil.drawBufferedObject(track.buf);
        this.gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
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
                return this.testtrack;
            case 1:
                return this.otrack;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        // return null;
        return this.testtrack;
    }
}

abstract class AbstractTrack
{
    /**
     * Array with control points for the O-track.
     */
    protected Vector[] controlPointsTrack;
    
    protected final boolean averaged = true;
    
    protected final int slices = 256*3;
    
    protected BufferData buf;
    
    protected final void init()
    {
        double d = 1D/slices;
        double cur = 0D;
        double tcur = 0D;
        double td = d*16;
        Vector height = new Vector(0, 0, 4);
        FloatBuffer vertices = FloatBuffer.allocate(slices * 4 * 4 * 3);
        FloatBuffer normals = FloatBuffer.allocate(slices * 4 * 4 * 3);
        FloatBuffer texture = FloatBuffer.allocate(slices * 4 * 4 * 2);
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
        int index = 0;
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
            
            // Whether the normals are averaged between adjecent sides
            if(averaged)
            {
                // Normals for p and q when it is neither Z nor -Z
                Vector np = this.getTangent(cur-d).cross(Vector.Z);
                Vector nq = this.getTangent(cur).cross(Vector.Z);

                // Vertices upper quad (top of the track)
                vec2buf(p1, vertices); texture.put(new float[]{0.5f, 1.0f}); si[0] = (short)(index++);
                vec2buf(p2, vertices); texture.put(new float[]{1.0f, 1.0f}); si[1] = (short)(index++);
                vec2buf(q2, vertices); texture.put(new float[]{1.0f, 0.0f}); si[2] = (short)(index++);
                vec2buf(q1, vertices); texture.put(new float[]{0.5f, 0.0f}); si[3] = (short)(index++);
                // Normals upper quad (top of the track)
                vec2buf(Vector.Z.add(np).normalized(), normals);
                vec2buf(Vector.Z.add(np).normalized(), normals);
                vec2buf(Vector.Z.add(nq).normalized(), normals);
                vec2buf(Vector.Z.add(nq).normalized(), normals);

                // Vertices back quad (outside of the track)
                vec2buf(p2, vertices); texture.put(new float[]{0f, Math.min((float)(tcur), 1.0f)}); si[4] = (short)(index++);
                vec2buf(p3, vertices); texture.put(new float[]{0.5f, Math.min((float)(tcur+td), 1.0f)}); si[5] = (short)(index++);
                vec2buf(q3, vertices); texture.put(new float[]{0.5f, Math.min((float)(tcur+td), 1.0f)}); si[6] = (short)(index++);
                vec2buf(q2, vertices); texture.put(new float[]{0.0f, Math.min((float)(tcur), 1.0f)}); si[7] = (short)(index++);
                // Normals back quad (outside of the track)
                vec2buf(Vector.Z.add(np).normalized(), normals);
                vec2buf(Vector.Z.scale(-1).add(np).normalized(), normals);
                vec2buf(Vector.Z.scale(-1).add(nq).normalized(), normals);
                vec2buf(Vector.Z.add(nq).normalized(), normals);

                // Vertices bottom quad (bottom of the track)
                vec2buf(p3, vertices); texture.put(new float[]{0.5f, 1.0f}); si[8] = (short)(index++);
                vec2buf(p4, vertices); texture.put(new float[]{1.0f, 1.0f}); si[9] = (short)(index++);
                vec2buf(q4, vertices); texture.put(new float[]{1.0f, 0.0f}); si[10] = (short)(index++);
                vec2buf(q3, vertices); texture.put(new float[]{0.5f, 0.0f}); si[11] = (short)(index++);
                // Normals bottom quad (bottom of the track)
                vec2buf(Vector.Z.scale(-1).add(np).normalized(), normals);
                vec2buf(Vector.Z.scale(-1).add(np).normalized(), normals);
                vec2buf(Vector.Z.scale(-1).add(nq).normalized(), normals);
                vec2buf(Vector.Z.scale(-1).add(nq).normalized(), normals);

                // Vertices front quad (inside of the track)
                vec2buf(p4, vertices); texture.put(new float[]{1.0f, 0.5f}); si[12] = (short)(index++);
                vec2buf(p1, vertices); texture.put(new float[]{1.0f, 0.0f}); si[13] = (short)(index++);
                vec2buf(q1, vertices); texture.put(new float[]{0.0f, 0.0f}); si[14] = (short)(index++);
                vec2buf(q4, vertices); texture.put(new float[]{0.0f, 0.5f}); si[15] = (short)(index++);
                // Normals front quad (inside of the track)
                vec2buf(Vector.Z.scale(-1).add(np).normalized(), normals);
                vec2buf(Vector.Z.add(np).normalized(), normals);
                vec2buf(Vector.Z.add(nq).normalized(), normals);
                vec2buf(Vector.Z.scale(-1).add(nq).normalized(), normals);
            }
            else
            {
                // Vertices upper quad (top of the track)
                vec2buf(p1, vertices); si[0] = (short)(index++);
                vec2buf(p2, vertices); si[1] = (short)(index++);
                vec2buf(q2, vertices); si[2] = (short)(index++);
                vec2buf(q1, vertices); si[3] = (short)(index++);
                // Normals upper quad (top of the track)
                vec2buf(Vector.Z, normals);
                vec2buf(Vector.Z, normals);
                vec2buf(Vector.Z, normals);
                vec2buf(Vector.Z, normals);

                // Vertices back quad (outside of the track)
                vec2buf(p2, vertices); si[4] = (short)(index++);
                vec2buf(p3, vertices); si[5] = (short)(index++);
                vec2buf(q3, vertices); si[6] = (short)(index++);
                vec2buf(q2, vertices); si[7] = (short)(index++);
                // Vertices back quad (outside of the track)
                vec2buf(this.getTangent(cur-d).cross(Vector.Z), normals);
                vec2buf(this.getTangent(cur-d).cross(Vector.Z), normals);
                vec2buf(this.getTangent(cur).cross(Vector.Z), normals);
                vec2buf(this.getTangent(cur).cross(Vector.Z), normals);

                // Vertices bottom quad (bottom of the track)
                vec2buf(p3, vertices); si[8] = (short)(index++);
                vec2buf(p4, vertices); si[9] = (short)(index++);
                vec2buf(q4, vertices); si[10] = (short)(index++);
                vec2buf(q3, vertices); si[11] = (short)(index++);
                // Normals bottom quad (bottom of the track)
                vec2buf(Vector.Z.scale(-1), normals);
                vec2buf(Vector.Z.scale(-1), normals);
                vec2buf(Vector.Z.scale(-1), normals);
                vec2buf(Vector.Z.scale(-1), normals);

                // Vertices front quad (inside of the track)
                vec2buf(p4, vertices); si[12] = (short)(index++);
                vec2buf(p1, vertices); si[13] = (short)(index++);
                vec2buf(q1, vertices); si[14] = (short)(index++);
                vec2buf(q4, vertices); si[15] = (short)(index++);
                // Normals front quad (inside of the track)
                vec2buf(Vector.Z.cross(this.getTangent(cur-d)), normals);
                vec2buf(Vector.Z.cross(this.getTangent(cur-d)), normals);
                vec2buf(Vector.Z.cross(this.getTangent(cur)), normals);
                vec2buf(Vector.Z.cross(this.getTangent(cur)), normals);
            }
            
            indices.put(si);
            
            p1 = q1;
            p2 = q2;
            p3 = q3;
            p4 = q4;
            
            tcur += td;
            tcur %= 1;
        }
        
        vertices.flip();
        normals.flip();
        texture.flip();
        indices.flip();
        
        this.buf = RobotRace.VBOUtil.bufferData(vertices, normals, texture, indices);
    }
    
    private void vec2buf(Vector vec, FloatBuffer buf)
    {
        buf.put((float)vec.x());
        buf.put((float)vec.y());
        buf.put((float)vec.z());
    }
    
    
    public abstract Vector getPoint(double t);
    
    public abstract Vector getTangent(double t);
    
    protected static int mod(int i, int m)
    {
        for(;i<0;) i += m;
        return i % m;
    }
}

class TestTrack extends AbstractTrack
{
    
    protected TestTrack() 
    {
        init();
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

class OTrack extends AbstractTrack
{

    /**
     * Control points for the Bézier curves,
     * specified for this track
     */
    private Vector[] controlPoints;
    
    private final int N = 256;
    
    protected OTrack()
    {
        int nn = 3*N;
        double d = 1D/N;
        double cur = 0D;
        controlPoints = new Vector[nn];
        for(int i = 0; i < nn; i += 3)
        {
            controlPoints[i] = new Vector(10*cos(2*PI*cur), 14*sin(2*PI*cur), 0D);
            Vector t = new Vector(-sin(2*PI*cur), cos(2*PI*cur), 0D).normalized().scale(0.01);
            controlPoints[mod((i-1), nn)] = controlPoints[i].subtract(t);
            controlPoints[mod((i+1), nn)] = controlPoints[i].add(t);
            cur += d;
        }
        init();
    }
    
    private Vector[] getControlPoints(int n)
    {
        int start = 3*(n);
        if(start < 0) // For development reasons only
            throw new NullPointerException("array index < 0: "+start);
        return new Vector[]{controlPoints[(start)%controlPoints.length], 
                            controlPoints[(start+1)%controlPoints.length],
                            controlPoints[(start+2)%controlPoints.length],
                            controlPoints[(start+3)%controlPoints.length]};
    }
    
    
    @Override
    public Vector getPoint(double t)
    {
        int slice = (int)(t * slices) % slices;
        int n = (int)Math.floor(slice / (slices / this.N));
        Vector[] controls = getControlPoints(n);
        int slicesperN = (slices / this.N);
        double u = (double)((t*slices)%slices - (slicesperN*((double)slice / (slices / this.N))))/(double)slicesperN;
        return getCubicBezierPnt(u, controls[0], controls[1], controls[2], controls[3]);
    }
    
    private Vector getCubicBezierPnt(double t, Vector P0, Vector P1, Vector P2, Vector P3)
    {
        return P0.scale(Math.pow(1-t, 3))
          .add(P1.scale(3*t*Math.pow(1-t, 2)))
          .add(P2.scale(3*Math.pow(t, 2)*(1-t)))
          .add(P3.scale(Math.pow(t, 3)));
    }


    @Override
    public Vector getTangent(double t)
    {
        int slice = (int)(t * slices) % slices;
        int n = (int)Math.floor(slice / (slices / this.N));
        Vector[] controls = getControlPoints(n);
        int slicesperN = (slices / this.N);
       double u = (double)((t*slices)%slices - (slicesperN*((double)slice / (slices / this.N))))/(double)slicesperN;
        return getCubicBezierTng(u, controls[0], controls[1], controls[2], controls[3]);
    }
    
    private Vector getCubicBezierTng(double t, Vector P0, Vector P1, Vector P2, Vector P3)
    {
        return P0.scale(-3*Math.pow(1-t, 2))
          .add(P1.scale(2*t-2))
          .add(P2.scale(6*t-9*Math.pow(t, 2)))
          .add(P3.scale(3*Math.pow(t, 2)));
    }
}