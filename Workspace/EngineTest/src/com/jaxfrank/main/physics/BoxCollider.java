package com.jaxfrank.main.physics;

import com.base.engine.math.Vector3f;

public class BoxCollider extends Collider{
	
	private Vector3f size;
	
	public BoxCollider(Vector3f pos, Vector3f size) {
		super(pos);
		this.setSize(size);
	}

	public Vector3f getSize() {
		return size;
	}

	public void setSize(Vector3f size) {
		this.size = size;
	}
	
	public boolean intersects(BoxCollider b){
		Vector3f pos1 = getPosition();
		Vector3f pos2 = b.getPosition();
		Vector3f size1 = getSize();
		Vector3f size2 = b.getSize();
		
		return (pos1.getX() < (pos2.getX() + size2.getX()) &&
			   (pos1.getX() + size1.getX()) > pos2.getX()  &&
				pos1.getY() < (pos2.getY() + size2.getY()) &&
			   (pos1.getY() + size1.getY()) > pos2.getY()  &&
				pos1.getZ() < (pos2.getZ() + size2.getZ()) &&
			   (pos1.getZ() + size1.getZ()) > pos2.getZ());
	}
	
	public Vector3f collisionDirs(BoxCollider b){
		
		Vector3f result = new Vector3f(1,1,1);
		if(intersects(b))
			result = getPosition().sub(b.getPosition()).normalized();
//		if(intersects(b)){
//			if(b.getPosition().getX() > getPosition().getX())
//				result.setX(0);
//			if(b.getPosition().getY() > getPosition().getY())
//				result.setY(0);
//			if(b.getPosition().getZ() > getPosition().getZ())
//				result.setZ(0);
//		}
		
		return result;
	}
	
}
