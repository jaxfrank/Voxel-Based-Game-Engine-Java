package com.jaxfrank.main.physics;

import com.base.engine.math.Vector3f;
import com.base.engine.rendering.Transform;

public class Collider {

	private Vector3f pos;
	
	public Collider(Vector3f pos){
		this.pos = pos;
	}

	public Vector3f getPosition() {
		return pos;
	}

	public void setTransform(Vector3f pos) {
		this.pos = pos;
	}
}
