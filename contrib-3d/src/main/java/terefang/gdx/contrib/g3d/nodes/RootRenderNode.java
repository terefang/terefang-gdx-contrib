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

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import terefang.gdx.contrib.g3d.*;

public class RootRenderNode extends AbstractScene3dNode<RootRenderNode> implements IScene3dNode<RootRenderNode>
{
	public static final String TYPE_NAME = "root";

	@Override
	public RootRenderNode clone(IScene3dNode node, IScene3dManager mgr)
	{
		return null;
	}
	
	@Override
	public RootRenderNode getNode()
	{
		return null;
	}
	
	@Override
	public void render(IScene3dContext sctx, int rp)
	{
		super.render(sctx, rp);
	}
	
	@Override
	public void renderNode(IScene3dContext vp, int rp)
	{
		// IGNORE
	}
	
	public void renderAll(IScene3dContext sctx)
	{
		for(int i = 0 ; i < 32; i++)
		{
			this.render(sctx, (1<<i));
		}
	}
	
	public void renderAllAtOnce(IScene3dContext sctx)
	{
		this.render(sctx, EScene3dRenderPass.RP_ALL_AT_ONCE.getValue());
	}
	
	public static class Factory implements IScene3dNodeFactory<RootRenderNode>
	{
		
		@Override
		public RootRenderNode createSceneNode(IScene3dNode parent)
		{
			RootRenderNode n = new RootRenderNode();
			n.setTypeName(TYPE_NAME);
			n.setRelativeRotation(new Vector3(0f, 0f, 0f));
			n.setRelativeScale(new Vector3(1f, 1f, 1f));
			n.setRelativeTranslation(new Vector3(0f, 0f, 0f));
			n.setBoundingBox(new BoundingBox());
			n.getBoundingBox().set(new Vector3(0f, 0f, 0f), new Vector3(0f, 0f, 0f));
			return n;
		}
		
		@Override
		public String getTypeName()
		{
			return TYPE_NAME;
		}
	}
}
