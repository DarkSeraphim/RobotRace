
import robot.Material;
import robot.Robot;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import robot.RobotPart;
import robotrace.Base;
import robotrace.GlobalState;

/**
 * Handles all of the RobotRace graphics functionality, which should be extended
 * per the assignment.
 *
 * OpenGL functionality: - Basic commands are called via the gl object; -
 * Utility commands are called via the glu and glut objects;
 *
 * GlobalState: The gs object contains the GlobalState as described in the
 * assignment: - The camera viewpoint angles, phi and theta, are changed
 * interactively by holding the left mouse button and dragging; - The camera
 * view width, vWidth, is changed interactively by holding the right mouse
 * button and dragging upwards or downwards; - The center point can be moved up
 * and down by pressing the 'q' and 'z' keys, forwards and backwards with the
 * 'w' and 's' keys, and left and right with the 'a' and 'd' keys; - Other
 * settings are changed via the menus at the top of the screen.
 *
 * Textures: Place your "track.jpg", "brick.jpg", "head.jpg", and "torso.jpg"
 * files in the same folder as this file. These will then be loaded as the
 * texture objects track, bricks, head, and torso respectively. Be aware, these
 * objects are already defined and cannot be used for other purposes. The
 * texture objects can be used as follows:
 *
 * gl.glColor3f(1f, 1f, 1f); track.bind(gl); gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0); gl.glVertex3d(0, 0, 0); gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0); gl.glTexCoord2d(1, 1); gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1); gl.glVertex3d(0, 1, 0); gl.glEnd();
 *
 * Note that it is hard or impossible to texture objects drawn with GLUT. Either
 * define the primitives of the object yourself (as seen above) or add
 * additional textured primitives to the GLUT object.
 */
public class RobotRace extends Base
{  
    /**
     * Array of the four robots.
     */
    private final Robot[] robots;
    /**
     * Instance of the camera.
     */
    private final Camera camera;
    /**
     * Instance of the race track.
     */
    private final RaceTrack raceTrack;
    /**
     * Instance of the terrain.
     */
    private final Terrain terrain;

    /**
     * Constructs this robot race by initializing robots, camera, track, and
     * terrain.
     */
    public RobotRace()
    {
        float[] headDimensions = new float[]{0.1524F, 0.2413F, 0.2286F};
        
        // Create a new array of four robots
        robots = new Robot[4];

        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD, headDimensions);

        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER, headDimensions);

        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD, headDimensions);

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE, headDimensions);

        // Initialize the camera
        camera = new Camera(this);

        // Initialize the race track
        raceTrack = new RaceTrack();

        // Initialize the terrain
        terrain = new Terrain();
    }
    
    public Robot[] getRobots()
    {
        return this.robots;
    }

    /**
     * Called upon the start of the application. Primarily used to configure
     * OpenGL.
     */
    @Override
    public void initialize()
    {
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL_NORMALIZE);
        
        // Enable anti-aliasing.
        gl.glEnable(GL_LINE_SMOOTH);
        gl.glEnable(GL_POLYGON_SMOOTH);
        gl.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        gl.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        
        //enable lighting
        gl.glEnable(GL_LIGHTING);
        //creating camera lightsource
        gl.glEnable(GL_LIGHT0); 
        float[] globalAmbient  = {0.5f, 0.5f, 0.5f, 1.0f}; 
        gl.glLightModelfv(GL_LIGHT_MODEL_AMBIENT, globalAmbient,0);
        
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);

        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        
        RobotPart.initialize(gl, glu, glut);
    }


    
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView()
    {
        
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);

        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        // Modify this to meet the requirements in the assignment.
        //glu.gluPerspective(40, (float) gs.w / (float) gs.h, 0.1, 100);
        float aspect = (float) (gs.w) / gs.h;
        float fovy = (float) Math.toDegrees(Math.atan((gs.vWidth / 2) / gs.vDist));
        glu.gluPerspective(fovy, aspect, 0.1*(gs.vDist), 10.0*(gs.vDist)); 
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        
        double camera_X = gs.vDist * (Math.sin(gs.theta)) * Math.cos(gs.phi); 
        double camera_Y = gs.vDist * (Math.cos(gs.theta)) * Math.cos(gs.phi);       
        double camera_Z = gs.vDist * Math.sin(gs.phi);     
        
        float[] cameraLightPos  = {(float)camera_X+2.5f,(float)camera_Y,(float)camera_Z+3f, 1.0f};
        gl.glLightfv( GL_LIGHT0, GL_POSITION, cameraLightPos,0); 
        
        // Update the view according to the camera mode
        camera.update(gs.camMode);
       // glu.gluLookAt(camera.eye.x(), camera.eye.y(), camera.eye.z(),
       //         camera.center.x(), camera.center.y(), camera.center.z(),
       //         camera.up.x(), camera.up.y(), camera.up.z());
        glu.gluLookAt(gs.vDist * (Math.sin(gs.theta)) * Math.cos(gs.phi) , 
                gs.vDist * (Math.cos(gs.theta)) * Math.cos(gs.phi),
                gs.vDist * Math.sin(gs.phi),
                gs.cnt.x(), gs.cnt.y(), gs.cnt.z(),
                camera.up.x(), camera.up.y(), camera.up.z());
    }

    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene()
    {
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);

        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);

        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);

        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        // Draw the axis frame
        if (gs.showAxes)
        {
            drawAxisFrame();
        }

        gl.glEnable(GL_LIGHTING);

        // Draw the first robot
        gl.glPushMatrix();
        gl.glTranslatef(1.5f, 0f, 0f);
        robots[0].draw(gs.showStick);
        gl.glPopMatrix();
        // Draw the second robot
        gl.glPushMatrix();        
        gl.glTranslatef(0.5f, 0f, 0f);
        robots[1].draw(gs.showStick);
        gl.glPopMatrix();
        // Draw the third robot
        gl.glPushMatrix();        
        gl.glTranslatef(-0.5f, 0f, 0f);
        robots[2].draw(gs.showStick);
        gl.glPopMatrix();
        // Draw the fourth robot
        gl.glPushMatrix();        
        gl.glTranslatef(-1.5f, 0f, 0f);
        robots[3].draw(gs.showStick);
        gl.glPopMatrix();
        gl.glDisable(GL_LIGHTING);
        
        // Draw race track
        raceTrack.draw(gs.trackNr);

        // Draw terrain
        terrain.draw();

        // Unit box around origin.
        glut.glutWireCube(1f);

        // Move in x-direction.
        gl.glTranslatef(2f, 0f, 0f);

        // Rotate 30 degrees, around z-axis.
        gl.glRotatef(30f, 0f, 0f, 1f);

        // Scale in z-direction.
        gl.glScalef(1f, 1f, 2f);

        // Translated, rotated, scaled box.
        glut.glutWireCube(1f);
    }

    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue), and origin
     * (yellow).
     */
    public void drawAxisFrame()
    {
        gl.glColor3ub((byte)255, (byte)255, (byte)0);
        gl.glScaled(0.1, 0.1, 0.1);
        glut.glutSolidSphere(1, 16, 16);
        gl.glScaled(10, 10, 10);
        // Create an array of unit vectors
        // The unit vectors are defined as an array of ints,
        // respectively x, y and z
        int[][] arrows = new int[][]
        {
            new int[]{1,0,0},
            new int[]{0,1,0},
            new int[]{0,0,1}
        };
        for(int[] vec : arrows)
        {
            // Save the position, rotation and colour (and more)
            gl.glPushMatrix();
                // Set the colour regarding to the vector
                // This seems to be matching the unit vector :3
                gl.glColor3f(vec[0], vec[1], vec[2]);
                // Move the line to draw it only on one side
                gl.glTranslated((double)vec[0]/2, (double)vec[1]/2, (double)vec[2]/2);
                // Scale the line so it has the length in the direction of the
                // unit vector. The rest defaults to 0.01 for visibility.
                gl.glScaled(getSide(vec[0]), getSide(vec[1]), getSide(vec[2]));
                glut.glutSolidCube(1);
                // Scale it back
                gl.glScaled(1/getSide(vec[0]), 1/getSide(vec[1]), 1/getSide(vec[2]));
                // Translate it to 1/4 of the length (including the cone length
                // it will be 1). The -0.1 is to fix the line showing outside the
                // cone (Might be a glut bug/imprecision)
                gl.glTranslated((double)vec[0]/(4-0.1), (double)vec[1]/(4-0.1), (double)vec[2]/(4-0.1));
                // Rotate to point the cone in the right direction. The y-axis
                // needed to be rotated 270 degrees, thus we rotated around
                // around the negative x-axis to compensate, since this would
                // make the rotation clockwise
                gl.glRotatef(90F, -vec[1], vec[0], vec[2]);
                glut.glutSolidCone(0.1, 0.25, 16, 16);
            // load the saved position, rotation and colour (and more)
            gl.glPopMatrix();
        }
        // Reset the colour to black, with alpha 1
        gl.glClearColor(0, 0, 0, 1);
    }
    
    private double getSide(int s)
    {
        return s == 0 ? 0.01 : (double)s;
    }

    /**
     * Main program execution body, delegates to an instance of the RobotRace
     * implementation.
     */
    public static void main(String args[])
    {
        RobotRace robotRace = new RobotRace();
    }
}
