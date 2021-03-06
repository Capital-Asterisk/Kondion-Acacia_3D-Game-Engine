/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.kondion.objectbase;

import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Vector3f;

public abstract class KObj_Oriented extends KObj_Node {
	
	//public static final int 
	public boolean transferScale = true;
	protected final Vector3f temp0 = new Vector3f();
	public final Matrix4f transform = new Matrix4f();
	public final Matrix4f actTransform = new Matrix4f();
	
	public abstract void updateB();
	
	public void applyTransform() {
		//transform.
		actTransform.identity();
		if (parent instanceof KObj_Oriented) {
			((KObj_Oriented) parent).multiplyByAct(actTransform);
			if (!transferScale) {
				actTransform.normalize3x3();
			}
		}
		//System.out.println("PARENT: " + parent);
		actTransform.mul(transform);
	}
	
	public void defaultUpdateB() {
		if (this.s != null) {
			this.s.setMember("obj", this);
			if (this.s.containsKey("onupdateb")) {
				this.s.callMember("onupdateb");
			}
		}
	}
	
	public void stretchSimple(float fx, float fy, float fz, float tx, float ty, float tz, float thickness) {
		transform.identity();
		
		temp0.set(fx - tx, fy - ty, fz - tz);
		//temp0.length();
		
		transform.m30 = fx - temp0.x / 2;
		transform.m31 = fy - temp0.y / 2;
		transform.m32 = fz - temp0.z / 2;
		float b = temp0.length();
		float a = (float) Math.sqrt(temp0.x * temp0.x + temp0.z * temp0.z);
		transform.rotateY((float) Math.atan2(transform.m30 - fx, transform.m32 - fz));
		transform.rotateX((float) (Math.atan(temp0.y / a) + Math.PI / 2));
		transform.scale(thickness, b, thickness);
		
		applyTransform();
	}
	
	public void dir(Vector3f in, float x, float y, float z, float amt, boolean local) {
		temp0.set(x, y, z);
		(local ? transform : actTransform).transformDirection(temp0);
		temp0.mul(amt);
		in.add(temp0);
	}
	
	public void dir(Matrix4f in, float x, float y, float z, float amt, boolean local) {
		temp0.set(x, y, z);
		(local ? transform : actTransform).transformDirection(temp0);
		temp0.mul(amt);
		in.m30 += temp0.x;
		in.m31 += temp0.y;
		in.m32 += temp0.z;
	}
	
	public void moveTo(Matrix4f tgt) {
		transform.m30 = tgt.m30;
		transform.m31 = tgt.m31;
		transform.m32 = tgt.m32;
	}
	
	public void moveTo(Vector3f tgt) {
		transform.m30 = tgt.x;
		transform.m31 = tgt.y;
		transform.m32 = tgt.z;
	}
	
	public void moveTo(float x, float y, float z) {
		transform.m30 = x;
		transform.m31 = y;
		transform.m32 = z;
	}
	
	public void eularYXZ(float y, float x, float z) {
		transform.setRotationYXZ(y, x, z);
	}
	
	public void multiplyByAct(Matrix4f h) {
		h.mul(actTransform);
	}
	
	public float getOffsetX() {
		return transform.m30;
	}
	
	public float getOffsetY() {
		return transform.m31;
	}
	
	public float getOffsetZ() {
		return transform.m32;
	}
	
	public void setX(float x) {
		transform.m30 = x;
	}
	
	public void setY(float y) {
		transform.m31 = y;
	}
	
	public void setZ(float z) {
		transform.m32 = z;
	}
	
	public float distance(KObj_Oriented o) {
		temp0.set(actTransform.m30, actTransform.m31, actTransform.m32);
		temp0.sub(o.actTransform.m30, o.actTransform.m31, o.actTransform.m32);
		return temp0.length();
	}
	
	public void point(Vector3f dest, KObj_Oriented o) {
		dest.set(o.actTransform.m30, o.actTransform.m31, o.actTransform.m32);
		dest.sub(actTransform.m30, actTransform.m31, actTransform.m32);
		dest.normalize();
	}
	
}
