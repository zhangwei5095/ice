// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package com.zeroc.IceBox;

public final class Admin
{
    private static class Client extends com.zeroc.Ice.Application
    {
        private void usage()
        {
            System.err.println(
                "Usage: " + appName() + " [options] [command...]\n" +
                "Options:\n" +
                "-h, --help          Show this message.\n" +
                "\n" +
                "Commands:\n" +
                "start SERVICE       Start a service.\n" +
                "stop SERVICE        Stop a service.\n" +
                "shutdown            Shutdown the server.");
        }

        @Override
        public int run(String[] args)
        {
            java.util.List<String> commands = new java.util.ArrayList<>();

            int idx = 0;
            while(idx < args.length)
            {
                if(args[idx].equals("-h") || args[idx].equals("--help"))
                {
                    usage();
                    return 1;
                }
                else if(args[idx].charAt(0) == '-')
                {
                    System.err.println(appName() + ": unknown option `" + args[idx] + "'");
                    usage();
                    return 1;
                }
                else
                {
                    commands.add(args[idx]);
                    ++idx;
                }
            }

            if(commands.isEmpty())
            {
                usage();
                return 0;
            }

            com.zeroc.Ice.ObjectPrx base = communicator().propertyToProxy("IceBoxAdmin.ServiceManager.Proxy");

            if(base == null)
            {
                //
                // The old deprecated way to retrieve the service manager proxy
                //

                com.zeroc.Ice.Properties properties = communicator().getProperties();

                com.zeroc.Ice.Identity managerIdentity = new com.zeroc.Ice.Identity();
                managerIdentity.category = properties.getPropertyWithDefault("IceBox.InstanceName", "IceBox");
                managerIdentity.name = "ServiceManager";

                String managerProxy;
                if(properties.getProperty("Ice.Default.Locator").length() == 0)
                {
                    String managerEndpoints = properties.getProperty("IceBox.ServiceManager.Endpoints");
                    if(managerEndpoints.length() == 0)
                    {
                        System.err.println(appName() + ": property `IceBoxAdmin.ServiceManager.Proxy' is not set");
                        return 1;
                    }

                    managerProxy = "\"" + com.zeroc.Ice.Util.identityToString(managerIdentity) + "\" :" +
                        managerEndpoints;
                }
                else
                {
                    String managerAdapterId = properties.getProperty("IceBox.ServiceManager.AdapterId");
                    if(managerAdapterId.length() == 0)
                    {
                        System.err.println(appName() + ": property `IceBoxAdmin.ServiceManager.Proxy' is not set");
                        return 1;
                    }

                    managerProxy = "\"" + com.zeroc.Ice.Util.identityToString(managerIdentity) + "\" @" +
                        managerAdapterId;
                }

                base = communicator().stringToProxy(managerProxy);
            }

            com.zeroc.IceBox.ServiceManagerPrx manager = com.zeroc.IceBox.ServiceManagerPrx.checkedCast(base);
            if(manager == null)
            {
                System.err.println(appName() + ": `" + base.toString() + "' is not an IceBox::ServiceManager");
                return 1;
            }

            for(int i = 0; i < commands.size(); i++)
            {
                String command = commands.get(i);
                if(command.equals("shutdown"))
                {
                    manager.shutdown();
                }
                else if(command.equals("start"))
                {
                    if(++i >= commands.size())
                    {
                        System.err.println(appName() + ": no service name specified.");
                        return 1;
                    }

                    String service = commands.get(i);
                    try
                    {
                        manager.startService(service);
                    }
                    catch(com.zeroc.IceBox.NoSuchServiceException ex)
                    {
                        System.err.println(appName() + ": unknown service `" + service + "'");
                        return 1;
                    }
                    catch(com.zeroc.IceBox.AlreadyStartedException ex)
                    {
                        System.err.println(appName() + "service already started.");
                    }
                }
                else if(command.equals("stop"))
                {
                    if(++i >= commands.size())
                    {
                        System.err.println(appName() + ": no service name specified.");
                        return 1;
                    }

                    String service = commands.get(i);
                    try
                    {
                        manager.stopService(service);
                    }
                    catch(com.zeroc.IceBox.NoSuchServiceException ex)
                    {
                        System.err.println(appName() + ": unknown service `" + service + "'");
                        return 1;
                    }
                    catch(com.zeroc.IceBox.AlreadyStoppedException ex)
                    {
                        System.err.println(appName() + "service already stopped.");
                    }
                }
                else
                {
                    System.err.println(appName() + ": unknown command `" + command + "'");
                    usage();
                    return 1;
                }
            }

            return 0;
        }
    }

    public static void main(String[] args)
    {
        Client app = new Client();
        int rc = app.main("IceBox.Admin", args);

        System.exit(rc);
    }
}
