package com.jaxfrank.main.voxelEngine.world;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Keyboard;

import com.base.engine.math.Vector3f;
import com.base.engine.math.Vector3i;
import com.jaxfrank.main.voxelEngine.block.Block;
import com.jaxfrank.main.voxelEngine.world.generation.WorldGenerator;

public class World {
	
	private static World instance;
	
	
	public ConcurrentHashMap<Vector3i, Chunk> chunks = new ConcurrentHashMap<>();
	public HashSet<Vector3i> loadedChunks = new HashSet<>();
	private HashSet<Vector3i> generated = new HashSet<Vector3i>();
	private int seed;
	
	Random rand;
	public int loadedChunkRadius = 10;
	
	private WorldGenerator generator;
	private boolean forceChunkLoadUpdate = true;
	private boolean updatedLoadList = false;
	
	public World(int seed){
		this.seed = seed;
		rand = new Random(seed);
		//chunks = new ConcurrentHashMap<Vector3i, Chunk>(loadedChunkRadius*loadedChunkRadius*loadedChunkRadius*20);
	}
	
	public World(String seed){
		this.seed = seed.hashCode();
		rand = new Random(this.seed);
		//chunks = new ConcurrentHashMap<Vector3i, Chunk>(loadedChunkRadius*loadedChunkRadius*loadedChunkRadius*20);
	}
	
	public void update(Vector3f playerPos){
		updatedLoadList = false;
		if(forceChunkLoadUpdate || needLoadedChunkUpdate(playerPos)) {
			updatedLoadList = true;
			forceChunkLoadUpdate = false;
			loadedChunks.clear();
			boolean rebuild = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_R);
			Vector3i chunkPos = new Vector3i(0,0,0);
			for(int i = -loadedChunkRadius + ((int)playerPos.getX() / Chunk.getSize().getX()); i < loadedChunkRadius + ((int)playerPos.getX() / Chunk.getSize().getX()); i++){
				for(int j = -loadedChunkRadius + ((int)playerPos.getY() / Chunk.getSize().getY()); j < loadedChunkRadius + ((int)playerPos.getY() / Chunk.getSize().getY()); j++){
					for(int k = -loadedChunkRadius + ((int)playerPos.getZ() / Chunk.getSize().getZ()); k < loadedChunkRadius + ((int)playerPos.getZ() / Chunk.getSize().getZ()); k++){
						chunkPos = new Vector3i(i,j,k);
						Chunk c = chunks.get(chunkPos);
						if(c != null) {
							if(c.isGenerated()) {
								loadedChunks.add(chunkPos);
							}
							if(rebuild)
								c.rebuild();
						} else {
							if(!generated.contains(chunkPos)) {
								generated.add(chunkPos);
								getGenerator().chunksToGenerate.offer(chunkPos);
							}
						}
					
					}
				}
			}
		}
		getGenerator().update();
	}
	
	public Block getBlock(Vector3i chunkPos, Vector3i pos){
		return Block.blocks.get(chunks.get(chunkPos).getBlockIDAt(pos));
	}
	
	public static int seedStringToInt(String seed){
		int iSeed = 0;
		for(int i = 0; i < seed.length(); i++)
			iSeed += seed.charAt(i);
		return iSeed;
	}

	public int getSeed() {
		return seed;
	}

	public WorldGenerator getGenerator() {
		if(generator == null)
			generator = new WorldGenerator(seed, 0, 30, 1);
		return generator;
	}

	Vector3i previousPlayerChunkLocation = new Vector3i(10000000,1000000,1000000);
	private boolean needLoadedChunkUpdate(Vector3f playerPos) {
		Vector3i playerChunkPos = playerPos.getXYZi().div(Chunk.getSize());
		if(!playerChunkPos.equals(playerPos)) {
			previousPlayerChunkLocation = playerChunkPos;
			return true;
		}
		
		return false;
	}
	
	public boolean loadListChanged() {
		return updatedLoadList;
	}
	
	public static World getInstance() {
		if(instance == null)
			instance = new World(new Random().nextInt());
		return instance;
	}

	public static void setInstance(World instance) {
		World.instance = instance;
	}
	
}
