package com.soma.fps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class InputManager implements InputProcessor {
    private PerspectiveCamera camera;
    private ModelInstance ball;

    private float positionX = 100f;
    private float positionZ = 100f;
    private float positionY = 50f;

    private int dragX, dragY;
    private float rotateSpeed = 0.2f;

    public InputManager(PerspectiveCamera camera, ModelInstance ball) {
        this.ball = ball;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            positionX = positionX - 100;
            ball.transform.setToTranslation(positionX, positionY, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
            positionX = positionX + 100;
            ball.transform.setToTranslation(positionX, positionY, 0);
        }
        if (keycode == Input.Keys.UP) {
            positionZ = positionZ - 100;
            ball.transform.setToTranslation(positionX, positionY, positionZ);
        }
        if (keycode == Input.Keys.DOWN) {
            positionZ = positionZ + 100;
            ball.transform.setToTranslation(positionX, positionY, positionZ);
        }
        if (keycode == Input.Keys.SPACE) {
            positionY = positionY + 50f;
            ball.transform.setToTranslation(positionX, positionY, positionZ);
        }
        if (keycode == Input.Keys.SHIFT_LEFT) {
            positionY = positionY - 50f;
            ball.transform.setToTranslation(positionX, positionY, positionZ);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 direction = camera.direction.cpy();

        // rotating on the y axis
        float x = dragX -screenX;
        // change this Vector3.y with cam.up if you have a dynamic up.
        camera.rotate(Vector3.Y,x * rotateSpeed);

        // rotating on the x and z axis is different
        float y = (float) Math.sin( (double)(dragY -screenY)/180f);
        if (Math.abs(camera.direction.y + y * (rotateSpeed*5.0f))< 0.9) {
            camera.direction.y +=  y * (rotateSpeed*5.0f) ;
        }

        camera.update();
        dragX = screenX;
        dragY = screenY;
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
