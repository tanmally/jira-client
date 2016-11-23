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

import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * Represents an issue status.
 */
public class RoleActor extends Resource {

    private String type = null;
    private User user = null;

    /**
     * Creates a status from a JSON payload.
     *
     * @param restclient REST client instance
     * @param json JSON payload
     */
    protected RoleActor(RestClient restclient, JSONObject json) {
        super(restclient);

        if (json != null)
            deserialise(json);
    }

    private void deserialise(JSONObject json) {
        Map map = json;

        self = Field.getString(map.get("self"));
        type = Field.getString(map.get("type"));
        user = Field.getResource(User.class, json, restclient);
    }

    /**
     * Retrieves the given status record.
     *
     * @param restclient REST client instance
     * @param id Internal JIRA ID of the status
     *
     * @return a status instance
     *
     * @throws JiraException when the retrieval fails
     */
    public static RoleActor get(RestClient restclient, String projectId ,String id)
        throws JiraException {

        JSON result = null;

        try {
            result = restclient.get(getBaseUri() + "project/"+ projectId + "/role/" + id);
        } catch (Exception ex) {
            throw new JiraException("Failed to retrieve role " + id, ex);
        }

        if (!(result instanceof JSONObject))
            throw new JiraException("JSON payload is malformed");

        return new RoleActor(restclient, (JSONObject)result);
    }
    
  
    @Override
    public String toString() {
        return  type + " : "  + user + " isUser : " + isUser();
    }


    public User getUser() {
        return user;
    }
    
    public boolean isUser(){
    	return "atlassian-user-role-actor".equals(type);
    }
}

