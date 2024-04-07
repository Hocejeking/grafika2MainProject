package p01start;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.Box2dShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.jme3.system.NativeLibraryLoader;
import global.AbstractRenderer;
import global.GLCamera;
import lwjglutils.OGLTexture2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static global.GluUtils.gluPerspective;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * Shows possibilities of camera definition and application
 *
 * @author PGRF FIM UHK
 * @version 3.1
 * @since 2020-01-20
 */
public class Renderer extends AbstractRenderer {
    private float dx, dy, ox, oy;
    private PhysicsSpace physicsSpace;
    private final float offset = 25f;

    private double ex, ey, ez;
    private float zenit, azimut;

    private float trans, deltaTrans = 0;
    private final MapFactory mapFactory = new MapFactory(100,50,1f);
    private SkyBox skybox;

    private boolean per = true, move = false;
    private static float speed = 0.05f;
    private int cameraMode, lastCameraMode = -1;
    private List<Vector3f> wallList = new ArrayList<>();

    private OGLTexture2D texture;
    private OGLTexture2D gun;
    private OGLTexture2D.Viewer textureViewer;
    private GLCamera camera;

    public Renderer() {
        super();
        /*used default glfwWindowSizeCallback see AbstractRenderer*/
        NativeLibraryLoader.loadLibbulletjme(true, null, "Debug", "Sp");

        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    // We will detect this in our rendering loop
                    glfwSetWindowShouldClose(window, true);
                if (action == GLFW_RELEASE) {
                    trans = 0;
                    deltaTrans = 0;
                }

                if (action == GLFW_PRESS) {
                    switch (key) {
                        case GLFW_KEY_P:
                            per = !per;
                            break;
                        case GLFW_KEY_M:
                            move = !move;
                            break;
                        case GLFW_KEY_C:
                            cameraMode++;
                            break;
                        case GLFW_KEY_LEFT_SHIFT:
                        case GLFW_KEY_LEFT_CONTROL:
                        case GLFW_KEY_W:
                        case GLFW_KEY_S:
                        case GLFW_KEY_A:
                        case GLFW_KEY_D:
                            deltaTrans = 0.001f;
                            break;
                    }
                }
                switch (key) {
                    case GLFW_KEY_W:
                        camera.forward(speed);
                        break;
                    case GLFW_KEY_S:
                        camera.backward(speed);
                        break;
                    case GLFW_KEY_A:
                        camera.left(speed);
                        break;
                    case GLFW_KEY_D:
                        camera.right(speed);
                        break;
                }

            }
        };

        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long window, int button, int action, int mods) {
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                double x = xBuffer.get(0);
                double y = yBuffer.get(0);


                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    ox = (float) x;
                    oy = (float) y;
                }
            }

        };

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    dx = (float) x - ox;
                    dy = (float) y - oy;
                    ox = (float) x;
                    oy = (float) y;
                    zenit -= dy / width * 180;
                    if (zenit > 90)
                        zenit = 90;
                    if (zenit <= -90)
                        zenit = -90;
                    azimut += dx / height * 180;
                    azimut = azimut % 360;
                    camera.setAzimuth(Math.toRadians(azimut));
                    camera.setZenith(Math.toRadians(zenit));
                    dx = 0;
                    dy = 0;
            }
        };

        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                //do nothing
            }
        };
    }

    @Override
    public void init() {
        super.init();
        wallList = mapFactory.generateMaze();
        physicsSpace = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        textureViewer = new OGLTexture2D.Viewer();
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glFrontFace(GL_CW);
        glPolygonMode(GL_FRONT, GL_FILL);
        glPolygonMode(GL_BACK, GL_FILL);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        System.out.println("Loading texture...");
        try {
            OGLTexture2D[] textureCube = new OGLTexture2D[6];
            texture = new OGLTexture2D("textures/wall.jpg");
            gun = new OGLTexture2D("textures/gun.gif");
            textureCube[0] = new OGLTexture2D("textures/skybox/yellowcloud_ft.jpg");
            textureCube[2] = new OGLTexture2D("textures/skybox/yellowcloud_bk.jpg");
            textureCube[4] = new OGLTexture2D("textures/skybox/yellowcloud_up.jpg");
            textureCube[5] = new OGLTexture2D("textures/skybox/yellowcloud_dn.jpg");
            textureCube[3] = new OGLTexture2D("textures/skybox/yellowcloud_rt.jpg");
            textureCube[1] = new OGLTexture2D("textures/skybox/yellowcloud_lf.jpg");
            skybox = new SkyBox(textureCube);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Loading texture failed");
        }
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        camera = new GLCamera();
    }

    private void drawScene() {
        glMatrixMode(GL_TEXTURE);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        glBegin(GL_QUADS);
        drawMaze(wallList);
        glEnd();
        drawSkyBox();
        drawGun();
    }

    private void drawSkyBox(){
        glEnable(GL_TEXTURE_2D);
        glActiveTexture(GL_TEXTURE0);
        skybox.textureCube[1].bind();
        glBegin(GL_QUADS);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(-100.0f, -100.0f, -100.0f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(-100.0f,  100.0f, -100.0f);
            glTexCoord2f(1.0f, 0.0f); glVertex3f( 100.0f,  100.0f, -100.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f( 100.0f, -100.0f, -100.0f);
        glEnd();

        skybox.textureCube[3].bind();
        glBegin(GL_QUADS);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(-100.0f, -100.0f, 100.0f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(-100.0f,  100.0f, 100.0f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f( 100.0f,  100.0f, 100.0f);
            glTexCoord2f(1.0f, 0.0f); glVertex3f( 100.0f, -100.0f, 100.0f);
        glEnd();

        skybox.textureCube[4].bind();
        glBegin(GL_QUADS);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(-100.0f, 100.0f, -100.0f);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(-100.0f, 100.0f,  100.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f( 100.0f, 100.0f,  100.0f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f( 100.0f, 100.0f, -100.0f);
        glEnd();

        skybox.textureCube[0].bind();
        glBegin(GL_QUADS);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(-100.0f, -100.0f, -100.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(-100.0f, -100.0f,  100.0f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f( 100.0f, -100.0f,  100.0f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f( 100.0f, -100.0f, -100.0f);
        glEnd();

        skybox.textureCube[2].bind();
        glBegin(GL_QUADS);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(-100.0f, -100.0f,  100.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(-100.0f,  100.0f,  100.0f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(-100.0f,  100.0f, -100.0f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(-100.0f, -100.0f, -100.0f);
        glEnd();

        skybox.textureCube[5].bind();
        glBegin(GL_QUADS);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(100.0f, -100.0f, -100.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(100.0f,  100.0f, -100.0f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(100.0f,  100.0f,  100.0f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(100.0f, -100.0f,  100.0f);
        glEnd();
    }

    private void drawGun(){
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0, width, 0, height, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        gun.bind();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);

        int gunWidth = 150;
        int gunHeight = 150;
        int gunX = (width - gunWidth) / 2;
        int gunY = 0;

        glTexCoord2f(0, 1); glVertex2f(gunX, gunY);
        glTexCoord2f(0, 0); glVertex2f(gunX, gunY + gunHeight*2);
        glTexCoord2f(1, 0); glVertex2f(gunX + gunWidth * 2, gunY + gunHeight *2);
        glTexCoord2f(1, 1); glVertex2f(gunX + gunWidth*2, gunY);

        glEnd();
        glEnable(GL_DEPTH_TEST);
    }

    private void drawMaze(List<Vector3f> wallPositions) {
        glColor3f(1.0f, 1.0f, 1.0f);
        for (Vector3f position : wallPositions) {
            // Draw a quad at each wall position
            glTexCoord2f(0.0f, 0.0f);
            glVertex3f(position.x, position.y, position.z);
            glTexCoord2f(1.0f, 0.0f);
            glVertex3f(position.x + mapFactory.getWALL_SIZE(), position.y, position.z);
            glTexCoord2f(1.0f, 1.0f);
            glVertex3f(position.x + mapFactory.getWALL_SIZE(), position.y + mapFactory.getWALL_SIZE(), position.z);
            glTexCoord2f(0.0f, 1.0f);
            glVertex3f(position.x, position.y + mapFactory.getWALL_SIZE(), position.z);
        }
    }


    @Override
    public void display() {
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

        trans += deltaTrans;

        double a_rad = azimut * Math.PI / 180;
        double z_rad = zenit * Math.PI / 180;
        ex = Math.sin(a_rad) * Math.cos(z_rad);
        ey = Math.sin(z_rad);
        ez = -Math.cos(a_rad) * Math.cos(z_rad);
        double ux = Math.sin(a_rad) * Math.cos(z_rad + Math.PI / 2);
        double uy = Math.sin(z_rad + Math.PI / 2);
        double uz = -Math.cos(a_rad) * Math.cos(z_rad + Math.PI / 2);


        glMatrixMode(GL_MODELVIEW);

        glLoadIdentity();
        camera.setFirstPerson(true);
        camera.setMatrix();


        lastCameraMode = cameraMode;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        gluPerspective(45, width / (float) height, 0.1f, 200.0f);

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        drawScene();
        glPopMatrix();
    }

}
