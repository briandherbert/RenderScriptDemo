/*
 * Copyright (C) 2011-2013 The Android Open Source Project
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

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: /Users/bherbert/Code/RenderScriptDemo/src/histogram.rs
 */
package ca.tutortutor.grayscale;

import android.support.v8.renderscript.*;
import android.content.res.Resources;

/**
 * @hide
 */
public class ScriptC_histogram extends ScriptC {
    private static final String __rs_resource_name = "histogram";
    // Constructor
    public  ScriptC_histogram(RenderScript rs) {
        this(rs,
             rs.getApplicationContext().getResources(),
             rs.getApplicationContext().getResources().getIdentifier(
                 __rs_resource_name, "raw",
                 rs.getApplicationContext().getPackageName()));
    }

    public  ScriptC_histogram(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        __ALLOCATION = Element.ALLOCATION(rs);
        __I32 = Element.I32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __ALLOCATION;
    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_I32;
    private final static int mExportVarIdx_gSrc = 0;
    private Allocation mExportVar_gSrc;
    public synchronized void set_gSrc(Allocation v) {
        setVar(mExportVarIdx_gSrc, v);
        mExportVar_gSrc = v;
    }

    public Allocation get_gSrc() {
        return mExportVar_gSrc;
    }

    public Script.FieldID getFieldID_gSrc() {
        return createFieldID(mExportVarIdx_gSrc, null);
    }

    private final static int mExportVarIdx_gDest = 1;
    private Allocation mExportVar_gDest;
    public synchronized void set_gDest(Allocation v) {
        setVar(mExportVarIdx_gDest, v);
        mExportVar_gDest = v;
    }

    public Allocation get_gDest() {
        return mExportVar_gDest;
    }

    public Script.FieldID getFieldID_gDest() {
        return createFieldID(mExportVarIdx_gDest, null);
    }

    private final static int mExportVarIdx_gSums = 2;
    private Allocation mExportVar_gSums;
    public synchronized void set_gSums(Allocation v) {
        setVar(mExportVarIdx_gSums, v);
        mExportVar_gSums = v;
    }

    public Allocation get_gSums() {
        return mExportVar_gSums;
    }

    public Script.FieldID getFieldID_gSums() {
        return createFieldID(mExportVarIdx_gSums, null);
    }

    private final static int mExportVarIdx_gSum = 3;
    private Allocation mExportVar_gSum;
    public synchronized void set_gSum(Allocation v) {
        setVar(mExportVarIdx_gSum, v);
        mExportVar_gSum = v;
    }

    public Allocation get_gSum() {
        return mExportVar_gSum;
    }

    public Script.FieldID getFieldID_gSum() {
        return createFieldID(mExportVarIdx_gSum, null);
    }

    private final static int mExportVarIdx_gWidth = 4;
    private int mExportVar_gWidth;
    public synchronized void set_gWidth(int v) {
        setVar(mExportVarIdx_gWidth, v);
        mExportVar_gWidth = v;
    }

    public int get_gWidth() {
        return mExportVar_gWidth;
    }

    public Script.FieldID getFieldID_gWidth() {
        return createFieldID(mExportVarIdx_gWidth, null);
    }

    private final static int mExportVarIdx_gHeight = 5;
    private int mExportVar_gHeight;
    public synchronized void set_gHeight(int v) {
        setVar(mExportVarIdx_gHeight, v);
        mExportVar_gHeight = v;
    }

    public int get_gHeight() {
        return mExportVar_gHeight;
    }

    public Script.FieldID getFieldID_gHeight() {
        return createFieldID(mExportVarIdx_gHeight, null);
    }

    private final static int mExportVarIdx_gStep = 6;
    private int mExportVar_gStep;
    public synchronized void set_gStep(int v) {
        setVar(mExportVarIdx_gStep, v);
        mExportVar_gStep = v;
    }

    public int get_gStep() {
        return mExportVar_gStep;
    }

    public Script.FieldID getFieldID_gStep() {
        return createFieldID(mExportVarIdx_gStep, null);
    }

    private final static int mExportVarIdx_gSteps = 7;
    private int mExportVar_gSteps;
    public synchronized void set_gSteps(int v) {
        setVar(mExportVarIdx_gSteps, v);
        mExportVar_gSteps = v;
    }

    public int get_gSteps() {
        return mExportVar_gSteps;
    }

    public Script.FieldID getFieldID_gSteps() {
        return createFieldID(mExportVarIdx_gSteps, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_pass1 = 1;
    public Script.KernelID getKernelID_pass1() {
        return createKernelID(mExportForEachIdx_pass1, 1, null, null);
    }

    public void forEach_pass1(Allocation ain) {
        forEach_pass1(ain, null);
    }

    public void forEach_pass1(Allocation ain, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__I32)) {
            throw new RSRuntimeException("Type mismatch with I32!");
        }
        forEach(mExportForEachIdx_pass1, ain, null, null, sc);
    }

    private final static int mExportForEachIdx_pass2 = 2;
    public Script.KernelID getKernelID_pass2() {
        return createKernelID(mExportForEachIdx_pass2, 2, null, null);
    }

    public void forEach_pass2(Allocation aout) {
        forEach_pass2(aout, null);
    }

    public void forEach_pass2(Allocation aout, Script.LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__I32)) {
            throw new RSRuntimeException("Type mismatch with I32!");
        }
        forEach(mExportForEachIdx_pass2, null, aout, null, sc);
    }

    private final static int mExportForEachIdx_drawhis = 3;
    public Script.KernelID getKernelID_drawhis() {
        return createKernelID(mExportForEachIdx_drawhis, 1, null, null);
    }

    public void forEach_drawhis(Allocation ain) {
        forEach_drawhis(ain, null);
    }

    public void forEach_drawhis(Allocation ain, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_drawhis, ain, null, null, sc);
    }

    private final static int mExportForEachIdx_drawhist = 4;
    public Script.KernelID getKernelID_drawhist() {
        return createKernelID(mExportForEachIdx_drawhist, 3, null, null);
    }

    public void forEach_drawhist(Allocation ain, Allocation aout) {
        forEach_drawhist(ain, aout, null);
    }

    public void forEach_drawhist(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // Verify dimensions
        Type tIn = ain.getType();
        Type tOut = aout.getType();
        if ((tIn.getCount() != tOut.getCount()) ||
            (tIn.getX() != tOut.getX()) ||
            (tIn.getY() != tOut.getY()) ||
            (tIn.getZ() != tOut.getZ()) ||
            (tIn.hasFaces() != tOut.hasFaces()) ||
            (tIn.hasMipmaps() != tOut.hasMipmaps())) {
            throw new RSRuntimeException("Dimension mismatch between input and output parameters!");
        }
        forEach(mExportForEachIdx_drawhist, ain, aout, null, sc);
    }

    private final static int mExportForEachIdx_invert = 5;
    public Script.KernelID getKernelID_invert() {
        return createKernelID(mExportForEachIdx_invert, 3, null, null);
    }

    public void forEach_invert(Allocation ain, Allocation aout) {
        forEach_invert(ain, aout, null);
    }

    public void forEach_invert(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // Verify dimensions
        Type tIn = ain.getType();
        Type tOut = aout.getType();
        if ((tIn.getCount() != tOut.getCount()) ||
            (tIn.getX() != tOut.getX()) ||
            (tIn.getY() != tOut.getY()) ||
            (tIn.getZ() != tOut.getZ()) ||
            (tIn.hasFaces() != tOut.hasFaces()) ||
            (tIn.hasMipmaps() != tOut.hasMipmaps())) {
            throw new RSRuntimeException("Dimension mismatch between input and output parameters!");
        }
        forEach(mExportForEachIdx_invert, ain, aout, null, sc);
    }

    private final static int mExportForEachIdx_drawxy = 6;
    public Script.KernelID getKernelID_drawxy() {
        return createKernelID(mExportForEachIdx_drawxy, 3, null, null);
    }

    public void forEach_drawxy(Allocation ain, Allocation aout) {
        forEach_drawxy(ain, aout, null);
    }

    public void forEach_drawxy(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // Verify dimensions
        Type tIn = ain.getType();
        Type tOut = aout.getType();
        if ((tIn.getCount() != tOut.getCount()) ||
            (tIn.getX() != tOut.getX()) ||
            (tIn.getY() != tOut.getY()) ||
            (tIn.getZ() != tOut.getZ()) ||
            (tIn.hasFaces() != tOut.hasFaces()) ||
            (tIn.hasMipmaps() != tOut.hasMipmaps())) {
            throw new RSRuntimeException("Dimension mismatch between input and output parameters!");
        }
        forEach(mExportForEachIdx_drawxy, ain, aout, null, sc);
    }

    private final static int mExportFuncIdx_rescale = 0;
    public void invoke_rescale() {
        invoke(mExportFuncIdx_rescale);
    }

}

