package com.example.david.airhockey.objects;

import com.example.david.airhockey.data.VertexArray;
import com.example.david.airhockey.programs.ColorShaderProgram;

import static android.opengl.GLES10.GL_POINTS;
import static android.opengl.GLES10.GL_TRIANGLE_FAN;
import static android.opengl.GLES10.glDrawArrays;
import static com.example.david.airhockey.Constants.BYTES_PER_FLOAT;

/**
 * Created by david on 09/04/15.
 */
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float [] VERTEX_DATA = {

            //Order of coordinates: X,Y,R,G,B

            0f, -0.4f,   0f, 0f, 1f,
            0f,  0.4f,   1f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public Mallet() {
        this.vertexArray =  new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0,colorProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);

        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw(){
        glDrawArrays(GL_POINTS, 0 , 2);
    }
}
