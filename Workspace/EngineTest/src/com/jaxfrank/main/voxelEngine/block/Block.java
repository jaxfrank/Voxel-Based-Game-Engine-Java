package com.jaxfrank.main.voxelEngine.block;

import java.util.concurrent.ConcurrentHashMap;

public class Block {
	
	public static ConcurrentHashMap<Short, Block> blocks = new ConcurrentHashMap<>();
	
	private short blockID;
	private int[] textureIndices;
	
	private boolean isOpaqueCube = true;
	private boolean rendered = true;
	private int rendererID = 0;
	
	public Block(short id, int textureIndex){
		this(id, new int[]{textureIndex,textureIndex,textureIndex,textureIndex,textureIndex,textureIndex});
	}
	
	public Block(short id, int[] textureIndices){
		if(blocks.containsKey(id)){
			System.err.println("Block already resgistered with the block ID: \"" + id + "\". Skipping.");
		} 
		this.blockID = id;
		this.textureIndices = textureIndices;
		blocks.put(id, this);
	}
	
	public short getBlockID(){
		return blockID;
	}
	
	public int[] getTextureIndices() {
		return textureIndices;
	}
	
	public int getTextureIndex(int side) {
		return textureIndices[side];
	}
	
	public Block setRendererID(int id){
		this.rendererID = id;
		return this;
	}
	
	public int getRendererID(){
		return this.rendererID;
	}
	
	public Block setOpaque(){
		this.isOpaqueCube = true;
		return this;
	}
	
	public Block setTransparent(){
		this.isOpaqueCube = false;
		return this;
	}
	
	public boolean isTransparent(){
		return !this.isOpaqueCube;
	}
	
	public boolean isOpaqueCube(){
		return this.isOpaqueCube;
	}
	
	public Block setRendered(boolean rendered){
		this.rendered = rendered;
		return this;
	}
	
	public boolean isRendered(){
		return this.rendered;
	}
	
	public enum Side{
		BACK, FRONT, RIGHT, LEFT, BOTTOM, TOP
	}
	
}
