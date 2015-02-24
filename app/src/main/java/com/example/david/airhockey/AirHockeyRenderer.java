package com.example.david.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.david.airhockey.util.LoggerConfig;
import com.example.david.airhockey.util.MatrixHelper;
import com.example.david.airhockey.util.ShaderHelper;
import com.example.david.airhockey.util.TextResourceReader;

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
import static android.opengl.Matrix.translateM;

/**
 * Created by david on 09/02/15.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private final Context mContext;
    private int program;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) *BYTES_PER_FLOAT;

    private int aColorLocation;

    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;

    //Matrix to move objects (vertex)
    private final float[] modelMatrix = new float[16];


    public AirHockeyRenderer(Context context)
    {
        this.mContext = context;
        float [] tableVertices = {

                //Order of coordinates: X,Y,R,G,B

                // Triangle 1
                 0f,    0f,     1f,   1f,   1f,
                -0.5f, -0.8f,   0.7f, 0.7f, 1f,
                 0.5f, -0.8f,   0.7f, 0.7f, 1f,
                 0.5f,  0.8f,   0.7f, 0.7f, 1f,
                -0.5f,  0.8f,   0.7f, 0.7f, 1f,
                -0.5f, -0.8f,   0.7f, 0.7f, 1f,

                // Line 1
                -0.5f,  0f,   1f, 0f, 0f,
                 0.5f,  0f,   1f, 0f, 0f,

                // Mallets
                0f, -0.4f,   0f, 0f, 1f,
                0f,  0.4f,   1f, 0f, 0f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVertices);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader,fragmentShader);
        if(LoggerConfig.ON){
            ShaderHelper.validateProgram(program);
        }

        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aPositionLocation = glGetAttribLocation(program,A_POSITION);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT,GL_FLOAT,false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix,0,-60f,1f,0f,0f);

        final float [] temp = new float[16];
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear the rendering surface

        glClear(GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);


    }
}
