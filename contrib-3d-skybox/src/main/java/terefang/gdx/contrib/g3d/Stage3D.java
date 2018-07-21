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

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by fredo on 03.07.17.
 */
public class Stage3D  implements InputProcessor, Disposable
{
	Viewport viewport;
	
	InputProcessor inputProcessor;
	
	public Stage3D()
	{
		super();
	}
	
	public static final Stage3D create() { return new Stage3D(); }
	
	public Viewport getViewport()
	{
		return viewport;
	}
	
	public void setViewport(Viewport viewport)
	{
		this.viewport = viewport;
	}
	
	public Stage3D viewport(Viewport viewport)
	{
		this.setViewport(viewport);
		return this;
	}
	
	public InputProcessor getInputProcessor()
	{
		return inputProcessor;
	}
	
	public void setInputProcessor(InputProcessor inputProcessor)
	{
		this.inputProcessor = inputProcessor;
	}
	
	public Stage3D inputProcessor(InputProcessor inputProcessor)
	{
		this.setInputProcessor(inputProcessor);
		return this;
	}
	
	@Override
	public boolean keyDown(int i)
	{
		return inputProcessor.keyDown(i);
	}
	
	@Override
	public boolean keyUp(int i)
	{
		return inputProcessor.keyUp(i);
	}
	
	@Override
	public boolean keyTyped(char c)
	{
		return inputProcessor.keyTyped(c);
	}
	
	@Override
	public boolean touchDown(int i, int i1, int i2, int i3)
	{
		return inputProcessor.touchDown(i, i1, i2, i3);
	}
	
	@Override
	public boolean touchUp(int i, int i1, int i2, int i3)
	{
		return inputProcessor.touchUp(i, i1, i2, i3);
	}
	
	@Override
	public boolean touchDragged(int i, int i1, int i2)
	{
		return inputProcessor.touchDragged(i, i1, i2);
	}
	
	@Override
	public boolean mouseMoved(int i, int i1)
	{
		return inputProcessor.mouseMoved(i, i1);
	}
	
	@Override
	public boolean scrolled(int i)
	{
		return inputProcessor.scrolled(i);
	}
	
	public void resize(int width, int height)
	{
		this.getViewport().update(width,height);
	}
	
	public void update()
	{
		
	}
	
	public void dispose()
	{
		
	}
	
}
