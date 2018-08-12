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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import terefang.gdx.contrib.g3d.IScene3dViewport;

public class Scene3dViewportImpl extends ScreenViewport implements IScene3dViewport
{
	public static Scene3dViewportImpl create(Camera camera)
	{
		Scene3dViewportImpl s = new Scene3dViewportImpl(camera);
		return s;
	}
	
	Scene3dViewportImpl(Camera camera)
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
	public void setEnvironment(Environment environment)
	{
		this.environment = environment;
	}
	
	public Vector2 getScreenCoordinatesFrom3DPosition(Vector3 position)
	{
		// DOES NOT WORK CORRECTLY
		
		int sw = this.getScreenWidth()/2;
		int sh = this.getScreenHeight()/2;
		
		float[] pos = {position.x, position.y, position.z, 1f};
		
		pos = multiplyWith1x4Matrix(pos, this.getProjectionMatrix());
		pos = multiplyWith1x4Matrix(pos, this.getViewMatrix());

		//	pos.X = screenWidth*(pos.X + 1.0)/2.0;
		//	pos.Y = screenHeight * (1.0 - ((pos.Y + 1.0) / 2.0));
		float zDiv = pos[3]==0f ? 1f : 1f/pos[3];
		
		return new Vector2(sw + (sw * (pos[0]*zDiv)), sh+ (sh * (pos[1]*zDiv)));
	}
	
	public static float[] multiplyWith1x4Matrix(float[] matrix, Matrix4 m)
	{
		float[] mat = new float[] { matrix[0], matrix[1], matrix[2], matrix[3] };
		
		float[] M = m.getValues();
		
		matrix[0] = M[0]*mat[0] + M[4]*mat[1] + M[8]*mat[2] + M[12]*mat[3];
		matrix[1] = M[1]*mat[0] + M[5]*mat[1] + M[9]*mat[2] + M[13]*mat[3];
		matrix[2] = M[2]*mat[0] + M[6]*mat[1] + M[10]*mat[2] + M[14]*mat[3];
		matrix[3] = M[3]*mat[0] + M[7]*mat[1] + M[11]*mat[2] + M[15]*mat[3];
		
		return matrix;
	}
}
