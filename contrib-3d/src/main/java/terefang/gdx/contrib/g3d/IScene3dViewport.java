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

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface IScene3dViewport
{
	Vector2 getScreenCoordinatesFrom3DPosition(Vector3 position);
	
	Matrix4 getViewMatrix();
	
	Matrix4 getProjectionMatrix();
	
	Camera getCamera();
	
	int getScreenHeight();
	
	int getScreenWidth();
	
	void resize(int width, int height);
	
	void setEnvironment(Environment environment);
	
	Environment getEnvironment();
}
