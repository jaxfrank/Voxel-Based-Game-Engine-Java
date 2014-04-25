package com.base.engine;

import com.base.engine.input.Input;
import com.base.engine.rendering.util.RenderUtil;
import com.base.engine.util.Window;
import com.base.engine.util.time.Time;

public class MainComponent 
{
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	public static String TITLE = "Window Title";
	public static double FRAME_CAP = 5000.0;
	
	protected boolean isRunning;
	
	protected int frames = 0;
	
	public MainComponent()
	{
		System.out.println("GL Version: " + RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
		isRunning = false;
		preInit();
		init();
	}
	
	public void start()
	{
		if(isRunning)
			return;
		
		run();
	}
	
	public void stop()
	{
		if(!isRunning)
			return;
		
		isRunning = false;
	}
	
	protected void run()
	{
		isRunning = true;
		
		int frames = 0;
		long frameCounter = 0;
		
		final double frameTime = 1.0 / FRAME_CAP;
		
		long lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while(isRunning)
		{
			boolean render = false;
			
			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime / (double)Time.SECOND;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime)
			{
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(Window.isCloseRequested())
					stop();
				
				Time.setDelta(frameTime);
				
				input();
				Input.update();
				
				update();
				
				if(frameCounter >= Time.SECOND)
				{
					this.frames = frames;
					frames = 0;
					frameCounter = 0;
				}
			}
			if(render)
			{
				beginFrame();
				render();
				endFrame();
				frames++;
			}
			else
			{
				try 
				{
					Thread.sleep(1);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		cleanUp();
		cleanUpWindow();
	}
	
	protected void preInit(){
		
	}
	
	protected void init(){
		
	}
	
	protected void input(){
		
	}
	
	protected void update(){
	
	}
	
	private void beginFrame(){
		RenderUtil.clearScreen();
	}
	
	protected void render(){
		
	}
	
	private void endFrame(){
		Window.render();
	}
	
	protected void cleanUp(){
	
	}
	
	private void cleanUpWindow() {
		Window.dispose();
	}
	
	public static void createWindow(boolean fullScreen) {
		Window.createWindow(WIDTH, HEIGHT, TITLE, fullScreen);
	}
	
	@Deprecated
	public static void main(String[] args)
	{
		Window.createWindow(WIDTH, HEIGHT, TITLE);
		
		MainComponent game = new MainComponent();
		
		game.start();
	}
}
