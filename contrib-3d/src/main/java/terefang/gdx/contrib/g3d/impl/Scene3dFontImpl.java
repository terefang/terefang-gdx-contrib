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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import terefang.gdx.contrib.g3d.IScene3dFont;
import terefang.gdx.contrib.g3d.IScene3dContext;

public class Scene3dFontImpl implements IScene3dFont
{
	BitmapFont font;
	
	public static Scene3dFontImpl create(BitmapFont font)
	{
		Scene3dFontImpl f = new Scene3dFontImpl();
		f.setFont(font);
		return f;
	}
	
	@Override
	public void draw(IScene3dContext vp, Batch batch, String text, Color textColor, float x, float y)
	{
		this.font.setColor(textColor);
		this.font.draw(batch, text, x, y);
	}
	
	public BitmapFont getFont()
	{
		return font;
	}
	
	public void setFont(BitmapFont font)
	{
		this.font = font;
	}
}
