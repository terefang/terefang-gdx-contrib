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

import terefang.gdx.contrib.g3d.*;
import terefang.gdx.contrib.g3d.skybox.SkyBox;

public class SkyBoxNode extends AbstractScene3dNode<SkyBoxNode>
{
	public static final String TYPE_NAME = "SkyBox";
	SkyBox skybox;
	
	public SkyBox getSkybox()
	{
		return skybox;
	}
	
	public void setSkybox(SkyBox skybox)
	{
		this.skybox = skybox;
	}
	
	@Override
	public SkyBoxNode clone(IScene3dNode node, IScene3dManager mgr)
	{
		return null;
	}
	
	@Override
	public void render(IScene3dViewport vp)
	{
		this.skybox.render(vp.getCamera());
		
		super.render(vp);
	}
	
	@Override
	public SkyBoxNode getNode()
	{
		return this;
	}
	
	public static class Factory implements IScene3dNodeFactory<SkyBoxNode>
	{
		
		@Override
		public SkyBoxNode createSceneNode(IScene3dNode parent)
		{
			SkyBoxNode sbn = new SkyBoxNode();
			sbn.setTypeName(TYPE_NAME);
			if(parent!=null)
			{
				parent.addChild(sbn);
			}
			return sbn;
		}
		
		@Override
		public String getTypeName()
		{
			return TYPE_NAME;
		}
	}
}
