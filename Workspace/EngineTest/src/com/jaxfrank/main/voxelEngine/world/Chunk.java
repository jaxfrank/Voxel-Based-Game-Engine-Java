package com.jaxfrank.main.voxelEngine.world;

import com.base.engine.math.Vector3i;
import com.jaxfrank.main.voxelEngine.block.Block;
import com.jaxfrank.main.voxelEngine.block.Block.Side;

public class Chunk {

	private short[][][] blocks;
	private byte[][][] visibleSides;
	private static Vector3i size = new Vector3i(32,32,32);
	private Vector3i chunkPos;
	private int numSolidBlocks = 0;
	private boolean shouldRebuild = true;
	private boolean generated = false;
	
	public Chunk(Vector3i position){
		chunkPos = position;
		blocks = new short[size.getX()][size.getY()][size.getZ()];
		visibleSides = new byte[size.getX()][size.getY()][size.getZ()];
		for(int i = 0; i < size.getX(); i++) {
			for(int j = 0; j < size.getY(); j++) {
				for(int k = 0; k < size.getZ(); k++) {
					visibleSides[i][j][k] = 0;
				}
			}
		}
	}
	
	public short getBlockIDAt(Vector3i pos){
		if(!posInBounds(pos)) return -1;
		
		return blocks[pos.getX()][pos.getY()][pos.getZ()];
	}
	
	public Block getBlockNoBoundsCheck(Vector3i pos) {
		return Block.blocks.get(blocks[pos.getX()][pos.getY()][pos.getZ()]);
	}
	
	public static Vector3i getSize(){
		return size;
	}
	
	public static int getWidth() {
		return size.getX();
	}
	
	public static int getHeight() {
		return size.getY();
	}
	
	public static int getDepth() {
		return size.getZ();
	}
	
	public void generate(int seed){
		Vector3i block = new Vector3i(0, 0, 0);
		for(int i = 0; i < size.getX(); i++) {
			for(int k = 0; k < size.getZ(); k++) {
				for(int j = 0; j < size.getY(); j++) {
					block.setX(i);
					block.setY(j);
					block.setZ(k);
					float value = (World.getInstance().getGenerator().getValue(chunkPos, block, 32));
					if(value > 0)
						setBlock(block, (short)1);
					else 
						setBlock(block, (short)3);
				}
			}
		}
	}
	
	public void setBlock(Vector3i loc, short blockID) {
		if(!posInBounds(loc)) return;
		int x = loc.getX();
		int y = loc.getY();
		int z = loc.getZ();
		
		//if(Block.blocks.get(blocks[x][y][z]) != null) {
			if(blocks[x][y][z] != blockID) {
				if(blocks[x][y][z] == 0) {
					numSolidBlocks++;
				} else if(blockID == 0)
					numSolidBlocks--;
				blocks[x][y][z] = blockID;
				rebuild();
			}
		//} else {
		//	if(blockID != 0)
		//		numSolidBlocks++;
		//	blocks[x][y][z] = blockID;
		//	rebuild();
		//}
	}
	
	public void setSideVisiblity(Vector3i block, Side side, boolean invisible) {
		if(!posInBounds(block)) return;
		//Order of bytes
		//Top, bottom, left, right, front, back
		//0 is invisible 1 is visible
		boolean wasVisible = isSideVisible(block, side);
		if(wasVisible && invisible)
			return;
		if(!wasVisible && !invisible)
			return;
		if(wasVisible && !invisible){
			visibleSides[block.getX()][block.getY()][block.getZ()] &= ~(1 << side.ordinal());
			return;
		}
		if(!wasVisible && invisible) {
			visibleSides[block.getX()][block.getY()][block.getZ()] |= (1 << side.ordinal());
			return;
		}
	}
	
	public boolean isSideVisible(Vector3i block, Side side) {
		return ((visibleSides[block.getX()][block.getY()][block.getZ()] & (1 << side.ordinal())) >> side.ordinal()) == 1;
	}
	
	public static boolean posInBounds(Vector3i location) {
		if(location.getX() < 0) return false;
		if(location.getY() < 0) return false;
		if(location.getZ() < 0) return false;
		if(location.getX() >= size.getX()) return false;
		if(location.getY() >= size.getY()) return false;
		if(location.getZ() >= size.getZ()) return false;
		return true;
	}
	
	public short[][][] getBlockIDs(){
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

	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean generated) {
		this.generated = generated;
	}
}
