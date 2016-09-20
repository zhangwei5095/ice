// **********************************************************************
//
// Copyright (c) 2016-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

#pragma once

[["cpp:header-ext:h", "objc:header-dir:objc", "js:ice-build"]]

#include <Ice/Endpoint.ice>

/**
 *
 * IceIAP provides a secure transport for Ice.
 *
 **/
["objc:prefix:ICEIAP"]
module IceIAP
{

/**
 *
 * Provides access to an IAP endpoint information.
 *
 **/
local class EndpointInfo extends Ice::EndpointInfo
{
    /**
     *
     * The accessory manufacturer or empty to not match against
     * a manufacturer.
     *
     **/
    string manufacturer;

    /**
     *
     * The accessory model number or empty to not match against
     * a model number.
     *
     **/
    string modelNumber;

    /**
     *
     * The accessory name or empty to not match against
     * the accessory name.
     *
     **/
    string name;

    /**
     *
     * The protocol supported by the accessory.
     *
     **/
    string protocol;
};

};

