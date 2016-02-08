package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.materials.KMat_FlatColor;
import vendalenger.kondion.materials.KMat_MultiTexture;
import vendalenger.kondion.materials.KMat_Strange;
import vendalenger.kondion.objectbase.CollisionData;
import vendalenger.kondion.objectbase.KObj_Solid;

public class SKO_Cube extends KObj_Solid {
	
	KondionShader eggs;
	FloatBuffer buffer;
	
	public SKO_Cube() {
		super();
		//eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		material = new KMat_FlatColor();
		buffer = null;
	}

	@Override
	public void update() {
		defaultUpdate();
		transform.rotate(rotVelocity);
		rotVelocity.w *= 1.02f;
		//rotVelocity.x *= 0.992;
		//rotVelocity.y *= 0.992;
		//rotVelocity.z *= 0.992;
		//velocity.y = -0.3f;
		rotVelocity.normalize();
		transform.m30 += velocity.x * Kondion.getDelta();
		transform.m31 += velocity.y * Kondion.getDelta();
		transform.m32 += velocity.z * Kondion.getDelta();
		//rotVelocity.
	}
	

	@Override
	public CollisionData collisionCheck(KObj_Solid kobj) {
		if (kobj instanceof SKO_InfinitePlane) {
			
		}
		return null;
	}

	@Override
	public void render(int type) {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		
		buffer.clear();
		actTransform.get(buffer);
		
		glPushMatrix();
		glLoadIdentity();
		glMultMatrixf(buffer);
		//eggs.useProgram();
		if (material != null)
			material.bind();
		GLDrawing.renderCube(1, KondionLoader.textures.get("K_Cube"));
		//GLDrawing.renderQuad(1, 1, KondionLoader.textures.get("K_Cube"));
		//KondionShader.unbind();
		if (material != null)
			material.unbind();
		glPopMatrix();
	}

	@Override
	public void updateB() {
		// TODO Auto-generated method stub
		
	}
}
