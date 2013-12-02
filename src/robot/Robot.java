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

        this.head.draw(stickFigure);
        this.body.draw(stickFigure);
        this.leftArm.draw(stickFigure);
        this.rightArm.draw(stickFigure);
        this.leftLeg.draw(stickFigure);
        this.rightLeg.draw(stickFigure);
    }
}
