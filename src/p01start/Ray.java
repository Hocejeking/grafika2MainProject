package p01start;

import transforms.Vec3D;

public class Ray {
    public Vec3D origin, direction;
    public Ray(Vec3D positionVectorOfTheCamera, Vec3D forwardDirectionVectorFromTheCamera) {
        origin = positionVectorOfTheCamera;
        direction = forwardDirectionVectorFromTheCamera;
    }
}
