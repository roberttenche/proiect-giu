package org.tencherobert.base;

import org.joml.Vector3f;

public class Entity {

    public Vector3f position;
    public Vector3f selfRotation;
    public Vector3f relativeRotation;


    public boolean hasRelativeRotation = false;
    protected Entity relativeRotationEntity = null;


    public Entity(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        this.selfRotation = new Vector3f();
        this.relativeRotation = new Vector3f();
    }

    public void translate(float x, float y, float z)
    {
        Vector3f angleIncrement = new Vector3f(x,y,z);

        this.position.x += angleIncrement.x;
        this.position.y += angleIncrement.y;
        this.position.z += angleIncrement.z;
    }

    public void rotateSelf(float x, float y, float z)
    {
        Vector3f angleIncrement = new Vector3f(x, y, z);

        if (this.selfRotation.x + angleIncrement.x <= 360.0f && this.selfRotation.x + angleIncrement.x >= -360.0f)
        {
            this.selfRotation.x += angleIncrement.x;
        }
        else
        {
            this.selfRotation.x = (this.selfRotation.x + angleIncrement.x) - 360.0f;
        }

        if (this.selfRotation.y + angleIncrement.y <= 360.0f && this.selfRotation.y + angleIncrement.y >= -360.0f)
        {
            this.selfRotation.y += angleIncrement.y;
        }
        else
        {
            this.selfRotation.y = (this.selfRotation.y + angleIncrement.y) - 360.0f;
        }

        if (this.selfRotation.z + angleIncrement.z <= 360.0f && this.selfRotation.z + angleIncrement.z >= -360.0f)
        {
            this.selfRotation.z += angleIncrement.z;
        }
        else
        {
            this.selfRotation.z = (this.selfRotation.z + angleIncrement.z) - 360.0f;
        }

    }

    public void rotateRelativeTo(Entity relativeEntity, float x, float y, float z)
    {
        this.relativeRotation.x = x;
        this.relativeRotation.y = y;
        this.relativeRotation.z = z;

        if (!this.hasRelativeRotation)
        {
            this.hasRelativeRotation = true;
            this.relativeRotationEntity = relativeEntity;
        }

        if (!relativeEntity.hasRelativeRotation)
        {
        float newX;
        float newY;
        float newZ;

        float distanceX = this.position.x - relativeEntity.position.x;
        float distanceY = this.position.y - relativeEntity.position.y;
        float distanceZ = this.position.z - relativeEntity.position.z;

        float radX = (float) Math.toRadians(x);
        float radY = (float) Math.toRadians(y);
        float radZ = (float) Math.toRadians(z);

        // rotation around x-axis
        float cosX = (float) Math.cos(radX);
        float sinX = (float) Math.sin(radX);
        newY = distanceY * cosX - distanceZ * sinX;
        float tempZ = distanceY * sinX + distanceZ * cosX;

        // rotation around y-axis
        float cosY = (float) Math.cos(radY);
        float sinY = (float) Math.sin(radY);
        newX = distanceX * cosY + tempZ * sinY;
        newZ = -distanceX * sinY + tempZ * cosY;

        // rotation around the z-axis
        float cosZ = (float) Math.cos(radZ);
        float sinZ = (float) Math.sin(radZ);
        float finalX = newX * cosZ - newY * sinZ;
        float finalY = newX * sinZ + newY * cosZ;

        this.position.x = finalX + relativeEntity.position.x;
        this.position.y = finalY + relativeEntity.position.y;
        this.position.z = newZ + relativeEntity.position.z;
        }
        else
        {
            float[] distance = new float[]{this.position.x - relativeEntity.position.x,
                    this.position.y - relativeEntity.position.y,
                    this.position.z - relativeEntity.position.z};

            // Apply the relative rotation of the entity to the distance vector
            float relativeRotationX = (float) Math.toRadians(relativeEntity.relativeRotation.x);
            float relativeRotationY = (float) Math.toRadians(relativeEntity.relativeRotation.y);
            float relativeRotationZ = (float) Math.toRadians(relativeEntity.relativeRotation.z);

            // Create rotation matrices
            float[][] rotationX = new float[][]{
                    {1, 0, 0},
                    {0, (float) Math.cos(relativeRotationX), (float) -Math.sin(relativeRotationX)},
                    {0, (float) Math.sin(relativeRotationX), (float) Math.cos(relativeRotationX)}
            };

            float[][] rotationY = new float[][]{
                    {(float) Math.cos(relativeRotationY), 0, (float) Math.sin(relativeRotationY)},
                    {0, 1, 0},
                    {(float) -Math.sin(relativeRotationY), 0, (float) Math.cos(relativeRotationY)}
            };

            float[][] rotationZ = new float[][]{
                    {(float) Math.cos(relativeRotationZ), (float) -Math.sin(relativeRotationZ), 0},
                    {(float) Math.sin(relativeRotationZ), (float) Math.cos(relativeRotationZ), 0},
                    {0, 0, 1}
            };

            // Apply rotation matrices to the distance vector
            distance = multiplyMatrix(rotationX, distance);
            distance = multiplyMatrix(rotationY, distance);
            distance = multiplyMatrix(rotationZ, distance);

            // Update the position based on the relative entity's position and rotation
            this.position.x = distance[0] + relativeEntity.position.x;
            this.position.y = distance[1] + relativeEntity.position.y;
            this.position.z = distance[2] + relativeEntity.position.z;
        }

    }

    public void setPositionRelativeTo(Entity entity, float x, float y, float z)
    {
        this.position.x = entity.position.x + x;
        this.position.y = entity.position.y + y;
        this.position.z = entity.position.z + z;
    }


    private float[] multiplyMatrix(float[][] matrix, float[] vector) {
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = matrix[i][0] * vector[0] + matrix[i][1] * vector[1] + matrix[i][2] * vector[2];
        }
        return result;
    }

}
