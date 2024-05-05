package p01start.Map;

import global.GLCamera;
import p01start.Geometry.BoundingBox;
import p01start.Geometry.Quad;
import p01start.Geometry.Ray;
import transforms.Mat3;
import transforms.Vec3D;

import java.util.*;

public class Enemy{
    private int magazines = 5;
    private int ammo = 10;
    private int health = 100;

    public Obj object;
    public Vec3D pos;
    BoundingBox AABB;

    public Enemy(Obj o, Vec3D pos){
        object=o;
        AABB = new BoundingBox(o.getVertices());
        this.pos = pos;
    }

    public boolean killAndGenerateEnemy(GLCamera cam){
        Ray ray = new Ray(cam.getPosition(), cam.getEyeVector());
        boolean colCheck = AABB.rayIntersectsAABB(ray,AABB);
        if(colCheck){
            this.pos = generateValue();
            List<Vec3D> vertsToTranslate = object.getVertices();
            List<Vec3D> translatedVerts = new ArrayList<>();
            for (Vec3D vert: vertsToTranslate) {
               translatedVerts.add(vert.add(new Vec3D(pos.getX(),pos.getY(),pos.getZ())));
            }
            this.AABB = new BoundingBox(translatedVerts);
            return true;
        }
        return false;
    }

    List<Vec3D> availablePositions = new ArrayList<>(Arrays.asList(
            new Vec3D(-13,0,-13),
            new Vec3D(14,0,14),
            new Vec3D(8,0,6),
            new Vec3D(9,0,-5),
            new Vec3D(5,0,-9),
            new Vec3D(13,0,4),
            new Vec3D(-1,0,3),
            new Vec3D(-11,0,4),
            new Vec3D(0,0,11)
    ));

    private Vec3D generateValue(){
        Random rand = new Random();
        Vec3D generatedPos = availablePositions.get(rand.nextInt(0,availablePositions.size()));
        if(this.pos.eEquals(generatedPos)){
            return generateValue();
        }
        else{
            return generatedPos;
        }
    }

    private class Rotation{
        public Vec3D rotation;
        public int angle;
        public Rotation(Vec3D rot, int angle){
            this.rotation = rot;
            this.angle = angle;
        }
    }
}
