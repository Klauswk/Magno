package com.objetos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Objeto {
	
	protected Body body;
	protected Texture tex;
	protected TextureRegion region1;
	protected int width;
	protected int height;
	
	public Objeto(Body body)
	{
		this.body = body;
		Texture tex = new Texture("objetos.png");
		TextureRegion[][] region= TextureRegion.split(tex, 32, 32);
		setTexture(region[0][0]);
	}
	public Objeto()
	{

	}
	
	public Body getBody()
	{
		return body;
	}
	public void setBody(Body body)
	{
		this.body = body;
	}
	
	public void setTexture(TextureRegion tex)
	{
		this.region1 = tex;
	}
	
	public void update(float dt)
	{
		
	}
	
	public void render(SpriteBatch sb)
	{
		sb.draw(region1,body.getPosition().x*100 - region1.getRegionWidth()/2,body.getPosition().y*100 - region1.getRegionHeight()/2);
	}
}
