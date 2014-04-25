package com.jaxfrank.main.voxelEngine.block;

import java.util.concurrent.ConcurrentHashMap;

public class Block {
	
	public static ConcurrentHashMap<String, Block> blocks = new ConcurrentHashMap<String, Block>();
	
	private String blockName;
	private int[] textureIndices;
	
	private boolean isOpaqueCube = true;
	private boolean rendered = true;
	private String rendererName = "Opaque Block";
	
	public Block(String name, int textureIndex){
		this(name, new int[]{textureIndex,textureIndex,textureIndex,textureIndex,textureIndex,textureIndex});
	}
	
	public Block(String name, int[] textureIndices){
		if(blocks.containsKey(name)){
			System.err.println("Block already resgistered with the block ID: \"" + name + "\". Skipping.");
			new Exception().printStackTrace();
			System.exit(-1);
		} 
		this.blockName = name;
		this.textureIndices = textureIndices;
		blocks.put(blockName, this);
	}
	
	public String getBlockName(){
		return blockName;
	}
	
	public int[] getTextureIndices() {
		return textureIndices;
	}
	
	public int getTextureIndex(int side) {
		return textureIndices[side];
	}
	
	public Block setRendererName(String name){
		this.rendererName = name;
		return this;
	}
	
	public String getRendererName(){
		return this.rendererName;
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
	
	public static Block getBlock(String ID){
		return blocks.get(ID);
	}
	
	public enum Side{
		BACK, FRONT, RIGHT, LEFT, BOTTOM, TOP
	}
	
}
