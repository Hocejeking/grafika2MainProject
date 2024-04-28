package p01start;

import global.GLCamera;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Enemy{
    private int magazines = 5;
    private int ammo = 10;
    private int health = 100;

    public Obj object;
    public Vec3D pos;
    BoundingBox AABB;

    Enemy(Obj o, Vec3D pos){
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
            new Vec3D(0,0,0),
            new Vec3D(-13,0,13),
            new Vec3D(-13,0,-13)
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
}
