package com.example.david.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.david.airhockey.objects.Mallet;
import com.example.david.airhockey.objects.Puck;
import com.example.david.airhockey.objects.Table;
import com.example.david.airhockey.programs.ColorShaderProgram;
import com.example.david.airhockey.programs.TextureShaderProgram;
import com.example.david.airhockey.util.Geometry;
import com.example.david.airhockey.util.LoggerConfig;
import com.example.david.airhockey.util.MatrixHelper;
import com.example.david.airhockey.util.ShaderHelper;
import com.example.david.airhockey.util.TextResourceReader;
import com.example.david.airhockey.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

/**
 * Created by david on 09/02/15.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private final Context mContext;
    private final float[] projectionMatrix = new float[16];
    //Matrix to move objects (vertex)
    private final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;
    private Puck puck;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private boolean malletPressed = false;
    private Geometry.Point blueMalletPosition;

    public AirHockeyRenderer(Context context)
    {
        this.mContext = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 0f);

        table = new Table();
        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);

        textureShaderProgram = new TextureShaderProgram(mContext);
        colorShaderProgram = new ColorShaderProgram(mContext);

        texture = TextureHelper.loadTexture(mContext,R.drawable.air_hockey_surface);

        blueMalletPosition = new Geometry.Point(0f, mallet.height / 2f, 0.4f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);
        /*setIdentityM(modelMatrix,0);
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix,0,-60f,1f,0f,0f);

        final float [] temp = new float[16];
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);*/
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {


        // Clear the rendering surface
        glClear(GL_COLOR_BUFFER_BIT);

        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        //Draw table
        positionTableInScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        //Draw mallets
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        positionObjectInScene(0f, mallet.height / 2f, 0.4f);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        mallet.draw();

        positionObjectInScene(0f, puck.height / 2f, 0f);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorShaderProgram);
        puck.draw();
    }

    private void positionTableInScene(){
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z){
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix,0, x,y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    public void handleTouchPress(float normalizedX, float normalizedY){
        Log.d("TOUCH", "press ("+normalizedX+","+normalizedY+")");

        Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);

        //Now test if this ray intersects with the mallet by creating a
        // bounding sphere that wraps the mallet
        Sphere malletBoundingSphere = new Sphere(new Geometry.Point(blueMalletPosition.x,blueMalletPosition.y,
                                                                    blueMalletPosition.z), mallet.height/2f);

    }

    public void handleTouchDrag(float normalizedX, float normalizedY){
        Log.d("TOUCH", "drag (" + normalizedX + "," + normalizedY + ")");
    }
}
