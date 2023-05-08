/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejemplohilos;

import javax.swing.*;

public class Animador extends Thread //Thread es una clase que modela un hilo y tiene un metodo Run
{
	private JLabel et; //Etiqueda donde se almacena la cara del fantasma
	private Parametros  p; //el parametro se encuentra en anime 
	private int width; //movimiento longitu
	private int height; //moviiento altura
        private Animador rebote; //rebote entre fantasmas 
        int direccion; 
        
	public Animador getRebote(){
            return rebote;
        }

        public void  setRebote(Animador rebote){
            this.rebote = rebote;
        }
        
         public JLabel getEt() {
        return et;
    }

    public void setEt(JLabel et) {
        this.et = et;
    }

    public Parametros getP() {
        return p;
    }

    public void setP(Parametros p) {
        this.p = p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
        
        public Animador(JLabel et, Parametros p, ImageIcon face, int w, int h)
	{
            //Se incializa en el constructor
		width = w;
		height= h;
		this.p = p;
		this.et = et;
		et.setIcon(face);
	}

        @Override //setBouns, SetLocation: Establece las coordenadas
	public void run() //Se tiene que sobreescribir el metodo Run
                //No se llama de manera directa, sino lo detecta la VM - permite que los fantasmas se muevan
	{
		    int x = et.getX(); //Coordenadas inciales de los fantasmas 
		    int y = et.getY();
                    int dirX=1; //dirX: Establce el movmiento de los fantasmas = Derecha
                    int dirY=1; //DirY: 

                    while(true) //Entra a ciclo infinito
                    {

			//System.out.println(et.getText()+ " en coordenadas (" + x + "," + y + ")");

			if(p.isMoving()) //Cuadno se crea el objeto con su constructor 
			{ //Si el usuario pone movimiento en falso entonces el if no se cumple y se detienen 
                             if(obtenerOtrasCoordenadas()){      
                        dirX*=-1;
                        System.out.println("La direccion es "+dirX);}
                             else{ //Declaracion de los limites de los fantasmas 
                                if(x > width-et.getWidth()-10 && dirX == 1) //{dirX=-1;}
                                {dirX=-1;}  //cuadno pega a la derecha             
                                if(x < 10 && dirX==-1 ) 
                                {dirX=1;} //Cuando pega a la izquierda
                            
                           //va de 1 en 1
                            if(y > height-et.getHeight() && dirY ==1) {dirY=-1;}//cuando pega abajo
                            if(y < 10 && dirY==-1) {dirY=1;} //cuando pega arriba
//Hasta aqui llega los parametros del movimeinto de los fantasmas
                             }
                            if (dirX == 1 || dirX == -1) dirY = 1;
                            if ((dirY == 1 || dirY == -1)&&(y < 10 && dirY==-1)) dirX = -1;
                          
                            x=x+dirX;//Se incrementan los valores
                            //y=y+dirY;
                            et.setLocation(x,y);

                            try
                            {
				sleep(10);//Se pone a dormir 10milisegundos para que no vaya tan rapido 
                            }
                            catch (Exception ex)
                            {
				ex.printStackTrace();
                            }
			}

                    }
	}
        
        public boolean obtenerOtrasCoordenadas(){
        
        
        if( (rebote.et.getX() == this.et.getX()+et.getWidth()) || 
            (rebote.et.getX()+rebote.et.getWidth() == this.et.getX()) ){
            return true; 
        }
        return false; 
    }
}
