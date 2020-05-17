package com.otaliastudios.cameraview.size;

import android.content.res.TypedArray;

import androidx.annotation.NonNull;

import com.otaliastudios.cameraview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses size selectors from XML attributes.
 */
public class SizeSelectorParser {

    private SizeSelector pictureSizeSelector;

    public SizeSelectorParser(@NonNull TypedArray array) {
        List<SizeSelector> pictureConstraints = new ArrayList<>(3);
        if (array.hasValue(R.styleable.CameraView_cameraPictureSizeMinWidth)) {
            pictureConstraints.add(SizeSelectors.minWidth(
                    array.getInteger(R.styleable.CameraView_cameraPictureSizeMinWidth, 0)
            ));
        }
        if (array.hasValue(R.styleable.CameraView_cameraPictureSizeMaxWidth)) {
            pictureConstraints.add(SizeSelectors.maxWidth(
                    array.getInteger(R.styleable.CameraView_cameraPictureSizeMaxWidth, 0)
            ));
        }
        if (array.hasValue(R.styleable.CameraView_cameraPictureSizeMinHeight)) {
            pictureConstraints.add(SizeSelectors.minHeight(
                    array.getInteger(R.styleable.CameraView_cameraPictureSizeMinHeight, 0)
            ));
        }
        if (array.hasValue(R.styleable.CameraView_cameraPictureSizeMaxHeight)) {
            pictureConstraints.add(SizeSelectors.maxHeight(
                    array.getInteger(R.styleable.CameraView_cameraPictureSizeMaxHeight, 0)
            ));
        }
        if (array.hasValue(R.styleable.CameraView_cameraPictureSizeMinArea)) {
            pictureConstraints.add(SizeSelectors.minArea(
                    array.getInteger(R.styleable.CameraView_cameraPictureSizeMinArea, 0)
            ));
        }
        if (array.hasValue(R.styleable.CameraView_cameraPictureSizeMaxArea)) {
            pictureConstraints.add(SizeSelectors.maxArea(
                    array.getInteger(R.styleable.CameraView_cameraPictureSizeMaxArea, 0)
            ));
        }
        if (array.hasValue(R.styleable.CameraView_cameraPictureSizeAspectRatio)) {
            //noinspection ConstantConditions
            pictureConstraints.add(SizeSelectors.aspectRatio(
                    AspectRatio.parse(array.getString(
                            R.styleable.CameraView_cameraPictureSizeAspectRatio)), 0));
        }

        if (array.getBoolean(R.styleable.CameraView_cameraPictureSizeSmallest, false)) {
            pictureConstraints.add(SizeSelectors.smallest());
        }
        if (array.getBoolean(R.styleable.CameraView_cameraPictureSizeBiggest, false)) {
            pictureConstraints.add(SizeSelectors.biggest());
        }
        pictureSizeSelector = !pictureConstraints.isEmpty() ?
                SizeSelectors.and(pictureConstraints.toArray(new SizeSelector[0])) :
                SizeSelectors.biggest();
    }

    @NonNull
    public SizeSelector getPictureSizeSelector() {
        return pictureSizeSelector;
    }


}
