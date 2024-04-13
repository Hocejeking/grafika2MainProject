package p01start;

import com.jme3.math.Vector3f;

public interface Entity {
    int shootAmmo();
    int reload();
    int pickupAmmo();
    int getDamage(int dmg);
}
