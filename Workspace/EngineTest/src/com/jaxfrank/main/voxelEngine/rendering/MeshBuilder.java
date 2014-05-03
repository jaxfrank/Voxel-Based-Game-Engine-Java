package com.jaxfrank.main.voxelEngine.rendering;

import java.util.ArrayList;

import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Vertex;
import com.base.engine.util.Util;
import com.base.engine.util.multiThread.NotifyingThread;
import com.jaxfrank.main.Main;
import com.jaxfrank.main.voxelEngine.block.Block;
import com.jaxfrank.main.voxelEngine.rendering.blockRenderers.BlockRenderer;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;

public class MeshBuilder extends NotifyingThread {

	@Override
	public void execute() {
		long totalMeshBuildTime = 0;
		int numMeshesBuilt = 0;
		while(true) {
			Vector3i chunkPos = new Vector3i(0,0,0);
			synchronized (Main.worldRenderer.chunkRenderers) {
				if(Main.worldRenderer.chunksToRebuild.size() == 0)
					break;
				chunkPos = Main.worldRenderer.chunksToRebuild.poll();
			}
			
			World.getInstance().chunks.get(chunkPos).rebuilt();
			long startTime = System.nanoTime();
			generateMesh(World.getInstance().chunks.get(chunkPos), chunkPos);
			totalMeshBuildTime += System.nanoTime() - startTime;
			numMeshesBuilt++;
		}
		//System.out.println("Meshes Built: " + numMeshesBuilt + ", Average build time: " + totalMeshBuildTime / (double)numMeshesBuilt / 1000000.0 + " milliseconds");
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
					Block b = Block.blocks.get(chunk.getBlockIDNoBoundsCheck(i, j, k));
					if(!b.isRendered()) continue; // if the current block doesn't have a texture then don't bother trying to calculate the vertices
					int rendererID = b.getRendererID();
					BlockRenderer renderer = WorldRenderer.renderers.get(rendererID);
					if(renderer != null) {
						if(b.isOpaqueCube())
							renderer.generate(vertices, indices, World.getInstance(), chunk, blockPos);
						else {
							renderer.generate(transparentVertices, transparentIndices, World.getInstance(), chunk, blockPos);
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
		int[] opaqueIndices = new int[indices.size()];
		
		vertices.toArray(opaqueVerts);
		for(int i = 0; i < indices.size(); i++) {
			opaqueIndices[i] = indices.get(i);
		}
		
		Vertex[] transparentVerts = new Vertex[transparentVertices.size()];
		int[] transparentIndicesArray = new int[transparentIndices.size()];
		
		transparentVertices.toArray(transparentVerts);
		for(int i = 0; i < transparentIndices.size(); i++) {
			transparentIndicesArray[i] = transparentIndices.get(i);
		}
		
		Main.worldRenderer.chunkRenderers.get(chunkPos).setTempData(opaqueVerts, opaqueIndices, transparentVerts, transparentIndicesArray);
		
	}
	
	
}
