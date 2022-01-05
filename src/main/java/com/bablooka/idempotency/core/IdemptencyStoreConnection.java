package com.bablooka.idempotency.core;

/**
 * An interface for connections to data stores. <br>
 * From the idempotency lib's perspective, this just needs to be
 */
public interface IdemptencyStoreConnection {

  /**
   * Commits the most recent transaction that is active on this connection. <br>
   * For SQL based connections, this should be the underlying connection's SQL commit.
   */
  void commit();
}
