package com.camera.cameraview;


import android.location.Location;

import com.camera.cameraview.controls.Audio;
import com.camera.cameraview.controls.Facing;
import com.camera.cameraview.controls.VideoCodec;
import com.camera.cameraview.size.Size;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileDescriptor;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class VideoResultTest extends BaseTest {

    private VideoResult.Stub stub = new VideoResult.Stub();

    @Test
    public void testResultWithFile() {
        File file = Mockito.mock(File.class);
        int rotation = 90;
        Size size = new Size(20, 120);
        VideoCodec codec = VideoCodec.H_263;
        Location location = Mockito.mock(Location.class);
        boolean isSnapshot = true;
        int maxDuration = 1234;
        long maxFileSize = 500000;
        int reason = VideoResult.REASON_MAX_DURATION_REACHED;
        int videoFrameRate = 30;
        int videoBitRate = 300000;
        int audioBitRate = 30000;
        Audio audio = Audio.ON;
        Facing facing = Facing.FRONT;

        stub.file = file;
        stub.rotation = rotation;
        stub.size = size;
        stub.videoCodec = codec;
        stub.location = location;
        stub.isSnapshot = isSnapshot;
        stub.maxDuration = maxDuration;
        stub.maxSize = maxFileSize;
        stub.endReason = reason;
        stub.videoFrameRate = videoFrameRate;
        stub.videoBitRate = videoBitRate;
        stub.audioBitRate = audioBitRate;
        stub.audio = audio;
        stub.facing = facing;

        VideoResult result = new VideoResult(stub);
        assertEquals(result.getFile(), file);
        assertEquals(result.getRotation(), rotation);
        Assert.assertEquals(result.getSize(), size);
        Assert.assertEquals(result.getVideoCodec(), codec);
        assertEquals(result.getLocation(), location);
        assertEquals(result.isSnapshot(), isSnapshot);
        assertEquals(result.getMaxSize(), maxFileSize);
        assertEquals(result.getMaxDuration(), maxDuration);
        assertEquals(result.getTerminationReason(), reason);
        assertEquals(result.getVideoFrameRate(), videoFrameRate);
        assertEquals(result.getVideoBitRate(), videoBitRate);
        assertEquals(result.getAudioBitRate(), audioBitRate);
        Assert.assertEquals(result.getAudio(), audio);
        Assert.assertEquals(result.getFacing(), facing);
    }

    @Test
    public void testResultWithFileDescriptor() {
        FileDescriptor fileDescriptor = FileDescriptor.in;
        int rotation = 90;
        Size size = new Size(20, 120);
        VideoCodec codec = VideoCodec.H_263;
        Location location = Mockito.mock(Location.class);
        boolean isSnapshot = true;
        int maxDuration = 1234;
        long maxFileSize = 500000;
        int reason = VideoResult.REASON_MAX_DURATION_REACHED;
        int videoFrameRate = 30;
        int videoBitRate = 300000;
        int audioBitRate = 30000;
        Audio audio = Audio.ON;
        Facing facing = Facing.FRONT;

        stub.fileDescriptor = fileDescriptor;
        stub.rotation = rotation;
        stub.size = size;
        stub.videoCodec = codec;
        stub.location = location;
        stub.isSnapshot = isSnapshot;
        stub.maxDuration = maxDuration;
        stub.maxSize = maxFileSize;
        stub.endReason = reason;
        stub.videoFrameRate = videoFrameRate;
        stub.videoBitRate = videoBitRate;
        stub.audioBitRate = audioBitRate;
        stub.audio = audio;
        stub.facing = facing;

        VideoResult result = new VideoResult(stub);
        assertEquals(result.getFileDescriptor(), fileDescriptor);
        assertEquals(result.getRotation(), rotation);
        Assert.assertEquals(result.getSize(), size);
        Assert.assertEquals(result.getVideoCodec(), codec);
        assertEquals(result.getLocation(), location);
        assertEquals(result.isSnapshot(), isSnapshot);
        assertEquals(result.getMaxSize(), maxFileSize);
        assertEquals(result.getMaxDuration(), maxDuration);
        assertEquals(result.getTerminationReason(), reason);
        assertEquals(result.getVideoFrameRate(), videoFrameRate);
        assertEquals(result.getVideoBitRate(), videoBitRate);
        assertEquals(result.getAudioBitRate(), audioBitRate);
        Assert.assertEquals(result.getAudio(), audio);
        Assert.assertEquals(result.getFacing(), facing);
    }

    @Test(expected = RuntimeException.class)
    public void testResultWithNoFile() {
        VideoResult result = new VideoResult(stub);
        result.getFile();
    }

    @Test(expected = RuntimeException.class)
    public void testResultWithNoFileDescriptor() {
        VideoResult result = new VideoResult(stub);
        result.getFileDescriptor();
    }
}
