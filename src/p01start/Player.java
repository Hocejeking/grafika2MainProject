package p01start;

public class Player implements Entity {
    private int magazines = 5;
    private int ammo = 10;
    private int health = 100;
    private int armor = 100;

    @Override
    public int reload(){
        magazines -= 1;
        ammo = 10;
        return magazines;
    }
    @Override
    public int shootAmmo(){
        ammo -= 1;
        return ammo;
    }

    @Override
    public int pickupAmmo(){
        magazines += 1;
        return magazines;
    }

    @Override
    public int getDamage(int dmg){
        if(armor <= 0) {
            health -= dmg;
            if (health <= 0) {
                return -1;
            } else {
                return health;
            }
        }
        else{
            armor -= dmg;
            return armor;
        }
    }
}
