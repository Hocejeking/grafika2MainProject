package p01start.Geometry;

import transforms.Vec3D;

import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class AmmoBox extends Quad{
    public AmmoBox(Vec3D translation, Vec3D rotation, int angleOfRotation){
        vertexes.add(new Vec3D(-0.2f, -0.2f, 0.2f));
        this.translation = translation;
        this.rotation = rotation;
        this.angleOfRotation = angleOfRotation;
    }
}
