package com.contato;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class contatos implements ContactListener{
	public static int canJump = 0;
	public static int bigJump = 0;
	public static int perderVida = 0;
	private Array<Body> removedBodies;
	public static int direita = 0;
	public static int esquerda = 0;
	
	//é chamado quando 2 fixtures colidem
	
	public contatos()
	{
		super();
		removedBodies = new Array<Body>();
	}
	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		
		//System.out.println("Colisao entre: " + a.getUserData() + " e: " + b.getUserData());
		
		if(a.getUserData() == "ground" && b.getUserData() == "foot")
		{
			canJump++;
		}
		
		else if(a.getUserData() == "foot" && b.getUserData() == "mobhead")
		{
			bigJump++;
			removedBodies.add(b.getBody());
		}
		
		else if(a.getUserData() == "player" && b.getUserData() == "blob")
		{
			if(a.getBody().getPosition().x > b.getBody().getPosition().x){
				direita++;
			}
			else
			{
				esquerda++;
			}
			b.getBody().setLinearVelocity(-b.getBody().getLinearVelocity().x,0);
			perderVida++;
		}
		
		else if(a.getUserData() == "blob" && b.getUserData() == "wall")
		{
			if(a.getBody().getLinearVelocity().x > 0)
			{
				a.getBody().setLinearVelocity(-1f,0);
			}
			else
			{
				a.getBody().setLinearVelocity(1f,0);
			}
		}
		
		
		else if(a.getUserData() == "blob" && b.getUserData() == "blob")
		{
			if(a.getBody().getLinearVelocity().x > 0)
			{
				a.getBody().setLinearVelocity(-1f,0);
			}
			else
			{
				a.getBody().setLinearVelocity(1f,0);
			}
			
			if(b.getBody().getLinearVelocity().x > 0)
			{
				b.getBody().setLinearVelocity(-1f,0);
			}
			else
			{
				b.getBody().setLinearVelocity(1f,0);
			}
		}
		
		else if(a.getUserData() == "ammo" && b.getUserData() == "ground")
		{
			removedBodies.add(a.getBody());
		}
		else if(a.getUserData() == "ground" && b.getUserData() == "ammo")
		{
			removedBodies.add(b.getBody());
		}
		
		else if(a.getUserData() == "blob" && b.getUserData() == "ammo")
		{
			removedBodies.add(a.getBody());
			removedBodies.add(b.getBody());
		}
		else if(a.getUserData() == "ammo" && b.getUserData() == "blob")
		{
			removedBodies.add(a.getBody());
			removedBodies.add(b.getBody());
		}
		
		else if(a.getUserData() == "mobhead" && b.getUserData() == "ammo")
		{
			removedBodies.add(b.getBody());
			removedBodies.add(a.getBody());
		}
	}


	//é chamado quando 2 fixtures deixam de coledir
	@Override
	public void endContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		
		//System.out.println("Fim de colisao entre: " + a.getUserData() + " e: " + b.getUserData());
		
		if(a.getUserData() == "ground" && b.getUserData() == "foot")
		{
			canJump--;
		}
		
		else if(a.getUserData() == "foot" && b.getUserData() == "mobhead")
		{
			bigJump--;
		}
		else if(a.getUserData() == "player" && b.getUserData() == "blob")
		{
			perderVida = 0;
			direita = 0;
			esquerda = 0;
		}
	}

	//quando colidem
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}
	//o que fazer quando colidem
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
	public Array<Body> getRemovedBodies()
	{
		return removedBodies;
	}

}
