// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

#include <Ice/Ice.h>
#include <TestCommon.h>
#include <Test.h>

using namespace std;

DEFINE_TEST("client")

int
run(int, char**, const Ice::CommunicatorPtr& communicator)
{
    Test::ChecksumPrxPtr allTests(const Ice::CommunicatorPtr&, bool);
    Test::ChecksumPrxPtr checksum = allTests(communicator, false);
    checksum->shutdown();
    return EXIT_SUCCESS;
}

int
main(int argc, char* argv[])
{
#ifdef ICE_STATIC_LIBS
    Ice::registerIceSSL();
#endif
    try
    {        
        Ice::CommunicatorHolder ich = Ice::initialize(argc, argv);
        return run(argc, argv, ich.communicator());
    }
    catch(const Ice::Exception& ex)
    {
        cerr << ex << endl;
        return EXIT_FAILURE;
    }
}
