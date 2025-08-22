package com.vhkfoundation.commonutility;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class PreferenceConnectorTest {

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        PreferenceConnector.cleanPrefrences(context);
    }

    @Test
    public void writeReadFloat_persistsValue() {
        float expected = 1.5f;
        PreferenceConnector.writeFloat(context, "float_key", expected);
        assertEquals(expected, PreferenceConnector.readFloat(context, "float_key", 0f), 0.0f);
    }

    @Test
    public void writeReadLong_persistsValue() {
        long expected = 123L;
        PreferenceConnector.writeLong(context, "long_key", expected);
        assertEquals(expected, PreferenceConnector.readLong(context, "long_key", 0L));
    }
}
