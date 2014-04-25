package com.jaxfrank.main.voxelEngine.rendering.util;

public class TextureUtil {

	public static float[] calcTexCoords(int numTextures, int numTextureExponent, int[] indices){
		float[] result = new float[indices.length * 6];
		for(int i = 0; i < indices.length; i++){
			float[] tempCoords = calcTexCoord(numTextures, numTextureExponent, indices[i]);
			result[i * 4] = tempCoords[0];
			result[i * 4 + 1] = tempCoords[1];
			result[i * 4 + 2] = tempCoords[2];
			result[i * 4 + 3] = tempCoords[3];
		}
		
		return result;
	}
	
	public static float[] calcTexCoord(int numTextures, int numTextureExponent, int index){
		int texCoord = numTextures - index;
		
		int texX = texCoord % numTextureExponent;
		int texY = texCoord / numTextureExponent;

		float[] result = new float[4];

		result[1] = 1f - (float)texX/(float)16;
		result[0] = result[1] - 1f/(float)16;
		result[2] = 1f - (float)texY/(float)16;
		result[3] = result[2] - 1f/(float)16;
		
		return result;
	}
}
