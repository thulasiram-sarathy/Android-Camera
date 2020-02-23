package com.camera.cameraview;


import android.location.Location;

import com.camera.cameraview.controls.Facing;
import com.camera.cameraview.controls.PictureFormat;
import com.camera.cameraview.size.Size;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class PictureResultTest extends BaseTest {

    private PictureResult.Stub stub = new PictureResult.Stub();

    @Test
    public void testResult() {
        PictureFormat format = PictureFormat.JPEG;
        int rotation = 90;
        Size size = new Size(20, 120);
        byte[] jpeg = new byte[]{2, 4, 1, 5, 2};
        Location location = Mockito.mock(Location.class);
        boolean isSnapshot = true;
        Facing facing = Facing.FRONT;

        stub.format = format;
        stub.rotation = rotation;
        stub.size = size;
        stub.data = jpeg;
        stub.location = location;
        stub.facing = facing;
        //noinspection ConstantConditions
        stub.isSnapshot = isSnapshot;

        PictureResult result = new PictureResult(stub);
        Assert.assertEquals(result.getFormat(), format);
        assertEquals(result.getRotation(), rotation);
        Assert.assertEquals(result.getSize(), size);
        assertEquals(result.getData(), jpeg);
        assertEquals(result.getLocation(), location);
        //noinspection ConstantConditions
        assertEquals(result.isSnapshot(), isSnapshot);
        Assert.assertEquals(result.getFacing(), facing);
    }
}
