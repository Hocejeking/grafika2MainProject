package p01start;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import transforms.Vec2D;
import transforms.Vec3D;

import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;

public class Obj extends Object {

    private final List<Vec3D> vertices;
    private final List<Vec2D> textureCoords;
    private final List<Vec3D> normals;
    private final List<Face> faces;
    private boolean enableSmoothShading;

    public Obj(List<Vec3D> vertices, List<Vec2D> textureCoords,
               List<Vec3D> normals, List<Face> faces, boolean enableSmoothShading) {
        super();

        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.faces = faces;
        this.enableSmoothShading = enableSmoothShading;
    }

    public Obj() {
        this(new ArrayList<Vec3D>(), new ArrayList<Vec2D>(),
                new ArrayList<Vec3D>(), new ArrayList<Face>(), true);
    }

    public void enableStates() {
        if (this.isSmoothShadingEnabled()) {
            GL11.glShadeModel(GL_SMOOTH);
        } else {
            GL11.glShadeModel(GL_FLAT);
        }
    }

    public boolean hasTextureCoordinates() {
        return this.getTextureCoordinates().size() > 0;
    }

    public boolean hasNormals() {
        return this.getNormals().size() > 0;
    }

    public List<Vec3D> getVertices() {
        return this.vertices;
    }

    public List<Vec2D> getTextureCoordinates() {
        return this.textureCoords;
    }

    public List<Vec3D> getNormals() {
        return this.normals;
    }

    public List<Face> getFaces() {
        return this.faces;
    }

    public boolean isSmoothShadingEnabled() {
        return this.enableSmoothShading;
    }

    public void setSmoothShadingEnabled(boolean isSmoothShadingEnabled) {
        this.enableSmoothShading = isSmoothShadingEnabled;
    }

    public static class Face extends Object {

        private final int[] vertexIndices;
        private final int[] normalIndices;
        private final int[] textureCoordinateIndices;

        public boolean hasNormals() {
            return this.normalIndices != null;
        }

        public boolean hasTextureCoords() {
            return this.textureCoordinateIndices != null;
        }

        public int[] getVertices() {
            return this.vertexIndices;
        }

        public int[] getTextureCoords() {
            return this.textureCoordinateIndices;
        }

        public int[] getNormals() {
            return this.normalIndices;
        }

        public Face(int[] vertexIndices, int[] textureCoordinateIndices,
                    int[] normalIndices) {
            super();

            this.vertexIndices = vertexIndices;
            this.normalIndices = normalIndices;
            this.textureCoordinateIndices = textureCoordinateIndices;
        }

        @Override
        public String toString() {
            return String.format("Face[vertexIndices%s normalIndices%s textureCoordinateIndices%s]",
                    Arrays.toString(vertexIndices), Arrays.toString(normalIndices), Arrays.toString(textureCoordinateIndices));
        }
    }
}