package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public class Blob extends Monster{

	public Blob(Body body, int xp) {
		super(body, xp);

		Texture tex = new Texture("Blob.png");
		setTexture(tex);
	}

	public void update(float dt)
	{
		if(body.getLinearVelocity().x > 0)
		{
			if(!(body.getLinearVelocity().x > 1))
			{
				body.applyForceToCenter(5, 0, true);
			}
		}
		else
		{
			if(body.getLinearVelocity().x < 0)
			{
				if(!(body.getLinearVelocity().x < -1))
				{
					body.applyForceToCenter(-5, 0, true);
				}
			}
		}
	}

}
