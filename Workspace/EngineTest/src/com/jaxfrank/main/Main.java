package com.jaxfrank.main;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.base.engine.MainComponent;
import com.base.engine.input.Input;
import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Transform;
import com.base.engine.rendering.Vertex;
import com.base.engine.rendering.shader.PhongShader;
import com.base.engine.rendering.shader.Shader;
import com.base.engine.util.Window;
import com.base.engine.util.time.Time;
import com.jaxfrank.main.physics.BoxCollider;
import com.jaxfrank.main.voxelEngine.block.Block;
import com.jaxfrank.main.voxelEngine.rendering.WorldRenderer;
import com.jaxfrank.main.voxelEngine.world.World;

public class Main extends MainComponent{
	
	//public static int TABLE_BLOCK_RENDERER_ID;
	//public static int PLANT_RENDERER_ID;
	
	//bottom side side top side side
	public static final Block air = new Block((short)0, -1).setTransparent().setRendered(false).setRendererID(-1);
	public static final Block stone = new Block((short)1, 2);
	public static final Block dirt = new Block((short)2, 3);
	public static final Block grass = new Block((short)3, new int[]{3, 4, 4, 1, 4, 4});
	public static final Block sand = new Block((short)4, 19);
	public static final Block log = new Block((short)5, new int[]{22, 21, 21, 22, 21, 21});
	public static final Block bedRock = new Block((short)6, 18);
	
	//public static final Block enchantingTable = new Block("table", new int[]{184, 183, 183, 167, 183, 183}).setTransparent().setRendererID(1);
	//public static final Block flower = new Block("flower", 13).setTransparent().setRendererID(2);
	public static final Block glass = new Block((short)7, 50).setTransparent();
	
	private Shader shader;
	private Transform transform;
	private Camera camera;
	
	//PointLight pLight1;
	//PointLight pLight2;
	
	//SpotLight sLight1;
	
	public static WorldRenderer worldRenderer;
	
	BoxCollider player;
	BoxCollider b;
	boolean colliding = false;
	
	Vector3f oldPos;
	Vector3f newPos;
	
	public static void main(String[] args){
		Scanner s = new Scanner(System.in);
		System.out.println("Press ENTER to continue!");
		s.nextLine();
		s.close();
		Main.WIDTH = 1280;
		Main.HEIGHT = 720;
		Main.TITLE = "3D Game!";
		Main.FRAME_CAP = 60.0;
		Window.createWindow(WIDTH, HEIGHT, TITLE, false);
		Window.setResizable(true);
		Display.getDesktopDisplayMode();
		new Main().start();
		
	}
	
	@Override
	protected void preInit(){
		World.setInstance(new World(((int)(Math.random() * Integer.MAX_VALUE)) - Integer.MAX_VALUE / 2));
		
		worldRenderer = new WorldRenderer(World.getInstance());
		//TABLE_BLOCK_RENDERER_ID = worldRenderer.addBlockRenderer(new TableRenderer());
		//PLANT_RENDERER_ID = worldRenderer.addBlockRenderer(new PlantRenderer());
	}
	
	@Override
	protected void init(){
		shader = PhongShader.getInstance();
		camera = new Camera();
		transform = new Transform();
		camera.setPos(new Vector3f(0, 0, 0));
		
		worldRenderer.init();
		
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
		Transform.setCamera(camera);
		
		PhongShader.setAmbientLight(new Vector3f(0.5f, 0.5f, 0.5f));
	
		//pLight1 = new PointLight(new BaseLight(new Vector3f(128,128,128), 10f), new Attenuation(0,0,1), new Vector3f(0,128,0f), 1000000000);
		//pLight2 = new PointLight(new BaseLight(new Vector3f(0,1f,1), 10f), new Attenuation(0,0,1), new Vector3f(2,0,7f), 100);
		//PhongShader.setPointLight(new PointLight[]{pLight1, pLight2});
		
		//sLight1 = new SpotLight(new PointLight(new BaseLight(new Vector3f(0,1f,1f), 0.8f), new Attenuation(0,0,0.1f), new Vector3f(-2,0,5f), 30),
		//		  new Vector3f(1,1,1), 0.7f);
		//PhongShader.setSpotLights(new SpotLight[]{sLight1});
		int red = 3;
		int green = 112;
		int blue = 210;
		glClearColor(red/255f,green/255f,blue/255f,1f);
		
		newPos = camera.getPos();
		oldPos = camera.getPos();
		
	}
	
	Vector3f collision = new Vector3f(1,1,1);
	
	@Override
	protected void input(){
		camera.input();
		
		float movAmt = (float)(13 * Time.getDelta());//13
		
		if(Input.getKey(Input.KEY_ESCAPE))
		{
			Input.setCursor(true);
			camera.mouseLocked = false;
		}
		if(Input.getMouseDown(0))
		{
			Input.setMousePosition(camera.centerPosition);
			Input.setCursor(false);
			camera.mouseLocked = true;
		}
		
		newPos = oldPos;
		if(Input.getKey(Input.KEY_SPACE))
			newPos = newPos.add(new Vector3f(0,1,0).mul((float)(movAmt)));
		if(Input.getKey(Input.KEY_LSHIFT))
			newPos = newPos.add(new Vector3f(0,-1,0).mul((float)(movAmt)));
		
		if(Input.getKey(Input.KEY_W))
			newPos = newPos.add(((camera.getForward().mul(new Vector3f(1,0,1)).normalized())).mul(movAmt));
		
		if(Input.getKey(Input.KEY_S))
			newPos = newPos.add(camera.getForward().mul(new Vector3f(1,0,1)).normalized().mul(-movAmt));
		
		if(Input.getKey(Input.KEY_A))
			newPos = newPos.add((camera.getLeft().mul( movAmt)));
		
		if(Input.getKey(Input.KEY_D))
			newPos = newPos.add((camera.getRight().mul(movAmt)));
	}
	
	@Override
	protected void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
			camera.setForward(new Vector3f(0, 0, -1f));
			camera.setUp(new Vector3f(0, 1, 0));
		}
		//sLight1.getPointLight().setPosition(camera.getPos());
		//sLight1.setDirection(camera.getForward());
		
		camera.setPos(newPos);
		oldPos = newPos;
		
		World.getInstance().update(oldPos);
		worldRenderer.updateCamera(camera.getPos(), camera.getForward(), camera.getUp());
		worldRenderer.update();
	}
	
	@Override
	protected void render(){
		Display.setTitle("FPS: " + this.frames);
		
		if(Display.wasResized()) {
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
			
		}
		shader.bind();
		worldRenderer.render(shader, transform);
	}
	
	float temp = 0.0f;
	
	public Vector2f[] generateNGon(Vector2f center, float radius, float angle, int numSides){
		if(numSides <= 0) {
			System.err.println("Really! You can't have zero or negatives sides on a shape!");
			System.exit(-1);
		}
		Vector2f[] verts = new Vector2f[numSides];
		float pi = (float) Math.PI;
		
		for(int i = 0; i < numSides; i++){
			verts[i] = new Vector2f((radius * (float)Math.cos(2*pi*i/numSides + angle) + center.getX()),
					(float)(radius * Math.sin(2*pi*i/numSides + angle) + center.getY()));
		}
		
		return verts;
	}
	
	public Vertex[] vec2fToVert(Vector2f[] vecs){
			Vertex[] verts = new Vertex[vecs.length];
	
			for(int i = 0; i < vecs.length; i++){
				System.out.println("X: " + vecs[i].getX() + " Y: " + vecs[i].getY());
				verts[i] = new Vertex(new Vector3f(vecs[i].getX(), vecs[i].getY(), 0.0f), vecs[i]);
			}
			int numSides = 6;
			
			int[] indices = new int[(numSides - 1) * 3];
			
			for(int i = 1; i < numSides; i+=1){
				if(i*3 >= indices.length) break;
				indices[i*3-1] = 0;
				indices[i*3] = i+1;
				indices[i*3+1] = i;
			}
			return verts;
	}
	
	@Override
	protected void cleanUp(){
		
	}
	
	@SuppressWarnings("unused")
	private void drawCube(Vector3f pos, Vector3f size, Vector3f color){
		glLineWidth(5);
		glColor3f(color.getX(),color.getY(),color.getZ());
		glBegin(GL_LINES);
			glVertex3f(pos.getX()              ,pos.getY()              ,pos.getZ());               glVertex3f(pos.getX()              ,pos.getY() + size.getY(),pos.getZ());
			glVertex3f(pos.getX()              ,pos.getY()              ,pos.getZ());               glVertex3f(pos.getX() + size.getX(),pos.getY()              ,pos.getZ());
			glVertex3f(pos.getX()              ,pos.getY()              ,pos.getZ());               glVertex3f(pos.getX()              ,pos.getY()              ,pos.getZ() + size.getZ());
			glVertex3f(pos.getX() + size.getX(),pos.getY() + size.getY(),pos.getZ() + size.getZ()); glVertex3f(pos.getX()              ,pos.getY() + size.getY(),pos.getZ() + size.getZ());
			glVertex3f(pos.getX() + size.getX(),pos.getY() + size.getY(),pos.getZ() + size.getZ()); glVertex3f(pos.getX() + size.getX(),pos.getY()              ,pos.getZ() + size.getZ());
			glVertex3f(pos.getX() + size.getX(),pos.getY() + size.getY(),pos.getZ() + size.getZ()); glVertex3f(pos.getX() + size.getX(),pos.getY() + size.getY(),pos.getZ());
			glVertex3f(pos.getX() + size.getX(),pos.getY()              ,pos.getZ());               glVertex3f(pos.getX() + size.getX(),pos.getY() + size.getY(),pos.getZ());
			glVertex3f(pos.getX() + size.getX(),pos.getY()              ,pos.getZ());               glVertex3f(pos.getX() + size.getX(),pos.getY()              ,pos.getZ() + size.getZ());
			glVertex3f(pos.getX()              ,pos.getY()              ,pos.getZ() + size.getZ()); glVertex3f(pos.getX()              ,pos.getY() + size.getY(),pos.getZ() + size.getZ());
			glVertex3f(pos.getX()              ,pos.getY()              ,pos.getZ() + size.getZ()); glVertex3f(pos.getX() + size.getX(),pos.getY()              ,pos.getZ() + size.getZ());
			glVertex3f(pos.getX()              ,pos.getY() + size.getY(),pos.getZ());               glVertex3f(pos.getX() + size.getX(),pos.getY() + size.getY(),pos.getZ());
			glVertex3f(pos.getX()              ,pos.getY() + size.getY(),pos.getZ());               glVertex3f(pos.getX()              ,pos.getY() + size.getY(),pos.getZ() + size.getZ());
		glEnd();
	}
	
}
