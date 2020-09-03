package com.soma.fps;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;

public class Game extends ApplicationAdapter {
	private PerspectiveCamera camera;
	FPSLogger logger = new FPSLogger();

	private SpriteBatch batch;
	private Sprite pointer;

	private ModelBatch modelBatch;
	private ModelInstance floor, ball, ground;
	private Model model, box;
	private Array<ModelInstance> instances;

	private Environment environment;

	private InputManager inputManager;

	boolean collision;

	@Override
	public void create() {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		// Perspective Camera
		camera = new PerspectiveCamera(75, width, height);
		camera.position.set(0f,100f,200f);
		camera.lookAt(0f,100f,0f);
		camera.near = 0.1f;
		camera.far = 600f;

		modelBatch = new ModelBatch();

		// Environment
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		UBJsonReader jsonReader = new UBJsonReader();
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

		// Load 3d models
		model = modelLoader.loadModel(Gdx.files.internal("floor.g3db"));
		floor = new ModelInstance(model);

		// Model Builder (Create Box)
		ModelBuilder mb = new ModelBuilder();
		box = mb.createBox(10f,10f,10f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position);
		ball = new ModelInstance(box);
		ball.transform.setToTranslation(0, 100f, 0);

		// List instances
		instances = new Array<ModelInstance>();
		instances.add(floor);
		instances.add(ball);

		// Sprite Pointer
		pointer = new Sprite(new Texture(Gdx.files.internal("pointer.png")));
		float pointerX = (width / 2) - (pointer.getWidth() / 2);
		float pointerY = (height / 2) - (pointer.getHeight() / 2);
		pointer.setPosition(pointerX, pointerY);

		batch = new SpriteBatch();

		// Input Processor
		inputManager = new InputManager(camera, ball);
		Gdx.input.setInputProcessor(inputManager);
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void render() {
		Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		//Camera position
		Vector3 boxPosition = ball.transform.getTranslation(new Vector3());
		boxPosition.x = boxPosition.x + 5f;
		camera.position.set(boxPosition);
		camera.update();

		Vector2 direction = new Vector2(camera.direction.x, camera.direction.y);
		System.out.println(direction);

		modelBatch.begin(camera);
		modelBatch.render(instances, environment);
		modelBatch.end();

		final float delta = Math.min(1f/30f, Gdx.graphics.getDeltaTime());

		if (!collision) {
			ball.transform.translate(0f, -(delta * 10), 0f);
			collision = checkCollision();
		}

		batch.begin();
		pointer.draw(batch);
		batch.end();

		//logger.log();
	}

	public boolean checkCollision() {
		return true;
	}

	@Override
	public void dispose() {
		box.dispose();
		modelBatch.dispose();
		model.dispose();
	}
}
