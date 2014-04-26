package com.jaxfrank.main.voxelEngine.world;

import java.util.ArrayList;
import java.util.HashMap;
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
	public ArrayList<Vector3i> loadedChunks = new ArrayList<>();
	private int seed;
	
	Random rand;
	public int loadedChunkRadius = 5;
	
	private WorldGenerator generator;
	
	public World(int seed){
		this.seed = seed;
		rand = new Random(seed);
		ConcurrentHashMap<Integer, Integer> a = new ConcurrentHashMap<>(11*11*11*5, 0.75f, 8);
	}
	
	public World(String seed){
		this.seed = seed.hashCode();
		rand = new Random(this.seed);
	}
	
	public void update(Vector3f playerPos){
		//System.out.println("Count of chunks " + chunks.size());
		loadedChunks.clear();
		//boolean rebuild = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_R);
		Vector3i chunkPos = new Vector3i(0,0,0);
		for(int i = -loadedChunkRadius + ((int)playerPos.getX() / Chunk.getSize().getX()); i < loadedChunkRadius + ((int)playerPos.getX() / Chunk.getSize().getX()); i++){
			for(int j = -loadedChunkRadius + ((int)playerPos.getY() / Chunk.getSize().getY()); j < loadedChunkRadius + ((int)playerPos.getY() / Chunk.getSize().getY()); j++){
				for(int k = -loadedChunkRadius + ((int)playerPos.getZ() / Chunk.getSize().getZ()); k < loadedChunkRadius + ((int)playerPos.getZ() / Chunk.getSize().getZ()); k++){
					chunkPos = new Vector3i(i,j,k);
					loadChunk(chunkPos);
					//if(rebuild && chunks.containsKey(chunkPos))
					//	chunks.get(chunkPos).rebuild();
				}
			}
		}
		getGenerator().update();
	}
	
	private HashMap<Vector3i, Object> generated = new HashMap<>();
	public void loadChunk(Vector3i location){
		if(!chunks.containsKey(location) && !generated.containsKey(location)) {
			generated.put(location, new Object());
			getGenerator().chunksToGenerate.offer(location);
		} else {
			loadedChunks.add(location);
		}
	}
	
	public Block getBlock(Vector3i chunkPos, Vector3i pos){
		return chunks.get(chunkPos).getBlock(pos);
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

	public static World getInstance() {
		if(instance == null)
			instance = new World(new Random().nextInt());
		return instance;
	}

	public static void setInstance(World instance) {
		World.instance = instance;
	}
}
