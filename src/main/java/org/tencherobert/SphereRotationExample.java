package org.tencherobert;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class SphereRotationExample extends JFrame implements GLEventListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private float rotationAngle = 0.0f;

    GLUT glut = new GLUT();
    GLU glu = new GLU();

    public SphereRotationExample() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        GLCanvas canvas = new GLCanvas(glCapabilities);
        canvas.addGLEventListener(this);

        setTitle("JOGL Sphere Rotation Example");
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(canvas);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        canvas.requestFocus();
    }

    public static void main(String[] args) {
        new SphereRotationExample();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Set background color
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Enable depth testing
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // Initialize GLUT
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Clear buffers
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Set up modelview matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        // Set camera position
        glu.gluLookAt(0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        // Rotate the larger sphere around its own y-axis
        gl.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);

        // Draw larger sphere
        gl.glPushMatrix();

            gl.glTranslatef(0.0f,0.0f,0.0f);
            gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(rotationAngle * 0.1f, 0.0f, 0.0f, 1.0f);

            gl.glEnable(GL.GL_TEXTURE_2D);
            GLUquadric sun = glu.gluNewQuadric();
            glu.gluQuadricTexture(sun, true);
            glu.gluSphere(sun, 5, 50, 50);
            glu.gluDeleteQuadric(sun);
        gl.glDisable(GL.GL_TEXTURE_2D);


        gl.glPopMatrix();

        // Translate to the position of the smaller sphere
        gl.glTranslatef(2.0f, 0.0f, 0.0f);

        // Rotate the smaller sphere around the y-axis of the larger sphere
        gl.glRotatef(rotationAngle * 2.0f, 0.0f, 1.0f, 0.0f);

        // Draw smaller sphere
        gl.glPushMatrix();

            gl.glTranslatef(10.0f,0.0f,0.0f);
            gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(rotationAngle * 3, 0.0f, 0.0f, 1.0f);

            gl.glEnable(GL.GL_TEXTURE_2D);
            GLUquadric earth = glu.gluNewQuadric();
            glu.gluQuadricTexture(earth, true);
            glu.gluSphere(earth, 1, 50, 50);
            glu.gluDeleteQuadric(earth);

        gl.glDisable(GL.GL_TEXTURE_2D);

        // Update rotation angle
        rotationAngle += 1.0f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        // Set viewport
        gl.glViewport(0, 0, width, height);

        // Set perspective projection
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, (double) width / (double) height, 0.1, 100.0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}