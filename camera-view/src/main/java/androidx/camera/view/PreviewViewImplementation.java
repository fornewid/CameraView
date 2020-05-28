/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.camera.view;

import android.util.Size;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.view.preview.transform.PreviewTransform;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Wraps the underlying handling of the {@link android.view.Surface} used for preview, which is
 * done using either a {@link android.view.TextureView} (see {@link TextureViewImplementation})
 * or a {@link android.view.SurfaceView} (see {@link SurfaceViewImplementation}).
 */
abstract class PreviewViewImplementation {

    @Nullable
    Size mResolution;

    @Nullable
    FrameLayout mParent;

    @Nullable
    private PreviewTransform mPreviewTransform;

    abstract void initializePreview();

    @Nullable
    abstract View getPreview();

    /**
     * Starts to execute the {@link SurfaceRequest} by providing a Surface.
     *
     * <p>A listener can be set optionally to be notified when the provided Surface is not in use
     * any more or the SurfaceRequest is cancelled before providing a Surface. This can be used
     * as a signal that SurfaceRequest (and the Preview) is no longer using PreviewView and we
     * can do some cleanup. The listener will be invoked on main thread.
     */
    abstract void onSurfaceRequested(@NonNull SurfaceRequest surfaceRequest,
            @Nullable OnSurfaceNotInUseListener onSurfaceNotInUseListener);

    /**
     * @param parent           the containing parent {@link PreviewView}.
     * @param previewTransform Allows to apply preview correction and scale types.
     */
    void init(@NonNull FrameLayout parent, @NonNull PreviewTransform previewTransform) {
        mParent = parent;
        mPreviewTransform = previewTransform;
    }

    @Nullable
    public Size getResolution() {
        return mResolution;
    }

    /**
     * Invoked when the preview needs to be adjusted, either because the layout bounds of the
     * preview's container {@link PreviewView} have changed, or the {@link PreviewView.ScaleType}
     * has changed.
     * <p>
     * Corrects and adjusts the preview using the latest {@link PreviewView.ScaleType} and
     * display properties such as the display orientation and size.
     */
    void redrawPreview() {
        applyCurrentScaleType();
    }

    /** Invoked after a {@link android.view.Surface} has been provided to the camera for preview. */
    void onSurfaceProvided() {
        applyCurrentScaleType();
    }

    private void applyCurrentScaleType() {
        final View preview = getPreview();
        if (mPreviewTransform != null && mParent != null && preview != null
                && mResolution != null) {
            mPreviewTransform.applyCurrentScaleType(mParent, preview, mResolution);
        }
    }

    /** Invoked when onAttachedToWindow happens in the PreviewView. */
    abstract void onAttachedToWindow();

    /** Invoked when onDetachedFromWindow happens in the PreviewView */
    abstract void onDetachedFromWindow();

    /**
     * Returns a {@link ListenableFuture} which will complete when the next frame is shown.
     *
     * <p>For implementation that does not support frame update event, the returned future will
     * complete immediately.
     */
    @NonNull
    abstract ListenableFuture<Void> waitForNextFrame();

    /**
     * Listener to be notified when the provided Surface is no longer in use or the request is
     * cancelled before a Surface is provided.
     */
    interface OnSurfaceNotInUseListener {
        void onSurfaceNotInUse();
    }
}
