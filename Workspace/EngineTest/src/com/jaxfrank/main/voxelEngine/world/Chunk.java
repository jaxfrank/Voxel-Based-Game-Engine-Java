package com.jaxfrank.main.voxelEngine.world;

import com.base.engine.math.Vector3i;
import com.jaxfrank.main.voxelEngine.block.Block;
import com.jaxfrank.main.voxelEngine.block.Block.Side;

public class Chunk {

	private Block[][][] blocks;
	private byte[][][] visibleSides;
	private static Vector3i size = new Vector3i(16,16,16);
	private Vector3i chunkPos;
	private int numSolidBlocks = 0;
	private boolean shouldRebuild = true;
	
	public Chunk(Vector3i position){
		chunkPos = position;
		blocks = new Block[size.getX()][size.getY()][size.getZ()];
		visibleSides = new byte[size.getX()][size.getY()][size.getZ()];
		for(int i = 0; i < size.getX(); i++) {
			for(int j = 0; j < size.getY(); j++) {
				for(int k = 0; k < size.getZ(); k++) {
					visibleSides[i][j][k] = 0;
				}
			}
		}
	}
	
	public Block getBlock(Vector3i pos){
		if(pos.getX() < 0 || pos.getX() >= size.getX())
			return null;
		if(pos.getY() < 0 || pos.getY() >= size.getY())
			return null;
		if(pos.getZ() < 0 || pos.getZ() >= size.getZ())
			return null;
		return blocks[pos.getX()][pos.getY()][pos.getZ()];
	}
	
	public static Vector3i getSize(){
		return size;
	}
	
	public void generate(int seed){
		//WorldGenerator generator = new WorldGenerator(seed, 0, 30, 1);
		Vector3i block = new Vector3i(0, 0, 0);
		for(int i = 0; i < size.getX(); i++) {
			for(int k = 0; k < size.getZ(); k++) {
				for(int j = 0; j < size.getY(); j++) {
					block.setX(i);
					block.setY(j);
					block.setZ(k);
					float value = (World.getInstance().getGenerator().getValue(new Vector3i(chunkPos.getX(), chunkPos.getY(), chunkPos.getZ()), block, 32));
					/*
					int y = j + (chunkPos.getY() * size.getY());
					if(y < (int)value - 4)
						setBlock(new Vector3i(i, j, k), "stone");
					else if(y < (int)value)
						setBlock(new Vector3i(i, j, k), "dirt");
					else if(y == (int)value)
						setBlock(new Vector3i(i, j, k), "grass");
					else
						setBlock(new Vector3i(i, j, k), "air");
					 */
					if(value > 0)
						setBlock(block, "stone");
					else 
						setBlock(block, "glass");
				}
			}
		}
	}
	
	public void setBlock(Vector3i loc, String type) {
		int x = loc.getX();
		int y = loc.getY();
		int z = loc.getZ();
		
		if(blocks[x][y][z] != null) {
			if(!blocks[x][y][z].getBlockName().equals(type)) {
				if(blocks[x][y][z].getBlockName().equals("air")) {
					numSolidBlocks++;
				} else if(type.equals("air"))
					numSolidBlocks--;
				blocks[x][y][z] = Block.getBlock(type);
				rebuild();
			}
		} else {
			if(!type.equals("air"))
				numSolidBlocks++;
			blocks[x][y][z] = Block.getBlock(type);
			rebuild();
		}
	}
	
	public void setSideVisiblity(Vector3i block, Side side, boolean visible) {
		//Order of bytes
		//Top, bottom, left, right, front, back
		//0 is invisible 1 is visible
		byte initial = visibleSides[block.getX()][block.getY()][block.getZ()];
		byte current = (byte)((byte)(initial & (1 << (byte)side.ordinal())) >> side.ordinal());
		boolean wasVisible = current == 1;
		if(wasVisible && visible)
			return;
		if(!wasVisible && !visible)
			return;
		if(wasVisible && !visible){
			visibleSides[block.getX()][block.getY()][block.getZ()] &= ~(1 << side.ordinal());
			return;
		}
		if(!wasVisible && visible) {
			visibleSides[block.getX()][block.getY()][block.getZ()] |= (1 << side.ordinal());
			return;
		}
	}
	
	public boolean isSideVisible(Vector3i block, Side side) {
		byte initial = visibleSides[block.getX()][block.getY()][block.getZ()];
		byte current = (byte)((byte)(initial & (1 << (byte)side.ordinal())) >> side.ordinal());
		return current == 1;
	}
	
	public Block[][][] getBlocks(){
		return this.blocks;
	}
	
	public Vector3i getPos(){
		return this.chunkPos;
	}

	public int getNumSolidBlocks() {
		return numSolidBlocks;
	}

	public boolean needsRebuild() {
		return shouldRebuild;
	}
	
	public void rebuilt() {
		shouldRebuild = false;
	}
	
	public void rebuild() {
		shouldRebuild = true;
	}
}
