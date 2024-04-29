package p01start.Geometry;

import java.util.ArrayList;
import java.util.List;

import transforms.*;

public class Quad {
    public List<Vec3D> vertexes = new ArrayList<>();
    public Vec3D translation;
    public Vec3D rotation;
    public int angleOfRotation;
    private Boolean Iscollidable = true;
    public BoundingBox boundingBox;

    private Mat3 rotMat = new Mat3Identity();

    Quad(Vec3D a, Vec3D b, Vec3D c, Vec3D d, Vec3D translation, Boolean IsCollidable, Vec3D rotation, int angle){
        vertexes.add(new Vec3D(a));
        vertexes.add(new Vec3D(b));
        vertexes.add(new Vec3D(c));
        vertexes.add(new Vec3D(d));
        this.translation = new Vec3D(translation);
        this.rotation = new Vec3D(rotation);
        this.angleOfRotation = angle;
        this.Iscollidable = IsCollidable;
        rotMat = calculateTheRotationMatrix(this.rotation,this.angleOfRotation);
        this.boundingBox = calcAABB();
    }

    Quad(boolean flagDefault, Vec3D translation, Boolean IsCollidable, Vec3D rotation, int angle){
        vertexes.add(new Vec3D(-1f, 1f, 0f));
        vertexes.add(new Vec3D(1f, 1f, 0));
        vertexes.add(new Vec3D(1, -1f, 0));
        vertexes.add(new Vec3D(-1f, -1f, 0));
        this.translation = new Vec3D(translation);
        this.rotation = new Vec3D(rotation);
        this.angleOfRotation = angle;
        this.Iscollidable = IsCollidable;
        rotMat = calculateTheRotationMatrix(this.rotation,this.angleOfRotation);
        this.boundingBox = calcAABB();
        System.out.println(boundingBox.toString());
    }

    public Quad() {
    }

    private BoundingBox calcAABB(){
        ArrayList<Vec3D> rotAndTransVertexesForBoundingBox = new ArrayList<>();
        for (Vec3D v:vertexes) {
            rotAndTransVertexesForBoundingBox.add(v.add(translation.mul(rotMat)));
        }
        BoundingBox boundingBox = new BoundingBox(rotAndTransVertexesForBoundingBox);
        return  boundingBox;
    }

    private Mat3 calculateTheRotationMatrix(Vec3D rot, int angle){
        float angleRadians = (float) Math.toRadians(angle);
        System.out.println(rot.toString());
        if(rot.getX() >= 1){
            Mat3RotX rotX = new Mat3RotX(angleRadians);
            Mat3 rotationMatrix = new Mat3(rotX);
            return rotationMatrix;
        }
        else if(rot.getY() >=1){
            Mat3RotY rotY = new Mat3RotY(angleRadians);
            Mat3 rotationMatrix = new Mat3(rotY);
            return rotationMatrix;
        }
        else if(rot.getZ() >= 1){
            transforms.Mat3RotZ rotZ = new Mat3RotZ(angleRadians);
            Mat3 rotationMatrix = new Mat3(rotZ);
            return rotationMatrix;
        }
        return new Mat3Identity();
    }

    public Boolean getIscollidable(){
        return Iscollidable;
    }
}
