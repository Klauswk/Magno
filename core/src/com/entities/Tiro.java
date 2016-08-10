package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public class Tiro extends Monster {

	public Tiro(Body body) {
		super(body,5);
		Texture tex = new Texture("tiro.png");
		setTexture(tex);
	}

}
