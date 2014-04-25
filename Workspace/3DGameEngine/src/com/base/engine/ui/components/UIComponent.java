package com.base.engine.ui.components;

import com.base.engine.math.Vector2f;

public class UIComponent {
	
	private Vector2f position;
	
	public UIComponent(Vector2f position){
		this.setPosition(position);
	}
	
	public void update(){
		
	}
	
	public void render(){
		
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
}
