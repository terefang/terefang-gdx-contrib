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

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.Collection;

public interface IScene3dNode<T extends IScene3dNode>
		extends IScene3dManaged, IScene3dParentable<IScene3dNode>, IScene3dCloneable<T>, IScene3dPlaceable, IScene3dAnimateable, IScene3dRenderable
{
	
	void serializeAttributes(Object out, Object options);
	
	void deserializeAttributes(Object in, Object options);
	
	// 	u32 AutomaticCullingState
	// Automatic culling state.
	// 	u32 DebugDataVisible
	// Flag if debug data should be drawn, such as Bounding Boxes.
	// bool IsDebugObject
	// Is debug object?
	// 	ITriangleSelector * TriangleSelector
	// Pointer to the triangle selector.
	
	T getNode();
}
