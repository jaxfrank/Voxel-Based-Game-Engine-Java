package com.jaxfrank.main.voxelEngine.rendering;

import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Vertex;

public class ChunkRenderer {
	
	private Mesh chunkMesh;
	private Mesh transparentChunkMesh;
	
	private int NUM_TEXTURES_EXP = 16;
	@SuppressWarnings("unused")
	private int NUM_TEXTURES = (int) Math.pow(2, NUM_TEXTURES_EXP);
	
	private Vector3i chunkPos;
	
	private Vertex[] opaqueVerts;
	private int[] opaqueIndices;
	private Vertex[] transparentVerts;
	private int[] transparentIndices;
	private boolean newData = false;
	
	public ChunkRenderer(Vector3i pos){
		setChunkPos(pos);
	}
	
	public void render(){
		if(chunkMesh == null || transparentChunkMesh == null)
			return;
		this.chunkMesh.draw();
		this.transparentChunkMesh.draw();
	}

	public Vector3i getChunkPos() {
		return chunkPos;
	}

	public void setChunkPos(Vector3i chunkPos) {
		this.chunkPos = chunkPos;
	}
	
	public void setMesh(Mesh opaqueMesh, Mesh transparentMesh) {
		if(chunkMesh != null) {
			chunkMesh.delete();
			chunkMesh = null;
		}
		if(transparentChunkMesh != null) {
			transparentChunkMesh.delete();
			transparentChunkMesh = null;
		}
		chunkMesh = opaqueMesh;
		transparentChunkMesh = transparentMesh;
	}
	
	public boolean hasNewData() {
		return newData;
	}
	
	public void swapMeshData() {
		if(newData) {
			if(chunkMesh != null)
				chunkMesh.delete();
			if(transparentChunkMesh != null)
				transparentChunkMesh.delete();
			chunkMesh = new Mesh(opaqueVerts, opaqueIndices, true);
			transparentChunkMesh = new Mesh(transparentVerts, transparentIndices, true);
			opaqueVerts = null;//TODO: Check if doing this creates a memory leak
			opaqueIndices = null;
			transparentVerts = null;
			transparentIndices = null;
			newData = false;
		}
	}
	
	public void setTempData(Vertex[] opaqueVerts, int[] opaqueIndices, Vertex[] transparentVerts, int[] transparentIndices) {
		this.opaqueVerts = opaqueVerts;
		this.opaqueIndices = opaqueIndices;
		this.transparentVerts = transparentVerts;
		this.transparentIndices = transparentIndices;
		this.newData = true;
	}
	
}
