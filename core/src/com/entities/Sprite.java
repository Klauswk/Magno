package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Sprite {

	protected Body body;
	protected Texture tex;
	protected TextureRegion[][] region;
	protected int width;
	protected int height;
	protected int framex;
	public int framey = 0;
	private float timerFrame = .25f;

	public Sprite(Body body)
	{
		this.body = body;
	}

	public Body getBody()
	{
		return body;
	}

	public void setTexture(Texture tex)
	{
		this.tex = tex;
	}

	public void setTexture(TextureRegion[][] tex)
	{
		this.region = tex;
		width = region[0][0].getRegionWidth();
		height = region[0][0].getRegionHeight();
		framex = 0;
		framey = 0;
	}

	public void update(float dt)
	{
		timerFrame = timerFrame - (1*dt);
		if(timerFrame <= 0)
		{
			timerFrame = .25f;
			if(!(framex > 3))
			{
				framex++;
			}
			else
			{
				framex = 0;
			}
		}
	}

	public void render(SpriteBatch sb)
	{
		sb.draw(region[framey][framex],body.getPosition().x*100 - width/2,body.getPosition().y*100 - height/2);
	}
}
