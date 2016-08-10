package com.objetos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class BronzeRock extends Objeto{

	public BronzeRock(Body body) {
		super(body);
		Texture tex = new Texture("objetos.png");
		TextureRegion[][] region= TextureRegion.split(tex, 32, 32);
		setTexture(region[0][0]);
	}
	
	public BronzeRock() {
		super();
		Texture tex = new Texture("objetos.png");
		TextureRegion[][] region= TextureRegion.split(tex, 32, 32);
		setTexture(region[0][0]);
	}

	
	public void update(float dt)
	{
		super.update(dt);
	}
	public void render(SpriteBatch sb)
	{
		super.render(sb);
	}

}
