package net.whitehole.bordercontrol.common.models;

import java.util.UUID;

/**
 * Model for requesting new permissions for a token
 * @param token public id
 * @param id ID of the request
 * @param timestamp timestamp the request was made
 * @param permissions array of permissions requesting
 * @param accepted if the request was accepted by
 */
public record BorderPermissionAddRequest(
        byte[] token,
        byte[] id,
        long timestamp,
        boolean accepted,
        String[] permissions
) {}
