package com.base.engine.util;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window 
{
	private static int WIDTH = 720, HEIGHT = 480;
	
	public static void createWindow(int width, int height, String title)
	{
		Window.createWindow(width, height, title, false);
	}
	
	public static void createWindow(int width, int height, String title, boolean fullscreen){
		if(width > 0)
			WIDTH = width;
		if(height > 0)
			HEIGHT = height;
		
		Display.setTitle(title);
		
		try 
		{
			if(!fullscreen)
				Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			else 
				Display.setFullscreen(fullscreen);
			Display.create();
			Keyboard.create();
			Mouse.create();
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void render()
	{
		if(Display.wasResized()) {
			WIDTH = Display.getWidth();
			HEIGHT = Display.getHeight();
		}
		Display.update();
	}
	
	public static void dispose()
	{
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void setResizable(boolean resizable){
		Display.setResizable(resizable);
	}
	
	public static void setFullscreen(boolean fullscreen){
		try {
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}
	
	public static int getWidth()
	{
		return WIDTH;
	}
	
	public static int getHeight()
	{
		return HEIGHT;
	}
	
	public static String getTitle()
	{
		return Display.getTitle();
	}

	public static boolean isFullscreen() {
		return Display.isFullscreen();
	}
}
