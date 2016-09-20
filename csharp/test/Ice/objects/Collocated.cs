// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

using Test;
using System;
using System.Reflection;

[assembly: CLSCompliant(true)]

[assembly: AssemblyTitle("IceTest")]
[assembly: AssemblyDescription("Ice test")]
[assembly: AssemblyCompany("ZeroC, Inc.")]

public class Client
{
    private static int run(string[] args, Ice.Communicator communicator)
    {
        communicator.getProperties().setProperty("TestAdapter.Endpoints", "default -p 12010");
        Ice.ObjectAdapter adapter = communicator.createObjectAdapter("TestAdapter");
        var initial = new InitialI(adapter);
        adapter.add(initial, Ice.Util.stringToIdentity("initial"));
        UnexpectedObjectExceptionTestI uet = new UnexpectedObjectExceptionTestI();
        adapter.add(uet, Ice.Util.stringToIdentity("uoet"));
        AllTests.allTests(communicator);
        // We must call shutdown even in the collocated case for cyclic dependency cleanup
        initial.shutdown();
        return 0;
    }

    public static int Main(string[] args)
    {
        int status = 0;
        Ice.Communicator communicator = null;

        try
        {
            var data = new Ice.InitializationData();
            communicator = Ice.Util.initialize(ref args, data);
            status = run(args, communicator);
        }
        catch(Exception ex)
        {
            Console.WriteLine(ex);
            status = 1;
        }

        if(communicator != null)
        {
            try
            {
                communicator.destroy();
            }
            catch(Ice.LocalException ex)
            {
                Console.WriteLine(ex);
                status = 1;
            }
        }

        return status;
    }
}
