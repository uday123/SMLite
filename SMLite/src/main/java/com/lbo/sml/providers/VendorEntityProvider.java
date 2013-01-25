package com.lbo.sml.providers;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sakaiproject.entitybus.EntityReference;
import org.sakaiproject.entitybus.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybus.entityprovider.EntityProvider;
import org.sakaiproject.entitybus.entityprovider.EntityProviderManager;
import org.sakaiproject.entitybus.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybus.entityprovider.extension.Formats;
import org.sakaiproject.entitybus.entityprovider.search.Search;

import com.lbo.sml.entity.VendorEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author ukola
 * 
 */
public class VendorEntityProvider implements EntityProvider, CoreEntityProvider, RESTful {

	public DBCollection collection = null;
	private EntityProviderManager entityProviderManager;

	public void setEntityProviderManager(EntityProviderManager entityProviderManager) {
		this.entityProviderManager = entityProviderManager;
	}

	public void init() throws Exception {
		entityProviderManager.registerEntityProvider(this);
	}

	public void destroy() throws Exception {
		entityProviderManager.unregisterEntityProvider(this);
	}

	/**
	 * @param entityProviderManager
	 */
	public VendorEntityProvider(EntityProviderManager entityProviderManager) {
		this.entityProviderManager = entityProviderManager;
		try {
			init();
		} catch (Exception e) {
			throw new RuntimeException("Unable to register the provider ("
					+ this + "): " + e, e);
		}

		try {
			// connect to mongoDB, ip and port number
			Mongo mongo = new Mongo("localhost", 27017);
			// get database from MongoDB, if database doesn't exists, mongoDB will create it automatically
			DB database = mongo.getDB("SMLite");
			// Get collection from MongoDB, database named "yourDB", if collection doesn't exists, mongoDB will create it automatically
			collection = database.getCollection("Vendor");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	public String getEntityPrefix() {
		return "vendor";
	}

	public String createEntity(EntityReference ref, Object entity,
			Map<String, Object> params) {
		String nextId = System.currentTimeMillis() + "";
		VendorEntity passedEntity = (VendorEntity) entity;
		passedEntity.setId(nextId); 
		BasicDBObject document = new BasicDBObject();
		document.put(VendorEntity.ID, nextId);
		document.put(VendorEntity.NAME, passedEntity.getName());
		document.put(VendorEntity.PO_CREATED, passedEntity.isPoCreated());
		document.put(VendorEntity.PO_DETAILS, passedEntity.getPoDetails());
		document.put(VendorEntity.PO_NUMBER, passedEntity.getPoNumber());
		document.put(VendorEntity.TYPE, passedEntity.getType());
		collection.insert(document);
		return nextId;
	}

	
	public void updateEntity(EntityReference ref, Object entity,
			Map<String, Object> params) {
		VendorEntity passedEntity = (VendorEntity) entity;
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(VendorEntity.ID, ref.getId());
    	DBCursor cursor = collection.find(searchQuery);

    	while (cursor.hasNext()) {
			DBObject document = cursor.next();
			document.put(VendorEntity.NAME, passedEntity.getName());
			document.put(VendorEntity.PO_CREATED, passedEntity.isPoCreated());
			document.put(VendorEntity.PO_DETAILS, passedEntity.getPoDetails());
			document.put(VendorEntity.PO_NUMBER, passedEntity.getPoNumber());
			document.put(VendorEntity.TYPE, passedEntity.getType());
			collection.insert(document);
			break;
		}
	}

	
	public Object getEntity(EntityReference ref) {
		if (ref.getId() == null) {
			return new VendorEntity();
		}

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(VendorEntity.ID, ref.getId());
		DBCursor cursor = collection.find(searchQuery);
		while (cursor.hasNext()) {
			DBObject document = cursor.next();
			String id = (String) document.get(VendorEntity.ID);
			String name = (String) document.get(VendorEntity.NAME);
			boolean poCreated = (Boolean) document.get(VendorEntity.PO_CREATED);
			long poNumber = (Long) document.get(VendorEntity.PO_NUMBER);
			String poDetails = (String) document.get(VendorEntity.PO_DETAILS);
			String type = (String) document.get(VendorEntity.TYPE);
			VendorEntity e = new VendorEntity(id, name, poCreated, poNumber, poDetails, type);
			return e;
		}
		return null;
	}


	public void deleteEntity(EntityReference ref, Map<String, Object> params) {
		if (ref.getId() == null) {
			throw new IllegalArgumentException("Invalid entity id, cannot find entity to remove: " + ref);
		}
		
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(VendorEntity.ID, ref.getId());

		DBCursor cursor = collection.find(searchQuery);
    	while (cursor.hasNext()) {
			DBObject document = cursor.next();
			collection.remove(document);
			break;
		}
	}

	public List<?> getEntities(EntityReference ref, Search search) {
		List<VendorEntity> entities = new ArrayList<VendorEntity>();
		if (search.isEmpty()) {
			// return all
			DBCursor cursor = collection.find();
			try {
				while (cursor.hasNext()) {
					DBObject document = cursor.next();
					String id = (String) document.get(VendorEntity.ID);
					String name = (String) document.get(VendorEntity.NAME);
					boolean poCreated = (Boolean) document.get(VendorEntity.PO_CREATED);
					long poNumber = (Long) document.get(VendorEntity.PO_NUMBER);
					String poDetails = (String) document.get(VendorEntity.PO_DETAILS);
					String type = (String) document.get(VendorEntity.TYPE);
					VendorEntity e = new VendorEntity(id, name, poCreated, poNumber, poDetails, type);
					entities.add(e);
				}
			} finally {
				cursor.close();
			}
		} else {
			if (search.getRestrictionByProperty("name") != null) {
				String sMatch = search.getRestrictionByProperty("name").value.toString();
				BasicDBObject searchQuery = new BasicDBObject();
				searchQuery.put(VendorEntity.ID, ref.getId());
				searchQuery.put(VendorEntity.NAME, sMatch);

				DBCursor cursor = collection.find(searchQuery);

				try {
					while (cursor.hasNext()) {
						DBObject document = cursor.next();
						String id = (String) document.get(VendorEntity.ID);
						String name = (String) document.get(VendorEntity.NAME);
						boolean poCreated = (Boolean) document.get(VendorEntity.PO_CREATED);
						long poNumber = (Long) document.get(VendorEntity.PO_NUMBER);
						String poDetails = (String) document.get(VendorEntity.PO_DETAILS);
						String type = (String) document.get(VendorEntity.TYPE);
						VendorEntity e = new VendorEntity(id, name, poCreated, poNumber, poDetails, type);
						entities.add(e);
					}
				} finally {
					cursor.close();
				}
			}
		}
		return entities;
	}

	public String[] getHandledOutputFormats() {
		return new String[] { Formats.HTML, Formats.JSON, Formats.XML,
				Formats.FORM };
	}

	public String[] getHandledInputFormats() {
		return new String[] { Formats.HTML, Formats.JSON, Formats.XML };
	}
	
	public Object getSampleEntity() {
		return new VendorEntity();
	}


	public boolean entityExists(String id) {
		boolean retValue = false;

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(VendorEntity.ID, id);
    	DBCursor cursor = collection.find(searchQuery);
		if (cursor.hasNext()) {
			retValue = true;
		}
		return retValue;
   }
}
