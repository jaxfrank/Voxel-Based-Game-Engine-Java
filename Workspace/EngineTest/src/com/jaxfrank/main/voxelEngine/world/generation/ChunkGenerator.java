package com.jaxfrank.main.voxelEngine.world.generation;

import com.base.engine.math.Vector3i;
import com.base.engine.util.multiThread.NotifyingThread;
import com.jaxfrank.main.Main;
import com.jaxfrank.main.voxelEngine.block.Block;
import com.jaxfrank.main.voxelEngine.block.Block.Side;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.jaxfrank.main.voxelEngine.world.World;
import com.sudoplay.joise.Joise;

public class ChunkGenerator extends NotifyingThread{

	private Joise joise;
	private Vector3i currentChunkPos;
	private float componentDivisor = 32;
	private Chunk chunk;
	private int xGlobal;
	private int yGlobal;
	private int zGlobal;
	
	@Override
	public void execute() {
		joise = new Joise(World.getInstance().getGenerator().getJoise().getModuleMap());
		long totalGenerationTime = 0;
		int numChunksGenerated = 0;
		while(true) {
			if(World.getInstance().getGenerator().chunksToGenerate.size() == 0 )
				break;
			currentChunkPos = World.getInstance().getGenerator().chunksToGenerate.poll();
			long startTime = System.nanoTime();
			Chunk c = getChunk();
			totalGenerationTime += (System.nanoTime() - startTime);
			numChunksGenerated++;
			World.getInstance().chunks.put(currentChunkPos, c);
		}
		System.out.println("Chunks generated: " + numChunksGenerated + ", Average build time: " + totalGenerationTime / (double)numChunksGenerated / 1000000.0 + " milliseconds");
	}
	
	private Chunk getChunk() {
		chunk = new Chunk(currentChunkPos);
		Vector3i blockPos;
		xGlobal = currentChunkPos.getX() * Chunk.getWidth();
		yGlobal = currentChunkPos.getY() * Chunk.getWidth();
		zGlobal = currentChunkPos.getZ() * Chunk.getWidth();
		
		for(int i = 0; i < Chunk.getSize().getX(); i++) {
			for(int k = 0; k < Chunk.getSize().getZ(); k++) {
				for(int j = 0; j < Chunk.getSize().getY(); j++) {
					blockPos = new Vector3i(i,j,k);
					
					short thisBlockID = getBlockID(blockPos);
					Block thisBlock = Block.blocks.get(thisBlockID);
					boolean thisBlockIsTransparent = thisBlock.isTransparent();
					chunk.setBlock(blockPos, thisBlockID);
					chunk.setSideVisiblity(blockPos.add(new Vector3i(0,0,-1)), Side.BACK,   thisBlockIsTransparent);
					chunk.setSideVisiblity(blockPos.add(new Vector3i(0,0,1)), Side.FRONT,  thisBlockIsTransparent);
					chunk.setSideVisiblity(blockPos.add(new Vector3i(-1,0,0)), Side.RIGHT,   thisBlockIsTransparent);
					chunk.setSideVisiblity(blockPos.add(new Vector3i(1,0,0)), Side.LEFT,  thisBlockIsTransparent);
					chunk.setSideVisiblity(blockPos.add(new Vector3i(0,-1,0)), Side.TOP,    thisBlockIsTransparent);
					chunk.setSideVisiblity(blockPos.add(new Vector3i(0,1,0)), Side.BOTTOM, thisBlockIsTransparent);
					
					if(i == 0) chunk.setSideVisiblity(blockPos, Side.LEFT, Block.blocks.get(getBlockID(blockPos.add(new Vector3i(-1,0,0)))).isTransparent());
					else if(i == Chunk.getWidth()-1) chunk.setSideVisiblity(blockPos, Side.RIGHT, Block.blocks.get(getBlockID(blockPos.add(new Vector3i(1,0,0)))).isTransparent());
					
					if(j == 0) chunk.setSideVisiblity(blockPos, Side.BOTTOM, Block.blocks.get(getBlockID(blockPos.add(new Vector3i(0,-1,0)))).isTransparent());
					else if(j == Chunk.getHeight()-1) chunk.setSideVisiblity(blockPos, Side.TOP, Block.blocks.get(getBlockID(blockPos.add(new Vector3i(0,1,0)))).isTransparent());
					
					if(k == 0) chunk.setSideVisiblity(blockPos, Side.FRONT, Block.blocks.get(getBlockID(blockPos.add(new Vector3i(0,0,-1)))).isTransparent());
					else if(k == Chunk.getDepth()-1) chunk.setSideVisiblity(blockPos, Side.BACK, Block.blocks.get(getBlockID(blockPos.add(new Vector3i(0,0,1)))).isTransparent());
				}
			}
		}
		chunk.rebuild();
		chunk.setGenerated(true);
		return chunk;
	}
	
	@SuppressWarnings("unused")
	private float getValue(Vector3i block) {
		Vector3i globalBlockLocation = currentChunkPos.mul(Chunk.getSize()).add(block);
		
		double value = joise.get(   globalBlockLocation.getX() / componentDivisor, 
									globalBlockLocation.getY() / componentDivisor, 
									globalBlockLocation.getZ() / componentDivisor);
		
		return (float)value;
	}
	
	private short getBlockID(Vector3i blockPos) {
		short block = 0;
		
		float value = (float)joise.get( (blockPos.getX() + xGlobal) / componentDivisor, 
										(blockPos.getY() + yGlobal) / componentDivisor, 
										(blockPos.getZ() + zGlobal) / componentDivisor);
		if(value > 0.5)
			block = Main.air.getBlockID();
		else if(value > 0.425)
			block = Main.grass.getBlockID();
		else if(value > 0.3)
			block = Main.dirt.getBlockID();
		else
			block = Main.stone.getBlockID();
		
		return block;
	}
	
}
