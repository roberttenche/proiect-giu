package org.tencherobert;

import com.jogamp.opengl.util.FPSAnimator;
import org.tencherobert.entities.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Main implements GLEventListener, KeyListener, WindowFocusListener, MouseMotionListener
{
    private Sun sun;
    private Mercury mercury;
    private Venus venus;
    private Earth earth;
    private Moon moon;
    private final GLU glu = new GLU();

    private static int WIDTH = 1280;
    private static int HEIGHT = 720;

    private static FPSCamera camera;

    private final String resourcePath = System.getProperty("user.dir") + "\\src\\main\\resources\\";


    boolean windowIsFocused = false;
    boolean listenForFocus = true;

    static JFrame frame;

    public static void main(String[] args) {

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        GLCanvas canvas = new GLCanvas(capabilities);

        Main GUI_JOGL = new Main();

        canvas.addGLEventListener(GUI_JOGL);
        canvas.addKeyListener(GUI_JOGL);
        canvas.addMouseMotionListener(GUI_JOGL);

        frame = new JFrame("GUI");
        frame.getContentPane().add(canvas);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.addWindowFocusListener(GUI_JOGL);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowFocusListener(GUI_JOGL);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();

        camera = new FPSCamera(0.0f,00.0f,35.0f, 1.0f, 0.1f);

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();


        sun = new Sun(resourcePath + "sun_texture.jpg", 5.0f, 0, 0, 0);
        mercury = new Mercury(resourcePath + "moon_texture.jpg", 0.5f, 0, 0, 0);
        venus = new Venus(resourcePath + "venus_texture.jpg", 0.75f, 0, 0, 0);
        earth = new Earth(resourcePath + "earth_texture.jpg", 1.0f, 0, 0, 0);
        moon = new Moon(resourcePath + "moon_texture.jpg", 0.2f, 0, 0, 0);

        mercury.setPositionRelativeTo(sun, 10.0f, 0.0f, 0.0f);
        venus.setPositionRelativeTo(sun, 20.0f, 0.0f, 0.0f);
        earth.setPositionRelativeTo(sun, 25.0f, 0.0f, 0.0f);
        moon.setPositionRelativeTo(earth, 3.0f, 0.0f, 0.0f);


        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glEnable(GL2.GL_DEPTH_TEST);

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glLoadIdentity();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        glu.gluLookAt(
            -camera.position.x, -camera.position.y, -camera.position.z,
            sun.position.x, sun.position.y, sun.position.z,
            0,1,0
        );

        sun.rotateSelf(0.0f, 1.0f, 0.0f);
        sun.render(gl, glu);

        mercury.rotateSelf(0.0f, 0.0168f, 0.0f);
        mercury.rotateRelativeTo(sun, 0.0f, 4.1520f, 0.0f);
        mercury.render(gl, glu);

        venus.rotateSelf(0.0f, 0.004f, 0.0f);
        venus.rotateRelativeTo(sun, 0.0f, 1.6255f, 0.0f);
        venus.render(gl, glu);

        earth.rotateSelf(0.0f, 0.99f, 0.0f);
        earth.rotateRelativeTo(sun, 0.0f, 1.0f, 0.00f);
        earth.render(gl, glu);

        moon.rotateSelf(0.0f, 3.0f, 0.0f);
        moon.rotateRelativeTo(earth, 0.0f, 3.0f, 0.0f);
        moon.render(gl, glu);


        gl.glFlush();

    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        WIDTH = width;
        HEIGHT = height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glViewport(x, y, width, height);
        gl.glLoadIdentity();

        float aspect = (float) WIDTH / HEIGHT;

        gl.glFrustum(-aspect, aspect, -1.0f, 1.0f, 1.0f, 100.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        int keyCode = keyEvent.getKeyCode();

        switch (keyCode) {

            case KeyEvent.VK_W:
                camera.moveForward();

                break;

            case KeyEvent.VK_S:
                camera.moveBackward();
                break;

            case KeyEvent.VK_A:
                camera.moveLeft();

                break;

            case KeyEvent.VK_D:
                camera.moveRight();
                break;

            case KeyEvent.VK_SPACE:
                camera.moveUp();
                break;

            case KeyEvent.VK_C:
                camera.moveDown();
                break;

            case KeyEvent.VK_ESCAPE:
                listenForFocus = !listenForFocus;

                if (listenForFocus)
                {
                    hideCursor();
                }
                else
                {
                    unhideCursor();
                }
                break;

        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        windowIsFocused = true;
        hideCursor();
    }

    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        windowIsFocused = false;
        unhideCursor();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        camera.updateLookDirection(mouseEvent.getX(), mouseEvent.getY());
    }

    private void hideCursor(){
        frame.setCursor(
                frame.getToolkit().createCustomCursor(
                        new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB),
                        new Point(0,0),
                        null
                )
        );
    }

    private void unhideCursor()
    {
        frame.setCursor(Cursor.getDefaultCursor());
    }
}