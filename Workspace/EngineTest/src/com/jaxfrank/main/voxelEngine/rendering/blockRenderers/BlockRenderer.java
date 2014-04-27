package com.jaxfrank.main.voxelEngine.rendering.blockRenderers;

import java.util.ArrayList;

import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Vertex;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;

public class BlockRenderer {
	
	protected int NUM_TEXTURES_EXP = 16;
	protected int NUM_TEXTURES = (int) Math.pow(2, NUM_TEXTURES_EXP);
	
	public void generate(ArrayList<Vertex> vertices, ArrayList<Integer> indices, World world, Chunk chunk, Vector3i blockPos){
		
	}
	
}
