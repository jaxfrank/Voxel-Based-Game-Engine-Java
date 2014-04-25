package com.base.engine.ui.components;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import com.base.engine.input.Input;
import com.base.engine.math.Vector2f;
import com.base.engine.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.TextureAtlas;
import com.base.engine.rendering.Transform;
import com.base.engine.rendering.Vertex;
import com.base.engine.ui.listeners.IActionListener;

public class CButton extends CRegion {

	private Transform transform;
	private Mesh mesh;
	private Material mat;
	
	private State state = State.NORMAL;
	private boolean beenPressed = false;
	
	private ArrayList<IActionListener> actionListeners = new ArrayList<>();
	
	private TextureAtlas atlas;
	
	public CButton(Vector2f position, Vector2f dimensions, State startingState, String path, String type){
		super(position, dimensions);
		this.state = startingState;
		atlas = new TextureAtlas(path, type, 16, true);
//		transform = new Transform();
//		transform.setTranslation(position.getX(), position.getY(), 0);
//		buildMesh();
//		mat = new Material(new Texture(atlas.getSpriteSheet().getTextureID()));
	}
	
	public CButton(Vector2f position, Vector2f dimensions, String path, String type){
		this(position, dimensions, State.NORMAL, path, type);
	}
	
	public CButton(Vector2f position, Vector2f dimensions){
		this(position, dimensions, State.NORMAL, "./res/gui", "png");
	}
	
	public CButton(Vector2f position, Vector2f dimensions, State startingState) {
		this(position, dimensions, startingState, "./res/gui", "png");
	}
	
	@Override
	public void update() {
		if(state != State.DISABLED) {
			updateRegionState();
			
			if(hovered && state != State.HOVERED)
				state = State.HOVERED;
			else if(!hovered && state != State.NORMAL)
				state = State.NORMAL;
			
			if(state == State.HOVERED && Input.getMouse(0) && !beenPressed){
				beenPressed = true;
				state = State.PRESSED;
				for(IActionListener actionListener : actionListeners){
					actionListener.actionPerformed();
				}
			} else if(beenPressed && !Input.getMouse(0))
				beenPressed = false;
		}
		
	}

	@Override
	public void render() {
		float[] coords = atlas.getTexCoordsF("BUTTON_" + state.name());

		glBegin(GL_QUADS);
//		atlas.bind();
//		BasicShader.getInstance().updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), mat);
//		mesh.draw();
		glTexCoord2f(coords[0], coords[1]);
		glVertex2f(getPosition().getX(), 
					getPosition().getY());
//		
		glTexCoord2f(coords[2], coords[3]);
		glVertex2f(getPosition().getX(), 
					getPosition().getY() + getDimensions().getY());
		
		glTexCoord2f(coords[4], coords[5]);
		glVertex2f(getPosition().getX() + getDimensions().getX(), 
					getPosition().getY() + getDimensions().getY());
		
		glTexCoord2f(coords[6], coords[7]);
		glVertex2f(getPosition().getX() + getDimensions().getX(), 
					getPosition().getY());
		glEnd();
		
	}

	public void addActionListener(IActionListener listener){
		actionListeners.add(listener);
	}
	
	public void removeActionListener(IActionListener listener) {
		actionListeners.remove(listener);
	}
	
	public enum State{
		NORMAL,
		HOVERED,
		PRESSED,
		DISABLED
	}
	
	private void buildMesh(){
		Vertex[] verts = new Vertex[4];
		verts[0] = new Vertex(new Vector3f(getPosition(), 0));
		verts[1] = new Vertex(new Vector3f(getPosition().add(new Vector2f(0, getDimensions().getY())), 0));
		verts[2] = new Vertex(new Vector3f(getPosition().add(new Vector2f(getDimensions().getX(), getDimensions().getY())), 0));
		verts[3] = new Vertex(new Vector3f(getPosition().add(new Vector2f(getDimensions().getX(),0)), 0));
		int[] indices = new int[]{
			0,1,2,
			2,1,3
		};
		mesh = new Mesh(verts, indices);
	}
	
}
