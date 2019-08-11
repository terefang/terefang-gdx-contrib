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

public enum EScene3dRenderPass
{
	
	RP_ALL_AT_ONCE			(-1),		// No pass currently active.
	RP_NONE					(0),		// No pass currently active.
	RP_CAMERA				(1),		// Camera pass. The active view is set up here. The very first pass.
	RP_LIGHT				(1<<1),		// In this pass, lights are transformed into camera space and added to the driver.
	RP_BACKGROUND			(1<<2),
	RP_SKY_BOX				(1<<3),		// This is used for sky boxes.
	RP_AUTOMATIC			(1<<10),	// All normal objects can use this for registering themselves.
	RP_SOLID				(1<<11),	// Solid scene nodes or special scene nodes without materials.
	RP_TRANSPARENT			(1<<12),	// Transparent scene nodes, drawn after solid nodes. They are sorted from back to front and drawn in that order.
	RP_TRANSPARENT_EFFECT	(1<<13),	// Transparent effect scene nodes, drawn after Transparent nodes. They are sorted from back to front and drawn in that order.
	RP_SHADOW				(1<<14),	// Drawn after the solid nodes, before the transparent nodes, the time for drawing shadow volumes.
	RP_FOREGROUND			(1<<15),
	RP_GUI					(1<<20),
	RP_TOP					(1<<30)
	;
	
	EScene3dRenderPass(int _v)
	{
		this.value = _v;
	}
	
	int value;
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
}
