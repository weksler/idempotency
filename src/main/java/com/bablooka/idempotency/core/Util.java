package com.bablooka.idempotency.core;

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

  byte[] protoToDbFormat(@NonNull Message message) throws IdempotencyException {
    try {
      return jsonFormatPrinter.print(message).getBytes();
    } catch (InvalidProtocolBufferException e) {
      logAndThrow(log, e, "Unable to parse idempotency record %s", message);
      return null;
    }
  }

  String protoToJsonString(@NonNull Message message) throws IdempotencyException {
    try {
      return jsonFormatPrinter.print(message);
    } catch (InvalidProtocolBufferException e) {
      logAndThrow(log, e, "Unable to parse proto %s", message);
      return null;
    }
  }

  <T extends Message> T protoFromDbFormat(
      @NonNull byte[] dbIdempotencyRecord, @NonNull Message.Builder builder)
      throws IdempotencyException {
    try {
      jsonFormatParser.merge(new String(dbIdempotencyRecord), builder);
      return (T) builder.build();
    } catch (InvalidProtocolBufferException e) {
      logAndThrow(log, e, "Unable to parse idempotency record %s", dbIdempotencyRecord);
      return null;
    }
  }

  static void logAndThrow(
      @NonNull Logger logger,
      @NonNull Throwable t,
      @NonNull String messageFormat,
      Object... optionalParams)
      throws IdempotencyException {
    String errorMessage = String.format(messageFormat, optionalParams);
    logger.error(errorMessage, t);
    throw new IdempotencyException(errorMessage, t);
  }
}
