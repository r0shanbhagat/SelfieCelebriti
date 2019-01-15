package com.base.commonframework.network.locator;

/**
 * <p>
 * ServiceLocator is the registry for all services used in application.
 * </p>
 * <p>
 * <p>
 * A service locator can have objects of all common services used in system. Services are looked up with their respective class.
 * </p>
 */
public interface IServiceLocator {
    <T> T getService(Class<T> serviceClass);
}
