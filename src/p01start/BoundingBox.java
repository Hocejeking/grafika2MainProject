package p01start;
import transforms.Vec3D;

import java.util.List;

public class BoundingBox {
    Vec3D min;
    Vec3D max;

    BoundingBox(Vec3D min, Vec3D max){
        this.min = min;
        this.max = max;
    }

    BoundingBox(List<Vec3D> vertices){
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE, maxZ = Float.MIN_VALUE;

        for (Vec3D vertex : vertices) {
            minX = (float) Math.min(minX, vertex.getX());
            minY = (float) Math.min(minY, vertex.getY());
            minZ = (float) Math.min(minZ, vertex.getZ());
            maxX = (float) Math.max(maxX, vertex.getX());
            maxY = (float) Math.max(maxY, vertex.getY());
            maxZ = (float) Math.max(maxZ, vertex.getZ());
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
