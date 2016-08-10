package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Monster {
	
	protected Body body;
	protected Texture tex;
	protected int width;
	protected int height;
	public static int xp = 0;
	
	public Monster(Body body, int xp)
	{
		this.body = body;
		this.xp = xp;
	}
	
	public Body getBody()
	{
		return body;
	}
	
	public void setTexture(Texture tex)
	{
		this.tex = tex;
	}
	
	public void update(float dt)
	{
		
	}
	
	public void render(SpriteBatch sb)
	{
		sb.draw(tex,body.getPosition().x*100 - tex.getWidth()/2,body.getPosition().y*100 - tex.getHeight()/2);
	}
}
