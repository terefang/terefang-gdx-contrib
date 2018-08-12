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
import com.badlogic.gdx.math.collision.BoundingBox;

public interface IScene3dPlaceable
{
	boolean isVisible();
	
	void setVisible(boolean visible);
	
	Vector3 getRelativeRotation();
	
	void setRelativeRotation(Vector3 relativeRotation);
	
	Vector3 getRelativeScale();
	
	void setRelativeScale(Vector3 relativeScale);
	
	Vector3 getRelativeTranslation();
	
	void setRelativeTranslation(Vector3 relativeTranslation);
	
	Matrix4 getAbsoluteTransformation();
	
	void setAbsoluteTransformation(Matrix4 absoluteTransformation);
	
	void updateAbsolutePosition();
	
	Matrix4 getRelativeTransformation();
	
	Vector3 getAbsolutePosition(Vector3 v);
	
	BoundingBox getBoundingBox();
	
	void setBoundingBox(BoundingBox boundingBox);
	
	BoundingBox getTransformedBoundingBox();
}
