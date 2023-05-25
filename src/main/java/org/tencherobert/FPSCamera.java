package org.tencherobert;

import org.joml.Vector3f;
import org.tencherobert.base.Entity;

public class FPSCamera extends Entity {

    public final Vector3f lookPoint;

    private final float moveSpeed;
    private final float mouseSensitivity;

    private float yaw = 0.0f;
    private float pitch = 0.0f;

    private  float prevMouseX = 0.0f;
    private  float prevMouseY = 0.0f;

    public FPSCamera(float x, float y, float z, float moveSpeed, float mouseSensitivity)
    {
        super(x,y,z);
        position = new Vector3f(x,y,z);
        lookPoint = new Vector3f(0,0,0);
        this.moveSpeed = moveSpeed;
        this.mouseSensitivity = mouseSensitivity;
    }

    public void moveForward()
    {
        this.translate(0,0,-moveSpeed);
    }

    public void moveBackward()
    {
        this.translate(0,0,moveSpeed);
    }

    public void moveLeft() { this.translate(-moveSpeed, 0, 0 );}
    public void moveRight() { this.translate(moveSpeed, 0, 0 );}
    public void moveUp() { this.translate(0, -moveSpeed, 0 );}
    public void moveDown() { this.translate(0, moveSpeed, 0 );}

    public void updateLookDirection(float x, float y)
    {
        float deltaX = x - prevMouseX;
        float deltaY = y - prevMouseY;

        prevMouseX = x;
        prevMouseY = y;

        if (deltaX >= 25 || deltaY >= 25)
        {
            return;
        }


        this.lookPoint.x -= deltaX * mouseSensitivity;
        this.lookPoint.y -= deltaY * mouseSensitivity;
        this.lookPoint.z = this.position.z + 10;


//        yaw += (deltaX * mouseSensitivity); // Adjust the yaw based on mouse movement
//        pitch += (deltaY * mouseSensitivity); // Adjust the pitch based on mouse movement
//
//        // Clamp the pitch to avoid camera flipping
//        pitch = Math.max(-90.0f, Math.min(90.0f, pitch));
//







    }

    public void setPrevMouseX(float prevMouseX) {
        this.prevMouseX = prevMouseX;
    }

    public void setPrevMouseY(float prevMouseY) {
        this.prevMouseY = prevMouseY;
    }
}
