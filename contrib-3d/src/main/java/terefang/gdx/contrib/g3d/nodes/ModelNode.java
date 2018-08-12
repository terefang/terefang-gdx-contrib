/*
 * Copyright (c) 2018. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package terefang.gdx.contrib.g3d.nodes;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import terefang.gdx.contrib.g3d.*;

public class ModelNode extends AbstractScene3dNode<ModelNode>
{
	public static final String TYPE_NAME = "Model";
	@Override
	public ModelNode clone(IScene3dNode node, IScene3dManager mgr)
	{
		return null;
	}
	
	@Override
	public ModelNode getNode()
	{
		return this;
	}
	
	ModelBatch modelBatch = new ModelBatch();
	
	public ModelBatch getModelBatch()
	{
		return modelBatch;
	}
	
	public void setModelBatch(ModelBatch modelBatch)
	{
		this.modelBatch = modelBatch;
	}
	
	ModelInstance modelInstance;
	
	public ModelInstance getModelInstance()
	{
		return modelInstance;
	}
	
	public void setModelInstance(ModelInstance modelInstance)
	{
		this.modelInstance = modelInstance;
	}
	
	@Override
	public void render(IScene3dViewport vp)
	{
		this.updateAbsolutePosition();
		
		this.getModelInstance().transform.set(this.getAbsoluteTransformation());
		
		this.modelBatch.begin(vp.getCamera());
		this.modelBatch.render(this.getModelInstance(), vp.getEnvironment());
		this.modelBatch.end();
		
		super.render(vp);
	}
	
	public static class Factory implements IScene3dNodeFactory<ModelNode>
	{
		
		@Override
		public ModelNode createSceneNode(IScene3dNode parent)
		{
			ModelNode mn = new ModelNode();
			mn.setTypeName(TYPE_NAME);
			if(parent!=null)
			{
				parent.addChild(mn);
			}
			return mn;
		}
		
		@Override
		public String getTypeName()
		{
			return TYPE_NAME;
		}
	}
}
