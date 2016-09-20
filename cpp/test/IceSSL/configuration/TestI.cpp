// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

#include <Ice/Ice.h>
#include <IceUtil/Thread.h>
#include <TestI.h>
#include <TestCommon.h>
#include <IceSSL/Plugin.h>

using namespace std;
using namespace Ice;

ServerI::ServerI(const CommunicatorPtr& communicator) :
    _communicator(communicator)
{
}

void
ServerI::noCert(const Ice::Current& c)
{
    try
    {
        IceSSL::NativeConnectionInfoPtr info = ICE_DYNAMIC_CAST(IceSSL::NativeConnectionInfo, c.con->getInfo());
        test(info->nativeCerts.size() == 0);
    }
    catch(const Ice::LocalException& ex)
    {
        cerr << ex << endl;
        test(false);
    }
}

void
ServerI::checkCert(ICE_IN(string) subjectDN, ICE_IN(string) issuerDN, const Ice::Current& c)
{
    try
    {
        IceSSL::NativeConnectionInfoPtr info = ICE_DYNAMIC_CAST(IceSSL::NativeConnectionInfo, c.con->getInfo());
        test(info->verified);
        test(info->nativeCerts.size() == 2 &&
             info->nativeCerts[0]->getSubjectDN() == IceSSL::DistinguishedName(subjectDN) &&
             info->nativeCerts[0]->getIssuerDN() == IceSSL::DistinguishedName(issuerDN)
        );
    }
    catch(const Ice::LocalException&)
    {
        test(false);
    }
}

void
ServerI::checkCipher(ICE_IN(string) cipher, const Ice::Current& c)
{
    try
    {
        IceSSL::NativeConnectionInfoPtr info = ICE_DYNAMIC_CAST(IceSSL::NativeConnectionInfo, c.con->getInfo());
        test(info->cipher.compare(0, cipher.size(), cipher) == 0);
    }
    catch(const Ice::LocalException&)
    {
        test(false);
    }
}

void
ServerI::destroy()
{
    string defaultDir = _communicator->getProperties()->getProperty("IceSSL.DefaultDir");
    _communicator->destroy();
}

Test::ServerPrxPtr
ServerFactoryI::createServer(ICE_IN(Test::Properties) props, const Current&)
{
    InitializationData initData;
    initData.properties = createProperties();
    for(Test::Properties::const_iterator p = props.begin(); p != props.end(); ++p)
    {
        initData.properties->setProperty(p->first, p->second);
    }

    CommunicatorPtr communicator = initialize(initData);
    ObjectAdapterPtr adapter = communicator->createObjectAdapterWithEndpoints("ServerAdapter", "ssl");
    ServerIPtr server = ICE_MAKE_SHARED(ServerI, communicator);
    ObjectPrxPtr obj = adapter->addWithUUID(server);
    _servers[obj->ice_getIdentity()] = server;
    adapter->activate();

    return ICE_UNCHECKED_CAST(Test::ServerPrx, obj);
}

void
ServerFactoryI::destroyServer(ICE_IN(Test::ServerPrxPtr) srv, const Ice::Current&)
{
    map<Identity, ServerIPtr>::iterator p = _servers.find(srv->ice_getIdentity());
    if(p != _servers.end())
    {
        p->second->destroy();
        _servers.erase(p);
    }
}

void
ServerFactoryI::shutdown(const Ice::Current& current)
{
    test(_servers.empty());
    current.adapter->getCommunicator()->shutdown();
}
