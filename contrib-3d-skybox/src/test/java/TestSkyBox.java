import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.viewport.FillViewport;
import terefang.gdx.contrib.g3d.Stage3D;
import terefang.gdx.contrib.g3d.skybox.SkyBox;
import terefang.gdx.contrib.gdf.GdfBitmapFont;

import java.io.IOException;

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
public class TestSkyBox implements ApplicationListener
{
	ClasspathFileHandleResolver resolver = new ClasspathFileHandleResolver();
	LwjglApplication application;

	private SpriteBatch batch;
	private BitmapFont font;
	long startFps = 0;
	long stopFps = 0;
	long countFps = 0;
	long currentFps = 0;
	private PerspectiveCamera camera;
	private FillViewport viewport;
	private Stage3D stage;
	private Environment environment;
	private ModelBatch modelBatch;
	private CameraInputController camController;
	private SkyBox skybox;
	private RenderContext renderContext;
	
	@Override
	public void create()
	{
		{
			this.environment = new Environment();
			this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
			this.environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
			
			this.modelBatch = new ModelBatch();
			
			this.camera.position.set(10f, 10f, 10f);
			this.camera.lookAt(0,0,0);
			this.camera.near = 0.1f;
			this.camera.far = 300f;
			this.camera.update();
			
			this.camController = new CameraInputController(this.camera);
			
			this.application.getInput().setInputProcessor(this.camController);
			
			
			this.skybox = new SkyBox(
				Gdx.files.internal("data/sky.0.png"),
				Gdx.files.internal("data/sky.2.png"),
				Gdx.files.internal("data/sky.4.png"),
				Gdx.files.internal("data/sky.5.png"),
				Gdx.files.internal("data/sky.3.png"),
				Gdx.files.internal("data/sky.1.png")
				/*
					Gdx.files.internal("data/SKY_BLUE.JPG")
				*/
			);
			this.renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
		}
		{
			this.batch = new SpriteBatch();
			try
			{
				this.font = GdfBitmapFont.create(null, null);
				this.font.setColor(Color.WHITE);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			this.startFps = this.stopFps = System.currentTimeMillis();
		}
	}

	@Override
	public void dispose() {
		this.batch.dispose();
		this.font.dispose();
		this.skybox.dispose();
	}

	@Override
	public void render()
	{
		{
			this.countFps++;
			this.stopFps = System.currentTimeMillis();
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
			
			long deltaFps = this.stopFps-this.startFps;
			if(deltaFps > 1000L)
			{
				this.currentFps = (this.countFps*1000)/deltaFps;
				this.startFps = this.stopFps = System.currentTimeMillis();
				this.countFps = 0;
			}
		}
		
		{
			this.camController.update();
			this.skybox.draw(this.camera, this.renderContext);
		}
		
		{
			this.batch.begin();
			this.font.draw(this.batch, "FPS:"+this.currentFps+" -- SkyMap Test", 10, 10);
			this.batch.end();
		}
	}

	@Override
	public void resize(int width, int height)
	{
		this.stage.getViewport().update(width,height);
		this.batch.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, (float)width, (float)height);
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	public static void main(String[] args) throws Exception
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = false;
		config.vSyncEnabled = true;
		config.width = 800;
		config.height = 600;
		config.title = "Terefang LibGDX Contrib SKYMAP Example";
		config.resizable = true;
		config.fullscreen = false;
		TestSkyBox t = new TestSkyBox();
		t.application = new LwjglApplication(t, config);
		t.camera = new PerspectiveCamera(60, 600, 600);
		t.viewport = new FillViewport(800, 600, t.camera);
		t.stage = new Stage3D(t.viewport);
	}
}