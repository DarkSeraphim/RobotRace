package robot;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;




/**
 * Represents a Robot, to be implemented according to the Assignments.
 */
public class Robot
{

    /**
     * The material from which this robot is built.
     */
    private final Material material;
    
    protected final RobotHead head;
    protected final RobotBody body;
    private final RobotArm leftArm;
    private final RobotArm rightArm;
    private final RobotLeg leftLeg;
    private final RobotLeg rightLeg;
    
    protected final float[] headDimensions;
    
    protected final float[] origin;
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, float[] headDimensions)
    {
        this.material = material;
        this.origin = new float[]{0F, 0F, 0F};
        this.headDimensions = headDimensions;
        this.head = new RobotHead(this);
        this.body = new RobotBody(this);
        this.leftArm = new RobotArm(this, true);
        this.rightArm = new RobotArm(this, false);
        this.leftLeg = new RobotLeg(this, true);
        this.rightLeg = new RobotLeg(this, false);
    }
    
    public float[] getColour()
    {
        return new float[]{0F, 0F, 0F};
    }
    
    public float[] getOrigin()
    {
        return this.origin;
    }
    
    public float[] getHeadDimensions()
    {
        return this.headDimensions;
    }
    
    public float[] getBodyDimensions()
    {
        return this.body.getDimensions();
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(boolean stickFigure)
    {
        this.head.draw(stickFigure);
        this.body.draw(stickFigure);
        this.leftArm.draw(stickFigure);
        this.rightArm.draw(stickFigure);
        this.leftLeg.draw(stickFigure);
        this.rightLeg.draw(stickFigure);
        // code goes here ...
    }
}
