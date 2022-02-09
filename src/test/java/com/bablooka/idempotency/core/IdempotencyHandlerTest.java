package com.bablooka.idempotency.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import com.google.protobuf.util.JsonFormat;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Duration;
import javax.inject.Singleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IdempotencyHandlerTest {
  @Module
  class IdempotencyHandlerTestModule {
    @Provides
    Duration provideDefaultIdempotencyDureation() {
      return Duration.ofMinutes(1);
    }

    @Provides
    IdempotentRpcContextFactory provideIdempotencyRpcContextFactory() {
      return idempotentRpcContextFactoryMock;
    }

    @Provides
    JsonFormat.Printer provideJsonFormatPrinter() {
      return jsonFormatPrinterSpy;
    }

    @Provides
    JsonFormat.Parser provideJsonFormatParser() {
      return jsonFormatParserSpy;
    }

    @Provides
    Clock providesClock() {
      return clockSpy;
    }
  }

  @Singleton
  @Component(modules = {IdempotencyHandlerTestModule.class})
  public interface IdempotencyHandlerTestRoot {
    IdempotencyHandler idempotencyHandler();
  }

  @Mock IdempotentRpcContextFactory idempotentRpcContextFactoryMock;
  @Mock IdempotentRpc.IdempotentRpcContext idempotentRpcContextMock;
  @Mock Connection connectionMock;
  @Mock IdempotencyStore idempotencyStoreMock;
  @Mock IdempotentRpc idempotentRpcMock;
  JsonFormat.Printer jsonFormatPrinterSpy;
  JsonFormat.Parser jsonFormatParserSpy;
  Clock clockSpy;
  IdempotencyHandler idempotencyHandler;

  @Before
  public void setUp() {
    jsonFormatPrinterSpy = spy(JsonFormat.printer());
    jsonFormatParserSpy = spy(JsonFormat.parser());
    clockSpy = spy(Clock.systemUTC());
    idempotencyHandler =
        DaggerIdempotencyHandlerTest_IdempotencyHandlerTestRoot.builder()
            .idempotencyHandlerTestModule(new IdempotencyHandlerTestModule())
            .build()
            .idempotencyHandler();
  }

  @Test
  public void testHandleRpcAutoCommitOff() throws Exception {
    doReturn(true).when(connectionMock).getAutoCommit();

    try {
      idempotencyHandler.handleRpc(
          connectionMock, idempotencyStoreMock, idempotentRpcMock, new Object());
      fail("Should have thrown an IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testHandleRpcSqlExceptionWhenCheckingAutoCommit() throws Exception {
    doThrow(new SQLException("Boo!")).when(connectionMock).getAutoCommit();

    try {
      idempotencyHandler.handleRpc(
          connectionMock, idempotencyStoreMock, idempotentRpcMock, new Object());
      fail("Should have thrown an IdempotencyException");
    } catch (IdempotencyException e) {
      assertEquals(SQLException.class, e.getCause().getClass());
      SQLException sqlException = (SQLException) e.getCause();
      assertEquals("Boo!", sqlException.getMessage());
      assertEquals("Exception while checking auto commit state", e.getMessage());
    }
  }

  @Test
  public void testHandleRpcNullIdempotencyKey() throws Exception {
    doReturn(false).when(connectionMock).getAutoCommit();
    doReturn(idempotentRpcContextMock)
        .when(idempotentRpcContextFactoryMock)
        .getIdempotencyRpcContext(any());
    doReturn(null).when(idempotentRpcContextMock).getIdempotencyKey();

    try {
      idempotencyHandler.handleRpc(
          connectionMock, idempotencyStoreMock, idempotentRpcMock, new Object());
      fail("Should have thrown an NullPointerException");
    } catch (NullPointerException e) {
      assertEquals("Idempotency key can not be null.", e.getMessage());
    }
  }
}
