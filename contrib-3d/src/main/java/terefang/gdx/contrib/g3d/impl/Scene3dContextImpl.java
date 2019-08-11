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
package terefang.gdx.contrib.g3d.impl;

import com.badlogic.gdx.graphics.Camera;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import terefang.gdx.contrib.g3d.IScene3dContext;

public class Scene3dContextImpl extends ScreenViewport implements IScene3dContext
{
	public static Scene3dContextImpl create(Camera camera)
	{
		Scene3dContextImpl s = new Scene3dContextImpl(camera);
		return s;
	}
	
	Scene3dContextImpl(Camera camera)
	{
		super(camera);
	}
	
	@Override
	public Matrix4 getViewMatrix()
	{
		return this.getCamera().view;
	}
	
	@Override
	public Matrix4 getProjectionMatrix()
	{
		return this.getCamera().projection;
	}
	
	@Override
	public void resize(int width, int height)
	{
		this.update(width, height);
	}
	
	
	Environment environment;
	
	public Environment getEnvironment()
	{
		return environment;
	}
	
	@Override
	public Viewport getViewport()
	{
		return this;
	}
	
	@Override
	public void setEnvironment(Environment environment)
	{
		this.environment = environment;
	}
	
	
	public Vector2 getScreenCoordinatesFrom3DPosition(Vector3 pWorld)
	{
		final int imageWidth = this.getScreenWidth();
		final int imageHeight = this.getScreenHeight();
		
		this.getCamera().update();
		
		Vector3 pScreen = this.getCamera().project(pWorld, 0,0, imageWidth, imageHeight);
		
		if(pScreen.z > 1f)
		{
			return null;
		}
		return new Vector2(pScreen.x, pScreen.y);
	}
	 
}
