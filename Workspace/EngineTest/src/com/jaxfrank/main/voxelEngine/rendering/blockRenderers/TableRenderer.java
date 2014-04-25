package com.jaxfrank.main.voxelEngine.rendering.blockRenderers;

import java.util.ArrayList;

import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Vertex;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;

public class TableRenderer extends BlockRenderer {

	//0.015625
	
	static int[] indexes;
	
	@Override
	public void generate(ArrayList<Vertex> vertices, ArrayList<Integer> indices, World world, Chunk chunk, int x, int y, int z) {
		indexes = chunk.getBlock(new Vector3i(x,y,z)).getTextureIndices();
		float[] texCoords;
		int startPos = vertices.size();
		try{
			if(world.getBlock(chunk.getPos(), new Vector3i(x,y-1,z)).isTransparent()) {
				startPos = vertices.size();
				texCoords =  calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[0]);
				
				vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[2]))); //Bottom 
				vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[2]))); //Bottom
				vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, -0.5f + z),	new Vector2f(texCoords[0], texCoords[3]))); //Bottom
				vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[3]))); //Bottom
				
				indices.add(2 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); //Bottom
				indices.add(1 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 3 2 1 2 3 4
			}
		} catch(ArrayIndexOutOfBoundsException | NullPointerException e){
			
		}
				
			startPos = vertices.size();
			texCoords = calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[1]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[2]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[2]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.25f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[3]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.25f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[3]))); //Left
		        
			indices.add(3 + startPos); indices.add(2 + startPos); indices.add(1 + startPos); //Left
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(0 + startPos); // 6 5 2 2 5 1

			startPos = vertices.size();
			texCoords = calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[2]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[2]))); //Front 
			vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, -0.5f + z),	new Vector2f(texCoords[1], texCoords[2]))); //Front 
			vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.25f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[3]))); //Front 
			vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.25f + y, -0.5f + z), new Vector2f(texCoords[1], texCoords[3]))); //Front 
		       
			indices.add(0 + startPos); indices.add(3 + startPos); indices.add(1 + startPos); //Front
			indices.add(0 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 1 7 3 1 5 6

			startPos = vertices.size();
			texCoords = calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[5]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[0], texCoords[2]))); //Back
			vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[2]))); //Back 
			vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.25f + y, +0.5f + z), new Vector2f(texCoords[0], texCoords[3]))); //Back  
			vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.25f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[3]))); //Back
		       
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(0 + startPos); //Top
			indices.add(1 + startPos); indices.add(3 + startPos); indices.add(2 + startPos); // 6 7 5 6 8 7

			startPos = vertices.size();
			texCoords = calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[4]);
			
			vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, -0.5f + z),	new Vector2f(texCoords[0], texCoords[2]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[2]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.25f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[3]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.25f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[3]))); //Right
		       
			indices.add(2 + startPos); indices.add(3 + startPos); indices.add(1 + startPos); //Right
			indices.add(2 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); // 8 4 7 7 4 3
			
			startPos = vertices.size();
			texCoords = calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[3]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.25f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[2]))); //Top   
			vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.25f + y, +0.5f + z), new Vector2f(texCoords[0], texCoords[3]))); //Top   
			vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.25f + y, -0.5f + z), new Vector2f(texCoords[1], texCoords[2]))); //Top   
			vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.25f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[3]))); //Top
		
			indices.add(0 + startPos); indices.add(1 + startPos); indices.add(2 + startPos); //Back
			indices.add(1 + startPos); indices.add(3 + startPos); indices.add(2 + startPos); // 2 4 6 4 8 6
	}
	
	public static float[] calcTexCoord(int numTextures, int numTextureExponent, int index){
		float[] result = new float[4];
		
		if(index == indexes[1] || index == indexes[2] || index == indexes[4] || index == indexes[5]){
			int texCoord = numTextures - index;
			
			int texX = texCoord % numTextureExponent;
			int texY = texCoord / numTextureExponent;
	
			result[1] = 1f - (float)texX/(float)16;
			result[0] = result[1] - 1f/(float)16;
			result[2] = (1f - (float)texY/(float)16);
			result[3] = (result[2] - 1f/(float)16) + (0.00390625f * 4);
		}else {
			int texCoord = numTextures - index;
			
			int texX = texCoord % numTextureExponent;
			int texY = texCoord / numTextureExponent;
	
			result[1] = 1f - (float)texX/(float)16;
			result[0] = result[1] - 1f/(float)16;
			result[2] = 1f - (float)texY/(float)16;
			result[3] = result[2] - 1f/(float)16;
		}
		
		return result;
	}
}
