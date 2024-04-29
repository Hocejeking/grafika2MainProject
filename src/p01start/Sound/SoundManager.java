package p01start.Sound;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

import static org.lwjgl.openal.ALC10.*;


public class SoundManager {
    private long device;
    private long context;

    public SoundManager() {
        init();
    }

    private void init() {
        String defaultDeviceName = alcGetString(0,ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        context = alcCreateContext(device,attributes);
        alcMakeContextCurrent(context);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

}
