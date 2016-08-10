package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;


public class Player extends Sprite{
	
	private int Hpmax;
	private int Hp;
	private int Mana;
	private int Manamax;
	private float ManaRegen = 10;
	private float HpRegen = 10;
	private float invisible = 2;
	private int Experience = 0;
	private int ExperienceToLvl = 10;
	private boolean Isinvisible = false;

	public Player(Body body) {
		super(body);
		Hp = 4;
		Hpmax = 4;
		Mana = 5;
		Manamax = 5;
		Texture tex = new Texture("Gug.png");
		TextureRegion[][] region= TextureRegion.split(tex, 16, 16);
		setTexture(region);
	}

	public int getHp()
	{
		return Hp;
	}

	public void setHp(int Hp)
	{
		this.Hp = Hp;
	}

	public boolean takeDamage(int dmg)
	{
		if(!Isinvisible)
		{
			Hp = Hp - dmg;
			if(Hp <= 0)
			{
				Hp = 0;
				return true;
			}
			else
			{
				Isinvisible = true;
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	public int getHpmax() {
		return Hpmax;
	}

	public void setHpmax(int hpmax) {
		Hpmax = hpmax;
	}
	public int getMana() {
		return Mana;
	}

	public void setMana(int mana) {
		Mana = mana;
	}

	public int getManamax() {
		return Manamax;
	}

	public void setManamax(int manamax) {
		Manamax = manamax;
	}

	public int getExperience() {
		return Experience;
	}

	public void setExperience(int experience) {
		Experience = experience;
	}

	public int getExperienceToLvl() {
		return ExperienceToLvl;
	}

	public void setExperienceToLvl(int experienceToLvl) {
		ExperienceToLvl = experienceToLvl;
	}

	public float getHpRegen() {
		return HpRegen;
	}

	public void setHpRegen(float hpRegen) {
		HpRegen = hpRegen;
	}

	public boolean takeMana(int manaTaken)
	{
		if(Mana <= 0)
		{
			Mana = 0;
			return true;
		}
		else
		{
			Mana -= manaTaken;
			return false;
		}
	}

	public String HP()
	{
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append(Hp);
		sb.append("/");
		sb.append(Hpmax);

		return sb.toString();	
	}



	public String Mana()
	{
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append(Mana);
		sb.append("/");
		sb.append(Manamax);

		return sb.toString();	
	}

	public void update(float dt)
	{
		ManaRegen = ManaRegen - (1* dt);
		HpRegen = HpRegen - (1*dt);
		if(ManaRegen < 0)
		{
			ManaRegen = 10;
			if(Mana >= Manamax)
			{

			}
			else
			{
				Mana++;
			}
		}
		if(HpRegen < 0)
		{
			HpRegen = 10;
			if(Hp >= Hpmax)
			{

			}
			else
			{
				Hp++;
			}
		}
		if(Isinvisible)
		{
			invisible = invisible - (1*dt);
			if(invisible < 0)
			{
				Isinvisible = false;
				invisible = 10;
			}
		}
		if(body.getLinearVelocity().y > 5)
		{
			body.setLinearVelocity(0,5);
		}
	}
	
	public void increaseXp(int Exp)
	{
		this.Experience += Exp;
		
		if(Experience >= ExperienceToLvl)
		{
			Experience = 0;
			ExperienceToLvl *= 2;
			Hpmax+=5;
			Manamax+=5;
		}
	}

	public String Exp() {

		StringBuilder sb;
		sb = new StringBuilder();
		sb.append(Experience);
		sb.append("/");
		sb.append(ExperienceToLvl);

		return sb.toString();	
	}
	
	public void IncreaseFramex(float dt)
	{
		super.update(dt);
	}
}
