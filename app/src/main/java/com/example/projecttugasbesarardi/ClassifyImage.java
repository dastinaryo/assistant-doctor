package com.example.projecttugasbesarardi;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.projecttugasbesarardi.ml.ModelBrain;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClassifyImage {

    public float classifyImage(Bitmap image, Context context) {
        try {
            ModelBrain model = ModelBrain.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            // Resize the image to (224, 224)
            Bitmap resizedImage = Bitmap.createScaledBitmap(image, 224, 224, true);

            // Convert Bitmap to ByteBuffer with normalization
            ByteBuffer byteBuffer = convertBitmapToByteBuffer(resizedImage);

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelBrain.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Post-process the output
            float[] resultArray = outputFeature0.getFloatArray();

            // Assuming your model output is a single probability value
            float prediction = resultArray[0];

            // Releases model resources if no longer used.
            model.close();
            return prediction;
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
            return -1;
        }
    }

    // Helper method to convert Bitmap to ByteBuffer with normalization
    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int[] intValues = new int[224 * 224];
        float[] floatValues = new float[224 * 224 * 3];

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * 3] = ((val >> 16) & 0xFF) /  255.0f;
            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF) / 255.0f;
            floatValues[i * 3 + 2] = (val & 0xFF) / 255.0f;
        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        for (float floatValue : floatValues) {
            byteBuffer.putFloat(floatValue);
        }

        return byteBuffer;
    }
}
