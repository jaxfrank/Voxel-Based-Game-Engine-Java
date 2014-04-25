package com.jaxfrank.main.voxelEngine.rendering.blockRenderers;

import java.util.ArrayList;

import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Vertex;
import com.jaxfrank.main.voxelEngine.block.Block.Side;
import com.jaxfrank.main.voxelEngine.rendering.util.TextureUtil;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;

public class StandardBlockRenderer extends BlockRenderer {
	
	@Override
	public void generate(ArrayList<Vertex> vertices, ArrayList<Integer> indices, World world, Chunk chunk, int x, int y, int z) {
		float[] texCoords;
		int startPos = vertices.size();
		int drawX = x + chunk.getPos().getX() * Chunk.getSize().getX();
		int drawY = y + chunk.getPos().getY() * Chunk.getSize().getY();
		int drawZ = z + chunk.getPos().getZ() * Chunk.getSize().getZ();
		Vector3i blockPos = new Vector3i(x, y, z);
		if(chunk.isSideVisible(blockPos, Side.BOTTOM)) {
			startPos = vertices.size();
			texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, chunk.getBlock(new Vector3i(x,y,z)).getTextureIndices()[0]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Bottom 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Bottom
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, -0.5f + drawZ),	new Vector2f(texCoords[0], texCoords[3]))); //Bottom
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Bottom
			
			indices.add(2 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); //Bottom
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 3 2 1 2 3 4
			
		}
		if(chunk.isSideVisible(blockPos, Side.LEFT)) {
			startPos = vertices.size();
			texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, chunk.getBlock(new Vector3i(x,y,z)).getTextureIndices()[1]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Left
		        
			indices.add(3 + startPos); indices.add(2 + startPos); indices.add(1 + startPos); //Left
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(0 + startPos); // 6 5 2 2 5 1
		}
		if(chunk.isSideVisible(blockPos, Side.FRONT)) {
			startPos = vertices.size();
			texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, chunk.getBlock(new Vector3i(x,y,z)).getTextureIndices()[2]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Front 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, -0.5f + drawZ),	new Vector2f(texCoords[1], texCoords[2]))); //Front 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Front 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Front 
		       
			indices.add(0 + startPos); indices.add(3 + startPos); indices.add(1 + startPos); //Front
			indices.add(0 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 1 7 3 1 5 6
		}
		if(chunk.isSideVisible(blockPos, Side.BACK)) {
			startPos = vertices.size();
			texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, chunk.getBlock(new Vector3i(x,y,z)).getTextureIndices()[5]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Back
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Back 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Back  
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Back
		       
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(0 + startPos); //Top
			indices.add(1 + startPos); indices.add(3 + startPos); indices.add(2 + startPos); // 6 7 5 6 8 7
			
		}
		if(chunk.isSideVisible(blockPos, Side.RIGHT)) {
			startPos = vertices.size();
			texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, chunk.getBlock(new Vector3i(x,y,z)).getTextureIndices()[4]);
			
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, -0.5f + drawZ),	new Vector2f(texCoords[0], texCoords[2]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Right
		       
			indices.add(2 + startPos); indices.add(3 + startPos); indices.add(1 + startPos); //Right
			indices.add(2 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); // 8 4 7 7 4 3
		}
		if(chunk.isSideVisible(blockPos, Side.TOP)) {
			startPos = vertices.size();
			texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, chunk.getBlock(new Vector3i(x,y,z)).getTextureIndices()[3]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Top   
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Top   
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Top   
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Top
		
			indices.add(0 + startPos); indices.add(1 + startPos); indices.add(2 + startPos); //Back
			indices.add(1 + startPos); indices.add(3 + startPos); indices.add(2 + startPos); // 2 4 6 4 8 6
			
		}
	}
}
