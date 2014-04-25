package com.base.engine.ui;

import java.util.ArrayList;

import com.base.engine.ui.components.UIComponent;

public class UI {
	
	private ArrayList<UIComponent> components = new ArrayList<>();
	
	public UI(){
		init();
	}
	
	public void init() {}
	
	public void update(){
		for(UIComponent component : components){
			if(component != null)
				component.update();
		}
	}
	
	public void render(){
		for(UIComponent component : components){
			if(component != null)
				component.render();
		}
	}

	public int addComponent(UIComponent component){
		components.add(component);
		return components.indexOf(component);
	}
	
	public void removeComponent(UIComponent component){
		components.remove(component);
	}
	
	public void removeComponent(int index){
		components.remove(index);
	}
	
	public UIComponent getComponent(int index) {
		return components.get(index);
	}
	
}
