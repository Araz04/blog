package com.xoppa.blog.libgdx.g3d.loadscene.step2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * See: http://blog.xoppa.com/loading-a-scene-with-libgdx/
 * @author Xoppa
 */
public class LoadSceneTest implements ApplicationListener {
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Lights lights;
    public boolean loading;
     
    public Array<ModelInstance> blocks = new Array<ModelInstance>();
    public Array<ModelInstance> invaders = new Array<ModelInstance>();
    public ModelInstance ship;
    public ModelInstance space;
     
    @Override
    public void create () {
        modelBatch = new ModelBatch();
        lights = new Lights();
        lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
         
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 7f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();
 
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
         
        assets = new AssetManager();
        assets.load("data/invaders.g3db", Model.class);
        loading = true;
    }
 
    private void doneLoading() {
        Model model = assets.get("data/invaders.g3db", Model.class);
        ship = new ModelInstance(model, "ship");
        ship.transform.setToRotation(Vector3.Y, 180).trn(0, 0, 6f);
        instances.add(ship);
 
        for (float x = -5f; x <= 5f; x += 2f) {
            ModelInstance block = new ModelInstance(model, "block");
            block.transform.setToTranslation(x, 0, 3f);
            instances.add(block);
            blocks.add(block);
        }
         
        for (float x = -5f; x <= 5f; x += 2f) {
            for (float z = -8f; z <= 0f; z += 2f) {
                ModelInstance invader = new ModelInstance(model, "invader");
                invader.transform.setToTranslation(x, 0, z);
                instances.add(invader);
                invaders.add(invader);
            }
        }
         
        space = new ModelInstance(model, "space");
         
        loading = false;
    }
     
    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();
        camController.update();
         
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(cam);
        for (ModelInstance instance : instances)
            modelBatch.render(instance, lights);
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
    }
     
    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}