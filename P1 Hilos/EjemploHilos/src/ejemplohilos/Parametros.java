/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplohilos;

/**
 *
 * @author Macario
 */

// Almacena el estado del Sprite

public class Parametros
{
	public boolean moving = true; //Solo tiene una variable para decirnos si esta en movimiento o no

	public boolean isMoving() //Regresa un boleano
	{
		return moving; 
	}

	public void stopMoving() //La bandera esta en falso
	{
		moving = false;
	}

	public void startMoving() //La bandera esta en verdadeo 
	{
		moving = true;
	}

}