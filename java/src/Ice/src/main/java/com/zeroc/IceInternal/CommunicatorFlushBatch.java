// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package com.zeroc.IceInternal;

import java.util.concurrent.Callable;

public class CommunicatorFlushBatch extends InvocationFutureI<Void>
{
    public CommunicatorFlushBatch(com.zeroc.Ice.Communicator communicator, Instance instance)
    {
        super(communicator, instance, "flushBatchRequests");

        _observer = ObserverHelper.get(instance, "flushBatchRequests");

        //
        // _useCount is initialized to 1 to prevent premature callbacks.
        // The caller must invoke ready() after all flush requests have
        // been initiated.
        //
        _useCount = 1;
    }

    @Override
    protected void __sent()
    {
        super.__sent();

        assert((_state & StateOK) != 0);
        complete(null);
    }

    public void flushConnection(final com.zeroc.Ice.ConnectionI con)
    {
        class FlushBatch extends OutgoingAsyncBaseI<Void>
        {
            public FlushBatch()
            {
                super(CommunicatorFlushBatch.this.getCommunicator(),
                      CommunicatorFlushBatch.this._instance,
                      CommunicatorFlushBatch.this.getOperation());
            }

            @Override
            protected void __sent()
            {
                assert(false);
            }

            @Override
            protected boolean __needCallback()
            {
                return false;
            }

            @Override
            protected void __completed()
            {
                assert(false);
            }

            @Override
            public boolean sent()
            {
                if(_childObserver != null)
                {
                    _childObserver.detach();
                    _childObserver = null;
                }
                doCheck(false);
                return false;
            }

            // TODO: MJN: This is missing a test.
            @Override
            public boolean completed(com.zeroc.Ice.Exception ex)
            {
                if(_childObserver != null)
                {
                    _childObserver.failed(ex.ice_id());
                    _childObserver.detach();
                    _childObserver = null;
                }
                doCheck(false);
                return false;
            }

            @Override
            protected com.zeroc.Ice.Instrumentation.InvocationObserver getObserver()
            {
                return CommunicatorFlushBatch.this._observer;
            }
        }

        synchronized(this)
        {
            ++_useCount;
        }

        try
        {
            final FlushBatch flushBatch = new FlushBatch();
            final int batchRequestNum = con.getBatchRequestQueue().swap(flushBatch.getOs());
            if(batchRequestNum == 0)
            {
                flushBatch.sent();
            }
            else if(_instance.queueRequests())
            {
                _instance.getQueueExecutor().executeNoThrow(new Callable<Void>()
                {
                    @Override
                    public Void call() throws RetryException
                    {
                        con.sendAsyncRequest(flushBatch, false, false, batchRequestNum);
                        return null;
                    }
                });
            }
            else
            {
                con.sendAsyncRequest(flushBatch, false, false, batchRequestNum);
            }
        }
        catch(RetryException ex)
        {
            doCheck(false);
            throw ex.get();
        }
        catch(com.zeroc.Ice.LocalException ex)
        {
            doCheck(false);
            throw ex;
        }
    }

    public void ready()
    {
        doCheck(true);
    }

    public void __wait()
    {
        if(Thread.interrupted())
        {
            throw new com.zeroc.Ice.OperationInterruptedException();
        }

        try
        {
            get();
        }
        catch(InterruptedException ex)
        {
            throw new com.zeroc.Ice.OperationInterruptedException();
        }
        catch(java.util.concurrent.ExecutionException ee)
        {
            try
            {
                throw ee.getCause();
            }
            catch(RuntimeException ex) // Includes LocalException
            {
                throw ex;
            }
            catch(Throwable ex)
            {
                throw new com.zeroc.Ice.UnknownException(ex);
            }
        }
    }

    private void doCheck(boolean userThread)
    {
        synchronized(this)
        {
            assert(_useCount > 0);
            if(--_useCount > 0)
            {
                return;
            }
        }

        if(sent(true))
        {
            if(userThread)
            {
                _sentSynchronously = true;
                invokeSent();
            }
            else
            {
                invokeSentAsync();
            }
        }
    }

    private int _useCount;
}
