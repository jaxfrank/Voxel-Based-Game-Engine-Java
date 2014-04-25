package com.base.engine.rendering.shader;

import com.base.engine.math.Matrix4f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.util.RenderUtil;

public class BasicShader extends Shader
{
	private static final BasicShader instance = new BasicShader();
	
	public static BasicShader getInstance()
	{
		return instance;
	}
	
	private BasicShader()
	{
		super();
		
		addVertexShaderFromFile("basicVertex.vs");
		addFragmentShaderFromFile("basicFragment.fs");
		compileShader();
		
		addUniform("transform");
		addUniform("color");
	}
	
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material)
	{
		if(material.getTexture() != null)
			material.getTexture().bind();
		else
			RenderUtil.unbindTextures();
		
		setUniform("transform", projectedMatrix);
		setUniform("color", material.getColor());
	}
}
