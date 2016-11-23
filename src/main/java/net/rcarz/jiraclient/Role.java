/**
 * jira-client - a simple JIRA REST client
 * Copyright (c) 2013 Bob Carroll (bob.carroll@alum.rit.edu)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.rcarz.jiraclient;

import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * Represents an issue status.
 */
public class Role extends Resource {

	private String description = null;
	private String name = null;
	private List<RoleActor> roleActors = null;

	/**
	 * Creates a status from a JSON payload.
	 *
	 * @param restclient
	 *            REST client instance
	 * @param json
	 *            JSON payload
	 */
	protected Role(RestClient restclient, JSONObject json) {
		super(restclient);

		if (json != null)
			deserialise(json);
	}

	private void deserialise(JSONObject json) {
		Map map = json;

		self = Field.getString(map.get("self"));
		id = Field.getString(map.get("id"));
		description = Field.getString(map.get("description"));
		name = Field.getString(map.get("name"));
		roleActors = Field.getResourceArray(RoleActor.class, map.get("actors"), restclient);
	}

	/**
	 * Retrieves the given status record.
	 *
	 * @param restclient
	 *            REST client instance
	 * @param id
	 *            Internal JIRA ID of the status
	 *
	 * @return a status instance
	 *
	 * @throws JiraException
	 *             when the retrieval fails
	 */
	public static Role get(RestClient restclient, String projectId, String id) throws JiraException {

		JSON result = null;

		try {
			result = restclient.get(getBaseUri() + "project/" + projectId + "/role/" + id);
		} catch (Exception ex) {
			throw new JiraException("Failed to retrieve role " + id, ex);
		}

		if (!(result instanceof JSONObject))
			throw new JiraException("JSON payload is malformed");

		return new Role(restclient, (JSONObject) result);
	}

	/**
	 * Retrieves the given status record.
	 *
	 * @param restclient
	 *            REST client instance
	 * @param id
	 *            Internal JIRA ID of the status
	 *
	 * @return a status instance
	 *
	 * @throws JiraException
	 *             when the retrieval fails
	 */
	public static Role get(RestClient restclient, String fullUrl) throws JiraException {

		JSON result = null;

		try {
			result = restclient.get(fullUrl);
		} catch (Exception ex) {
			throw new JiraException("Failed to retrieve role for url " + fullUrl, ex);
		}

		if (!(result instanceof JSONObject))
			throw new JiraException("JSON payload is malformed");

		return new Role(restclient, (JSONObject) result);
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public List<RoleActor> getRoleActors() {
		return roleActors;
	}

}
