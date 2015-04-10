package com.example.david.airhockey.programs;

import android.content.Context;

import com.example.david.airhockey.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by david on 10/04/15.
 */
public class ColorShaderProgram extends ShaderProgram{

    //Uniform location
    private final int uMatrixLocation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;


    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);

        // Retrieve attribute locations for the shader program
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
    }

    public void setUniforms(float[] matrix){
        //Pass the matrix into the shaders program
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return aColorLocation;
    }
}
