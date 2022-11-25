package org.prammers.kdt.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcut {

    @Pointcut("execution(public * org.prammers.kdt..*.*(..))")
    public void servicePublicMethodPointcut() {}

    @Pointcut("execution(public * org.prammers.kdt..*.*(..))")
    public void repositoryMethodPointcut() {}

    @Pointcut("execution(public * org.prammers.kdt..*.*(..))")
    public void repositoryInsertMethodPointcut() {}
}
