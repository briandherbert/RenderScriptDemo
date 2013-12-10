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
 * The source Renderscript file: /Users/bherbert/Code/RenderScriptDemo/src/grayscale.rs
 */
package ca.tutortutor.grayscale;

import android.support.v8.renderscript.*;
import android.content.res.Resources;

/**
 * @hide
 */
public class ScriptC_grayscale extends ScriptC {
    private static final String __rs_resource_name = "grayscale";
    // Constructor
    public  ScriptC_grayscale(RenderScript rs) {
        this(rs,
             rs.getApplicationContext().getResources(),
             rs.getApplicationContext().getResources().getIdentifier(
                 __rs_resource_name, "raw",
                 rs.getApplicationContext().getPackageName()));
    }

    public  ScriptC_grayscale(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        __ALLOCATION = Element.ALLOCATION(rs);
        __SCRIPT = Element.SCRIPT(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __ALLOCATION;
    private Element __SCRIPT;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_SCRIPT;
    private final static int mExportVarIdx_in = 0;
    private Allocation mExportVar_in;
    public synchronized void set_in(Allocation v) {
        setVar(mExportVarIdx_in, v);
        mExportVar_in = v;
    }

    public Allocation get_in() {
        return mExportVar_in;
    }

    public Script.FieldID getFieldID_in() {
        return createFieldID(mExportVarIdx_in, null);
    }

    private final static int mExportVarIdx_out = 1;
    private Allocation mExportVar_out;
    public synchronized void set_out(Allocation v) {
        setVar(mExportVarIdx_out, v);
        mExportVar_out = v;
    }

    public Allocation get_out() {
        return mExportVar_out;
    }

    public Script.FieldID getFieldID_out() {
        return createFieldID(mExportVarIdx_out, null);
    }

    private final static int mExportVarIdx_script = 2;
    private Script mExportVar_script;
    public synchronized void set_script(Script v) {
        setVar(mExportVarIdx_script, v);
        mExportVar_script = v;
    }

    public Script get_script() {
        return mExportVar_script;
    }

    public Script.FieldID getFieldID_script() {
        return createFieldID(mExportVarIdx_script, null);
    }

    private final static int mExportForEachIdx_root = 0;
    public Script.KernelID getKernelID_root() {
        return createKernelID(mExportForEachIdx_root, 3, null, null);
    }

    public void forEach_root(Allocation ain, Allocation aout) {
        forEach_root(ain, aout, null);
    }

    public void forEach_root(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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
        forEach(mExportForEachIdx_root, ain, aout, null, sc);
    }

    private final static int mExportFuncIdx_filter = 0;
    public void invoke_filter() {
        invoke(mExportFuncIdx_filter);
    }

}

