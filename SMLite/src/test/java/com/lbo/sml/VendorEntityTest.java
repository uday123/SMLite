package com.lbo.sml;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.sakaiproject.entitybus.EntityReference;
import org.sakaiproject.entitybus.entityprovider.EntityProviderManager;
import org.sakaiproject.entitybus.impl.EntityBrokerCoreServiceManager;

import com.lbo.sml.entity.VendorEntity;
import com.lbo.sml.providers.VendorEntityProvider;

public class VendorEntityTest extends TestCase {
	
	/**
	 * 
	 */
	private EntityBrokerCoreServiceManager entityBrokerCoreServiceManager;
	private EntityProviderManager entityProviderManager;
	
	/**
	 * Constructor
	 * @param str
	 */
	public VendorEntityTest(String str) {
		super(str);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		entityBrokerCoreServiceManager = new EntityBrokerCoreServiceManager();
		entityProviderManager = entityBrokerCoreServiceManager.getEntityProviderManager();
	}

	public void testCreateVendor() {
		String nextId = System.currentTimeMillis() + "";
		VendorEntity vendorEntity  = new VendorEntity();
		vendorEntity.setId(nextId);
		vendorEntity.setName("Cisco");
		vendorEntity.setPoCreated(true);
		vendorEntity.setPoNumber(564774);
		vendorEntity.setPoDetails("Webex Communicator");
		vendorEntity.setType("Staff_Augmentation");
		
		VendorEntityProvider entityProvider = new VendorEntityProvider(entityProviderManager);
		entityProvider.createEntity(null, vendorEntity, null);
		assertNotNull(vendorEntity.getPoDetails());
	}

	public void testModifyVendor() {
		String nextId = System.currentTimeMillis() + "";
    	VendorEntity vendorEntity = new VendorEntity();
		vendorEntity.setPoDetails("Webex Communicator");
		vendorEntity.setId(nextId);
		vendorEntity.setName("Cisco");
		vendorEntity.setPoCreated(true);
		vendorEntity.setPoNumber(257846);
		vendorEntity.setPoDetails("Webex Communicator");
		vendorEntity.setType("Staff_Augmentation");
		
		VendorEntityProvider entityProvider = new VendorEntityProvider(entityProviderManager);
		entityProvider.updateEntity(new EntityReference(), vendorEntity, null);
		assertNotNull(vendorEntity.getId());
	}
	
	public void testSelectVendor() {
		VendorEntityProvider entityProvider = new VendorEntityProvider(entityProviderManager);
		entityProvider.getEntity(new EntityReference());
		assertNotNull(entityProvider.getEntityPrefix());
	}
	
	/*public void testDeleteVendor() {
		VendorEntityProvider entityProvider = new VendorEntityProvider(entityProviderManager);
		entityProvider.deleteEntity(new EntityReference(), null);
		assertNotNull(entityProvider.getSampleEntity());
	}*/

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new VendorEntityTest("testCreateVendor"));
		suite.addTest(new VendorEntityTest("testModifyVendor"));
		suite.addTest(new VendorEntityTest("testSelectVendor"));
		//suite.addTest(new VendorEntityTest("testDeleteVendor"));	
		return suite;
	}
}
