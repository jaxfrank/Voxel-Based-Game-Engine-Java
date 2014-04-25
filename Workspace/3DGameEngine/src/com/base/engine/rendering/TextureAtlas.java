package com.base.engine.rendering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;

import com.base.engine.math.Vector2f;

public class TextureAtlas {
	
	private Texture spriteSheet;
	private Vector2f sheetSize;
	private int spriteSize;
	private HashMap<String, Vector2f[]> textureCoords = new HashMap<>();
	
	public TextureAtlas(String path, String type, int spriteSize){
		this.spriteSheet = loadTexture(path + "." + type, false);
		this.spriteSize = spriteSize;
		this.sheetSize = new Vector2f(spriteSheet.getWidth(), spriteSheet.getHeight());
		loadTextureCoordinates(path + "." + "sprites");
	}

	public TextureAtlas(String path, String type, int spriteSize, boolean flipped){
		this.spriteSheet = loadTexture(path + "." + type, flipped);
		this.spriteSize = spriteSize;
		this.sheetSize = new Vector2f(spriteSheet.getWidth(), spriteSheet.getHeight());
		loadTextureCoordinates(path + "." + "sprites");
	}
	
	private void loadTextureCoordinates(String path) {
		try {
			BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			String curLine = null;
			String curName = null;
			ArrayList<Vector2f> verts = new ArrayList<>();
			Vector2f[] vertices = new Vector2f[4];
			int numTexCoords = 0;
			while(true){
				curLine = stream.readLine();
				if(curLine == null) break;
				String[] splitLine = curLine.split(" ");
				
				if(splitLine.length == 0) 
					continue;
				else if(splitLine.length == 1) 
					continue;
				else if(splitLine.length == 2 && splitLine[0].equals("N") && curName == null) 
					curName = splitLine[1];
				else if(splitLine.length == 3 && splitLine[0].equals("V") && curName != null){
					verts.add(numTexCoords, new Vector2f(Float.parseFloat(splitLine[1]), Float.parseFloat(splitLine[2])));
//					System.out.println(curName + " " + numTexCoords + " "+ verts.get(numTexCoords).toString());
					numTexCoords++;
				}
				
				if(numTexCoords == 4 && curName != null){
					verts.toArray(vertices);
					textureCoords.put(curName, vertices);
					verts.clear();
					vertices = new Vector2f[4];
					numTexCoords = 0;
					curName = null;
				}
			}
			stream.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Texture loadTexture(String path, boolean flipped){
		String[] splitPath = path.split("\\.");
		try {
			return TextureLoader.getTexture(splitPath[splitPath.length - 1].toUpperCase(), new FileInputStream(new File(path)), flipped, GL_NEAREST);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, spriteSheet.getTextureID());
	}
	
	public void release(){
		spriteSheet.release();
	}

	public Texture getSpriteSheet() {
		return spriteSheet;
	}

	public Vector2f getSheetSize() {
		return sheetSize;
	}

	public int getSpriteSize() {
		return spriteSize;
	}
	
	public Vector2f[] getTexCoordsV2f(String key){
		return textureCoords.get(key);
	}
	
	public float[] getTexCoordsF(String key){
		float[] texCoords = new float[8];
		Vector2f[] coords = getTexCoordsV2f(key);
		
		for(int i = 0; i < coords.length; i++){
			texCoords[i*2] = coords[i].getX();
			texCoords[i*2 + 1] = coords[i].getY();
		}
		
		return texCoords;
	}
	
}
