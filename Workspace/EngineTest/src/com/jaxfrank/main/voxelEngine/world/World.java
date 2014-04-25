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
	}
	
	public World(String seed){
		this.seed = seed.hashCode();
		rand = new Random(this.seed);
	}
	
	public void update(Vector3f playerPos){
		//System.out.println("Count of chunks " + chunks.size());
		loadedChunks.clear();
		boolean rebuild = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_R);
		for(int i = -loadedChunkRadius + ((int)playerPos.getX() / Chunk.getSize().getX()); i < loadedChunkRadius + ((int)playerPos.getX() / Chunk.getSize().getX()); i++){
			for(int j = -loadedChunkRadius + ((int)playerPos.getY() / Chunk.getSize().getY()); j < loadedChunkRadius + ((int)playerPos.getY() / Chunk.getSize().getY()); j++){
				for(int k = -loadedChunkRadius + ((int)playerPos.getZ() / Chunk.getSize().getZ()); k < loadedChunkRadius + ((int)playerPos.getZ() / Chunk.getSize().getZ()); k++){
					loadChunk(new Vector3i(i,j,k));
					if(rebuild && chunks.containsKey(new Vector3i(i,j,k)))
						chunks.get(new Vector3i(i,j,k)).rebuild();
				}
			}
		}
		getGenerator().update();
	}
	
	public void generateChunks(Vector3i bottomLeft, Vector3i topRight) {
		long totalTime = 0;
		for(int i = bottomLeft.getX(); i <= topRight.getX(); i++) {
			for(int j = bottomLeft.getX(); j <= topRight.getX(); j++) {
				for(int k = bottomLeft.getX(); k <= topRight.getX(); k++) {
					long start = System.nanoTime();
					generateChunk(new Vector3i(i, j, k));
					long end = System.nanoTime();
					totalTime += (end - start);
					//System.out.println(new Vector3i(i, j, k).toString() + ", " + (end - start));
				}
			}
		}
		int x = topRight.getX() - bottomLeft.getX();
		int y = topRight.getY() - bottomLeft.getY();
		int z = topRight.getZ() - bottomLeft.getZ();
		System.out.println("Total Elapsed Time: " + (totalTime*1e-9) + " seconds , Average: " + ((totalTime / (x * y * z)) * 1e-9) + " seconds");
		
	}
	
	public void generateChunk(Vector3i location) {
		if(!chunks.containsKey(location)) {
			Chunk c = new Chunk(location);
			c.generate(seed);
			chunks.put(location, c);
		} else {
			chunks.get(location).generate(seed);
		}
	}
	
	public Chunk[] getLoadedChunks(){
		Chunk[] chunks = new Chunk[this.loadedChunks.size()];
		for(int i = 0; i < loadedChunks.size(); i++){
			chunks[i] = this.chunks.get(loadedChunks.get(i));
		}
		return chunks;
	}
	
	public Vector3i[] getLoadedChunkKeys(){
		Vector3i[] chunkKeys = new Vector3i[loadedChunks.size()]; 
		loadedChunks.toArray(chunkKeys);
		return chunkKeys;
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
	
	public void unloadChunk(Vector3i location){
		String loc = location.toString();
		if(loadedChunks.contains(loc)){
			loadedChunks.remove(loc);
//			System.out.println("Unloading chunk " + location.toString());
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
