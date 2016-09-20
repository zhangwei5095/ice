// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

#import <objc/Ice/Initialize.h>

#include <Ice/BatchRequestInterceptor.h>

@interface ICEBatchRequestInterceptor : NSObject
+(Ice::BatchRequestInterceptor*)
batchRequestInterceptorWithBatchRequestInterceptor:(void(^)(id<ICEBatchRequest>, int, int))arg;
@end
