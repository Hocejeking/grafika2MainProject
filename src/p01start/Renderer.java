package p01start;

import global.AbstractRenderer;
import global.GLCamera;
import lwjglutils.OGLTexture2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import transforms.Vec3D;

import java.io.File;
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
    private final float offset = 25f;

    private double ex, ey, ez;
    private float zenit, azimut;
    private int score = 0;
    private float trans, deltaTrans = 0;
    private final MapFactory mapFactory = new MapFactory(100,50,1f);
    private long timer = System.nanoTime() + (15 * 1_000_000_000L);;
    private SkyBox skybox;

    public List<Vec3D> quads = new ArrayList<>();

    private boolean per = true, move = false;
    private static float speed = 0.2f;
    private int cameraMode, lastCameraMode = -1;
    private List<Quad> wallList = new ArrayList<>();
    private List<Quad> floorList = new ArrayList<>();
    private List<Quad> doorList = new ArrayList<>();
    private OGLTexture2D texture;
    private OGLTexture2D gun;
    private OGLTexture2D floor;
    private OGLTexture2D door;
    private OGLTexture2D enemyTex;
    private OGLTexture2D.Viewer textureViewer;
    private OBJLoader objLoader = new OBJLoader();
    private Obj enemyOBJ = new Obj();
    private Enemy enemy;
    private GLCamera camera;
    private boolean debugInfo = false;

    private boolean noTimeLeft = false;

    public Renderer() {
        super();
        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(!noTimeLeft) {
                    switch (key) {
                        case GLFW_KEY_W:
                            if(camera.collidesWithQuad(wallList,camera, GLCamera.CollisionDirection.FORWARD)) {
                                break;
                            }
                            else{
                                camera.forward(speed);
                                break;
                            }
                        case GLFW_KEY_S:
                            if(camera.collidesWithQuad(wallList,camera, GLCamera.CollisionDirection.BACK)) {
                                break;
                            }
                            else{
                                camera.backward(speed);
                                break;
                            }
                        case GLFW_KEY_A:
                            if(camera.collidesWithQuad(wallList,camera, GLCamera.CollisionDirection.LEFT)) {
                                break;
                            }
                            else{
                                camera.left(speed);
                                break;
                            }
                        case GLFW_KEY_D:
                            if(camera.collidesWithQuad(wallList,camera, GLCamera.CollisionDirection.RIGHT)) {
                                break;
                            }
                            else{
                                camera.right(speed);
                                break;
                            }
                    }
                }
                if(key==GLFW_KEY_R && action == GLFW_PRESS && noTimeLeft){
                    restart();
                }
                if (action == GLFW_PRESS && key == GLFW_KEY_F1) {
                    System.out.println("dbg");
                    debugInfo = !debugInfo;
                }
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        };

        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(!noTimeLeft) {
                    DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                    DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                    glfwGetCursorPos(window, xBuffer, yBuffer);
                    double x = xBuffer.get(0);
                    double y = yBuffer.get(0);

                    if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                        if(enemy.killAndGenerateEnemy(camera)){
                            timer = System.nanoTime() + (15 * 1_000_000_000L);
                            score++;
                        }
                        ox = (float) x;
                        oy = (float) y;
                    }
                }
            }

        };

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if(!noTimeLeft) {
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
                    //camera.setZenith(Math.toRadians(zenit));
                    dx = 0;
                    dy = 0;
                }
            }
        };

        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {

            }
        };
    }

    @Override
    public void init() {
        super.init();
        wallList = mapFactory.generateMaze();
        floorList = mapFactory.generateFloor();
        doorList = mapFactory.generateDoors();
        try {
            System.out.println("Loading");
            enemyOBJ = objLoader.loadModel(new File("src/models/swampGhoul.obj"));
            enemy = new Enemy(enemyOBJ, new Vec3D(0,0,0));
        }catch (IOException e){
            System.out.println(e);
        }
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
            floor = new OGLTexture2D("textures/floor.jpg");
            door = new OGLTexture2D("textures/door.jfif");
            enemyTex = new OGLTexture2D("textures/swampGhoul_diffuse.png");
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
        camera.setRadius(0.01f);
        camera.setPosition(new Vec3D(10,0,10));
    }

    private void drawScene() {
        glMatrixMode(GL_TEXTURE);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        enemyTex.bind();
        objLoader.render(enemyOBJ,enemy.pos);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();
        drawMaze(wallList);
        floor.bind();
        drawFloor(floorList);
        door.bind();
        drawDoors(doorList);
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

        glBegin(GL_LINES);
        glVertex3f(-0.1f, 0.0f, 0.0f);
        glVertex3f(0.1f, 0.0f, 0.0f);
        glEnd();

        glBegin(GL_LINES);
        glVertex3f(0.0f, -0.1f, 0.0f);
        glVertex3f(0.0f, 0.1f, 0.0f);
        glEnd();

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

    private void drawDoors(List<Quad> doorPositions){
        for(Quad q : doorPositions){
            glPushMatrix();
            glTranslated(q.translation.getX(),q.translation.getY(),q.translation.getZ());
            glRotated(q.angleOfRotation,q.rotation.getX(),q.rotation.getY(),q.rotation.getZ());
            glBegin(GL_QUADS);
            Vec3D v1 = q.vertexes.get(0);
            Vec3D v2 = q.vertexes.get(1);
            Vec3D v3 = q.vertexes.get(2);
            Vec3D v4 = q.vertexes.get(3);

            glTexCoord2f(0f, 0f);
            glVertex3d(v1.getX(), v1.getY(), v1.getZ());
            glTexCoord2f(1f, 0f);
            glVertex3d(v2.getX(), v2.getY(), v2.getZ());
            glTexCoord2f(1f, 1f);
            glVertex3d(v3.getX(), v3.getY(), v3.getZ());
            glTexCoord2f(0.0f, 1.0f);
            glVertex3d(v4.getX(), v4.getY(), v4.getZ());
            glEnd();
            glPopMatrix();
        }
    }

    private void drawMaze(List<Quad> wallPositions) {
        for (Quad q: wallPositions ) {
            glPushMatrix();
            glTranslated(q.translation.getX(),q.translation.getY(),q.translation.getZ());
            glRotated(q.angleOfRotation, q.rotation.getX(), q.rotation.getY(), q.rotation.getZ());
            glBegin(GL_QUADS);
            Vec3D v1 = q.vertexes.get(0);
            Vec3D v2 = q.vertexes.get(1);
            Vec3D v3 = q.vertexes.get(2);
            Vec3D v4 = q.vertexes.get(3);

            glTexCoord2f(0f, 0f);
            glVertex3d(v1.getX(), v1.getY(), v1.getZ());
            glTexCoord2f(1f, 0f);
            glVertex3d(v2.getX(), v2.getY(), v2.getZ());
            glTexCoord2f(1f, 1f);
            glVertex3d(v3.getX(), v3.getY(), v3.getZ());
            glTexCoord2f(0.0f, 1.0f);
            glVertex3d(v4.getX(), v4.getY(), v4.getZ());
            glEnd();
            glPopMatrix();
        }

    }

    private void drawFloor(List<Quad> floorList){
        glPushMatrix();
        glRotatef(90, 01, 0, 0);
        glTranslatef(0,0,1f);
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_QUADS);
        for (Quad q: floorList ) {
            Vec3D v1 = q.vertexes.get(0);
            Vec3D v2 = q.vertexes.get(1);
            Vec3D v3 = q.vertexes.get(2);
            Vec3D v4 = q.vertexes.get(3);

            glTexCoord2f(0f, 0f);
            glVertex3d(v1.getX(), v1.getY(), v1.getZ());
            glTexCoord2f(1f, 0f);
            glVertex3d(v2.getX(), v2.getY(), v2.getZ());
            glTexCoord2f(1f, 1f);
            glVertex3d(v3.getX(), v3.getY(), v3.getZ());
            glTexCoord2f(0.0f, 1.0f);
            glVertex3d(v4.getX(), v4.getY(), v4.getZ());
        }
        glEnd();
        glPopMatrix();
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
        long currentTime = System.nanoTime();
        long elapsedTime = (currentTime - timer) / 1_000_000_000;
        long timeRemaining = Math.max(0, 30 - elapsedTime);
        long minutes = timeRemaining / 60;
        long seconds = timeRemaining % 60;
        seconds /=  2;
        if(seconds <= 0){
            textRenderer.setScale(3d);
            textRenderer.addStr2D(height/2 + 60,width/2, "YOU LOST!");
            textRenderer.addStr2D((height/2) -25,width/2, "Press R to restart");
            textRenderer.setScale(1d);
            noTimeLeft = true;
        }

        if(debugInfo) {
            textRenderer.addStr2D(3, 20, camera.getPosition().toString());
            textRenderer.addStr2D(3, 40, String.format(" azimuth %3.1f, zenith %3.1f", azimut, zenit));
            textRenderer.addStr2D(3, 60, "Number of quads in the scene: " + (wallList.toArray().length + floorList.toArray().length + doorList.toArray().length));
            textRenderer.addStr2D(3, 80, "Number of triangles in the scene: " + (enemy.object.getFaces().size()));
            textRenderer.addStr2D(3,100, "time: " + String.format("%02d:%02d", minutes,seconds));
            textRenderer.addStr2D(3,120,"YOUR SCORE: " + score);
        }
        else{
            textRenderer.addStr2D(3,20, "time: " + String.format("%02d:%02d", minutes,seconds));
            textRenderer.addStr2D(3,40,"YOUR SCORE: " + score);
        }

        textRenderer.draw();
    }

    private void restart(){
        camera.setPosition(new Vec3D(1,0,1));
        noTimeLeft = false;
        timer = System.nanoTime() + (15 * 1_000_000_000L);
        score = 0;
    }

}
