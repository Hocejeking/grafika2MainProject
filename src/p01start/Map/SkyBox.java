package p01start.Map;

import lwjglutils.OGLTexture2D;
import org.w3c.dom.Entity;

public class SkyBox {
    public lwjglutils.OGLTexture2D[] textureCube;
    public SkyBox(OGLTexture2D[] arr){
        textureCube = arr;
    }
}
