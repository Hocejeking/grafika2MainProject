package p01start;

import com.jme3.math.Vector3f;
import transforms.Vec3D;

import java.util.List;

public class BoundingBox {
    Vec3D min;
    Vec3D max;

    BoundingBox(Vec3D min, Vec3D max){
        this.min = min;
        this.max = max;
    }

    BoundingBox(List<Vector3f> vertices){
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE, maxZ = Float.MIN_VALUE;

        for (Vector3f vertex : vertices) {
            minX = Math.min(minX, vertex.x);
            minY = Math.min(minY, vertex.y);
            minZ = Math.min(minZ, vertex.z);
            maxX = Math.max(maxX, vertex.x);
            maxY = Math.max(maxY, vertex.y);
            maxZ = Math.max(maxZ, vertex.z);
        }

         min = new Vec3D(minX, minY, minZ);
         max = new Vec3D(maxX, maxY, maxZ);
    }

    public boolean rayIntersectsAABB(Ray ray, BoundingBox box){
        float tmin = (float) ((box.min.getX() - ray.origin.getX()) / ray.direction.getX());
        float tmax = (float) ((box.max.getX() - ray.origin.getX()) / ray.direction.getX());

        if (tmin > tmax) {
            float temp = tmin;
            tmin = tmax;
            tmax = temp;
        }

        float tymin = (float) ((box.min.getY() - ray.origin.getY()) / ray.direction.getY());
        float tymax = (float) ((box.max.getY() - ray.origin.getY()) / ray.direction.getY());

        if (tymin > tymax) {
            float temp = tymin;
            tymin = tymax;
            tymax = temp;
        }

        if ((tmin > tymax) || (tymin > tmax))
            return false;

        if (tymin > tmin)
            tmin = tymin;

        if (tymax < tmax)
            tmax = tymax;

        float tzmin = (float) ((box.min.getZ() - ray.origin.getZ()) / ray.direction.getZ());
        float tzmax = (float) ((box.max.getZ() - ray.origin.getZ()) / ray.direction.getZ());

        if (tzmin > tzmax) {
            float temp = tzmin;
            tzmin = tzmax;
            tzmax = temp;
        }

        return !((tmin > tzmax) || (tzmin > tmax));
    }

    private static boolean isPointInsideAABB(Vec3D point, BoundingBox box) {
        return point.getX() >= box.min.getX() && point.getX() <= box.max.getX() &&
                point.getY() >= box.min.getY() && point.getY() <= box.max.getY() &&
                point.getZ() >= box.min.getZ() && point.getZ() <= box.max.getZ();
    }
}
