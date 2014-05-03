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
	public void generate(ArrayList<Vertex> vertices, ArrayList<Integer> indices, World world, Chunk chunk, Vector3i blockPos) {
		if(!Chunk.posInBounds(blockPos)) return;
		float[] texCoords = new float[4];
		int startPos;
		int drawX = blockPos.getX() + chunk.getPos().getX() * Chunk.getWidth();
		int drawY = blockPos.getY() + chunk.getPos().getY() * Chunk.getHeight();
		int drawZ = blockPos.getZ() + chunk.getPos().getZ() * Chunk.getDepth();
		int[] textureIndices = chunk.getBlockNoBoundsCheck(blockPos).getTextureIndices();
		if(chunk.isSideVisible(blockPos, Side.BOTTOM)) {
			startPos = vertices.size();
			TextureUtil.calcTexCoord(texCoords, NUM_TEXTURES, NUM_TEXTURES_EXP, textureIndices[0]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Bottom 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Bottom
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, -0.5f + drawZ),	new Vector2f(texCoords[0], texCoords[3]))); //Bottom
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Bottom
			
			indices.add(2 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); //Bottom
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 3 2 1 2 3 4
			
		}
		if(chunk.isSideVisible(blockPos, Side.LEFT)) {
			startPos = vertices.size();
			TextureUtil.calcTexCoord(texCoords, NUM_TEXTURES, NUM_TEXTURES_EXP, textureIndices[1]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Left 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Left
		        
			indices.add(3 + startPos); indices.add(2 + startPos); indices.add(1 + startPos); //Left
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(0 + startPos); // 6 5 2 2 5 1
		}
		if(chunk.isSideVisible(blockPos, Side.FRONT)) {
			startPos = vertices.size();
			TextureUtil.calcTexCoord(texCoords, NUM_TEXTURES, NUM_TEXTURES_EXP, textureIndices[2]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Front 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, -0.5f + drawZ),	new Vector2f(texCoords[1], texCoords[2]))); //Front 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Front 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Front 
		       
			indices.add(0 + startPos); indices.add(3 + startPos); indices.add(1 + startPos); //Front
			indices.add(0 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 1 7 3 1 5 6
		}
		if(chunk.isSideVisible(blockPos, Side.BACK)) {
			startPos = vertices.size();
			TextureUtil.calcTexCoord(texCoords, NUM_TEXTURES, NUM_TEXTURES_EXP, textureIndices[5]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Back
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Back 
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Back  
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Back
		       
			indices.add(1 + startPos); indices.add(2 + startPos); indices.add(0 + startPos); //Top
			indices.add(1 + startPos); indices.add(3 + startPos); indices.add(2 + startPos); // 6 7 5 6 8 7
			
		}
		if(chunk.isSideVisible(blockPos, Side.RIGHT)) {
			startPos = vertices.size();
			TextureUtil.calcTexCoord(texCoords, NUM_TEXTURES, NUM_TEXTURES_EXP, textureIndices[4]);
			
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, -0.5f + drawZ),	new Vector2f(texCoords[0], texCoords[2]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, -0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Right 
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Right
		       
			indices.add(2 + startPos); indices.add(3 + startPos); indices.add(1 + startPos); //Right
			indices.add(2 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); // 8 4 7 7 4 3
		}
		if(chunk.isSideVisible(blockPos, Side.TOP)) {
			startPos = vertices.size();
			TextureUtil.calcTexCoord(texCoords, NUM_TEXTURES, NUM_TEXTURES_EXP, textureIndices[3]);
			
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[0], texCoords[2]))); //Top   
			vertices.add(new Vertex( new Vector3f(-0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[0], texCoords[3]))); //Top   
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, -0.5f + drawZ), new Vector2f(texCoords[1], texCoords[2]))); //Top   
			vertices.add(new Vertex( new Vector3f(+0.5f + drawX, +0.5f + drawY, +0.5f + drawZ), new Vector2f(texCoords[1], texCoords[3]))); //Top
		
			indices.add(0 + startPos); indices.add(1 + startPos); indices.add(2 + startPos); //Back
			indices.add(1 + startPos); indices.add(3 + startPos); indices.add(2 + startPos); // 2 4 6 4 8 6
			
		}
	}
}
