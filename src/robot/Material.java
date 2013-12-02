package robot;


/**
 * Materials that can be used for the robots.
 */
public enum Material
{

    /**
     * Gold material properties. Modify the default values to make it look like
     * gold.
     */
    GOLD(
    new float[] { 0.24725f, 0.2245f, 0.0645f, 1.0f },
    new float[] { 0.34615f, 0.3143f, 0.0903f, 1.0f },
    new float[] { 0.797357f, 0.723991f, 0.208006f, 1.0f},
    83.2f),
    /**
     * Silver material properties. Modify the default values to make it look
     * like silver.
     */
    SILVER(
    new float[] { 0.19225f, 0.19225f, 0.19225f, 1.0f },
    new float[] { 0.50754f, 0.50754f, 0.50754f, 1.0f},
    new float[] {0.508273f, 0.508273f, 0.508273f, 1.0f },
    51.2f),
    /**
     * Wood material properties. Modify the default values to make it look like
     * wood.
     */
    WOOD(
    new float[] {0.0f,0.0f,0.0f,1.0f},
    new float[] {0.4f,0.25f,0.0f,1.0f},
    new float[] {0.1f,0.1f,0.1f,1.0f},
    10.0f),
    /**
     * Orange material properties. Modify the default values to make it look
     * like orange.
     */
    ORANGE(
    new float[] {0.0f,0.0f,0.0f,1.0f},
    new float[] {0.8f,0.2f,0.0f,1.0f},
    new float[] {0.3f,0.23f,0.2f,1.0f},
    32.0f);
    /**
     * The diffuse RGBA reflectance of the material.
     */
    float shine;
    /**
     * The diffuse RGBA reflectance of the material.
     */
    float[] ambient;
    /**
     * The diffuse RGBA reflectance of the material.
     */
    float[] diffuse;
    /**
     * The specular RGBA reflectance of the material.
     */
    float[] specular;

    /**
     * Constructs a new material with ambient, diffuse, specular and shine properties.
     */
    private Material(float[] ambient, float[] diffuse, float[] specular, float shine)
    {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shine = shine;
    }
}
