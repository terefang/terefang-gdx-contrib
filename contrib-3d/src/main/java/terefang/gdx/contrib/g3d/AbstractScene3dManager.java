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
package terefang.gdx.contrib.g3d;

import java.util.List;

public class AbstractScene3dManager implements IScene3dManager
{
	List<IScene3dNodeFactory> sceneNodeFactories;
	
	@Override
	public List<IScene3dNodeFactory> getSceneNodeFactories()
	{
		return sceneNodeFactories;
	}
	
	@Override
	public void setSceneNodeFactories(List<IScene3dNodeFactory> sceneNodeFactories)
	{
		this.sceneNodeFactories = sceneNodeFactories;
	}
	
	@Override
	public void addSceneNodeFactory(IScene3dNodeFactory sceneNodeFactory)
	{
		this.sceneNodeFactories.add(sceneNodeFactory);
	}
	
	@Override
	public IScene3dNode addSceneNode(String typeName, IScene3dNode parent)
	{
		for(IScene3dNodeFactory f : this.sceneNodeFactories)
		{
			if(f.getTypeName().equalsIgnoreCase(typeName))
			{
				IScene3dNode n = f.createSceneNode(parent);
				n.setSceneManager(this);
				return n;
			}
		}
		return null;
	}
}
