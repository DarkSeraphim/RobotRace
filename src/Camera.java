
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.Random;
import javax.media.opengl.GL2;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import javax.media.opengl.glu.GLU;
import robotrace.GlobalState;
import robotrace.Vector;

/**
 * Implementation of a camera with a position and orientation.
 */
public class Camera
{
    
    /**
     * 
     */
    public final RobotRace main;
    
    /**
     * The position of the camera.
     */
    public Vector eye = new Vector(3f, 6f, 5f);
    /**
     * The point to which the camera is looking.
     */
    public Vector center = Vector.O;
    /**
     * The up vector.
     */
    public Vector up = Vector.Z;
    
    private boolean newstate = false;
    private int oldmode = -1;
    
    private int submode = -1;
    private int modecounter = 0;
    private final Random rand;

    protected Camera(RobotRace main, GL2 gl, GLU glu, GLUT glut, GlobalState gs)
    {
        this.main = main;
        this.rand = new Random();
    }
    
    /**
     * Updates the camera viewpoint and direction based on the selected camera
     * mode.
     */
    public void update(int mode)
    {
        Robot[] robots = this.main.getRobots();
        robots[0].toString();
        if(mode != this.oldmode)
        {
            this.newstate = true;
            this.oldmode = mode;
        }
        if (1 == mode)
        {
            // Helicopter mode
            setHelicopterMode();
        }
        else if (2 == mode)
        {
            // Motor cycle mode
            setMotorCycleMode();
        }
        else if (3 == mode)
        {
             // First person mode
            setFirstPersonMode();
        }
        else if (4 == mode)
        {
            if(this.modecounter <= 0)
            {
                this.modecounter = 50+this.rand.nextInt(100);
                this.submode++;
                this.submode %= 4;
            }
            else
                this.modecounter--;
            switch(submode)
            {
                default:
                case 0:
                    setDefaultMode();
                    break;
                case 1:
                    setHelicopterMode();
                    break;
                case 2:
                    setMotorCycleMode();
                    break;
                case 3:
                    setFirstPersonMode();
                    break;
            }
        }
        else
        {
            setDefaultMode();
        }
        this.main.glu().gluLookAt(eye.x(), eye.y(), eye.z(), 
                center.x(), center.y(), center.z(), 
                up.x(), up.y(), up.z());
        this.newstate = false;
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the
     * camera's default mode.
     */
    private void setDefaultMode()
    {
        // Short references
        GlobalState gs = this.main.gs();
        GL2 gl = this.main.gl();
        
        // get the coordinates of the camera
        double camera_X = gs.vDist * (Math.sin(gs.theta)) * Math.cos(gs.phi); 
        double camera_Y = gs.vDist * (Math.cos(gs.theta)) * Math.cos(gs.phi);       
        double camera_Z = gs.vDist * Math.sin(gs.phi);     
        
        //sets he light source to the camera point of vew, but a little bit shifted uppwards and to the left 
        float[] cameraLightPos  = {(float)camera_X+2.5f,(float)camera_Y,(float)camera_Z+3f, 1.0f};
        gl.glLightfv( GL_LIGHT0, GL_POSITION, cameraLightPos,0); 
        
        this.eye = new Vector(gs.vDist * (Math.sin(gs.theta)) * Math.cos(gs.phi) , 
                gs.vDist * (Math.cos(gs.theta)) * Math.cos(gs.phi),
                gs.vDist * Math.sin(gs.phi));
        this.center = Vector.O;
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the
     * helicopter mode.
     */
    private void setHelicopterMode()
    {
        Robot r = this.main.getRobots()[1];
        // Since no robot walks in the center of the track
        // Take the positions of the two middle robots
        // Take the Vector between them
        // Multiply by 0.5 to get half the Vector (which is in between both Robots)
        // Add it to the one we substracted from, and you have the exact middle
        Vector pos = r.getPosition();
        this.center = pos.subtract(r.getTangent().scale(-0.01));
        this.eye = pos.add(new Vector(0, 0, 10));
        float camera_X = (float)this.eye.x();
        float camera_Y = (float)this.eye.y();
        float camera_Z = (float)this.eye.z();
        float[] cameraLightPos  = {camera_X,camera_Y,camera_Z+5, 1.0f};
        this.main.gl().glLightfv( GL_LIGHT0, GL_POSITION, cameraLightPos,0);
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the
     * motorcycle mode.
     */
    private void setMotorCycleMode()
    {
        if(this.newstate)
        {
            float camera_X = 0;
            float camera_Y = 0;
            float camera_Z = 10;
            float[] cameraLightPos  = {(float)camera_X+2.5f,(float)camera_Y,(float)camera_Z+3f, 1.0f};
            this.main.gl().glLightfv( GL_LIGHT0, GL_POSITION, cameraLightPos,0); 
        }
        Robot r = this.main.getLeadingRobot();
        this.center = r.getPosition().add(new Vector(0,0,1.8));
        Vector right = r.getTangent().normalized().cross(this.up).scale(10);
        this.eye = this.center.add(right);
    }

    /**
     * Computes {@code eye}, {@code center}, and {@code up}, based on the first
     * person mode.
     */
    private void setFirstPersonMode()
    {
        if(this.newstate)
        {
            float camera_X = 0;
            float camera_Y = 0;
            float camera_Z = 10;
            float[] cameraLightPos  = {(float)camera_X+2.5f,(float)camera_Y,(float)camera_Z+3f, 1.0f};
            this.main.gl().glLightfv( GL_LIGHT0, GL_POSITION, cameraLightPos,0); 
        }
        Robot r = this.main.getLastRobot();
        this.eye = r.getPositionWithOffset().add(new Vector(0,0,2.1)).subtract(r.getTangent().normalized().scale(1.2));
        Vector front = r.getTangent().normalized();
        this.center = this.eye.add(front).subtract(new Vector(0,0,0.15));
    }
}
