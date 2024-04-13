package p01start;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3d;
import transforms.Vec3D;

public class Quad {
    public List<Vec3D> vertexes = new ArrayList<>();
    public Vec3D translation;
    public Vec3D rotation;
    public int angleOfRotation;
    private final Boolean Iscollidable;

    Quad(Vec3D a, Vec3D b, Vec3D c, Vec3D d, Vec3D translation, Boolean IsCollidable, Vec3D rotation, int angle){
        vertexes.add(new Vec3D(a));
        vertexes.add(new Vec3D(b));
        vertexes.add(new Vec3D(c));
        vertexes.add(new Vec3D(d));
        this.translation = new Vec3D(translation);
        this.rotation = new Vec3D(rotation);
        this.angleOfRotation = angle;
        this.Iscollidable = IsCollidable;
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
    }

    public Boolean getIscollidable(){
        return Iscollidable;
    }
}
