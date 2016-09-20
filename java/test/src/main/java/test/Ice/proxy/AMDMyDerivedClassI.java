// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package test.Ice.proxy;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import test.Ice.proxy.AMD.Test._MyDerivedClassDisp;

public final class AMDMyDerivedClassI implements _MyDerivedClassDisp
{
    public AMDMyDerivedClassI()
    {
    }

    @Override
    public CompletionStage<com.zeroc.Ice.ObjectPrx> echoAsync(com.zeroc.Ice.ObjectPrx obj,
                                                              com.zeroc.Ice.Current current)
    {
        return CompletableFuture.completedFuture(obj);
    }

    @Override
    public CompletionStage<Void> shutdownAsync(com.zeroc.Ice.Current c)
    {
        c.adapter.getCommunicator().shutdown();
        return CompletableFuture.completedFuture((Void)null);
    }

    @Override
    public CompletionStage<java.util.Map<String, String>> getContextAsync(com.zeroc.Ice.Current current)
    {
        return CompletableFuture.completedFuture(_ctx);
    }

    @Override
    public boolean ice_isA(String s, com.zeroc.Ice.Current current)
    {
        _ctx = current.ctx;
        return _MyDerivedClassDisp.super.ice_isA(s, current);
    }

    private java.util.Map<String, String> _ctx;
}
