package com.magno;

import java.util.Random;

import screens.BoundedCamera;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.contato.BD2Vars;
import com.contato.contatos;
import com.entities.Blob;
import com.entities.Monster;
import com.entities.Player;
import com.entities.RevoluteJoint;
import com.entities.Tiro;
import com.objetos.BronzeRock;
import com.objetos.Objeto;
import com.objetos.AppleTree;

public class Game extends ApplicationAdapter {

	private Array<Blob> mobList;
	private SpriteBatch batch;
	private BoundedCamera cam;
	private OrthographicCamera hudcam;
	private OrthographicCamera b2dcam;
	private Texture img;
	private Sprite background;
	private World world;
	private Box2DDebugRenderer b2dr;
	private TiledMap tilemap;
	private int tileMapWidth;
	private int tileMapHeight;
	private int tileSize;
	float tilesize ;
	private OrthogonalTiledMapRenderer tmrender;
	Player player;
	private Blob blob;
	private contatos Contatos;
	private TiledMapTileLayer layer;
	private Array<Tiro> bullets;
	private Array<Objeto> objetos;
	private boolean desenhatiles = true;
	private boolean debug = false;
	private long javaHeap;
	private long nativeHeap;
	private ShapeRenderer srender;
	private RayHandler rayHandler;
	RevoluteJoint joint;
	//float contador;
	BitmapFont font;
	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0f,-9.81f),true);
		Contatos = new contatos();
		mobList = new Array<Blob>();
		objetos = new Array<Objeto>();
		world.setContactListener(Contatos);
		b2dr = new Box2DDebugRenderer();
		bullets = new Array<Tiro>();
		hudcam = new OrthographicCamera();
		srender = new ShapeRenderer();
		hudcam.setToOrtho(false, 320, 320);
		srender.setProjectionMatrix(hudcam.combined);
		srender.setAutoShapeType(true);
		img = new Texture("bg.png");
		background = new Sprite(img);
		//////////////
		
		//contador = 60;
		font = new BitmapFont();
		font.setColor(Color.BLACK);

		//////////////

		createMap();
		cam = new BoundedCamera();
		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
		cam.setToOrtho(false,320,320);

		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false,320f/100f,320f/100f);

		createWalls();
		createPlayer();
		createMob();
		try {
			createObjects();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		javaHeap = Gdx.app.getJavaHeap()/1024;
		nativeHeap = Gdx.app.getNativeHeap()/1024;

		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(cam.combined);
		rayHandler.setShadows(true);
		PointLight pl = new PointLight(rayHandler, 50, new Color(.10f,.10f,.1f,1), 30000, 180,720);
		pl.setXray(true);
		//pl.attachToBody(player.getBody(), 0, 0);
	}



	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Gdx.graphics.setTitle("Magno: Heap "  + javaHeap + "kb nativeHeap: " + nativeHeap + "kb " + Gdx.graphics.getDeltaTime());
		Gdx.graphics.setTitle("x: " + cam.position.x + " y: " + cam.position.y);
		update(1/69f);

		if(desenhatiles)
		{
			batch.setProjectionMatrix(hudcam.combined);
			batch.begin();
			background.setScale(2);
			background.draw(batch);
			batch.end();

			tmrender.setView(cam);
			tmrender.render();

			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			for (int i = 0; i < objetos.size; i++) {
				objetos.get(i).render(batch);
			}
			for (int i = 0; i < mobList.size; i++) {
				mobList.get(i).render(batch);
			}

			for (int i = 0; i < bullets.size; i++) {
				bullets.get(i).render(batch);
			}
			player.render(batch);
			batch.end();

			rayHandler.setCombinedMatrix(cam.combined);
			rayHandler.updateAndRender();

			hud();

			batch.setProjectionMatrix(hudcam.combined);
			batch.begin();
			font.draw(batch, player.HP(), 30 , 300);
			font.draw(batch, player.Mana(), 90 , 300);
			font.draw(batch, player.Exp(), 150 , 300);
			batch.end();
		}

		if(debug)
		{
			//b2dcam.position.set(player.getBody().getPosition().x*100,160,0);
			b2dr.render(world,b2dcam.combined);
		}
		b2dcam.update();
		cam.update();



	}

	private void hud()
	{
		//desenha vida
		srender.begin(ShapeType.Line);
		srender.setColor(Color.BLACK);
		srender.box(20, 290, 0, 50, 10, 0);

		srender.set(ShapeType.Filled);
		srender.setColor(Color.RED);
		srender.box(20, 290, 0, 50*((float)player.getHp()/(float)player.getHpmax()), 10, 0);

		//desenha mana
		srender.set(ShapeType.Line);
		srender.setColor(Color.BLACK);
		srender.box(80, 290, 0, 50, 10, 0);

		srender.set(ShapeType.Filled);
		srender.setColor(Color.BLUE);
		srender.box(80, 290, 0, 50*((float)player.getMana()/(float)player.getManamax()), 10, 0);

		//desenha xp
		srender.set(ShapeType.Line);
		srender.setColor(Color.BLACK);
		srender.box(140, 290, 0, 50, 10, 0);

		srender.set(ShapeType.Filled);
		srender.setColor(Color.GREEN);
		srender.box(140, 290, 0, 50*((float)player.getExperience()/(float)player.getExperienceToLvl()), 10, 0);
		srender.end();
	}

	private void inputs()
	{
		if(contatos.bigJump > 0)
		{
			player.getBody().applyForceToCenter(0, 500,true);
		}
		if(contatos.perderVida > 0)
		{
			if(contatos.direita > 0)
			{
				player.getBody().applyForceToCenter(50, 50, true);
			}
			else if(contatos.esquerda > 0)
			{
				player.getBody().applyForceToCenter(-50, 50, true);
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			if(contatos.canJump>0)
			{
				player.getBody().applyForceToCenter(0, 400, true);

			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			if(!(player.getBody().getLinearVelocity().x > 2f))
			{
				player.getBody().applyForceToCenter(5, 0, true);
			}
			player.IncreaseFramex(Gdx.graphics.getDeltaTime());
			player.framey = 0;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			if(!(player.getBody().getLinearVelocity().x < -2f))
			{
				player.getBody().applyForceToCenter(-5, 0, true);
			}
			player.IncreaseFramex(Gdx.graphics.getDeltaTime());
			player.framey = 1;

		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.Z))
		{
			createshoot();
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
		{
			System.exit(0);
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.C))
		{
			player.getBody().applyForceToCenter(new Vector2(50,75), true);
		}		
		else if(Gdx.input.isKeyJustPressed(Input.Keys.A))
		{
			b2dcam.translate(-1, 0);
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.D))
		{
			b2dcam.translate(1, 0);
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.W))
		{
			b2dcam.translate(0, 1);
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.S))
		{
			b2dcam.translate(0, -1);
		}
	}

	private void createMap()
	{
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		tilemap = new TmxMapLoader().load("mapa1.tmx");

		tmrender = new OrthogonalTiledMapRenderer(tilemap);

		layer = (TiledMapTileLayer) tilemap.getLayers().get("tiles");
		tilesize = layer.getTileHeight();

		tileMapWidth = (int) tilemap.getProperties().get("width",Integer.class);
		tileMapHeight = (int) tilemap.getProperties().get("height",Integer.class);
		tileSize = (int)  tilemap.getProperties().get("tilewidth",Integer.class);
		MapLayer layer = tilemap.getLayers().get("chao");

		for(MapObject mo : layer.getObjects())
		{
			ChainShape cs = new ChainShape();
			bdef.type = BodyType.StaticBody;

			Polyline rect = ((PolylineMapObject)mo).getPolyline();
			float x = rect.getScaleX()/100f ;
			float y = rect.getScaleY()/100f ;

			bdef.position.set(x, y);
			Vector2[] v = new Vector2[rect.getTransformedVertices().length/2];

			for (int i = 0 , j = 0; i < rect.getVertices().length; i=i + 2, j ++) {
				v[j] = new Vector2(rect.getTransformedVertices()[i]/100f,rect.getTransformedVertices()[i+1]/100f);

			}
			for (int i = 0; i < v.length; i++) {
			}
			cs.createChain(v);
			fdef.shape = cs;
			fdef.filter.categoryBits = BD2Vars.BitGround;
			fdef.filter.maskBits = -1;

			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("ground");
			cs.dispose();

		}

		//passa por todas as camadas
		/*for(int row = 0 ; row < layer.getHeight() ; row++)
		{
			for(int col = 0 ; col < layer.getWidth(); col++ )
			{
				//primeiro se pega a cela
				Cell cell = layer.getCell(col, row);

				if(cell == null) continue;
				if(cell.getTile() == null) continue;

				//agora se cria o corpo e o fixture

				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f)*tilesize/100f, (row+0.5f)*tilesize/100f);

				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[2];
				//v[0] = new Vector2(-tilesize/2f/100f, -tilesize/2f/100f);
				v[1] = new Vector2(-tilesize/2f/100f, tilesize/2f/100f);
				v[0] = new Vector2(tilesize/2f/100f, tilesize/2f/100f);

				//System.out.print(v[0].toString()+ "\n");
				cs.createChain(v);
				fdef.friction = .1f;
				fdef.shape = cs;
				fdef.filter.categoryBits = BD2Vars.BitGround;
				fdef.filter.maskBits = -1;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef).setUserData("ground");
				cs.dispose();
			}
		}*/
	}

	private void createPlayer()
	{

		Body Player = null;
		MapLayer layer = tilemap.getLayers().get("player");
		PolygonShape shape = new PolygonShape();

		layer.getObjects().get(0);
		BodyDef bdef = new BodyDef();
		for(MapObject mo : layer.getObjects())
		{
			Polyline rect = ((PolylineMapObject)mo).getPolyline();
			float x = rect.getX()/100f;
			float y = rect.getY()/100f;
			bdef.position.set(x , y);

			bdef.type = BodyType.DynamicBody;
			Player = world.createBody(bdef);

			shape.setAsBox(7f/100f, 8f/100f);

			FixtureDef fdef = new FixtureDef();
			fdef.filter.categoryBits = BD2Vars.BitPlayer;
			fdef.filter.maskBits = BD2Vars.BitGround |BD2Vars.BitMonster;
			fdef.shape = shape;
			Player.createFixture(fdef).setUserData("player");


			///Fixture Foot
			shape.setAsBox(6f/100f, 3f/100f, new Vector2(0f,-8f/100f),0);
			fdef.shape = shape;
			fdef.filter.categoryBits = BD2Vars.BitPlayer;
			fdef.filter.maskBits = BD2Vars.BitGround |BD2Vars.BitMonster;
			fdef.shape = shape;
			fdef.isSensor = true;
			Player.createFixture(fdef).setUserData("foot");

			
			
		}
		player = new Player(Player);

		shape.dispose();
	}

	private void createMob()
	{
		MapLayer layer = tilemap.getLayers().get("monstros");

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		Body mobBody;
		PolygonShape shape = new PolygonShape();

		for(MapObject mo : layer.getObjects())
		{
			bdef.type = BodyType.DynamicBody;

			Polyline rect = ((PolylineMapObject)mo).getPolyline();
			float x = rect.getX()/100f;
			float y = rect.getY()/100f;
			bdef.position.set(x, y);
			bdef.linearVelocity.set(.5f,0f);
			mobBody = world.createBody(bdef);

			shape.setAsBox(7f/100f, 8f/100f);
			fdef.shape = shape;
			fdef.filter.categoryBits = BD2Vars.BitMonster;
			fdef.filter.maskBits = -1;
			fdef.shape = shape;

			mobBody.createFixture(fdef).setUserData("blob");

			shape.setAsBox(6.6f/100f, 2f/100f, new Vector2(0f,6f/100f),0);
			fdef.shape = shape;
			fdef.filter.categoryBits = BD2Vars.BitMonster;
			fdef.shape = shape;
			mobBody.createFixture(fdef).setUserData("mobhead");
			blob = new Blob(mobBody, 5);
			mobList.add(blob);
			mobBody.setUserData(blob);
		}
		shape.dispose();
	}

	private void createWalls()
	{
		MapLayer layer = tilemap.getLayers().get("parede");

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		for(MapObject mo : layer.getObjects())
		{
			bdef.type = BodyType.StaticBody;

			Polyline rect = ((PolylineMapObject)mo).getPolyline();
			float x = rect.getX()/100f;
			float y = rect.getY()/100f;
			bdef.position.set(x, y);

			shape.setAsBox(1f/100f, 10f/100f);
			fdef.shape = shape;
			fdef.filter.categoryBits = BD2Vars.BitWall;
			fdef.filter.maskBits = BD2Vars.BitMonster;

			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("wall");

		}

		/*for(MapObject mo : layer.getObjects())
		{
			ChainShape cs = new ChainShape();
			bdef.type = BodyType.StaticBody;

			Polyline rect = ((PolylineMapObject)mo).getPolyline();
			float x = rect.getScaleX()/100f ;
			float y = rect.getScaleY()/100f ;

			System.out.println("mapa x " + x + " mapa y " + y);
			bdef.position.set(x, y);
			Vector2[] v = new Vector2[rect.getTransformedVertices().length/2];

			for (int i = 0 , j = 0; i < rect.getVertices().length; i=i + 2, j ++) {
				v[j] = new Vector2(rect.getTransformedVertices()[i]/100f,rect.getTransformedVertices()[i+1]/100f);

			}
			//System.out.println("Saiu do for");
			for (int i = 0; i < v.length; i++) {
				//System.out.println(v[i].x + " " + v[i].y);
				//System.out.println();
			}
			cs.createChain(v);
			fdef.shape = cs;
			fdef.filter.categoryBits = BD2Vars.BitGround;
			fdef.filter.maskBits = -1;

			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("ground");
			cs.dispose();

		}*/

	}

	private void createObjects() throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		MapLayer layer = tilemap.getLayers().get("objetos");
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		Body corpoObjeto;
		PolygonShape shape = new PolygonShape();

		for(MapObject mo : layer.getObjects())
		{
				bdef.type = BodyType.StaticBody;

				Polyline rect = ((PolylineMapObject)mo).getPolyline();
				float x = rect.getX()/100f;
				float y = rect.getY()/100f;
				bdef.position.set(x, y);
				corpoObjeto = world.createBody(bdef);

				shape.setAsBox(7f/100f, 8f/100f);
				fdef.shape = shape;
				fdef.filter.categoryBits = BD2Vars.BitObjeto;
				fdef.filter.maskBits = -1;
				fdef.shape = shape;

				corpoObjeto.createFixture(fdef).setUserData("Objeto");
				System.out.println("com.objetos."+mo.getName());
				Class a = Class.forName("com.objetos."+mo.getName());
				Objeto ob = (Objeto) a.newInstance();
				ob.setBody(corpoObjeto);
				objetos.add(ob);
				
				/*if(mo.getName().contains("tree"))
				{
				Objeto RedTree = new AppleTree(corpoObjeto);
				objetos.add(RedTree);
				corpoObjeto.setUserData(RedTree);
				}
				else
				{

					Objeto BronzeRock = new BronzeRock(corpoObjeto);
					objetos.add(BronzeRock);
					corpoObjeto.setUserData(BronzeRock);
				}*/
		}
		shape.dispose();
	}

	private void createshoot() {

		if(!player.takeMana(1))
		{
			Body Ammo;

			BodyDef bdef = new BodyDef();
			bdef.position.set(player.getBody().getPosition().x+ 10/100,player.getBody().getPosition().y);
			bdef.type = BodyType.DynamicBody;
			if(player.framey == 0)
			{
				bdef.linearVelocity.set(3f, 0);
			}
			else
			{
				bdef.linearVelocity.set(-3f,0);
			}
			bdef.gravityScale = 0;
			bdef.bullet = true;
			Ammo = world.createBody(bdef);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(5f/100f, 5f/100f);

			FixtureDef fdef = new FixtureDef();
			fdef.filter.categoryBits = BD2Vars.BitTiro;
			fdef.filter.maskBits = BD2Vars.BitGround|BD2Vars.BitMonster|BD2Vars.BitTiro;
			fdef.shape = shape;
			Ammo.createFixture(fdef).setUserData("ammo");

			Tiro bullet = new Tiro(Ammo);
			bullets.add(bullet);
			Ammo.setUserData(bullet);
		}

	}

	private void createLight()
	{
		new PointLight(rayHandler, 5000, Color.RED, 100, player.getBody().getPosition().x*100,player.getBody().getPosition().y*100);
	}

	private void update(float dt)
	{
		world.step(dt, 6, 2);
		for (int i = 0; i < mobList.size; i++) {
			mobList.get(i).update(1/69);
		}

		for (int i = 0; i < bullets.size; i++) {
			bullets.get(i).update(1/69f);
		}

		inputs();

		Array<Body> removes = Contatos.getRemovedBodies();
		for (int i = 0; i < removes.size; i++) {
			Object obj = removes.get(i).getUserData();
			if(obj instanceof Blob)
			{
				System.out.println("Removendo inimigo!");
				Body b = removes.get(i);
				player.increaseXp(Monster.xp);
				if(mobList.removeValue((Blob) b.getUserData(), true))
				{
					System.out.println("Deletou mob");
				}
				world.destroyBody(b);
			}
			if(obj instanceof Tiro)
			{
				System.out.println("Removendo Tiro!");
				Body b = removes.get(i);	
				if(bullets.removeValue((Tiro) b.getUserData(), true))
				{
					System.out.println("Deletou tiro");
				}
				world.destroyBody(b);
			}
		}
		removes.clear();
		Contatos.getRemovedBodies().clear();

		if(contatos.perderVida > 0)
		{
			player.takeDamage(1);
		}
		cam.position.set(player.getBody().getPosition().x*100,player.getBody().getPosition().y*100,0);

		if(cam.position.x < cam.viewportWidth/2)
		{
			cam.position.set(cam.viewportWidth/2, player.getBody().getPosition().y*100, 0);
		}
		else if(cam.position.x + cam.viewportWidth/2 >= tileSize*tileMapWidth)
		{
			cam.position.set(tileSize*tileMapWidth-cam.viewportWidth/2,player.getBody().getPosition().y*100,0);
		}

		cam.update();
		hudcam.update();

		//contador = contador - (1 * Gdx.graphics.getDeltaTime());
		player.update(Gdx.graphics.getDeltaTime());
		javaHeap = Gdx.app.getJavaHeap()/1024;
		nativeHeap = Gdx.app.getNativeHeap()/1024;

	}

	public void dispose()
	{
		batch.dispose();
		img.dispose();
		tilemap.dispose();
		rayHandler.dispose();
	}
}
