package com.base.engine.ui.components;

import java.util.ArrayList;

import com.base.engine.input.Input;
import com.base.engine.math.Vector2f;
import com.base.engine.ui.listeners.IRegionListener;
import com.base.engine.util.Window;

public class CRegion extends UIComponent{
	
	private Vector2f dimensions;
	
	private ArrayList<IRegionListener> regionListeners = new ArrayList<>();
	
	protected boolean hovered = false;
	
	public CRegion(Vector2f position, Vector2f dimensions) {
		super(position);
		setDimensions(dimensions);
	}
	
	@Override
	public void update(){
		updateRegionState();
	}
	
	protected void updateRegionState(){
		Vector2f mousePos = Input.getMousePosition().div(new Vector2f(Window.getWidth(), Window.getHeight()));
		
		if( mousePos.getX() > getPosition().getX() && 
			mousePos.getX() < getPosition().getX() + getDimensions().getX() && 
			mousePos.getY() > getPosition().getY() && 
			mousePos.getY() < getPosition().getY() + getDimensions().getY() && 
			!hovered){
			hovered = true;
			for(IRegionListener listener : regionListeners){
				listener.onMouseEnter();
			}
		} else if(  mousePos.getX() > getPosition().getX() && 
					mousePos.getX() < getPosition().getX() + getDimensions().getX() && 
					mousePos.getY() > getPosition().getY() && 
					mousePos.getY() < getPosition().getY() + getDimensions().getY() && 
					hovered){
			for(IRegionListener listener : regionListeners){
				listener.onMouseHover();
			}
		} else if(hovered){
			hovered = false;
			for(IRegionListener listener : regionListeners){
				listener.onMouseExit();
			}
		}
	}
	
	public Vector2f getDimensions() {
		return dimensions;
	}

	public void setDimensions(Vector2f dimensions) {
		this.dimensions = dimensions;
	}
	
	public void addRegionListener(IRegionListener listener){
		regionListeners.add(listener);
	}

}
