/*******************************************************************************
 * Copyright (c) 2011 GigaSpaces Technologies Ltd. All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.cloudifysource.dsl;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.cloudifysource.dsl.autoscaling.ScalingRulesDetails;
import org.cloudifysource.dsl.internal.DSLException;
import org.cloudifysource.dsl.internal.ServiceReader;
import org.junit.Test;

public class ServiceParsingTest {

	private static final String TEST_PARSING_RESOURCE_PATH = "testResources/testparsing/";

	
	@Test
	public void testFeaturesParsing() throws DSLException, UnknownHostException {
		final File testParsingBaseDslFile = new File(TEST_PARSING_RESOURCE_PATH + "test_features-service.groovy");
		final File testParsingBaseWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		final Service service = ServiceReader.getServiceFromFile(testParsingBaseDslFile, testParsingBaseWorkDir)
				.getService();
		Assert.assertEquals("test features", service.getName());
		Assert.assertEquals("http://" + InetAddress.getLocalHost().getHostName() + ":8080", service.getUrl());
		final ServiceLifecycle lifecycle = service.getLifecycle();
		
		Assert.assertNotNull(lifecycle.getStart());
		Assert.assertNotNull(lifecycle.getPostStart());
		Assert.assertNotNull(lifecycle.getPreStop());
	}

	
	@Test
	public void testBasicParsing() throws DSLException {
		final File testParsingBaseDslFile = new File(TEST_PARSING_RESOURCE_PATH + "test_parsing_base-service.groovy");
		final File testParsingBaseWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		final Service service = ServiceReader.getServiceFromFile(testParsingBaseDslFile, testParsingBaseWorkDir)
				.getService();
		Assert.assertEquals("test parsing base", service.getName());
		final ServiceLifecycle lifecycle = service.getLifecycle();
		Assert.assertEquals("test_parsing_base_install.groovy", lifecycle.getInit());
		Assert.assertNotNull(lifecycle.getStart());
		Assert.assertNotNull(lifecycle.getPostStart());
		Assert.assertNotNull(lifecycle.getPreStop());
	}

	@Test
	public void testBasicExtendParsing() throws DSLException {

		final File testParsingBaseDslFile = new File(TEST_PARSING_RESOURCE_PATH + "test_parsing_base-service.groovy");
		final File testParsingBaseWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		final Service baseService = ServiceReader.getServiceFromFile(testParsingBaseDslFile, testParsingBaseWorkDir)
				.getService();
		Assert.assertFalse(baseService.getName().equals("test parsing extend"));
		final ServiceLifecycle baseLifecycle = baseService.getLifecycle();
		Assert.assertFalse(baseLifecycle.getInit().equals("test_parsing_extend_install.groovy"));
		Assert.assertNull(baseLifecycle.getStop());

		final File testParsingExtendDslFile = new File(TEST_PARSING_RESOURCE_PATH
				+ "test_parsing_extend-service.groovy");
		final File testParsingExtendWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		final Service service = ServiceReader.getServiceFromFile(testParsingExtendDslFile, testParsingExtendWorkDir)
				.getService();
		Assert.assertEquals("test parsing extend", service.getName());
		final ServiceLifecycle lifecycle = service.getLifecycle();
		Assert.assertEquals("test_parsing_extend_install.groovy", lifecycle.getInit());
		Assert.assertNotNull(lifecycle.getStart());
		Assert.assertNotNull(lifecycle.getPostStart());
		Assert.assertNotNull(lifecycle.getPreStop());
		Assert.assertNotNull(lifecycle.getStop());
		Assert.assertEquals(1, service.getExtendedServicesPaths().size());
		Assert.assertEquals("test_parsing_base-service.groovy", service.getExtendedServicesPaths().getFirst());
	}

	@Test
	public void testBasicExtendIllegalPropertyLocation() {
		final File testParsingExtendDslFile = new File(TEST_PARSING_RESOURCE_PATH
				+ "test_parsing_extend_illegal-service.groovy");
		final File testParsingExtendWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		try {
			ServiceReader.getServiceFromFile(testParsingExtendDslFile, testParsingExtendWorkDir).getService();
			Assert.fail("No exception thrown while extend resides in illegal place");
		} catch (final Exception e) {
			// ignore
		}
	}

	@Test
	public void testBasicExtendIllegalNestedPropertyLocation() {
		final File testParsingExtendDslFile = new File(TEST_PARSING_RESOURCE_PATH
				+ "test_parsing_extend_illegal_nested-service.groovy");
		final File testParsingExtendWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		try {
			ServiceReader.getServiceFromFile(testParsingExtendDslFile, testParsingExtendWorkDir).getService();
			Assert.fail("No exception thrown while extend resides in illegal place");
		} catch (final Exception e) {
			// ignore
		}
	}

	@Test
	public void testTwoLevelExtension() throws DSLException {
		final File testParsingBaseDslFile = new File(TEST_PARSING_RESOURCE_PATH + "test_parsing_base-service.groovy");
		final File testParsingBaseWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		final Service baseService = ServiceReader.getServiceFromFile(testParsingBaseDslFile, testParsingBaseWorkDir)
				.getService();
		Assert.assertFalse(baseService.getName().equals("test parsing extend"));
		final ServiceLifecycle baseLifecycle = baseService.getLifecycle();
		Assert.assertFalse(baseLifecycle.getInit().equals("test_parsing_extend_install.groovy"));
		Assert.assertNull(baseLifecycle.getStop());
		Assert.assertFalse(baseLifecycle.getStart().equals("start"));

		final File testParsingExtendDslFile = new File(TEST_PARSING_RESOURCE_PATH
				+ "test_parsing_extend_two_level-service.groovy");
		final File testParsingExtendWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		final Service service = ServiceReader.getServiceFromFile(testParsingExtendDslFile, testParsingExtendWorkDir)
				.getService();
		Assert.assertEquals("test parsing extend two level", service.getName());
		final ServiceLifecycle lifecycle = service.getLifecycle();
		Assert.assertEquals("test_parsing_extend_install.groovy", lifecycle.getInit());
		Assert.assertNotNull(lifecycle.getStart());
		Assert.assertNotNull(lifecycle.getPostStart());
		Assert.assertNotNull(lifecycle.getPreStop());
		Assert.assertNotNull(lifecycle.getStop());
		Assert.assertEquals("install", lifecycle.getInstall());
		Assert.assertEquals("start", lifecycle.getStart());
		Assert.assertEquals(2, service.getExtendedServicesPaths().size());
		Assert.assertEquals("test_parsing_extend-service.groovy", service.getExtendedServicesPaths().getFirst());
		Assert.assertEquals("test_parsing_base-service.groovy", service.getExtendedServicesPaths().getLast());
	}
	
	@Test
	public void testAutoScalingParsing() throws DSLException, UnknownHostException {
		final File testParsingBaseDslFile = new File(TEST_PARSING_RESOURCE_PATH + "test_parsing_autoscaling-service.groovy");
		final File testParsingBaseWorkDir = new File(TEST_PARSING_RESOURCE_PATH);
		final Service service = ServiceReader.getServiceFromFile(testParsingBaseDslFile, testParsingBaseWorkDir)
				.getService();
		Assert.assertTrue(service.getMinAllowedInstances() > 1);
		Assert.assertTrue(service.getNumInstances() >= service.getMinAllowedInstances());
		Assert.assertTrue(service.getMaxAllowedInstances() >= service.getNumInstances());
		Assert.assertEquals("scalingRules", service.getName());
		ScalingRulesDetails scalingRules = service.getScalingRules();
		Assert.assertNotNull(scalingRules);
		Assert.assertNotNull(scalingRules.getHighThreshold());
		Assert.assertNotNull(scalingRules.getLowThreshold());
		Assert.assertNotNull(scalingRules.getMetric());
		Assert.assertNotNull(scalingRules.getInstancesStatistics());
		Assert.assertNotNull(scalingRules.getSamplingPeriodInSeconds());
		Assert.assertNotNull(scalingRules.getTimeStatistics());
		Assert.assertNotNull(scalingRules.getMovingTimeRangeInSeconds());
	}
}
