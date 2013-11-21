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
    new float[]
    {
        0.8f, 0.8f, 0.8f, 1.0f
    },
    new float[]
    {
        0.0f, 0.0f, 0.0f, 1.0f
    }),
    /**
     * Silver material properties. Modify the default values to make it look
     * like silver.
     */
    SILVER(
    new float[]
    {
        0.8f, 0.8f, 0.8f, 1.0f
    },
    new float[]
    {
        0.0f, 0.0f, 0.0f, 1.0f
    }),
    /**
     * Wood material properties. Modify the default values to make it look like
     * wood.
     */
    WOOD(
    new float[]
    {
        0.8f, 0.8f, 0.8f, 1.0f
    },
    new float[]
    {
        0.0f, 0.0f, 0.0f, 1.0f
    }),
    /**
     * Orange material properties. Modify the default values to make it look
     * like orange.
     */
    ORANGE(
    new float[]
    {
        0.8f, 0.8f, 0.8f, 1.0f
    },
    new float[]
    {
        0.0f, 0.0f, 0.0f, 1.0f
    });
    /**
     * The diffuse RGBA reflectance of the material.
     */
    float[] diffuse;
    /**
     * The specular RGBA reflectance of the material.
     */
    float[] specular;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular)
    {
        this.diffuse = diffuse;
        this.specular = specular;
    }
}
