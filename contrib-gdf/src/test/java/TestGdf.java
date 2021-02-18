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
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import terefang.gdx.contrib.gdf.GdfBitmapFont;

import java.io.IOException;

public class TestGdf implements ApplicationListener
{
	ClasspathFileHandleResolver resolver = new ClasspathFileHandleResolver();
	LwjglApplication application;
	
	private SpriteBatch batch;
	private BitmapFont font;
	long startFps = 0;
	long stopFps = 0;
	long countFps = 0;
	long currentFps = 0;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		try
		{
			//font = GdfBitmapFont.create(this.resolver, "assets/gdf/cfnt/C_6x10_LE.gdf.gz");
			//font = GdfBitmapFont.create(this.resolver, "assets/gdf/gd/giant.gdfa.gz");
			//font = GdfBitmapFont.create(this.resolver, "assets/gdf/ami8.gdf");
			//font = GdfBitmapFont.create(this.resolver, "assets/gdf/gd/large.gdfa.gz");
			//font = GdfBitmapFont.create8x16();
			font = GdfBitmapFont.create(this.resolver, "assets/gdf/pet.gdfa");
			
			font.setColor(Color.ROYAL);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		this.startFps = this.stopFps = System.currentTimeMillis();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
	
	@Override
	public void render() {
		this.countFps++;
		this.stopFps = System.currentTimeMillis();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		font.draw(batch, "Hello World -- qwertzuiop -- asdfghjkl -- yxcvbnm -- 1234567890 -- !\"§$%&/()=+-*#,.;:", 200, 200);
		batch.end();
		long deltaFps = this.stopFps-this.startFps;
		if(deltaFps > 1000L)
		{
			this.currentFps = (this.countFps*1000)/deltaFps;
			this.startFps = this.stopFps = System.currentTimeMillis();
			this.countFps = 0;
		}
		batch.begin();
		font.draw(batch, "FPS:"+this.currentFps, 10, 10);
		batch.end();
	}
	
	@Override
	public void resize(int width, int height)
	{
		batch.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, (float)width, (float)height);
	}
	
	@Override
	public void pause() {
	}
	
	@Override
	public void resume() {
	}
	
	public static void main(String[] args) throws Exception
	{
		for(Graphics.DisplayMode dm : LwjglApplicationConfiguration.getDisplayModes())
		{
			System.err.println("# "+dm.width+" x "+dm.height+" @ "+dm.refreshRate+" / "+dm.bitsPerPixel);
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = false;
		config.vSyncEnabled = true;
		//config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		config.width = 1024;
		config.height = 768;
		config.title = "Terefang LibGDX Contrib GDF Fonts Example";
		config.resizable = true;
		config.fullscreen = false;
		TestGdf t = new TestGdf();
		t.application = new LwjglApplication(t, config);
	}
}