// Generated from /Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home/jre/lib/rt.jar

#pragma once

#include <java/lang/fwd-${project.parent.artifactId}-core2.hpp>
#include <java/lang/Object.hpp>

struct java::lang::AutoCloseable
    : public virtual Object
{

    virtual void close() = 0;

    // Generated
    static ::java::lang::Class *class_();
};
