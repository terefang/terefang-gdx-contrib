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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import terefang.gdx.contrib.g3d.*;

public class TextNode extends AbstractScene3dNode<TextNode> implements IScene3dNode<TextNode>
{
	public static final String TYPE_NAME = "Text";
	@Override
	public TextNode clone(IScene3dNode node, IScene3dManager mgr)
	{
		return null;
	}
	SpriteBatch batch = new SpriteBatch();
	@Override
	public void render(IScene3dViewport vp)
	{
		this.updateAbsolutePosition();
		
		Vector2 pos = vp.getScreenCoordinatesFrom3DPosition(this.getAbsolutePosition(new Vector3()));
		this.batch.begin();
		this.getFont().draw(vp, this.batch, this.getText(), this.getTextColor(), pos.x, pos.y);
		this.batch.end();
		
		
		super.render(vp);
	}
	
	String text;
	Color textColor;
	IScene3dFont font;
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public Color getTextColor()
	{
		return textColor;
	}
	
	public void setTextColor(Color textColor)
	{
		this.textColor = textColor;
	}
	
	public IScene3dFont getFont()
	{
		return font;
	}
	
	public void setFont(IScene3dFont font)
	{
		this.font = font;
	}
	
	@Override
	public TextNode getNode()
	{
		return this;
	}
	
	public static class Factory implements IScene3dNodeFactory<TextNode>
	{
		
		@Override
		public TextNode createSceneNode(IScene3dNode parent)
		{
			TextNode n = new TextNode();
			n.setTypeName(TYPE_NAME);
			n.setRelativeRotation(new Vector3(0f, 0f, 0f));
			n.setRelativeScale(new Vector3(1f, 1f, 1f));
			n.setRelativeTranslation(new Vector3(0f, 0f, 0f));
			n.setBoundingBox(new BoundingBox());
			n.getBoundingBox().set(new Vector3(0f, 0f, 0f), new Vector3(0f, 0f, 0f));
			if(parent!=null)
			{
				parent.addChild(n);
			}
			return n;
		}
		
		@Override
		public String getTypeName()
		{
			return TYPE_NAME;
		}
	}
}
