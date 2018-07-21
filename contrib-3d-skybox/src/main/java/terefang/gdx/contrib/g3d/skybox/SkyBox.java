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
package terefang.gdx.contrib.g3d.skybox;import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

/**
 * Created by fredo on 04.07.17.
 */
public class SkyBox extends EnvironmentCubemap
{
	public SkyBox(Pixmap cubemap)
	{
		super(cubemap);
	}
	
	public SkyBox(FileHandle f0)
	{
		super(f0);
	}
	
	public SkyBox(FileHandle f0, FileHandle f1, FileHandle f2, FileHandle f3, FileHandle f4, FileHandle f5)
	{
		super(f0, f1, f2, f3, f4, f5);
	}
	
}
