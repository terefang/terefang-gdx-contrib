import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import terefang.gdx.contrib.g3d.IScene3dNode;
import terefang.gdx.contrib.g3d.impl.Scene3dFontImpl;
import terefang.gdx.contrib.g3d.impl.Scene3dViewportImpl;
import terefang.gdx.contrib.g3d.nodes.ModelNode;
import terefang.gdx.contrib.g3d.nodes.TextNode;
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
public class Test3D implements ApplicationListener
{
	private LwjglApplication application;
	private PerspectiveCamera camera;
	private Scene3dViewportImpl viewport;
	private Environment environment;
	private CameraInputController camController;
	private OrthographicCamera hudCam;
	private SpriteBatch batch;
	private BitmapFont font;
	private long startFps;
	private long stopFps;
	private long countFps;
	private long currentFps;
	private ModelNode rootNode;
	private RenderContext renderContext;
	private Model model;
	private TextNode textNode;
	
	@Override
	public void create()
	{
		{
			try
			{
				this.font = GdfBitmapFont.create(null, null);
				this.font.setColor(Color.WHITE);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		{
			this.camera = new PerspectiveCamera(60, this.application.getGraphics().getWidth(), this.application.getGraphics().getHeight());
			this.viewport = Scene3dViewportImpl.create(this.camera);
			this.viewport.resize(this.application.getGraphics().getWidth(), this.application.getGraphics().getHeight());
			
			ModelBuilder modelBuilder = new ModelBuilder();
			this.model = modelBuilder.createBox(5f, 5f, 5f,
												new Material(ColorAttribute.createDiffuse(Color.GREEN)),
												VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
			this.rootNode = new ModelNode.Factory().createSceneNode(this.rootNode);
			this.rootNode.setModelInstance(new ModelInstance(model));
			
			this.environment = new Environment();
			this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
			this.environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
			this.viewport.setEnvironment(this.environment);
			
			this.textNode = new TextNode.Factory().createSceneNode(this.rootNode);
			this.textNode.getNode().setFont(Scene3dFontImpl.create(this.font));
			this.textNode.getNode().setText("Cube");
			this.textNode.getNode().setTextColor(Color.GOLD);
			
			this.camera.position.set(10f, 10f, 10f);
			this.camera.lookAt(0,0,0);
			this.camera.near = 0.1f;
			this.camera.far = 300f;
			this.camera.update();
			
			this.renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
			
			this.camController = new CameraInputController(this.camera);
			this.application.getInput().setInputProcessor(this.camController);
		}
		{
			this.hudCam = new OrthographicCamera();
			this.batch = new SpriteBatch();
			this.startFps = this.stopFps = System.currentTimeMillis();
		}
	}
	
	@Override
	public void dispose() {
		this.batch.dispose();
		this.font.dispose();
	}
	
	@Override
	public void render()
	{
		{
			this.countFps++;
			this.stopFps = System.currentTimeMillis();
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			
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
			this.renderContext.begin();
			this.rootNode.render(this.viewport);
			this.renderContext.end();
		}
		
		{
			this.batch.begin();
			this.font.draw(this.batch, "FPS:"+this.currentFps+" -- scene Test -- p="+this.camera.position+" d="+this.camera.direction, 0, 0);
			this.batch.end();
		}
	}
	
	@Override
	public void resize(int width, int height)
	{
		this.viewport.resize(width,height);
		this.hudCam.setToOrtho(false, width, height);
		this.hudCam.update();
		this.batch.setProjectionMatrix(this.hudCam.combined);
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
		config.title = "Terefang LibGDX Contrib 3d Example";
		config.resizable = true;
		config.fullscreen = false;
		Test3D t = new Test3D();
		t.application = new LwjglApplication(t, config);
	}
}
