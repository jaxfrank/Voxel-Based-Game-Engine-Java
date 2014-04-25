package com.jaxfrank.main.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.base.engine.math.Vector3f;
import com.jaxfrank.main.voxelEngine.block.Block;

public class WorldTest {
	
	private Block[][][] blocks;
	
	public WorldTest(Vector3f worldSize){
		blocks = new Block[(int) worldSize.getX()][(int) worldSize.getY()][(int) worldSize.getZ()];
		generate();
	}
	
	private void generate(){
		Random rand = new Random();
		
		ArrayList<String> keys = new ArrayList<String>();
		
		Iterator<String> it = Block.blocks.keySet().iterator();
		
		while(it.hasNext()){
			keys.add(it.next());
		}
		
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks[0].length; j++){
				for(int k = 0; k < blocks[0][0].length; k++){
					do{
						blocks[i][j][k] = Block.blocks.get( keys.get( Math.abs( rand.nextInt() % Block.blocks.size() ) ) );
					} while(blocks[i][j][k].getBlockName().equals("air"));
					
					if(j == 0){
						blocks[i][j][k] = Block.getBlock("bedrock");
					} else if(j <= 3 ){
						if(rand.nextBoolean())
							blocks[i][j][k] = Block.getBlock("bedrock");
						else
							blocks[i][j][k] = Block.getBlock("stone");
					} else if(j < 64){
						blocks[i][j][k] = Block.getBlock("stone");
					} else {
						blocks[i][j][k] = Block.getBlock("air");
					}
				}
			}
		}
		
		Vector3f prevPos;
		Vector3f curPos;
		int radius = rand.nextInt(7) + 3;
		
		prevPos = new Vector3f(rand.nextInt(blocks.length), rand.nextInt(blocks[0].length - 96) + 64, rand.nextInt(blocks[0][0].length));
		for(int i = 0; i < 50; i++){
			curPos = new Vector3f(rand.nextInt(blocks.length), rand.nextInt(blocks[0].length - 96) + 64, rand.nextInt(blocks[0][0].length));
			lerp(prevPos, curPos, radius, "stone");
			prevPos = curPos;
		}
		
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks[0][0].length; j++){
				int topY = this.findTopBlockY(i, blocks[0].length, j);
				for(int k = topY; k > topY - 5; k--){
					if(!blocks[i][k][j].getBlockName().equals("air"))
						blocks[i][k][j] = Block.getBlock("dirt");
				}
				//if(rand.nextBoolean())
					blocks[i][topY][j] = Block.getBlock("grass");
				//else
					//blocks[i][topY][j] = Block.getBlock("table");
				if(rand.nextBoolean() && rand.nextBoolean())
					blocks[i][topY + 1][j] = Block.getBlock("flower");
			}
		}
		
		for(int j = 0; j < 4; j++){
			radius = rand.nextInt(2) + 2;
			prevPos = new Vector3f(rand.nextInt(blocks.length), rand.nextInt(blocks[0].length), rand.nextInt(blocks[0][0].length));
			for(int i = 0; i < 5; i++){
				curPos = new Vector3f(rand.nextInt(blocks.length), rand.nextInt(64), rand.nextInt(blocks[0][0].length));
				lerp2(prevPos, curPos, radius, "air");
				prevPos = curPos;
			}
		}
		
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks[0][0].length; j++){
				int topY = this.findTopBlockY(i, blocks[0].length, j);
				if(blocks[i][topY][j].getBlockName().equals("dirt"))
					blocks[i][topY][j] = Block.getBlock("grass");
			}
		}
		
	}
	
	private void lerp(Vector3f pos1, Vector3f pos2, int r, String blockName){
		
		float unit = 1 / (pos2.sub(pos1)).length();
		
		for(float i = 0; i < 1; i+=unit){
			Vector3f temp1 = pos1.mul(1 - i).add(pos2.mul(i));

			int x = (int) (temp1.getX() + 0.5f);
			int y = (int) (temp1.getY() + 0.5f);
			int z = (int) (temp1.getZ() + 0.5f);
			
			int tempR = r;
			
			for(int posY = y; posY >= 64; posY--){
				for(int j = x - (r + (y - 64)); j < x + (r + (y - 64)); j++){
					for(int k = z - (r + (y - 64)); k < z + (r + (y - 64)); k++){
						if(Math.pow(j - x , 2) + Math.pow(k - z, 2) < Math.pow(tempR, 2)){
							this.setBlock(j, posY, k, blockName);
						}
					}
				}
				tempR++;
			}
		}
	}
	
	private void lerp2(Vector3f pos1, Vector3f pos2, int r, String blockName){
		
		float unit = 1 / (pos2.sub(pos1)).length();
		
		for(float i = 0; i < 1; i+=unit){
			Vector3f temp1 = pos1.mul(1 - i).add(pos2.mul(i));

			int x = (int) (temp1.getX() + 0.5f);
			int y = (int) (temp1.getY() + 0.5f);
			int z = (int) (temp1.getZ() + 0.5f);
			
			//int tempR = r;
			
			for(int i2 = x - r; i2 <= x + r; i2++){
				for(int j = y - r; j <= y + r; j++){
					for(int k = z - r; k < z + r; k++){
						if(Math.pow(i2 - x, 2) + Math.pow(j - y, 2) + Math.pow(k - z, 2) < Math.pow(r, 2))
							this.setBlock(i2, j, k, blockName);
					}
				}
			}
		}
		
	}
	
	private void setBlock(int x, int y, int z, String blockName){
		if(x >= 0 && y >= 0 && z >= 0 && x < blocks.length && y < blocks[0].length && z < blocks[0][0].length)
			if(!blocks[x][y][z].getBlockName().equals("bedrock")) blocks[x][y][z] = Block.blocks.get(blockName);
	}
	
	private int findTopBlockY(int x, int y, int z){
		for(int i = y - 1; i >= 0; i--){
			if(x >= 0 && x < blocks.length && z >= 0 && z < blocks[0][0].length && i >= 0 && i <= blocks[0].length ){
				if(!blocks[x][i][z].getBlockName().equals("air")){
					return i;
				}
			}
				
		}
		return -1;
	}
	
	public Block[][][] getBlocks(){
		return this.blocks;
	}
	
}
