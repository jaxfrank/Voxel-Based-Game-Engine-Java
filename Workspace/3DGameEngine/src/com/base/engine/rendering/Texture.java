package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;

import org.newdawn.slick.opengl.TextureLoader;

public class Texture
{
	private int id;
	
	public Texture(String fileName)
	{
		this(loadTexture(fileName, false));
	}
	
	public Texture(String fileName, boolean flipped)
	{
		this(loadTexture(fileName, flipped));
	}
	
	public Texture(int id)
	{
		this.id = id;
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public int getID()
	{
		return id;
	}
	
	private static int loadTexture(String fileName, boolean flipped)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		try
		{		
			return TextureLoader.getTexture(ext, new FileInputStream(new File("./res/textures/" + fileName)), flipped, GL_NEAREST).getTextureID();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return 0;
	}
}
