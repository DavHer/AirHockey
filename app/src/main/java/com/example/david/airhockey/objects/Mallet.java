package com.example.david.airhockey.objects;

import com.example.david.airhockey.data.VertexArray;
import com.example.david.airhockey.programs.ColorShaderProgram;
import com.example.david.airhockey.util.Geometry;

import java.util.List;

import static android.opengl.GLES10.GL_POINTS;
import static android.opengl.GLES10.GL_TRIANGLE_FAN;
import static android.opengl.GLES10.glDrawArrays;
import static com.example.david.airhockey.Constants.BYTES_PER_FLOAT;

/**
 * Created by david on 09/04/15.
 */
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;


    public Mallet(float radius, float height, int numPointsAroundMallet){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet( new Geometry.Point(0f,0f,0f), radius, height, numPointsAroundMallet);
        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0,colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }

    public void draw(){
        for(ObjectBuilder.DrawCommand drawCommand:drawList){
            drawCommand.draw();
        }
    }
}
