// **********************************************************************
//
// Copyright (c) 2003-2008 ZeroC, Inc. All rights reserved.
//
// This copy of Ice-E is licensed to you under the terms described in the
// ICEE_LICENSE file included in this distribution.
//
// **********************************************************************

public class Collocated
{
    public static int
    run(String[] args, Ice.Communicator communicator, Ice.InitializationData data, java.io.PrintStream out)
    {
        communicator.getProperties().setProperty("Test.Proxy", "d:default -p 12010 -t 10000");
        communicator.getProperties().setProperty("TestAdapter.Endpoints", "default -p 12010 -t 10000");
        Ice.ObjectAdapter adapter = communicator.createObjectAdapter("TestAdapter");
        Ice.Object d = new DI();
        adapter.add(d, communicator.stringToIdentity("d"));
        adapter.addFacet(d, communicator.stringToIdentity("d"), "facetABCD");
        Ice.Object f = new FI();
        adapter.addFacet(f, communicator.stringToIdentity("d"), "facetEF");
        Ice.Object h = new HI(communicator);
        adapter.addFacet(h, communicator.stringToIdentity("d"), "facetGH");
        adapter.activate();
 
        AllTests.allTests(communicator, out);

        return 0;
    }

    public static void
    main(String[] args)
    {
        int status = 0;
        Ice.Communicator communicator = null;

        try
        {
            communicator = Ice.Util.initialize(args);
            status = run(args, communicator, null, System.out);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
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
                ex.printStackTrace();
                status = 1;
            }
        }

        System.exit(status);
    }
}
