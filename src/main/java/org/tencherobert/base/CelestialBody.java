package org.tencherobert.base;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import org.tencherobert.interfaces.Renderable;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.io.File;
import java.io.IOException;

public class CelestialBody extends Entity implements Renderable {

    private Texture texture;
    protected final float sphereSize;

    public CelestialBody(String texturePath, float sphereSize, float x, float y, float z) {
        super(x, y, z);

        this.sphereSize = sphereSize;

        try
        {
            this.texture = TextureIO.newTexture(new File(texturePath), true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void render(GL2 gl, GLU glu) {

        gl.glPushMatrix();

        gl.glTranslatef(
            this.position.x,
            this.position.y,
            this.position.z
        );

        gl.glRotatef(this.selfRotation.x, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(this.selfRotation.y, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(this.selfRotation.z, 0.0f, 0.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);

        texture.bind(gl);

        GLUquadric sphere = glu.gluNewQuadric();
        glu.gluQuadricTexture(sphere, true);
        glu.gluSphere(sphere, this.sphereSize, 50, 50);
        glu.gluDeleteQuadric(sphere);

        gl.glDisable(GL.GL_TEXTURE_2D);

        gl.glPopMatrix();
    }
}
