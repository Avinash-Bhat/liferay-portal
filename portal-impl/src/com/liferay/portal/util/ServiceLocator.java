/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.util;

import com.liferay.portal.kernel.bean.ExceptionSafeBeanHandler;
import com.liferay.portal.kernel.util.MethodInvoker;
import com.liferay.portal.kernel.util.MethodWrapper;

import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="ServiceLocator.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ServiceLocator {

	public static ServiceLocator getInstance() {
		return _instance;
	}

	public Object findService(String serviceName) {
		Object obj = null;

		try {
			String factoryName = serviceName + _FACTORY_SUFFIX;

			MethodWrapper methodWrapper = new MethodWrapper(
				factoryName, _SERVICE_METHOD, new Object[0]);

			obj = MethodInvoker.invoke(methodWrapper, false);
		}
		catch (Exception e) {
			_log.error(e);
		}

		return obj;
	}

	public Object findExceptionSafeService(Class<?> serviceClass) {
		Object obj = findService(serviceClass.getName());

		if (obj != null) {
			obj = Proxy.newProxyInstance(
				serviceClass.getClassLoader(), new Class[] {serviceClass},
				new ExceptionSafeBeanHandler(obj));
		}

		return obj;
	}

	private ServiceLocator() {
	}

	private static Log _log = LogFactory.getLog(ServiceLocator.class);

	private static final String _FACTORY_SUFFIX = "Factory";

	private static final String _SERVICE_METHOD = "getService";

	private static ServiceLocator _instance = new ServiceLocator();

}