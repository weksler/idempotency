package com.bablooka.idempotency.core;

import static com.google.common.base.Preconditions.checkState;

import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import java.time.Clock;
import java.time.Instant;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;

@Log4j2
public class Util {

  private final Clock clock;
  private final JsonFormat.Printer jsonFormatPrinter;
  private final JsonFormat.Parser jsonFormatParser;

  @Inject
  public Util(
      @NonNull Clock clock,
      @NonNull JsonFormat.Printer jsonFormatPrinter,
      @NonNull JsonFormat.Parser jsonFormatParser) {
    this.clock = clock;
    this.jsonFormatPrinter = jsonFormatPrinter;
    this.jsonFormatParser = jsonFormatParser;
  }

  Timestamp timestampFromInstant(@NonNull Instant instant) {
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  Timestamp timestampOfCurrentTime() {
    return timestampFromInstant(now());
  }

  Instant now() {
    return Instant.now(clock);
  }

  /**
   * Updates the {@code updated} record with information from {@code from}, ensuring that the {@code
   * idempotencyKey} field is not being changed, and returns the resulting proto.
   */
  public static IdempotencyRecord validateAndUpdateIdempotencyRecord(
      IdempotencyRecord updated, IdempotencyRecord from) {
    checkState(
        updated.getIdempotencyKey().equals(from.getIdempotencyKey()),
        "Can't change idempotency key while updating [%s] -> [%s]",
        updated,
        from);
    return updated
        .toBuilder()
        .setStatus(from.getStatus())
        .setLeaseExpiresAt(from.getLeaseExpiresAt())
        .setRequestFingerprint(from.getRequestFingerprint())
        .setRpcResponse(from.getRpcResponse())
        .build();
  }

  byte[] protoToDbFormat(Message message) throws IdempotencyException {
    try {
      return jsonFormatPrinter.print(message).getBytes();
    } catch (InvalidProtocolBufferException e) {
      logAndThrow(log, e, "Unable to parse idempotency record {}", message);
      return null;
    }
  }

  String protoToJsonString(Message message) throws IdempotencyException {
    try {
      return jsonFormatPrinter.print(message);
    } catch (InvalidProtocolBufferException e) {
      logAndThrow(log, e, "Unable to parse proto {}", message);
      return null;
    }
  }

  <T extends Message> T protoFromDbFormat(byte[] dbIdempotencyRecord, T builder)
      throws IdempotencyException {
    try {
      T.Builder newBuilder = builder.newBuilderForType();
      jsonFormatParser.merge(new String(dbIdempotencyRecord), newBuilder);
      return (T) newBuilder.build();
    } catch (InvalidProtocolBufferException e) {
      logAndThrow(log, e, "Unable to parse idempotency record {}", dbIdempotencyRecord);
      return null;
    }
  }

  private String protobufToString(Message message) throws IdempotencyException {
    try {
      return jsonFormatPrinter.print(message);
    } catch (InvalidProtocolBufferException e) {
      logAndThrow(log, e, "Unable to convert %s to json", message);
      return "";
    }
  }

  static void logAndThrow(
      Logger logger, Throwable t, String messageFormat, Object... optionalParams)
      throws IdempotencyException {
    String errorMessage = String.format(messageFormat, optionalParams);
    logger.error(errorMessage, t);
    throw new IdempotencyException(errorMessage, t);
  }
}
