package p01start;

import java.io.*;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;
import transforms.Vec2D;
import transforms.Vec3D;

import static org.lwjgl.opengl.GL11.*;

public class OBJLoader extends Object {

    public OBJLoader() {
        super();
    }

    public void render(Obj model, Vec3D enemyPos) {
        glPushMatrix();
        GL11.glMaterialf(GL_FRONT, GL_SHININESS, 120);
        GL11.glTranslated(enemyPos.getX(),enemyPos.getY(),enemyPos.getZ());
        GL11.glBegin(GL_TRIANGLES);
        {
            for (Obj.Face face : model.getFaces()) {
                Vec3D[] normals = {
                        model.getNormals().get(face.getNormals()[0] - 1),
                        model.getNormals().get(face.getNormals()[1] - 1),
                        model.getNormals().get(face.getNormals()[2] - 1)
                };
                Vec2D[] texCoords = {
                        model.getTextureCoordinates().get(face.getTextureCoords()[0] - 1),
                        model.getTextureCoordinates().get(face.getTextureCoords()[1] - 1),
                        model.getTextureCoordinates().get(face.getTextureCoords()[2] - 1)
                };
                Vec3D[] vertices = {
                        model.getVertices().get(face.getVertices()[0] - 1),
                        model.getVertices().get(face.getVertices()[1] - 1),
                        model.getVertices().get(face.getVertices()[2] - 1)
                };
                    GL11.glNormal3d(normals[0].getX(), normals[0].getY(), normals[0].getZ());
                    GL11.glTexCoord2d(texCoords[0].getX(), texCoords[0].getY());
                    GL11.glVertex3d(vertices[0].getX(), vertices[0].getY(), vertices[0].getZ());
                    GL11.glNormal3d(normals[1].getX(), normals[1].getY(), normals[1].getZ());
                    GL11.glTexCoord2d(texCoords[1].getX(),  texCoords[1].getY());
                    GL11.glVertex3d(vertices[1].getX(), vertices[1].getY(), vertices[1].getZ());
                    GL11.glNormal3d(normals[2].getX(), normals[2].getY(), normals[2].getZ());
                    GL11.glTexCoord2d( texCoords[2].getX(), texCoords[2].getY());
                    GL11.glVertex3d(vertices[2].getX(), vertices[2].getY(), vertices[2].getZ());

            }
        }
        GL11.glEnd();
        glPopMatrix();
    }

    public Obj loadModel(File file) throws FileNotFoundException {
        return this.loadModel(new Scanner(file));
    }
    public Obj loadModel(Scanner sc) {
        Obj model = new Obj();
        while (sc.hasNextLine()) {
            String ln = sc.nextLine();
            if (ln == null || ln.equals("") || ln.startsWith("#")) {
            } else {
                String[] split = ln.split("\\s+");
                switch (split[0]) {
                    case "v":
                        model.getVertices().add(new Vec3D(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        ));
                        break;
                    case "vn":
                        model.getNormals().add(new Vec3D(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2]),
                                Float.parseFloat(split[3])
                        ));
                        break;
                    case "vt":
                        model.getTextureCoordinates().add(new Vec2D(
                                Float.parseFloat(split[1]),
                                Float.parseFloat(split[2])
                        ));
                        break;
                    case "f":
                        if (split.length == 4) {
                            model.getFaces().add(new Obj.Face(
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[0]),
                                            Integer.parseInt(split[2].split("/")[0]),
                                            Integer.parseInt(split[3].split("/")[0])
                                    },
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[1]),
                                            Integer.parseInt(split[2].split("/")[1]),
                                            Integer.parseInt(split[3].split("/")[1])
                                    },
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[2]),
                                            Integer.parseInt(split[2].split("/")[2]),
                                            Integer.parseInt(split[3].split("/")[2])
                                    }
                            ));
                        } else if (split.length == 5) {
                            model.getFaces().add(new Obj.Face(
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[0]),
                                            Integer.parseInt(split[2].split("/")[0]),
                                            Integer.parseInt(split[3].split("/")[0])
                                    },
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[1]),
                                            Integer.parseInt(split[2].split("/")[1]),
                                            Integer.parseInt(split[3].split("/")[1])
                                    },
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[2]),
                                            Integer.parseInt(split[2].split("/")[2]),
                                            Integer.parseInt(split[3].split("/")[2])
                                    }
                            ));
                            model.getFaces().add(new Obj.Face(
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[0]),
                                            Integer.parseInt(split[3].split("/")[0]),
                                            Integer.parseInt(split[4].split("/")[0])
                                    },
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[1]),
                                            Integer.parseInt(split[3].split("/")[1]),
                                            Integer.parseInt(split[4].split("/")[1])
                                    },
                                    new int[]{
                                            Integer.parseInt(split[1].split("/")[2]),
                                            Integer.parseInt(split[3].split("/")[2]),
                                            Integer.parseInt(split[4].split("/")[2])
                                    }
                            ));
                            break;
                        }
                    case "s":
                        model.setSmoothShadingEnabled(!ln.contains("off"));
                        break;
                    default:
                        System.err.println("[OBJ] Unknown Line: " + ln);
                }
            }
        }
        sc.close();
        return model;
    }
}