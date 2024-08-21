package com.helion.catalog.infrastructure.kafka;

import com.helion.catalog.AbstractEmbeddedKafkaTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryListenerTest extends AbstractEmbeddedKafkaTest {

    @Test
    public void testDummy(){
        Assertions.assertNotNull(producer());
    }
}
