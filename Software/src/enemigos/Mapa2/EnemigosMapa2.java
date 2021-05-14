package enemigos.Mapa2;

import enemigos.EnemyFactory;
import enemigos.Skeleton;
import enemigos.Chief;
import enemigos.Wolf;
import enemigos.Robot;

public class EnemigosMapa2 implements EnemyFactory{
    public Skeleton crearSkeleton(int HP, int ATK, int DEF, int SPD) {
        return new SkeletonMapa2(HP, ATK, DEF, SPD);
    }

    public Chief crearChief(int HP, int ATK, int DEF, int SPD) {
        return new ChiefMapa2(HP, ATK, DEF, SPD);
    }

    public Wolf crearWolf(int HP, int ATK, int DEF, int SPD) {
        return new WolfMapa2(HP, ATK, DEF, SPD);
    }

    public Robot crearRobot(int HP, int ATK, int DEF, int SPD) {
        return new RobotMapa2(HP, ATK, DEF, SPD);
    }
}
