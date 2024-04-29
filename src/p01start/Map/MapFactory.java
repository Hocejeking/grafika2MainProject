package p01start.Map;
import p01start.Geometry.AmmoBox;
import p01start.Geometry.Quad;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class MapFactory {

    private final int MAZE_WIDTH;
    private final int MAZE_HEIGHT;
    private final float WALL_SIZE;

    public MapFactory(int maze_width, int maze_height, float wall_size) {
        MAZE_WIDTH = maze_width;
        MAZE_HEIGHT = maze_height;
        WALL_SIZE = wall_size;
    }

    public List<Quad> generateMaze() {
        Quad quad = new Quad(
                true,
                new Vec3D(2,0,0),
                true,
                new Vec3D(0,0,0),
                0
        );

        Quad quad2 = new Quad(
                true,
                new Vec3D(4,0,0),
                true,
                new Vec3D(0,0,0),
                0
        );

        Quad quad3 = new Quad(
                true,
                new Vec3D(6,0,0),
                true,
                new Vec3D(0,0,0),
                0
        );

        Quad quad4 = new Quad(
                true,
                new Vec3D(7,0,1),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad5 = new Quad(
                true,
                new Vec3D(7,0,3),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad6 = new Quad(
                true,
                new Vec3D(7,0,5),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad7 = new Quad(
                true,
                new Vec3D(6,0,6),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad8 = new Quad(
                true,
                new Vec3D(4,0,6),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad9 = new Quad(
                true,
                new Vec3D(2,0,6),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad10 = new Quad(
                true,
                new Vec3D(1,0,5),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad11 = new Quad(
                true,
                new Vec3D(1,0,3),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad12 = new Quad(
                true,
                new Vec3D(0,0,0),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad13 = new Quad(
                true,
                new Vec3D(-2,0,0),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad14 = new Quad(
                true,
                new Vec3D(-4,0,0),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad15 = new Quad(
                true,
                new Vec3D(-7,0,-1),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad16 = new Quad(
                true,
                new Vec3D(0,0,2),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad17 = new Quad(
                true,
                new Vec3D(-2,0,2),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad18 = new Quad(
                true,
                new Vec3D(-4,0,2),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad19 = new Quad(
                true,
                new Vec3D(-7,0,1),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad20 = new Quad(
                true,
                new Vec3D(-7,0,3),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad21 = new Quad(
                true,
                new Vec3D(-7,00,5),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad22 = new Quad(
                true,
                new Vec3D(-5,0,5),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad23 = new Quad(
                true,
                new Vec3D(-4,0,6),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad24 = new Quad(
                true,
                new Vec3D(-2,0,6),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad25 = new Quad(
                true,
                new Vec3D(0,0,6),
                false,
                new Vec3D(0,0,0),
                0
        );

        Quad quad26 = new Quad(
                true,
                new Vec3D(-8,0,-2),
                false,
                new Vec3D(0,0,0),
                0
        );

        ArrayList<Quad> Quads = new ArrayList<>();
        Quads.add(quad);
        Quads.add(quad2);
        Quads.add(quad3);
        Quads.add(quad4);
        Quads.add(quad5);
        Quads.add(quad6);
        Quads.add(quad7);
        Quads.add(quad8);
        Quads.add(quad9);
        Quads.add(quad10);
        Quads.add(quad11);
        Quads.add(quad12);
        Quads.add(quad13);
        Quads.add(quad14);
        Quads.add(quad15);
        Quads.add(quad16);
        Quads.add(quad17);
        Quads.add(quad18);
        Quads.add(quad19);
        Quads.add(quad20);
        Quads.add(quad21);
        Quads.add(quad22);
        Quads.add(quad23);
        Quads.add(quad24);
        Quads.add(quad25);
        Quads.add(quad26);
        return Quads;
    }

    public List<Quad> generateAmmoBoxes(){
        ArrayList<Quad> Quads = new ArrayList<>();
        AmmoBox box = new AmmoBox(new Vec3D(14,0,14), new Vec3D(0,0,0),0);
        Quads.add(box);
        return Quads;
    }

    public List<Quad> generateNewAmmoBox(){
        ArrayList<Quad> Quads = new ArrayList<>();
        AmmoBox box = new AmmoBox(new Vec3D(8,0,8), new Vec3D(0,0,0),0);
        Quads.add(box);
        return Quads;
    }

    public List<Quad> generateDoors(){
        ArrayList<Quad> Quads = new ArrayList<>();
        Quad quad1 = new Quad(
                true,
                new Vec3D(1,0,1),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quad quad2 = new Quad(
                true,
                new Vec3D(-5,0,3),
                false,
                new Vec3D(0,1,0),
                90
        );

        Quads.add(quad1);
        Quads.add(quad2);
        return Quads;
    }

    public List<Quad> generateFloor(){
        ArrayList<Quad> Quads = new ArrayList<>();
        Quad quad1 = new Quad(
                new Vec3D(-15f, 15f, 0f),
                new Vec3D(15f, 15f, 0),
                new Vec3D(15f, -15f, 0),
                new Vec3D(-15f, -15f, 0),
                new Vec3D(0,0,0),
                false,
                new Vec3D(0,0,0),
                0
        );
        Quads.add(quad1);
        return Quads;
    }

}



