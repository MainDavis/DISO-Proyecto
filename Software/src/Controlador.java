import java.util.Random;

import Calculadora.CalcularDamage;
import Decorator.EstadisticasBase;
import Decorator.NewEstadisticas;
import Jugador.Jugador;
import enemigos.Enemigo;
import enemigos.EnemyFactory;
import enemigos.Estrategia.*;
import enemigos.Mapa1.EnemigosMapa1;
import enemigos.Mapa2.EnemigosMapa2;
import enemigos.Mapa3.EnemigosMapa3;
import enemigos.Mapa4.EnemigosMapa4;
import graficos.Pantalla;
import template.*;

public class Controlador {
    
    public static void main(String[] args){
        int mapa[] = {0,1,2,3};
        //Creo los enemigos
        EnemyFactory enemyFactory = new EnemigosMapa1(); //Por defecto lo pongo a Mapa1
        Enemigo enemigo;

        CalcularDamage calculadora = CalcularDamage.instance();
        Contexto c = null;
        TemplateMethod template;

        boolean seleccionJugador=false;

        Pantalla pantalla = new Pantalla();
        pantalla.setVisible(true);

        //Pantalla de inicio
        pantalla.pantallaInicio();
        
        //Espero a que pulse
        while(!pantalla.continuar()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        pantalla.pantallaSeleccionDePersonaje();

        //Espero a que seleccione el personaje
        while(!pantalla.continuar()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pantalla.pantallaSeleccionAtributos();

        //Espero a que seleccione los atributos
        while(!pantalla.continuar()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Crear el personaje
        Jugador jugador = new Jugador(pantalla.getNpersonaje());
        //Patrón decorator
        EstadisticasBase estadisticasBase = new EstadisticasBase(pantalla.getStats());
        NewEstadisticas newEstadisticas =  new NewEstadisticas(estadisticasBase);
        jugador.actualizarVida(newEstadisticas.getEstadisticas()[0]);


        //Asigno un recorrido aleatorio del mapa
        Random rand = new Random();
        for(int i = 0; i < mapa.length; ++i) {
            int index = rand.nextInt(mapa.length - i);
            int tmp = mapa[mapa.length - 1 - i];
            mapa[mapa.length - 1 - i] = mapa[index];
            mapa[index] = tmp;
        }

        //Combate
        pantalla.iniciaCombate();

        for(int i=0; i<4 && !jugador.muerto(); i++){

            //Cargo el mapa
            pantalla.cambiarMapa(mapa[i]);
            //Pongo la fabrica
            switch(i){
                case 0:
                    enemyFactory = new EnemigosMapa1();
                    break;
                case 1: 
                    enemyFactory = new EnemigosMapa2();
                    break;
                case 2:
                    enemyFactory = new EnemigosMapa3();
                    break;
                case 3:
                    enemyFactory = new EnemigosMapa4();
                    break;    
            }


            //Duelos

            for(int j=0; j<4; j++){
                System.out.println("NUEVO COMBATE");
                
                switch(j){
                    case 0:
                        enemigo = enemyFactory.crearSkeleton(200, 2, 1, 2, 2);
                        break;
                    case 1:
                        enemigo = enemyFactory.crearChief(250, 5, 5, 5, 5);
                        break;
                    case 2:
                        enemigo = enemyFactory.crearWolf(300, 5, 5, 5, 5);
                        break;
                    case 3:
                        enemigo = enemyFactory.crearRobot(350, 5, 5, 5, 5);
                        break;
                    default:
                        enemigo = null; //para que no salgan errores
                }


                pantalla.cambiarEnemigo(j);

                while(!jugador.muerto() && !enemigo.muerto()){
                    pantalla.actualizarHP(jugador.getHP(), enemigo.getStats()[0]);
                    //Turno jugador
                    seleccionJugador = true;
                    while(seleccionJugador){
                        if(pantalla.getAtacar()){
    
                            int damage = calculadora.calcDamage(newEstadisticas.getEstadisticas(), enemigo.getStats());
                            enemigo.recibirDamage(damage);
    
                            animAttackPJ(pantalla, jugador.getNPJ());
    
                            pantalla.actualizarHP(jugador.getHP(), enemigo.getStats()[0]);
    
                            seleccionJugador = false;
    
                        }else if(pantalla.getCurarse()){
                            
                            curarPJ(jugador, newEstadisticas.getEstadisticas()[2], pantalla);
    
                            pantalla.actualizarHP(jugador.getHP(), enemigo.getStats()[0]);
    
                            seleccionJugador = false;
                        }
    
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                    }
    
                    //Turno enemigo
                    if(!enemigo.muerto()){
                        //Acciones del skeleto
                        
                        //Dependiendo de la vida tomo una estrategia
                        int enemigoHPMax = enemigo.getStats()[5];
                        int enemigoHP = enemigo.getStats()[0];
                        
                        if(enemigoHP == enemigoHPMax) c = new Contexto(new EstrategiaFullAtk());
                        else if( enemigoHP < enemigoHPMax  &&  enemigoHP >= enemigoHPMax/2) c = new Contexto(new EstrategiaAtaque());
                        else if( enemigoHP < enemigoHP/2 && enemigoHP >= enemigoHPMax/4) c = new Contexto(new EstrategiaCura());
                        else c = new Contexto(new EstrategiaFullCura());
                    
                        boolean accion = c.ejecutaEstrategia(); //True: Ataque, False: Cura

                        //Ejecuto un algoritmo dependiendo de la accion que se debe ejecutar

                        if(accion) template = new Atacar();//Ataca
                        else template = new Curar(); //Cura

                        template.turnoEnemigo(jugador, newEstadisticas, enemigo, calculadora, pantalla, j);

                        System.out.println("TURNO ENEMIGO");   
    
                    }
                }

                //Termina el duelo

            }

            newEstadisticas = new NewEstadisticas(newEstadisticas);
            jugador.actualizarVida(newEstadisticas.getEstadisticas()[0]);

 
    }

}

    public static void curarPJ(Jugador jugador, int MAG, Pantalla pantalla){
        int curacion = MAG*7;
        jugador.curarse(curacion);

        pantalla.curarPJ();

        try {
            Thread.sleep(1800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void animAttackPJ(Pantalla pantalla, int nPJ){

        pantalla.animAttackPJ(true);

        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pantalla.impactoEnemigo();

        if(nPJ == 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(nPJ == 1){
            try {
                Thread.sleep(1600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
                        
        pantalla.animAttackPJ(false);
    }

}

