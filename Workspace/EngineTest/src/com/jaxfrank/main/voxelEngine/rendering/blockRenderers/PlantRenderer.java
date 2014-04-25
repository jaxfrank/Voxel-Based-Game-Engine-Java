package com.jaxfrank.main.voxelEngine.rendering.blockRenderers;

import java.util.ArrayList;

import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Vertex;
import com.jaxfrank.main.voxelEngine.rendering.util.TextureUtil;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;

public class PlantRenderer extends BlockRenderer {

	@Override
	public void generate(ArrayList<Vertex> vertices, ArrayList<Integer> indices, World world, Chunk chunk, int x, int y, int z) {
		float[] texCoords;
		int startPos = 0;
		
		startPos = vertices.size();
		texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[0]);
		
		vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[2]))); //Bottom 
		vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.5f + y, -0.5f + z), new Vector2f(texCoords[0], texCoords[3]))); //Bottom
		vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.5f + y, +0.5f + z),	new Vector2f(texCoords[1], texCoords[3]))); //Bottom
		vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[1], texCoords[2]))); //Bottom
		
		indices.add(1 + startPos); indices.add(3 + startPos); indices.add(0 + startPos); //Bottom
		indices.add(1 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 3 2 1 2 3 4
		
		indices.add(3 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); //Bottom
		indices.add(2 + startPos); indices.add(1 + startPos); indices.add(3 + startPos); // 3 2 1 2 3 4
		
		startPos = vertices.size();
		texCoords = TextureUtil.calcTexCoord(NUM_TEXTURES, NUM_TEXTURES_EXP, world.getBlock(chunk.getPos(), new Vector3i(x,y,z)).getTextureIndices()[0]);
		
		vertices.add(new Vertex( new Vector3f(-0.5f + x, -0.5f + y, +0.5f + z), new Vector2f(texCoords[0], texCoords[2]))); //Bottom 
		vertices.add(new Vertex( new Vector3f(-0.5f + x, +0.5f + y, +0.5f + z), new Vector2f(texCoords[0], texCoords[3]))); //Bottom
		vertices.add(new Vertex( new Vector3f(+0.5f + x, +0.5f + y, -0.5f + z),	new Vector2f(texCoords[1], texCoords[3]))); //Bottom
		vertices.add(new Vertex( new Vector3f(+0.5f + x, -0.5f + y, -0.5f + z), new Vector2f(texCoords[1], texCoords[2]))); //Bottom
		
		indices.add(1 + startPos); indices.add(3 + startPos); indices.add(0 + startPos); //Bottom
		indices.add(1 + startPos); indices.add(2 + startPos); indices.add(3 + startPos); // 3 2 1 2 3 4
		
		indices.add(3 + startPos); indices.add(1 + startPos); indices.add(0 + startPos); //Bottom
		indices.add(2 + startPos); indices.add(1 + startPos); indices.add(3 + startPos); // 3 2 1 2 3 4
	}
	
}
