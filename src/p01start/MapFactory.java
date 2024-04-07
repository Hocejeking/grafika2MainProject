package p01start;

import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MapFactory {
    List<Vector3f> wallList = new ArrayList<>();
    private final int MAZE_WIDTH;
    private final int MAZE_HEIGHT;
    private final float WALL_SIZE;

    public MapFactory(int maze_width, int maze_height, float wall_size) {
        MAZE_WIDTH = maze_width;
        MAZE_HEIGHT = maze_height;
        WALL_SIZE = wall_size;
    }

    public List<Vector3f> generateMaze() {
        wallList.add(new Vector3f(-3, 0, -3)); // Room 1
        wallList.add(new Vector3f(3, 0, -3));  // Room 2
        wallList.add(new Vector3f(-3, 0, 3));  // Room 3
        wallList.add(new Vector3f(3, 0, 3));   // Room 4

        // Corridors
        wallList.add(new Vector3f(0, 0, -1));  // Corridor 1
        wallList.add(new Vector3f(0, 0, 1));   // Corridor 2
        wallList.add(new Vector3f(-1, 0, 0));  // Corridor 3
        wallList.add(new Vector3f(1, 0, 0));   // Corridor 4
        return wallList;
    }

    public float getWALL_SIZE(){
        return WALL_SIZE;
    }
}



