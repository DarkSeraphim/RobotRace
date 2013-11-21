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
    
    private final RobotHead head;
    private final RobotBody body;
    private final RobotArm leftArm;
    private final RobotArm rightArm;
    private final RobotLeg leftLeg;
    private final RobotLeg rightLeg;
    
    protected final float[] bodyDimensions;
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, float[] bodyDimensions)
    {
        this.material = material;
        this.bodyDimensions = bodyDimensions;
        this.head = new RobotHead(this, new float[]{0,0,this.bodyDimensions[2]/2});
        this.body = new RobotBody(this, this.bodyDimensions);
        this.leftArm = new RobotArm(this, 0.9144F, 0.0635F, true);
        this.rightArm = new RobotArm(this, 0.9144F, 0.0635F, false);
        this.leftLeg = new RobotLeg(this, 0.8128F, this.bodyDimensions[1]/3, true);
        this.rightLeg = new RobotLeg(this, 0.8128F, this.bodyDimensions[1]/3, false);
    }
    
    public float[] getColour()
    {
        return new float[]{0F, 0F, 0F};
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
