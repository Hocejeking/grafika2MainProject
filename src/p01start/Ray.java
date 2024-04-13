package p01start;

import com.jme3.math.Vector3f;
import transforms.Vec3D;

public class Ray {
    public Vec3D origin, direction;
    public Ray(Vec3D positionVectorOfTheCamera, Vec3D forwardDirectionVectorFromTheCamera) {
        origin = positionVectorOfTheCamera;
        direction = forwardDirectionVectorFromTheCamera;
    }
}
