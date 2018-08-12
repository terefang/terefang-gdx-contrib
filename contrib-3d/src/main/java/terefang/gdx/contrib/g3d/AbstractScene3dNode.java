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

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.Collection;
import java.util.Vector;

public abstract class AbstractScene3dNode<T extends IScene3dNode>
		extends AbstractScene3dTyped
		implements IScene3dNode<T>
{
	IScene3dNode parent;
	
	@Override
	public IScene3dNode getParent()
	{
		return parent;
	}
	
	@Override
	public void setParent(IScene3dNode parent)
	{
		this.parent = parent;
	}
	
	Collection<IScene3dNode> children = new Vector();
	
	@Override
	public void setChildren(Collection<IScene3dNode> children)
	{
		this.children = children;
	}
	
	@Override
	public Collection<IScene3dNode> getChildren()
	{
		return this.children;
	}
	
	@Override
	public void addChild(IScene3dNode newChild)
	{
		if(newChild!=null && !this.equals(newChild))
		{
			newChild.setSceneManager(this.getSceneManager());
			this.children.add(newChild);
			newChild.setParent(this);
		}
	}
	
	@Override
	public boolean removeChild(IScene3dNode child)
	{
		if(this.children.contains(child))
		{
			child.setParent(null);
			return this.children.remove(child);
		}
		return false;
	}
	
	@Override
	public void remove()
	{
		if(this.parent!=null)
		{
			this.parent.removeChild(this);
		}
	}
	
	boolean visible;
	
	@Override
	public boolean isVisible()
	{
		return visible;
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	Vector3 relativeRotation = new Vector3(0,0,0);
	Vector3 relativeScale = new Vector3(1,1,1);
	Vector3 relativeTranslation = new Vector3(0,0,0);
	
	@Override
	public Vector3 getRelativeRotation()
	{
		return relativeRotation;
	}
	
	@Override
	public void setRelativeRotation(Vector3 relativeRotation)
	{
		this.relativeRotation = relativeRotation;
	}
	
	@Override
	public Vector3 getRelativeScale()
	{
		return relativeScale;
	}
	
	@Override
	public void setRelativeScale(Vector3 relativeScale)
	{
		this.relativeScale = relativeScale;
	}
	
	@Override
	public Vector3 getRelativeTranslation()
	{
		return relativeTranslation;
	}
	
	@Override
	public void setRelativeTranslation(Vector3 relativeTranslation)
	{
		this.relativeTranslation = relativeTranslation;
	}
	
	Matrix4 absoluteTransformation;
	
	@Override
	public Matrix4 getAbsoluteTransformation()
	{
		return absoluteTransformation;
	}
	
	@Override
	public void setAbsoluteTransformation(Matrix4 absoluteTransformation)
	{
		this.absoluteTransformation = absoluteTransformation;
	}
	
	@Override
	public void updateAbsolutePosition()
	{
		if(this.getParent()!=null && (this.getParent() instanceof IScene3dPlaceable))
		{
			this.getParent().updateAbsolutePosition();
			this.absoluteTransformation = ((IScene3dPlaceable)this.getParent()).getAbsoluteTransformation().mul(this.getRelativeTransformation());
		}
		else
		{
			this.absoluteTransformation = this.getRelativeTransformation();
		}
	}
	
	@Override
	public Matrix4 getRelativeTransformation()
	{
		Matrix4 mat = new Matrix4();
		mat.setToRotation(this.relativeRotation, 1f);
		mat.setTranslation(this.relativeTranslation);
		if(!this.relativeScale.epsilonEquals(1.f,1.f,1.f))
		{
			mat = mat.mul(new Matrix4().setToScaling(this.relativeScale));
		}
		return mat;
	}
	
	@Override
	public Vector3 getAbsolutePosition(Vector3 v)
	{
		return this.getAbsoluteTransformation().getTranslation((v==null) ? new Vector3() : v);
	}
	
	BoundingBox boundingBox;
	
	@Override
	public BoundingBox getBoundingBox()
	{
		return boundingBox;
	}
	
	@Override
	public void setBoundingBox(BoundingBox boundingBox)
	{
		this.boundingBox = boundingBox;
	}
	
	@Override
	public BoundingBox getTransformedBoundingBox()
	{
		BoundingBox box = new BoundingBox(this.getBoundingBox());
		return box.mul(this.getAbsoluteTransformation());
	}
	
	Collection<IScene3dNodeAnimator> sceneNodeAnimators;
	
	@Override
	public void onAnimate(long timeMs)
	{
		for(IScene3dNodeAnimator ani : this.sceneNodeAnimators)
		{
			ani.animateNode(this, timeMs);
		}
		this.updateAbsolutePosition();
		for(IScene3dNode ch : this.getChildren())
		{
			ch.onAnimate(timeMs);
		}
	}
	
	@Override
	public Collection<IScene3dNodeAnimator> getSceneNodeAnimators()
	{
		return this.sceneNodeAnimators;
	}
	
	@Override
	public void setSceneNodeAnimators(Collection<IScene3dNodeAnimator> scene3dNodeAnimators)
	{
		this.sceneNodeAnimators = scene3dNodeAnimators;
	}
	
	@Override
	public void addSceneNodeAnimator(IScene3dNodeAnimator scene3dNodeAnimator)
	{
		this.sceneNodeAnimators.add(scene3dNodeAnimator);
	}
	
	@Override
	public void animateStart()
	{
		for(IScene3dNodeAnimator ani : this.sceneNodeAnimators)
		{
			ani.animateStart();
		}
	}
	
	@Override
	public void animatePause()
	{
		for(IScene3dNodeAnimator ani : this.sceneNodeAnimators)
		{
			ani.animatePause();
		}
	}
	
	@Override
	public void animateStop()
	{
		for(IScene3dNodeAnimator ani : this.sceneNodeAnimators)
		{
			ani.animateStop();
		}
	}
	
	@Override
	public void serializeAttributes(Object out, Object options)
	{
		if(out==null) return;
		
		//    out->addString  ("Name", Name.c_str());
		//    out->addInt ("Id", ID );
		//
		//    out->addVector3d("Position", getPosition() );
		//    out->addVector3d("Rotation", getRotation() );
		//    out->addVector3d("Scale", getScale() );
		//
		//    out->addBool    ("Visible", IsVisible );
		//    out->addInt ("AutomaticCulling", AutomaticCullingState);
		//    out->addInt ("DebugDataVisible", DebugDataVisible );
		//    out->addBool    ("IsDebugObject", IsDebugObject );
	}
	
	@Override
	public void deserializeAttributes(Object in, Object options)
	{
		if(in==null) return;
		
	//    Name = in->getAttributeAsString("Name");
	//    ID = in->getAttributeAsInt("Id");
	//
	//    setPosition(in->getAttributeAsVector3d("Position"));
	//    setRotation(in->getAttributeAsVector3d("Rotation"));
	//    setScale(in->getAttributeAsVector3d("Scale"));
	//
	//    IsVisible = in->getAttributeAsBool("Visible");
	//    s32 tmpState = in->getAttributeAsEnumeration("AutomaticCulling", scene::AutomaticCullingNames);
	//    if (tmpState != -1)
	//            AutomaticCullingState = (u32)tmpState;
	//    else
	//        AutomaticCullingState = in->getAttributeAsInt("AutomaticCulling");
	//
	//    DebugDataVisible = in->getAttributeAsInt("DebugDataVisible");
	//    IsDebugObject = in->getAttributeAsBool("IsDebugObject");
	//
		this.updateAbsolutePosition();
	}
	
	@Override
	public abstract T clone(IScene3dNode node, IScene3dManager mgr);
	
	@Override
	public void render(IScene3dViewport vp)
	{
		if(this.getChildren()!=null)
		{
			for(IScene3dNode c : this.getChildren())
			{
				c.render(vp);
			}
		}
	}
	
	
}
