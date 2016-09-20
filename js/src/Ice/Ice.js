// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************


const __M = require("../Ice/ModuleRegistry").Ice.__M;

module.exports.Ice = __M.require(module,
    [
        "../Ice/Initialize",
        "../Ice/Communicator",
        "../Ice/HashMap",
        "../Ice/Object",
        "../Ice/Long",
        "../Ice/Logger",
        "../Ice/ObjectPrx",
        "../Ice/BatchRequestQueue",
        "../Ice/Properties",
        "../Ice/IdentityUtil",
        "../Ice/ProcessLogger",
        "../Ice/Protocol",
        "../Ice/Identity",
        "../Ice/Exception",
        "../Ice/LocalException",
        "../Ice/BuiltinSequences",
        "../Ice/StreamHelpers",
        "../Ice/Promise",
        "../Ice/EndpointTypes",
        "../Ice/Locator",
        "../Ice/Router",
        "../Ice/Version",
        "../Ice/ObjectFactory",
        "../Ice/Buffer",
        "../Ice/ArrayUtil",
        "../Ice/UnknownSlicedValue",
        "../Ice/Process",
        "../Ice/MapUtil"
    ]).Ice;

module.exports.IceMX = require("../Ice/Metrics").IceMX;

module.exports.IceSSL = require("../Ice/EndpointInfo").IceSSL;
