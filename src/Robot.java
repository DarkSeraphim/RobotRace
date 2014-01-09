import static java.lang.Math.*;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import robotrace.Vector;




/**
 * Represents a Robot, to be implemented according to the Assignments.
 */
public class Robot
{
    
    private static final double DELTA_MOVEMENT = 0.001D;
    private static final Vector normal = new Vector(1, 0, 0);

    /**
     * The material from which this robot is built.
     */
    private final Material material;
    
    /**
     * Parts of a Robot
     */
    protected final RobotHead head;
    protected final RobotBody body;
    private final RobotArm leftArm;
    private final RobotArm rightArm;
    private final RobotLeg leftLeg;
    private final RobotLeg rightLeg;
    
    protected final float[] headDimensions;
    
    protected final float[] origin;
    
    // Speed 1-10 for a normal speed
    private int speed = 1;
    private double time = 0;
    private Vector pos = new Vector(0,0,0);
    private Vector opos = new Vector(0,0,0);
    private Vector tangent = new Vector(0,0,0);
    private double offsetMultiplier;
    private double rot = 0D;
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, float[] headDimensions, double offsetMultiplier)
    {
        this.material = material;
        this.origin = new float[]{0F, 0F, 0F};
        this.headDimensions = headDimensions;
        this.offsetMultiplier = offsetMultiplier;
        this.head = new RobotHead(this);
        this.body = new RobotBody(this);
        this.leftArm = new RobotArm(this, true);
        this.rightArm = new RobotArm(this, false);
        this.leftLeg = new RobotLeg(this, true);
        this.rightLeg = new RobotLeg(this, false);
    }
    
    /**
     * The use of glMaterial disables glColor
     * @deprecated 
     * @return a float[] with respectively the RGB values
     */
    public float[] getColour() //Colors are not used due to the reflective properties of the materials
    {
        return new float[]{0F, 0F, 0F};
    }
    
    /**
     * 
     * @return the {@code Material} of the Robot
     */
    public Material getMaterial()
    {
        return this.material;
    }
    
    /**
     * 
     * @return the origin of the Robot
     */
    public float[] getOrigin()
    {
        return this.origin;
    }
    
    /**
     * 
     * @return the dimensions of the head, respectively x, y and z
     */
    public float[] getHeadDimensions()
    {
        return this.headDimensions;
    }
    
    /**
     * 
     * @return the dimensions of the body, based on the calculations done
     * in RobotBody#recalculate()
     */
    public float[] getBodyDimensions()
    {
        return this.body.getDimensions();
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(boolean stickFigure)
    {
        //giving the refective propperties of the materials to the robots
        RobotPart.gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, material.diffuse,0);
        RobotPart.gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, material.specular,0);
        RobotPart.gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.shine,0);
        RobotPart.gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, material.ambient,0);
        
        // Translation and rotation towards the point
        this.opos = this.pos.add(this.pos.normalized().scale(this.offsetMultiplier));
        RobotPart.gl.glTranslated(this.opos.x(), this.opos.y(), this.opos.z());
        // Rotate around Z-axis to turn the robot towards its moving direction
        RobotPart.gl.glRotated(-this.rot, 0F, 0F, 1F);
        
        this.head.draw(stickFigure);
        this.body.draw(stickFigure);
        this.leftArm.update();
        this.leftArm.draw(stickFigure);
        this.rightArm.update();
        this.rightArm.draw(stickFigure);
        this.leftLeg.draw(stickFigure);
        this.rightLeg.draw(stickFigure);
    }
    
    public double getSpeed()
    {
        return this.speed * DELTA_MOVEMENT;
    }
    public void update(RaceTrack track)
    {
        this.time += getSpeed();
        this.pos = track.getPoint(time%1);
        this.rot = getDegrees(track);
    }
    
    public double getTime()
    {
        return this.time;
    }
    
    private double getDegrees(RaceTrack track)
    {
        this.tangent = track.getTangent(time%1);
        double degrees = acos(this.tangent.normalized().dot(Robot.normal));
        degrees = toDegrees(degrees+PI);
        for(;degrees < 0;)degrees += 360;
        degrees %= 360;
        if(time%1 < 0.25 || time%1 > 0.75)
            degrees *= -1;
        degrees -= 90;
        for(;degrees < 0;)degrees += 360;
        degrees %= 360;
        return degrees;
    }
    
    public Vector getPosition()
    {
        return this.pos;
    }
    
    public Vector getPositionWithOffset()
    {
        return this.opos;
    }
    
    public Vector getTangent()
    {
        return this.tangent;
    }
}