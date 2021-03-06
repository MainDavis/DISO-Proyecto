package enemigos;

public abstract class Enemigo {
    int HPmax;
    int HP;
    int ATK;
    int MAG;
    int DEF;
    int SPD;

    public Enemigo(int HP, int ATK, int MAG,int DEF, int SPD){
        this.HP = HP;
        this.HPmax = HP;
        this.ATK = ATK;
        this.MAG = MAG;
        this.DEF = DEF;
        this.SPD = SPD;
    }
    
    public boolean muerto() { return (HP > 0) ? false : true; }
    
    public int[] getStats(){
        int[] stats = {HP, ATK, MAG, DEF, SPD, HPmax};
        return stats;
    }

    public void curarse(int cura){
        this.HP += cura;
        if(this.HP > this.HPmax) this.HP = this.HPmax;
    }

    public void recibirDamage(int damage){
        this.HP -= damage;

        if(this.HP < 0) this.HP = 0;
    }

}
