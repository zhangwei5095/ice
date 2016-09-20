// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package com.zeroc.Ice;

/**
 * Base class for interoperating with existing applications that pass interfaces by value.
 **/
public class InterfaceByValue extends Value
{
    /**
     * The constructor accepts the Slice type ID of the interface being passed by value.
     *
     * @param id The Slice type ID of the interface.
     **/
    public InterfaceByValue(String id)
    {
        _id = id;
    }

    /**
     * Returns the Slice type ID of the interface being passed by value.
     *
     * @return The Slice type ID.
     **/
    public String ice_id()
    {
        return _id;
    }

    @Override
    protected void __writeImpl(OutputStream __os)
    {
        __os.startSlice(ice_id(), -1, true);
        __os.endSlice();
    }

    @Override
    protected void __readImpl(InputStream __is)
    {
        __is.startSlice();
        __is.endSlice();
    }

    public static final long serialVersionUID = 0L;

    private String _id;
}
