package p01start;

import transforms.Vec3D;

public class AmmoBox extends Quad{
    public AmmoBox(Vec3D translation, Vec3D rotation, int angleOfRotation){
        // Given face vertices lying in the xy-plane
        Vec3D v1 = new Vec3D(-0.2f, 0.2f, 0f);
        Vec3D v2 = new Vec3D(0.2f, 0.2f, 0f);
        Vec3D v3 = new Vec3D(0.2f, -0.2f, 0f);
        Vec3D v4 = new Vec3D(-0.2f, -0.2f, 0f);

        // Add the original face vertices to the list
        vertexes.add(v1);
        vertexes.add(v2);
        vertexes.add(v3);
        vertexes.add(v4);

        // Generate the remaining vertices of the cube by mirroring and adjusting the z-coordinate
        vertexes.add(new Vec3D(v1.getX(), v1.getY(), 0.4f)); // Front-top-left
        vertexes.add(new Vec3D(v2.getX(), v2.getY(), 0.4f)); // Front-top-right
        vertexes.add(new Vec3D(v3.getX(), v3.getY(), 0.4f)); // Front-bottom-right
        vertexes.add(new Vec3D(v4.getX(), v4.getY(), 0.4f)); // Front-bottom-left

        vertexes.add(new Vec3D(v1.getX(), v1.getY(), -0.4f)); // Back-top-left
        vertexes.add(new Vec3D(v2.getX(), v2.getY(), -0.4f)); // Back-top-right
        vertexes.add(new Vec3D(v3.getX(), v3.getY(), -0.4f)); // Back-bottom-right
        vertexes.add(new Vec3D(v4.getX(), v4.getY(), -0.4f)); // Back-bottom-left

        this.translation = translation;
        this.rotation = rotation;
        this.angleOfRotation = angleOfRotation;
    }
}
