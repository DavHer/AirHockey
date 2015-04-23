package com.example.david.airhockey.programs;

import android.content.Context;
import static android.opengl.GLES20.glUseProgram;
import com.example.david.airhockey.util.ShaderHelper;
import com.example.david.airhockey.util.TextResourceReader;

import java.nio.FloatBuffer;

/**
 * Created by david on 09/04/15.
 */
public class ShaderProgram {

    //shader program
    protected int program;

    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    //Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";

    protected ShaderProgram(Context context, int vertexShaderResourceId,int fragmentShaderResourceId){
        //Compile the shaders and link the program
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId));
    }

    public void useProgram(){
        //Set the current OpenGL sahder program to this program
        glUseProgram(program);
    }

}
