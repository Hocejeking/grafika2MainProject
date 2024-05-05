package p01start.Map;

import lwjglutils.OGLTexture2D;

public class SkyBox {
    public lwjglutils.OGLTexture2D[] textureCube;
    public SkyBox(OGLTexture2D[] arr){
        textureCube = arr;
    }
}
