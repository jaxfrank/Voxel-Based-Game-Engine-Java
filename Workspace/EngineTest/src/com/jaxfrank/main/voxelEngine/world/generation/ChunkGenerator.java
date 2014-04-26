package com.jaxfrank.main.voxelEngine.world.generation;

import com.base.engine.math.Vector3i;
import com.base.engine.util.multiThread.NotifyingThread;
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
	
	@Override
	public void execute() {
		synchronized (World.getInstance().getGenerator().getJoise()) {
			joise = new Joise(World.getInstance().getGenerator().getJoise().getModuleMap());
		}
		
		while(true) {
			if(World.getInstance().getGenerator().chunksToGenerate.size() == 0 )
				break;
			currentChunkPos = World.getInstance().getGenerator().chunksToGenerate.poll();
			//System.out.println("Generating New Chunk!");
			
			World.getInstance().chunks.put(currentChunkPos, getChunk());
			
		}
	}
	
	private Chunk getChunk() {
		chunk = new Chunk(currentChunkPos);
		Vector3i blockPos = new Vector3i(0, 0, 0);
		for(int i = 0; i < Chunk.getSize().getX(); i++) {
			for(int k = 0; k < Chunk.getSize().getZ(); k++) {
				for(int j = 0; j < Chunk.getSize().getY(); j++) {
					blockPos.setX(i);
					blockPos.setY(j);
					blockPos.setZ(k);
					
					chunk.setBlock(blockPos, getBlockID(blockPos));
					chunk.setSideVisiblity(blockPos, Side.BACK,   isSideVisible(blockPos.add(new Vector3i(+0, +0, +1)), Side.BACK));
					chunk.setSideVisiblity(blockPos, Side.FRONT,  isSideVisible(blockPos.add(new Vector3i(+0, +0, -1)), Side.FRONT));
					chunk.setSideVisiblity(blockPos, Side.LEFT,   isSideVisible(blockPos.add(new Vector3i(-1, +0, +0)), Side.LEFT));
					chunk.setSideVisiblity(blockPos, Side.RIGHT,  isSideVisible(blockPos.add(new Vector3i(+1, +0, +0)), Side.RIGHT));
					chunk.setSideVisiblity(blockPos, Side.TOP,    isSideVisible(blockPos.add(new Vector3i(+0, +1, +0)), Side.TOP));
					chunk.setSideVisiblity(blockPos, Side.BOTTOM, isSideVisible(blockPos.add(new Vector3i(+0, -1, +0)), Side.BOTTOM));
				}
			}
		}
		chunk.rebuild();
		return chunk;
	}
	
	private float getValue(Vector3i block) {
		Vector3i globalBlockLocation = currentChunkPos.mul(Chunk.getSize()).add(block);
		
		double value = joise.get(   globalBlockLocation.getX() / componentDivisor, 
									globalBlockLocation.getY() / componentDivisor, 
									globalBlockLocation.getZ() / componentDivisor);
		
		return (float)value;
	}
	
	private boolean isSideVisible(Vector3i blockPos, Side side){
		Vector3i checkingBlockPos = null;
		switch(side) {
		case BACK:
			checkingBlockPos = blockPos.add(new Vector3i(+0, +0, +0));
			break;
		case FRONT:
			checkingBlockPos = blockPos.add(new Vector3i(+0, +0, -0));
			break;
		case LEFT:
			checkingBlockPos = blockPos.add(new Vector3i(-0, +0, +0));
			break;
		case RIGHT:
			checkingBlockPos = blockPos.add(new Vector3i(+0, +0, +0));
			break;
		case TOP:
			checkingBlockPos = blockPos.add(new Vector3i(+0, +0, +0));
			break;
		case BOTTOM:
			checkingBlockPos = blockPos.add(new Vector3i(+0, -1, +0));
			break;
		}
		
		Block block = chunk.getBlock(checkingBlockPos);
		if(block == null) {
			return Block.blocks.get(getBlockID(checkingBlockPos)).isTransparent();
		} else {
			return block.isTransparent();
		}
	}
	
	private int getBlockID(Vector3i blockPos) {
		int block = 0;
		
		float value = getValue(blockPos);
		if(value > 0)
			block = 4;
		else 
			block = 0;
		
		return block;
	}
	
}
