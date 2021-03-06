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

import vendalenger.kondion.Kondion;
import vendalenger.kondion.kobj.GKO_RenderPass;
import vendalenger.kondion.lwjgl.resource.KTexture;
import vendalenger.kondion.materials.KMat_FlatColor;
import vendalenger.kondion.materials.KMat_Monotexture;
import vendalenger.kondion.materials.KMat_erial;


public abstract class KObj_Renderable extends KObj_Oriented {
	
	public boolean hidden = false;
	public float fogIntensity;
	protected KMat_erial material = new KMat_Monotexture();
	public int id;
	
	public KObj_Renderable() {
		this(1);
	}
	
	public KObj_Renderable(int id) {
		fogIntensity = 0.0f;
		
		this.id = id;
		for (GKO_RenderPass rp : Kondion.getWorld().passes) {
			if (rp.auto) {
				rp.consider(this);
			}
		}
	}

	public KMat_erial getMaterial() {
		return material;
	}
	
	public void hideAll(boolean b) {
		hidden = b;
		for (KObj_Node child : children) {
			if (child instanceof KObj_Renderable) {
				((KObj_Renderable) child).hideAll(b);
			}
		}
	}

	public void setMaterial(KMat_erial material) {
		this.material = material;
	}

	public abstract void render(int type, GKO_RenderPass pass);
	
}
