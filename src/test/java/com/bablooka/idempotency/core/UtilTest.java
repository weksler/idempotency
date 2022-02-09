package com.bablooka.idempotency.core;

import static com.bablooka.idempotency.proto.IdempotencyRecord.Status.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import java.time.Clock;
import java.time.Instant;
import javax.inject.Singleton;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilTest {

  static final long TIMESTAMPS_SECONDS = 1642192717;
  static final String DB_FORMAT_SAMPLE =
      "{\"idempotencyKey\": \"hello\",\"requestFingerprint\": \"\","
          + "\"status\": \"EXECUTING\",\"rpcResponse\": \"SGkgVGhlcmUhISE=\","
          + "\"leaseExpiresAt\":\"2019-10-01T08:25:24.080Z\"}";

  @Module
  class UtilTestModule {
    @Provides
    Clock provideClock() {
      return clock;
    }

    @Provides
    @Singleton
    JsonFormat.Printer getJsonFormatPrinter() {
      return jsonFormatPrinterSpy;
    }

    @Provides
    @Singleton
    JsonFormat.Parser getJsonFormatParser() {
      return JsonFormat.parser();
    }
  }

  @Singleton
  @Component(modules = {UtilTestModule.class})
  public interface UtilTestRoot {
    Util util();
  }

  @Mock Clock clock;
  JsonFormat.Printer jsonFormatPrinterSpy;
  Util util;

  @Before
  public void setUp() {
    jsonFormatPrinterSpy = spy(JsonFormat.printer().includingDefaultValueFields());
    util =
        DaggerUtilTest_UtilTestRoot.builder().utilTestModule(new UtilTestModule()).build().util();
  }

  @Test
  public void testNow() {
    when(clock.instant()).thenReturn(Instant.ofEpochSecond(TIMESTAMPS_SECONDS));
    assertEquals(util.now().getEpochSecond(), TIMESTAMPS_SECONDS);
  }

  @Test
  public void testTimestampFromInstant() {
    Instant instant = Instant.parse("2019-10-01T08:25:24.08Z");
    Timestamp timestamp = util.timestampFromInstant(instant);
    assertEquals(1_569_918_324, timestamp.getSeconds());
    assertEquals(80_000_000, timestamp.getNanos());
  }

  @SneakyThrows
  @Test
  public void TestProtoToDbFormatHappyPath() throws Exception {
    Instant instant = Instant.parse("2019-10-01T08:25:24.08Z");

    IdempotencyRecord idempotencyRecord =
        IdempotencyRecord.newBuilder()
            .setIdempotencyKey("hello")
            .setStatus(EXECUTING)
            .setRpcResponse(ByteString.copyFromUtf8("Hi There!!!"))
            .setLeaseExpiresAt(util.timestampFromInstant(instant))
            .build();

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode expected = objectMapper.readTree(DB_FORMAT_SAMPLE);
    assertEquals(expected, objectMapper.readTree(util.protoToDbFormat(idempotencyRecord)));
  }

  @Test
  public void TestProtoToDbFormatInvalidProto() throws Exception {
    doThrow(InvalidProtocolBufferException.class).when(jsonFormatPrinterSpy).print(any());

    IdempotencyRecord idempotencyRecord =
        IdempotencyRecord.newBuilder()
            .setIdempotencyKey("hello")
            .setStatus(EXECUTING)
            .setRpcResponse(ByteString.copyFromUtf8("Hi There!!!"))
            .build();

    try {
      util.protoToDbFormat(idempotencyRecord);
      fail("Should have thrown an IdempotencyException");
    } catch (IdempotencyException e) {
      // expected
    }
  }

  @Test
  public void testProtoFromDbFormatHappyPath() throws Exception {
    IdempotencyRecord idempotencyRecord =
        util.protoFromDbFormat(DB_FORMAT_SAMPLE.getBytes(), IdempotencyRecord.newBuilder());
    assertEquals("hello", idempotencyRecord.getIdempotencyKey());
    assertEquals(EXECUTING, idempotencyRecord.getStatus());
    assertEquals(1569918324, idempotencyRecord.getLeaseExpiresAt().getSeconds());
    assertEquals(80000000, idempotencyRecord.getLeaseExpiresAt().getNanos());
  }

  @Test
  public void testProtoFromDbFormatInvalidInput() {
    try {
      util.protoFromDbFormat("Bad input".getBytes(), IdempotencyRecord.newBuilder());
      fail("should have thrown an IdempotencyException");
    } catch (IdempotencyException e) {
      // expected
    }
  }
}
