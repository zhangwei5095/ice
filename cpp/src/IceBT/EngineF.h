// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

#ifndef ICE_BT_ENGINE_F_H
#define ICE_BT_ENGINE_F_H

#include <IceUtil/Shared.h>
#include <Ice/Handle.h>

namespace IceBT
{

class Engine;
IceUtil::Shared* upCast(Engine*);
typedef IceInternal::Handle<Engine> EnginePtr;

class BluetoothService;
IceUtil::Shared* upCast(BluetoothService*);
typedef IceInternal::Handle<BluetoothService> BluetoothServicePtr;

}

#endif
