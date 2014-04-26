package com.jaxfrank.main.voxelEngine.world.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.base.engine.math.Vector3i;
import com.base.engine.util.multiThread.ThreadCompleteListener;
import com.jaxfrank.main.voxelEngine.world.Chunk;
import com.sudoplay.joise.Joise;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.converter.JoiseJSONConverter;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleGradient;
import com.sudoplay.joise.module.ModuleSelect;
import com.sudoplay.joise.module.ModuleTranslateDomain;

public class WorldGenerator implements ThreadCompleteListener {
	
	private static WorldGenerator instance;
	
	private int seed;
	
	private HashMap<Vector3i, Double> map = new HashMap<>();
	public ConcurrentLinkedQueue<Vector3i> chunksToGenerate = new ConcurrentLinkedQueue<>();
	int numGenerators = 0;
	
	private Joise joise;
	
	private static final int numCores = Runtime.getRuntime().availableProcessors() / 2;
	
	public WorldGenerator(int seed, float bias, float scale, float exponent) {
		this.setSeed(seed);
		saveToFile("./res/world gen/generation.json");
		//loadFromFile("./res/world gen/generation.json");
	}
	
	public void update() {
		//System.out.println("Updating!");
		int numChunksToGenerate = chunksToGenerate.size();
		if(numChunksToGenerate > 0 ) {
			//System.out.println("Chunks to generate " + numChunksToGenerate + " numGenerators " + numGenerators);
			if(numGenerators == 0) {
				//int numThreadsToStart = ((numChunksToGenerate / 10.0f) != 0 ? (numChunksToGenerate / 10) + 1 : (numChunksToGenerate / 10));
				int numThreadsToStart = 3;
				//if(numThreadsToStart >= numCores)
				//	numThreadsToStart = numCores - 1;
				for(int i = 0; i < numThreadsToStart; i++) {
					ChunkGenerator thread = new ChunkGenerator();
					thread.addListener(this);
					new Thread(thread).start();
					numGenerators++;
				}
			}
		}
	}
	
	@Override
	public void notifyOfThreadComplete(Runnable thread) {
		numGenerators--;
	}
	
	public float getValue(Vector3i chunk, Vector3i block, float componentDivisor) {
		Vector3i globalBlockLocation = chunk.mul(Chunk.getSize()).add(block);
		double value = 0;
		if(!map.containsKey(globalBlockLocation)) {
			/*
			value = scaleBias.getValue( globalBlockLocation.getX() / componentDivisor, 
										globalBlockLocation.getY() / componentDivisor, 
										globalBlockLocation.getZ() / componentDivisor);
										*/
			value = joise.get(  globalBlockLocation.getX() / componentDivisor, 
								globalBlockLocation.getY() / componentDivisor, 
								globalBlockLocation.getZ() / componentDivisor);
			map.put(globalBlockLocation, value);
			
		} else {
			 value = map.get(globalBlockLocation);
		}
		return (float)value;
	}
	
	private void saveToFile(String location) {
		ModuleGradient groundGradient = new ModuleGradient();
		groundGradient.setGradient(0, 0, 0, 1, 0, 0);
		
		ModuleFractal groundShape = new ModuleFractal();
		groundShape.setAllSourceBasisTypes(BasisType.GRADIENT);
		groundShape.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
		groundShape.setType(FractalType.FBM);
		groundShape.setFrequency(0.75);
		groundShape.setNumOctaves(1);
		groundShape.setSeed(seed);
		
		ModuleTranslateDomain translate = new ModuleTranslateDomain();
		translate.setAxisYSource(groundShape);
		translate.setSource(groundGradient);
		
		ModuleSelect select = new ModuleSelect();
		select.setControlSource(translate);
		select.setLowSource(1);
		select.setHighSource(-1);
		select.setThreshold(0.5);
		select.setFalloff(0);
		
		joise = new Joise(select);
		JoiseJSONConverter converter = new JoiseJSONConverter();
		ModuleMap map = new ModuleMap();
		converter.toJson(map);
		String json = converter.toJson(joise.getModuleMap());
		try {
			FileOutputStream stream = new FileOutputStream(new File(location));
			stream.write(json.getBytes());
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unused")
	private void loadFromFile(String fileLocation) {
		JoiseJSONConverter converter = new JoiseJSONConverter();
		try {
			joise = new Joise(converter.fromJson(new FileReader((new File(fileLocation)))));
			//System.out.println(joise.get(10, 10, 10));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	Object baton = new Object();
	public Joise getJoise() {
		synchronized(baton) {
			return joise;
		}
	}
	
	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}
}
