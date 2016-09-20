// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************


const Ice = require("../Ice/ModuleRegistry").Ice;
//
// Ice.EnumBase
//
class EnumBase
{
    constructor(name, value)
    {
        this._name = name;
        this._value = value;
    }

    equals(rhs)
    {
        if(this === rhs)
        {
            return true;
        }

        if(!(rhs instanceof Object.getPrototypeOf(this).constructor))
        {
            return false;
        }

        return this._value == rhs._value;
    }

    hashCode()
    {
        return this._value;
    }

    toString()
    {
        return this._name;
    }
    
    get name()
    {
        return this._name;
    }
    
    get value()
    {
        return this._value;
    }
}
Ice.EnumBase = EnumBase;

class EnumHelper
{
    constructor(enumType)
    {
        this._enumType = enumType;
    }

    write(os, v)
    {
        this._enumType.__write(os, v);
    }

    writeOptional(os, tag, v)
    {
        this._enumType.__writeOpt(os, tag, v);
    }

    read(is)
    {
        return this._enumType.__read(is);
    }

    readOptional(is, tag)
    {
        return this._enumType.__readOpt(is, tag);
    }
}

Ice.EnumHelper = EnumHelper;

const Slice = Ice.Slice;
Slice.defineEnum = function(enumerators)
{
    const type = class extends EnumBase
    {
        constructor(n, v)
        {
            super(n, v);
        }
    };

    const enums = [];
    let maxValue = 0;
    let firstEnum = null;
    
    for(let idx in enumerators)
    {
        let e = enumerators[idx][0], value = enumerators[idx][1];
        let enumerator = new type(e, value);
        enums[value] = enumerator;
        if(!firstEnum)
        {
            firstEnum = enumerator;
        }
        Object.defineProperty(type, e, {
            enumerable: true,
            value: enumerator
        });
        if(value > maxValue)
        {
            maxValue = value;
        }
    }

    Object.defineProperty(type, "minWireSize", {
        get: function(){ return 1; }
    });

    type.__write = function(os, v)
    {
        if(v)
        {
            os.writeEnum(v);
        }
        else
        {
            os.writeEnum(firstEnum);
        }
    };
    type.__read = function(is)
    {
        return is.readEnum(type);
    };
    type.__writeOpt = function(os, tag, v)
    {
        if(v !== undefined)
        {
            if(os.writeOptional(tag, Ice.OptionalFormat.Size))
            {
                type.__write(os, v);
            }
        }
    };
    type.__readOpt = function(is, tag)
    {
        return is.readOptionalEnum(tag, type);
    };

    type.__helper = new EnumHelper(type);

    Object.defineProperty(type, 'valueOf', {
        value: function(v) {
            if(v === undefined)
            {
                return type;
            }
            return enums[v];
        }
    });

    Object.defineProperty(type, 'maxValue', {
        value: maxValue
    });

    Object.defineProperty(type.prototype, 'maxValue', {
        value: maxValue
    });

    return type;
};
module.exports.Ice = Ice;
