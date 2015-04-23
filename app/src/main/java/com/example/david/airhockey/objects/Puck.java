package com.example.david.airhockey.objects;

import com.example.david.airhockey.data.VertexArray;
import com.example.david.airhockey.programs.ColorShaderProgram;

import static android.opengl.GLES10.GL_POINTS;
import static android.opengl.GLES10.glDrawArrays;
import static com.example.david.airhockey.util.Geometry.*;
import static com.example.david.airhockey.objects.ObjectBuilder.*;
import java.util.List;

/**
 * Created by david on 11/04/15.
 */
public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexArray;
    private final List<DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck){
        GeneratedData generatedData = ObjectBuilder.createPuck(new Cylinder( new Point(0f,0f,0f), radius, height), numPointsAroundPuck);
        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram){
        vertexArray.setVertexAttribPointer(0,colorProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,0);
    }

    public void draw(){
        for(DrawCommand drawCommand:drawList){
            drawCommand.draw();
        }
    }
}
