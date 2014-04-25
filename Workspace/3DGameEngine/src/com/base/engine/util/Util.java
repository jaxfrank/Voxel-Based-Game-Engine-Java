package com.base.engine.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.BufferUtils;

import com.base.engine.math.Matrix4f;
import com.base.engine.math.Vector3i;
import com.base.engine.rendering.Vertex;

public class Util
{
	public static FloatBuffer createFloatBuffer(int size)
	{
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIntBuffer(int size)
	{
		return BufferUtils.createIntBuffer(size);
	}
	
	public static IntBuffer createFlippedBuffer(int... values)
	{
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vertex[] vertices)
	{
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getPos().getX());
			buffer.put(vertices[i].getPos().getY());
			buffer.put(vertices[i].getPos().getZ());
			buffer.put(vertices[i].getTexCoord().getX());
			buffer.put(vertices[i].getTexCoord().getY());
			buffer.put(vertices[i].getNormal().getX());
			buffer.put(vertices[i].getNormal().getY());
			buffer.put(vertices[i].getNormal().getZ());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f value)
	{
		FloatBuffer buffer = createFloatBuffer(4 * 4);
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				buffer.put(value.get(i, j));
		
		buffer.flip();
		
		return buffer;
	}
	
	public static String[] removeEmptyStrings(String[] data)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < data.length; i++)
			if(!data[i].equals(""))
				result.add(data[i]);
		
		String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	public static int[] toIntArray(Integer[] data)
	{
		int[] result = new int[data.length];
		
		for(int i = 0; i < data.length; i++)
			result[i] = data[i].intValue();
		
		return result;
	}
	
	public static Vector3i stringToVec3i(String vec){
		String[] splitVec = vec.split(",");
		
		return new Vector3i(Integer.parseInt(splitVec[0]), Integer.parseInt(splitVec[1]), Integer.parseInt(splitVec[2]));
	}
	
	public static <T> void fromArrayToCollection(T[] a, Collection<T> c) {
	    for (T o : a) {
	        c.add(o);
	    }
	}
}