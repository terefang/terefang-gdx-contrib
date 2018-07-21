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

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by fredo on 03.07.17.
 */
public class Stage3D  extends InputAdapter implements Disposable
{
	private final Viewport viewport;
	
	public Stage3D(Viewport viewport)
	{
		this.viewport = viewport;
	}
	
	public Viewport getViewport()
	{
		return viewport;
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		return super.keyDown(keycode);
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		return super.keyUp(keycode);
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		return super.keyTyped(character);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return super.touchDragged(screenX, screenY, pointer);
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return super.mouseMoved(screenX, screenY);
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		return super.scrolled(amount);
	}
	
	public void dispose()
	{
		
	}
	
}
