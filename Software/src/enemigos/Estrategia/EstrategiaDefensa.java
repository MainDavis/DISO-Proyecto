package enemigos.Estrategia;

public class EstrategiaDefensa implements EstrategiaCombate{
    private final int posATK = 7;
 //   private final int posHEAL = 3;

    public boolean sacarAccion(int HP) {
        int rand = (int) (Math.random()*10);
        
        if(rand<=posATK) return true;
        return false;
    }
}
