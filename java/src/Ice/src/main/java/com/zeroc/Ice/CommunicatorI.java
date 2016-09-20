// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package com.zeroc.Ice;

public final class CommunicatorI implements Communicator
{
    @Override
    public void
    destroy()
    {
        _instance.destroy();
    }

    @Override
    public void
    shutdown()
    {
        _instance.objectAdapterFactory().shutdown();
    }

    @Override
    public void
    waitForShutdown()
    {
        _instance.objectAdapterFactory().waitForShutdown();
    }

    @Override
    public boolean
    isShutdown()
    {
        return _instance.objectAdapterFactory().isShutdown();
    }

    @Override
    public ObjectPrx
    stringToProxy(String s)
    {
        return _instance.proxyFactory().stringToProxy(s);
    }

    @Override
    public String
    proxyToString(ObjectPrx proxy)
    {
        return _instance.proxyFactory().proxyToString(proxy);
    }

    @Override
    public ObjectPrx
    propertyToProxy(String s)
    {
        return _instance.proxyFactory().propertyToProxy(s);
    }

    @Override
    public java.util.Map<String, String>
    proxyToProperty(ObjectPrx proxy, String prefix)
    {
        return _instance.proxyFactory().proxyToProperty(proxy, prefix);
    }

    @Override @SuppressWarnings("deprecation")
    public Identity
    stringToIdentity(String s)
    {
        return Util.stringToIdentity(s);
    }

    @Override @SuppressWarnings("deprecation")
    public String
    identityToString(Identity ident)
    {
        return Util.identityToString(ident);
    }

    @Override
    public ObjectAdapter
    createObjectAdapter(String name)
    {
        return _instance.objectAdapterFactory().createObjectAdapter(name, null);
    }

    @Override
    public ObjectAdapter
    createObjectAdapterWithEndpoints(String name, String endpoints)
    {
        if(name.length() == 0)
        {
            name = java.util.UUID.randomUUID().toString();
        }

        getProperties().setProperty(name + ".Endpoints", endpoints);
        return _instance.objectAdapterFactory().createObjectAdapter(name, null);
    }

    @Override
    public ObjectAdapter
    createObjectAdapterWithRouter(String name, RouterPrx router)
    {
        if(name.length() == 0)
        {
            name = java.util.UUID.randomUUID().toString();
        }

        //
        // We set the proxy properties here, although we still use the proxy supplied.
        //
        java.util.Map<String, String> properties = proxyToProperty(router, name + ".Router");
        for(java.util.Map.Entry<String, String> p : properties.entrySet())
        {
            getProperties().setProperty(p.getKey(), p.getValue());
        }

        return _instance.objectAdapterFactory().createObjectAdapter(name, router);
    }

    @Override @SuppressWarnings("deprecation")
    public void addObjectFactory(ObjectFactory factory, String id)
    {
        _instance.addObjectFactory(factory, id);
    }

    @Override @SuppressWarnings("deprecation")
    public ObjectFactory findObjectFactory(String id)
    {
        return _instance.findObjectFactory(id);
    }

    @Override
    public ValueFactoryManager getValueFactoryManager()
    {
        return _instance.initializationData().valueFactoryManager;
    }

    @Override
    public Properties
    getProperties()
    {
        return _instance.initializationData().properties;
    }

    @Override
    public Logger
    getLogger()
    {
        return _instance.initializationData().logger;
    }

    @Override
    public com.zeroc.Ice.Instrumentation.CommunicatorObserver
    getObserver()
    {
        return _instance.initializationData().observer;
    }

    @Override
    public RouterPrx
    getDefaultRouter()
    {
        return _instance.referenceFactory().getDefaultRouter();
    }

    @Override
    public void
    setDefaultRouter(RouterPrx router)
    {
        _instance.setDefaultRouter(router);
    }

    @Override
    public LocatorPrx
    getDefaultLocator()
    {
        return _instance.referenceFactory().getDefaultLocator();
    }

    @Override
    public void
    setDefaultLocator(LocatorPrx locator)
    {
        _instance.setDefaultLocator(locator);
    }

    @Override
    public ImplicitContext
    getImplicitContext()
    {
        return _instance.getImplicitContext();
    }

    @Override
    public PluginManager
    getPluginManager()
    {
        return _instance.pluginManager();
    }

    @Override
    public void flushBatchRequests()
    {
        __flushBatchRequestsAsync().__wait();
    }

    @Override
    public java.util.concurrent.CompletableFuture<Void> flushBatchRequestsAsync()
    {
        return __flushBatchRequestsAsync();
    }

    public com.zeroc.IceInternal.CommunicatorFlushBatch __flushBatchRequestsAsync()
    {
        com.zeroc.IceInternal.OutgoingConnectionFactory connectionFactory = _instance.outgoingConnectionFactory();
        com.zeroc.IceInternal.ObjectAdapterFactory adapterFactory = _instance.objectAdapterFactory();

        //
        // This callback object receives the results of all invocations
        // of Connection.begin_flushBatchRequests.
        //
        com.zeroc.IceInternal.CommunicatorFlushBatch __f =
            new com.zeroc.IceInternal.CommunicatorFlushBatch(this, _instance);

        connectionFactory.flushAsyncBatchRequests(__f);
        adapterFactory.flushAsyncBatchRequests(__f);

        //
        // Inform the callback that we have finished initiating all of the
        // flush requests.
        //
        __f.ready();

        return __f;
    }

    @Override
    public ObjectPrx
    createAdmin(ObjectAdapter adminAdapter, Identity adminId)
    {
        return _instance.createAdmin(adminAdapter, adminId);
    }

    @Override
    public ObjectPrx
    getAdmin()
    {
        return _instance.getAdmin();
    }

    @Override
    public void
    addAdminFacet(Object servant, String facet)
    {
        _instance.addAdminFacet(servant, facet);
    }

    @Override
    public Object
    removeAdminFacet(String facet)
    {
        return _instance.removeAdminFacet(facet);
    }

    @Override
    public Object
    findAdminFacet(String facet)
    {
        return _instance.findAdminFacet(facet);
    }

    @Override
    public java.util.Map<String, com.zeroc.Ice.Object>
    findAllAdminFacets()
    {
        return _instance.findAllAdminFacets();
    }

    CommunicatorI(InitializationData initData)
    {
        _instance = new com.zeroc.IceInternal.Instance(this, initData);
    }

    /**
      * For compatibility with C#, we do not invoke methods on other objects
      * from within a finalizer.
      *
    protected synchronized void
    finalize()
        throws Throwable
    {
        if(!_instance.destroyed())
        {
            _instance.logger().warning("Ice::Communicator::destroy() has not been called");
        }

        super.finalize();
    }
      */

    //
    // Certain initialization tasks need to be completed after the
    // constructor.
    //
    String[] finishSetup(String[] args)
    {
        try
        {
            return _instance.finishSetup(args, this);
        }
        catch(RuntimeException ex)
        {
            _instance.destroy();
            throw ex;
        }
    }

    //
    // For use by com.zeroc.IceInternal.Util.getInstance()
    //
    public com.zeroc.IceInternal.Instance
    getInstance()
    {
        return _instance;
    }

    private com.zeroc.IceInternal.Instance _instance;
}
