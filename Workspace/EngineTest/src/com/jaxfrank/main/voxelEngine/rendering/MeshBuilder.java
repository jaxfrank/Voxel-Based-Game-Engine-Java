package com.jaxfrank.main.voxelEngine.rendering;

import java.util.ArrayList;

import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Vertex;
import com.base.engine.util.Util;
import com.base.engine.util.multiThread.NotifyingThread;
import com.jaxfrank.main.Main;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;

public class MeshBuilder extends NotifyingThread {

	@Override
	public void execute() {
		while(true) {
			Vector3i chunkPos = new Vector3i(0,0,0);
			synchronized (Main.worldRenderer.chunkRenderers) {
				if(Main.worldRenderer.chunksToRebuild.size() == 0)
					break;
				chunkPos = Main.worldRenderer.chunksToRebuild.poll();
			}
			
			World.getInstance().chunks.get(chunkPos).rebuilt();
			generateMesh(World.getInstance().chunks.get(chunkPos), chunkPos);
		}
	}
	
	private void generateMesh(Chunk chunk, Vector3i chunkPos) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		ArrayList<Vertex> transparentVertices = new ArrayList<Vertex>();
		ArrayList<Integer> transparentIndices = new ArrayList<Integer>();
		
		for(int i = 0; i < Chunk.getWidth(); i++){
			for(int j = 0; j < Chunk.getHeight(); j++){
				for(int k = 0; k < Chunk.getDepth(); k++){
					Vector3i blockPos = new Vector3i(i,j,k);
					if(!chunk.getBlockNoBoundsCheck(blockPos).isRendered()) continue; // if the current block doesn't have a texture then don't bother trying to calculate the vertices

					if(WorldRenderer.renderers.get(chunk.getBlockNoBoundsCheck(blockPos).getRendererID()) != null) {
						if(chunk.getBlockNoBoundsCheck(blockPos).isOpaqueCube())
							WorldRenderer.renderers.get(chunk.getBlockNoBoundsCheck(blockPos).getRendererID()).generate(vertices, indices, World.getInstance(), chunk, blockPos);
						else {
							WorldRenderer.renderers.get(chunk.getBlockNoBoundsCheck(blockPos).getRendererID()).generate(transparentVertices, transparentIndices, World.getInstance(), chunk, blockPos);
						}
					} else {
						System.err.println("Trying to use invalid block renderer: " + chunk.getBlockNoBoundsCheck(blockPos).getRendererID());
						new Exception().printStackTrace();
						System.exit(-1);
					}
				}
			}
		}
		
		Vertex[] opaqueVerts = new Vertex[vertices.size()];
		Integer[] opaqueIndices = new Integer[indices.size()];
		
		vertices.toArray(opaqueVerts);
		indices.toArray(opaqueIndices);
		
		Vertex[] transparentVerts = new Vertex[transparentVertices.size()];
		Integer[] transparentIndicesArray = new Integer[transparentIndices.size()];
		
		transparentVertices.toArray(transparentVerts);
		transparentIndices.toArray(transparentIndicesArray);
		
		Main.worldRenderer.chunkRenderers.get(chunkPos).setTempData(opaqueVerts, Util.toIntArray(opaqueIndices), transparentVerts, Util.toIntArray(transparentIndicesArray));
		
	}
	
	
}
