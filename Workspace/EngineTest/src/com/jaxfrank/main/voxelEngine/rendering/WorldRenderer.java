package com.jaxfrank.main.voxelEngine.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Transform;
import com.base.engine.rendering.shader.Shader;
import com.base.engine.util.multiThread.ThreadCompleteListener;
import com.jaxfrank.main.voxelEngine.rendering.blockRenderers.BlockRenderer;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;

public class WorldRenderer implements ThreadCompleteListener{
	
	private World world;
	
	private Material worldMat;
	private int NUM_TEXTURES_EXP = 16;
	@SuppressWarnings("unused")
	private int NUM_TEXTURES = (int) Math.pow(2, NUM_TEXTURES_EXP);
	
	//Block renderers
	public static HashMap<String, BlockRenderer> renderers = new HashMap<>();
	
	public ConcurrentHashMap<Vector3i, ChunkRenderer> chunkRenderers = new ConcurrentHashMap<>();
	private ArrayList<Vector3i> chunksToRender = new ArrayList<>();
	public ConcurrentLinkedDeque<Vector3i> chunksToRebuild = new ConcurrentLinkedDeque<>();
	
	private int meshBuilders = 0;
	private static final int numCores = Runtime.getRuntime().availableProcessors() / 2  + 1;
	
	private Vector3f cameraPos;
	private Vector3f cameraForward;
	private Vector3f cameraUp;
	
	private int renderDistance = 2;
	
	public WorldRenderer(World world){
		this.world = world;
	}
	
	public void init(){
		worldMat = new Material(new Texture("terrain.png"), new Vector3f(1,1,1), 0, 0);
	}
	
	public void addBlockRenderer(String name, BlockRenderer renderer){
		renderers.put(name, renderer);
	}
	
	public void update(){
		updateRenderers();
		rebuildChunks();
	}
	
	private void updateRenderers() {
		chunksToRender.clear();
		for(Vector3i loc : world.loadedChunks) {
			Chunk c = world.chunks.get(loc);
			if(!chunkRenderers.containsKey(loc)){
				if(shouldRender(loc)) {
					chunkRenderers.put(loc, new ChunkRenderer(loc));
					chunksToRebuild.add(loc);
					chunksToRender.add(loc);
					//chunkRenderers.get(loc).rebuild();
					world.chunks.get(loc).rebuilt();
				}
			} else {
				if(c.needsRebuild())
					chunksToRebuild.offer(loc);
				if(shouldRender(c))
					chunksToRender.add(loc);
			}
		}
		/*
		//Vector3i[] loadedChunkKeys = world.getLoadedChunkKeys();
		for(int i = 0; i < loadedChunkKeys.length; i++){
			Vector3i loc = loadedChunkKeys[i];
			Chunk c = world.chunks.get(loc);
			if(!chunkRenderers.containsKey(loc)){
				if(shouldRender(loc)) {
//					Chunk c = world.chunks.get(loc);
					chunkRenderers.put(loc, new ChunkRenderer(world, c, loc));
					chunksToRebuild.add(loc);
					chunksToRender.add(loc);
					//chunkRenderers.get(loc).rebuild();
					world.chunks.get(loc).rebuilt();
				}
			} else {
				if(c.needsRebuild())
					chunksToRebuild.offer(loc);
				if(shouldRender(c))
					chunksToRender.add(loc);
			}
		}
		*/
	}
	
	private void rebuildChunks() {
		int numChunksToRebuild = chunksToRebuild.size();
		if(numChunksToRebuild > 0 ) {
			if(meshBuilders == 0) {
				//System.out.println("Chunks to generate " + numChunksToGenerate + " numGenerators " + numGenerators);
				int numThreadsToStart = ((numChunksToRebuild / 10.0f) != 0 ? (numChunksToRebuild / 10) + 1 : (numChunksToRebuild / 10));
				if(numThreadsToStart >= numCores)
					numThreadsToStart = numCores - 1;
				for(int i = 0; i < numThreadsToStart; i++) {
					MeshBuilder thread = new MeshBuilder();
					thread.addListener(this);
					new Thread(thread).start();
					meshBuilders++;
				}
			}
		}
	}
	
	@Override
	public void notifyOfThreadComplete(Runnable thread) {
		meshBuilders--;
	}
	
	private boolean shouldRender(Vector3i location) {
		return shouldRender(world.chunks.get(location));
	}
	
	private boolean shouldRender(Chunk c) {
		if(c == null)
			return false;
		if(c.getNumSolidBlocks() == 0)
			return false;
		
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean relativeToPlayer(Vector3i location) {
		Vector3i cameraPosI = new Vector3i((int)cameraPos.getX(), (int)cameraPos.getY(), (int)cameraPos.getZ());
		cameraPosI = cameraPosI.div(Chunk.getSize());
		if(cameraPosI.sub(location).lengthSqrd() > renderDistance * renderDistance)
			return false;
		
		
		Vector3f right = cameraForward.cross(cameraUp);
		Vector2f pos = new Vector2f((int)cameraPos.getX() / Chunk.getSize().getX(), (int)cameraPos.getZ() / Chunk.getSize().getZ());
		pos = pos.sub(new Vector2f(cameraForward.getX(), cameraForward.getZ()).normalized().mul(2));
		Vector2f rightPoint = pos.add(new Vector2f(right.getX(), right.getZ()).mul(10));
		Vector2f leftPoint = pos.sub(new Vector2f(right.getX(), right.getZ()).mul(10));
		
		if(!inFront(leftPoint, rightPoint, new Vector2f(location.getX(), location.getZ())))
			return false;
		return true;
	}
	
	private boolean inFront(Vector2f a, Vector2f b, Vector2f c){
	     return ((b.getX() - a.getX())*(c.getY() - a.getY()) - (b.getY() - a.getY())*(c.getX() - a.getX())) < 0;
	}
	
	public void render(Shader s, Transform t){
		s.updateUniforms(t.getTransformation(), t.getProjectedTransformation(), worldMat);
		for(Vector3i key : chunksToRender){
			chunkRenderers.get(key).render();
		}
	}
	
	public void updateCamera(Vector3f cameraPos, Vector3f cameraForward, Vector3f cameraUp) {
		this.cameraPos = cameraPos;
		this.cameraForward = cameraForward;
		this.cameraUp = cameraUp;
	}
}
