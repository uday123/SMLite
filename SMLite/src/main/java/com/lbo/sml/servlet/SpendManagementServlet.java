package com.lbo.sml.servlet;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.entitybus.EntityBrokerManager;
import org.sakaiproject.entitybus.entityprovider.EntityProviderManager;
import org.sakaiproject.entitybus.impl.EntityBrokerCoreServiceManager;
import org.sakaiproject.entitybus.providers.EntityRequestHandler;
import org.sakaiproject.entitybus.rest.EntityBrokerRESTServiceManager;
import org.sakaiproject.entitybus.util.servlet.DirectServlet;
import com.lbo.sml.providers.VendorEntityProvider;

/**
 * The spend management servlet allows unchained access to entity URLs within
 * the system and provides the EB REST access
 * 
 * @author ukola
 */
public class SpendManagementServlet extends DirectServlet {

	private static final long serialVersionUID = 4887192770271670117L;
	private transient EntityBrokerCoreServiceManager entityBrokerCoreServiceManager;
	private transient EntityBrokerRESTServiceManager entityRESTServiceManager;
	private transient List<VendorEntityProvider> entityProviders;

	/**
	 * Starts up all the entity providers and places them into the list
	 * 
	 * @param entityProviderManager
	 *            the provider manager
	 */
	protected void startProviders(EntityProviderManager entityProviderManager) {
		this.entityProviders = new Vector<VendorEntityProvider>();
		this.entityProviders
				.add(new VendorEntityProvider(entityProviderManager));
	}

	@Override
	public EntityRequestHandler initializeEntityRequestHandler() {
		EntityRequestHandler entityReqHandler;
		try {
			// fire up the EB services
			this.entityBrokerCoreServiceManager = new EntityBrokerCoreServiceManager();
			EntityBrokerManager entityBrokerManager = this.entityBrokerCoreServiceManager
					.getEntityBrokerManager();
			// create the EB REST services
			this.entityRESTServiceManager = new EntityBrokerRESTServiceManager(
					entityBrokerManager);
			entityReqHandler = this.entityRESTServiceManager
					.getEntityRequestHandler();
			if (entityReqHandler == null) {
				throw new RuntimeException(
						"FAILED to load EB EntityRequestHandler");
			}

			EntityProviderManager epm = this.entityBrokerCoreServiceManager
					.getEntityProviderManager();
			// fire up the providers
			startProviders(epm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(
					"FAILURE during init of direct servlet: " + e, e);
		}
		return entityReqHandler;
	}

	@Override
	public void destroy() {
		super.destroy();
		if (this.entityProviders != null) {
			for (VendorEntityProvider provider : entityProviders) {
				if (provider != null) {
					try {
						provider.destroy();
					} catch (Exception e) {
						System.err.println("WARN Could not clean up provider ("
								+ provider + ") on destroy: " + e);
					}
				}
			}
			this.entityProviders.clear();
			this.entityProviders = null;
		}
		if (this.entityRESTServiceManager != null) {
			this.entityRESTServiceManager.destroy();
			this.entityRESTServiceManager = null;
		}
		if (this.entityBrokerCoreServiceManager != null) {
			this.entityBrokerCoreServiceManager.destroy();
			this.entityBrokerCoreServiceManager = null;
		}
	}

	@Override
	public String getCurrentLoggedInUserId() {
		return "tester";
	}

	@Override
	public void handleUserLogin(HttpServletRequest req,
			HttpServletResponse res, String path) {
		throw new SecurityException("Not able to handle login redirects yet");
	}

}
