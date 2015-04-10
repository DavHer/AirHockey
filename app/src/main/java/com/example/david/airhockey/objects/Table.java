package com.example.david.airhockey.objects;

import com.example.david.airhockey.data.VertexArray;
import com.example.david.airhockey.programs.TextureShaderProgram;


import static android.opengl.GLES10.glDrawArrays;
import static com.example.david.airhockey.Constants.BYTES_PER_FLOAT;
import static android.opengl.GLES10.GL_TRIANGLE_FAN;

/**
 * Created by david on 09/04/15.
 */
public class Table {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float [] VERTEX_DATA = {

            //Order of coordinates: X,Y, S, T

            // Triangle 1
            0f,    0f,     0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
            0.5f, -0.8f,    1f, 0.9f,
            0.5f,  0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,  0f, 0.9f,

    };

    private final VertexArray vertexArray;

    public Table() {
        this.vertexArray =  new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram){
        vertexArray.setVertexAttribPointer(0,textureProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);

        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLE_FAN, 0 , 6);
    }
}
